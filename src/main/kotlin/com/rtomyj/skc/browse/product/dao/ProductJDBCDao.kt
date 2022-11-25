package com.rtomyj.skc.browse.product.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.browse.card.model.MonsterAssociation
import com.rtomyj.skc.browse.product.model.Product
import com.rtomyj.skc.browse.product.model.ProductContent
import com.rtomyj.skc.browse.product.model.Products
import com.rtomyj.skc.util.constant.DBQueryConstants
import com.rtomyj.skc.util.enumeration.ProductType
import com.rtomyj.skc.util.enumeration.table.definitions.ProductsTableDefinition
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.text.ParseException
import java.text.SimpleDateFormat

@Repository
@Qualifier("product-jdbc")
class ProductJDBCDao @Autowired constructor(
	private val jdbcNamedTemplate: NamedParameterJdbcTemplate,
	@Qualifier("dbSimpleDateFormat") private val dateFormat: SimpleDateFormat,
	private val objectMapper: ObjectMapper
) : ProductDao {
	companion object {
		private val log = LoggerFactory.getLogger(this::class.java.name)

		private const val DATE_PARSE_EXCEPTION_LOGGER =
			"Cannot parse date retrieved from DB when retrieving product {}. Exception: {}"
		private const val PRODUCT_ID = "productId"
		private const val LOCALE = "locale"

		// SQL queries
		private const val FROM_PRODUCT_CONTENT_TABLE = " FROM product_contents"
		private const val WHERE_CARD_NUMBER_IS_CARD_ID = " WHERE card_number = :cardId"
		private const val GET_PRODUCT_DETAILS =
			"SELECT DISTINCT product_id, product_locale, product_name, product_release_date, product_content_total, product_type, product_sub_type" +
					FROM_PRODUCT_CONTENT_TABLE +
					" WHERE product_id = :" + PRODUCT_ID +
					" AND product_locale = :" + LOCALE
		private val GET_AVAILABLE_PRODUCTS_BY_LOCALE = String.format(
			"SELECT %S, %S, %S, %S, %S, %S, %S" +
					" FROM product_info" +
					" WHERE product_locale = :" + LOCALE,
			ProductsTableDefinition.PRODUCT_ID,
			ProductsTableDefinition.PRODUCT_LOCALE,
			ProductsTableDefinition.PRODUCT_NAME,
			ProductsTableDefinition.PRODUCT_RELEASE_DATE,
			ProductsTableDefinition.PRODUCT_CONTENT_TOTAL,
			ProductsTableDefinition.PRODUCT_TYPE,
			ProductsTableDefinition.PRODUCT_SUB_TYPE
		)
		private const val GET_PRODUCT_INFO_FOR_CARD =
			"select product_id, product_locale, product_name, product_release_date, product_type, product_sub_type, product_position, card_rarity" +
					FROM_PRODUCT_CONTENT_TABLE +
					WHERE_CARD_NUMBER_IS_CARD_ID +
					" ORDER BY product_release_date DESC"
	}


	override fun getProductInfo(productId: String, locale: String): Product {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue(PRODUCT_ID, productId)
		sqlParams.addValue(LOCALE, locale)

		return jdbcNamedTemplate.queryForObject(GET_PRODUCT_DETAILS, sqlParams) { row: ResultSet, _: Int ->
			return@queryForObject Product(productId, locale)
				.apply {
					productName = row.getString(ProductsTableDefinition.PRODUCT_NAME.toString())
					productTotal = row.getInt(ProductsTableDefinition.PRODUCT_CONTENT_TOTAL.toString())
					productType = row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString())
					productSubType = row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString())
					productRarityStats =
						getProductRarityCount(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))

					try {
						productReleaseDate =
							dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
					} catch (e: ParseException) {
						log.error(DATE_PARSE_EXCEPTION_LOGGER, productId, e.toString())
						return@queryForObject null
					}
				}
		}!!
	}

	override fun getProductsByLocale(locale: String): List<Product> {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue(LOCALE, locale)
		return jdbcNamedTemplate.query(GET_AVAILABLE_PRODUCTS_BY_LOCALE, sqlParams) { row: ResultSet, _: Int ->
			return@query Product(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()), locale)
				.apply {
					productName = row.getString(ProductsTableDefinition.PRODUCT_NAME.toString())
					productTotal = row.getInt(ProductsTableDefinition.PRODUCT_CONTENT_TOTAL.toString())
					productType = row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString())
					productSubType = row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString())

					try {
						productReleaseDate =
							dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
					} catch (e: ParseException) {
						log.error(DATE_PARSE_EXCEPTION_LOGGER, productId, e.toString())
					}
				}
		}
	}

	override fun getProductDetailsForCard(cardId: String): Set<Product> {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("cardId", cardId)

		val rarities: MutableMap<String, MutableMap<String, MutableSet<String>>> = HashMap()

		val products: Set<Product> = LinkedHashSet(
			jdbcNamedTemplate.query(GET_PRODUCT_INFO_FOR_CARD, sqlParams) { row: ResultSet, _: Int ->
				val productId = row.getString(ProductsTableDefinition.PRODUCT_ID.toString())
				val cardPosition = row.getString(ProductsTableDefinition.PRODUCT_POSITION.toString())

				rarities
					.getOrPut(productId) { HashMap() } // add empty HashMap if rarity info is missing for product
					.getOrPut(
						cardPosition
					) { mutableSetOf() } // add empty rarity info for card at given position if no rarity info exists
					.add(row.getString(ProductsTableDefinition.CARD_RARITY.toString()))

				Product(productId, row.getString(ProductsTableDefinition.PRODUCT_LOCALE.toString()))
					.apply {
						productName = row.getString(ProductsTableDefinition.PRODUCT_NAME.toString())
						productType = row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString())
						productSubType = row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString())

						try {
							productReleaseDate =
								dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
						} catch (e: ParseException) {
							log.error(
								"Cannot parse date from DB when retrieving product info for card {} with exception: {}",
								cardId,
								e.toString()
							)
						}
					}
			})

		for (product in products) {
			product.productContent = ArrayList()
			for ((key, value) in rarities[product.productId]!!) {
				product
					.productContent
					.add(
						ProductContent(null, key, value)
					)
			}
		}
		return products
	}


	override fun getProductContents(
		productId: String,
		locale: String
	): Set<ProductContent> {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("productId", productId)
		sqlParams.addValue(LOCALE, locale)

		val rarities: MutableMap<String, MutableSet<String>> = HashMap()
		// map will store references to ProductContent where each ProductContent is equivalent to a cards position ina product eg: 001
		val cardPositionInProductMapsCardInfo = HashMap<String, ProductContent>()


		jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_CONTENT, sqlParams) { row: ResultSet, _: Int ->
			rarities
				.getOrPut(row.getString(ProductsTableDefinition.CARD_ID.toString())) { HashSet() }
				.add(row.getString(ProductsTableDefinition.CARD_RARITY.toString()))

			val cardPositionWithinProduct = row.getString(ProductsTableDefinition.PRODUCT_POSITION.toString())

			// reference is stored once and only once. Rarity info is still being updated but since we are storing the reference to the rarity info we don't need to keep modifying the card
			cardPositionInProductMapsCardInfo.computeIfAbsent(cardPositionWithinProduct) {
				val card = Card(
					row.getString(10),
					row.getString(12),
					row.getString(11),
					row.getString(13),
					row.getString(14)
				).apply {
					monsterType = row.getString(15)
					monsterAttack = row.getInt(16)
					monsterDefense = row.getInt(17)
					monsterAssociation = MonsterAssociation.parseDBString(row.getString(18), objectMapper)
				}

				ProductContent(
					card,
					row.getString(ProductsTableDefinition.PRODUCT_POSITION.toString()),
					rarities[card.cardID]!!
				)
			}
		}

		return cardPositionInProductMapsCardInfo
			.values
			.toSortedSet(compareBy { it.productPosition })
	}


	override fun getAllProductsByType(productType: ProductType, locale: String): Products {
		val sqlParams = MapSqlParameterSource()
		sqlParams.addValue("productType", productType.toString().replace("_", " "))
		return jdbcNamedTemplate.query<Products>(DBQueryConstants.GET_AVAILABLE_PACKS, sqlParams) { row: ResultSet ->
			val availableProductsList: MutableList<Product> = ArrayList()
			while (row.next()) {
				val product = Product(row.getString(1), locale)
				product.productName = row.getString(ProductsTableDefinition.PRODUCT_NAME.toString())
				product.productTotal = row.getInt(ProductsTableDefinition.PRODUCT_CONTENT_TOTAL.toString())
				product.productType = row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString())
				product.productSubType = row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString())
				product.productRarityStats =
					getProductRarityCount(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))
				availableProductsList.add(product)
				try {
					product.productReleaseDate =
						dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString()))
				} catch (e: ParseException) {
					log.error(
						"Cannot parse date from DB when retrieving all packs with exception: {}",
						e.toString()
					)
				}
			}

			val products = Products(locale)
			products.products = availableProductsList
			products
		}!!
	}

	override fun getProductRarityCount(productId: String): Map<String, Int> {
		val queryParams = MapSqlParameterSource()
		queryParams.addValue(PRODUCT_ID, productId)
		return jdbcNamedTemplate.query<Map<String, Int>>(
			DBQueryConstants.GET_PRODUCT_RARITY_INFO,
			queryParams
		) { row: ResultSet ->
			val rarities: MutableMap<String, Int> = HashMap()
			while (row.next()) {
				rarities[row.getString(1)] = row.getInt(2)
			}
			rarities
		}!!
	}

}