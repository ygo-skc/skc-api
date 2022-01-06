package com.rtomyj.skc.dao.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.constant.DBQueryConstants;
import com.rtomyj.skc.dao.ProductDao;
import com.rtomyj.skc.enums.ProductType;
import com.rtomyj.skc.enums.table.definitions.ProductsTableDefinition;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.card.MonsterAssociation;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.model.product.ProductContent;
import com.rtomyj.skc.model.product.Products;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
@Qualifier("product-jdbc")
@Slf4j
public class ProductJDBCDao implements ProductDao {
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    private final SimpleDateFormat dateFormat;

    private final ObjectMapper objectMapper;

    private static final String DATE_PARSE_EXCEPTION_LOGGER = "Cannot parse date retrieved from DB when retrieving product {}. Exception: {}";

    private static final String PRODUCT_ID = "productId";
    private static final String LOCALE = "locale";

    private static final String FROM_PRODUCT_CONTENT_TABLE = " FROM product_contents";
    private static final String GET_PRODUCT_DETAILS = "SELECT DISTINCT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type" +
            FROM_PRODUCT_CONTENT_TABLE +
            " WHERE product_id = :" + PRODUCT_ID +
            " AND product_locale = :" + LOCALE;


    @Autowired
    public ProductJDBCDao(final NamedParameterJdbcTemplate jdbcNamedTemplate
            , @Qualifier("dbSimpleDateFormat") final SimpleDateFormat dateFormat, final ObjectMapper objectMapper) {
        this.jdbcNamedTemplate = jdbcNamedTemplate;
        this.dateFormat = dateFormat;
        this.objectMapper = objectMapper;
    }


    public Product getProductInfo(final String productId, final String locale) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue(PRODUCT_ID, productId);
        sqlParams.addValue(LOCALE, locale);


        return jdbcNamedTemplate.queryForObject(GET_PRODUCT_DETAILS, sqlParams, (ResultSet row, int rowNum) -> {
            try {
                return Product
                        .builder()
                        .productId(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))
                        .productLocale(row.getString(ProductsTableDefinition.PRODUCT_LOCALE.toString()))
                        .productName(row.getString(ProductsTableDefinition.PRODUCT_NAME.toString()))
                        .productReleaseDate(
                                dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
                        )
                        .productTotal(row.getInt(ProductsTableDefinition.PRODUCT_CONTENT_TOTAL.toString()))
                        .productType(row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString()))
                        .productSubType(row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString()))
                        .productRarityStats(
                                this.getProductRarityCount(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))
                        )
                        .productContent(new ArrayList<>())
                        .build();
            } catch (Exception e) {
                log.error(DATE_PARSE_EXCEPTION_LOGGER, productId, e.toString());
                return null;
            }
        });
    }


    public List<Product> getProductsByLocale(final String locale) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue(LOCALE, locale);

        return jdbcNamedTemplate.query(DBQueryConstants.GET_AVAILABLE_PRODUCTS_BY_LOCALE, sqlParams, (ResultSet row, int rowNum) -> {
            Product product = Product
                    .builder()
                    .productId(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))
                    .productLocale(row.getString(ProductsTableDefinition.PRODUCT_LOCALE.toString()))
                    .productName(row.getString(ProductsTableDefinition.PRODUCT_NAME.toString()))
                    .productType(row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString()))
                    .productSubType(row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString()))
                    .productTotal(row.getInt(ProductsTableDefinition.PRODUCT_CONTENT_TOTAL.toString()))
                    .build();
            try {
                product.setProductReleaseDate(
                        dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
                );
            } catch (ParseException e) {
                log.error(DATE_PARSE_EXCEPTION_LOGGER, product.getProductId(), e.toString());
                return null;
            }

            return product;
        });
    }


    public Set<Product> getProductDetailsForCard(final String cardId) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("cardId", cardId);


        final Map<String, Map<String, Set<String>>> rarities = new HashMap<>();
        final Set<Product> products = new LinkedHashSet<>(jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
            final String productId = row.getString(ProductsTableDefinition.PRODUCT_ID.toString());
            final String cardPosition = row.getString(7);

            rarities.computeIfAbsent(productId, k -> new HashMap<>());

            rarities.get(productId).computeIfAbsent(cardPosition, k -> new HashSet<>());
            rarities.get(productId).get(cardPosition).add(row.getString(8));


            try {
                return Product
                        .builder()
                        .productId(productId)
                        .productLocale(row.getString(ProductsTableDefinition.PRODUCT_LOCALE.toString()))
                        .productName(row.getString(ProductsTableDefinition.PRODUCT_NAME.toString()))
                        .productReleaseDate(
                                dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
                        )
                        .productType((row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString())))
                        .productSubType(row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString()))
                        .build();
            } catch (ParseException e) {
                log.error("Cannot parse date from DB when retrieving product info for card {} with exception: {}", cardId, e.toString());
                return null;
            }
        }));


        for (Product product : products) {
            product.setProductContent(new ArrayList<>());
            for (Map.Entry<String, Set<String>> cardPositionAndRarityMap : rarities.get(product.getProductId()).entrySet()) {
                product
                        .getProductContent()
                        .add(
                                ProductContent
                                        .builder()
                                        .productPosition(cardPositionAndRarityMap.getKey())
                                        .rarities(cardPositionAndRarityMap.getValue())
                                        .build()
                        );
            }
        }
        return products;
    }


    public Set<ProductContent> getProductContents(final String packId, final String locale) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("packId", packId);
        sqlParams.addValue(LOCALE, locale);


        final Map<String, Set<String>> rarities = new HashMap<>();
        final Set<ProductContent> productContents = new LinkedHashSet<>(jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_CONTENT, sqlParams, (ResultSet row, int rowNum) -> {
            rarities.computeIfAbsent(row.getString(10), k -> new HashSet<>());
            rarities.get(row.getString(10)).add(row.getString(9));


            final Card card = Card
                    .builder()
                    .cardID(row.getString(10))
                    .cardColor(row.getString(11))
                    .cardName(row.getString(12))
                    .cardAttribute(row.getString(13))
                    .cardEffect(row.getString(14))
                    .monsterType(row.getString(15))
                    .monsterAttack(row.getObject(16, Integer.class))
                    .monsterDefense(row.getObject(17, Integer.class))
                    .monsterAssociation(MonsterAssociation.parseDBString(row.getString(18), objectMapper))
                    .build();

            return ProductContent
                    .builder()
                    .productPosition(row.getString(8))
                    .card(card)
                    .build();
        }));

        for (ProductContent content : productContents) {
            content.setRarities(rarities.get(content.getCard().getCardID()));
        }
        return productContents;
    }


    public Products getAllProductsByType(final ProductType productType, final String locale) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("productType", productType.toString().replace("_", " "));


        return jdbcNamedTemplate.query(DBQueryConstants.GET_AVAILABLE_PACKS, sqlParams, (ResultSet row) -> {
            final List<Product> availableProductsList = new ArrayList<>();

            while (row.next()) {
                try {
                    availableProductsList.add(
                            Product
                                    .builder()
                                    .productId(row.getString(1))
                                    .productLocale(row.getString(2))
                                    .productName(row.getString(3))
                                    .productReleaseDate(dateFormat.parse(row.getString(4)))
                                    .productTotal(row.getInt(5))
                                    .productType(row.getString(6))
                                    .productSubType(row.getString(7))
                                    .productRarityStats(this.getProductRarityCount(row.getString(1)))
                                    .build());
                } catch (ParseException e) {
                    log.error("Cannot parse date from DB when retrieving all packs with exception: {}", e.toString());
                }
            }

            return Products.builder().products(availableProductsList).build();
        });
    }


    public Map<String, Integer> getProductRarityCount(final String productId) {
        final MapSqlParameterSource queryParams = new MapSqlParameterSource();
        queryParams.addValue(PRODUCT_ID, productId);

        return jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_RARITY_INFO, queryParams, (ResultSet row) -> {
            final Map<String, Integer> rarities = new HashMap<>();

            while (row.next()) {
                rarities.put(row.getString(1), row.getInt(2));
            }

            return rarities;
        });
    }
}
