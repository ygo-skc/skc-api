package com.rtomyj.yugiohAPI.dao.database.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.yugiohAPI.dao.DBQueryConstants;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.helper.constants.LogConstants;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.helper.enumeration.table.definitions.BrowseQueryDefinition;
import com.rtomyj.yugiohAPI.helper.enumeration.table.definitions.ProductViewDefinition;
import com.rtomyj.yugiohAPI.helper.enumeration.table.definitions.ProductsTableDefinition;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.helper.util.StringUtil;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterTypeStats;
import com.rtomyj.yugiohAPI.model.banlist.BanListDates;
import com.rtomyj.yugiohAPI.model.banlist.CardBanListStatus;
import com.rtomyj.yugiohAPI.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.yugiohAPI.model.card.Card;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.card.MonsterAssociation;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.ProductContent;
import com.rtomyj.yugiohAPI.model.product.Products;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
@Slf4j
public class JDBCDao implements Dao
{

	private final NamedParameterJdbcTemplate jdbcNamedTemplate;

	private final SimpleDateFormat dateFormat;

	private final ObjectMapper objectMapper;


	@Autowired
	public JDBCDao(final NamedParameterJdbcTemplate jdbcNamedTemplate
			, @Qualifier("dbSimpleDateFormat") final SimpleDateFormat dateFormat
			, final ObjectMapper objectMapper)
	{

		this.jdbcNamedTemplate = jdbcNamedTemplate;
		this.dateFormat = dateFormat;
		this.objectMapper = objectMapper;

	}


	@Override
	public BanListDates getBanListDates()
	{
		return null;
	}


	@Override
	public Card getCardInfo(@NonNull String cardID) throws YgoException
	{

		final String query = DBQueryConstants.GET_CARD_BY_ID;

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardID);
		log.debug("Fetching card info from DB using query: ( {} ) with sql params ( {} ).", query, sqlParams);


		final Card card = jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) ->
		{
			if (row.next())
			{
				MonsterAssociation monsterAssociation = null;
				try
				{
					if (row.getString(8) != null)  monsterAssociation = objectMapper.readValue(row.getString(8), MonsterAssociation.class);
				} catch (JsonProcessingException e)
				{
					log.error("Exception occurred when parsing monster association column, {}", e.toString());
					return null;
				}


				return Card
						.builder()
						.cardID(cardID)
						.cardColor(row.getString(1))
						.cardName(row.getString(2))
						.cardAttribute(row.getString(3))
						.cardEffect(row.getString(4))
						.monsterType(row.getString(5))
						.monsterAttack(row.getObject(6, Integer.class))
						.monsterDefense(row.getObject(7, Integer.class))
						.monsterAssociation(monsterAssociation)
						.build();
			}

			return null;
		});

		if (card == null) throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format("Unable to find card in DB with ID: %s", cardID));

		return card;

	}



	@Override
	public List<Card> getBanListByBanStatus(@NonNull final String date, @NonNull final Status status)
	{
		final String query = DBQueryConstants.GET_BAN_LIST_BY_STATUS;

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("date", date);
		sqlParams.addValue("status", status.toString());


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<Card> cardList = new ArrayList<>();
			while (row.next())
			{
				cardList.add(
					Card
						.builder()
						.cardName(row.getString(1))
						.monsterType(row.getString(2))
						.cardColor(row.getString(3))
						.cardEffect(row.getString(4))
						.cardID(row.getString(5))
						.build()
					);
			}
			return cardList;
		});
	}



	public int getNumberOfBanLists() {

		String query = "SELECT COUNT(DISTINCT ban_list_date) AS 'Total Ban Lists' FROM ban_lists";
		final Integer numBanLists = jdbcNamedTemplate.query(query, (ResultSet row) ->  {
			if (row.next())	return row.getInt(1);

			return null;
		});

		if (numBanLists == null)	return 0;
		return numBanLists;

	}



	public List<String> getBanListDatesInOrder()
	{
		final String query = "select distinct ban_list_date from ban_lists order by ban_list_date";

		return jdbcNamedTemplate.queryForList(query, (SqlParameterSource) null, String.class);

	}



	public String getPreviousBanListDate(String currentBanList)
	{
		final List<String> sortedBanListDates = this.getBanListDatesInOrder();
		final int currentBanListPosition = sortedBanListDates.indexOf(currentBanList);

		if (currentBanListPosition == 0)	return "";
		final int previousBanListPosition = currentBanListPosition - 1;

		return sortedBanListDates.get(previousBanListPosition);
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<CardsPreviousBanListStatus> getNewContentOfBanList(final String newBanList, final Status status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList.equals(""))	return new ArrayList<>();

		String query = "select new_cards.card_number, cards.card_name from (select new_list.card_number" +
				" from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)" +
				" as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList" +
				" and ban_status = :status) as old_list on new_list.card_number = old_list.card_number" +
				" where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number" +
				" ORDER BY cards.card_name";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("status", status.toString());
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<CardsPreviousBanListStatus> newCards = new ArrayList<>();

			while (row.next())
			{
				CardsPreviousBanListStatus cardsPreviousBanListStatus = new CardsPreviousBanListStatus();
				final String cardID = row.getString(1);
				String previousStatus = this.getCardBanListStatusByDate(cardID, oldBanList);
				previousStatus = ( previousStatus == null ) ? "Unlimited" : previousStatus;


				cardsPreviousBanListStatus.setCardId(cardID);
				cardsPreviousBanListStatus.setPreviousBanStatus(previousStatus);
				cardsPreviousBanListStatus.setCardName(row.getString(2));

				newCards.add(cardsPreviousBanListStatus);
			}

			return newCards;
		});
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<CardsPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList.equals(""))	return new ArrayList<>();

		String query = "select removed_cards.card_number, removed_cards.ban_status, cards.card_name" +
				" from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists" +
				" where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status" +
				" from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number" +
				" where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number" +
				" ORDER BY cards.card_name";

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<CardsPreviousBanListStatus> REMOVED_CARDS = new ArrayList<>();

			while(row.next())
			{
				final CardsPreviousBanListStatus REMOVED_CARD = new CardsPreviousBanListStatus();

				REMOVED_CARD.setCardId(row.getString(1));
				REMOVED_CARD.setPreviousBanStatus(row.getString(2));
				REMOVED_CARD.setCardName(row.getString(3));

				REMOVED_CARDS.add(REMOVED_CARD);
			}

			return REMOVED_CARDS;
		});
	}



	public String getCardBanListStatusByDate(String cardId, String banListDate)
	{
		String query = "select ban_status from ban_lists where card_number = :cardId and ban_list_date = :banListDate";

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("banListDate", banListDate);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	public List<Card> getCardNameByCriteria(
			String cardId, String cardName, String cardAttribute, String cardColor, String monsterType, final int limit
	)
	{
		cardId = '%' + cardId + '%';
		cardName = '%' + cardName + '%';
		cardAttribute = (cardAttribute.isEmpty())? ".*" : cardAttribute;
		cardColor = (cardColor.isEmpty())? ".*" : cardColor;
		monsterType = (monsterType.isEmpty())? ".*" : monsterType;


		final String query = "SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, ban_list_date, ban_status" +
				" FROM search" +
				" WHERE card_number LIKE :cardId" +
				" AND card_name LIKE :cardName" +
				" AND card_attribute REGEXP :cardAttribute" +
				" AND card_color REGEXP :cardColor" +
				" AND IFNULL(monster_type, '') REGEXP :monsterType" +
				" ORDER BY color_id, card_name ASC";

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("cardName", cardName);
		sqlParams.addValue("cardAttribute", cardAttribute);
		sqlParams.addValue("cardColor", cardColor);
		sqlParams.addValue("monsterType", monsterType);

		log.debug("Fetching card search results from DB using query: ( {} ) with sql params ( {} ).", query, sqlParams);


		return new ArrayList<>(Objects.requireNonNull(jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			/*
				Since a join between ban lists and card info is done, there will be multiple rows having the same card info (id, name, atk, etc) but with different ban info.
				ie:	ID		Name		BanList
						1234	Stratos	2019-07-15
						1234	Stratos	2019-04-29
				To prevent this, the map will use the cardId (unique) to map to a Card object containing info already gathered from previous rows.
				An array within the Card object will then be used to keep track of all the ban lists the card was a part of. The array will be updated
				 every time a new row has new ban list info of a card already in the map.
			*/
			final Map<String, Card> cardInfoTracker = new LinkedHashMap<>();
			int numUniqueCardsParsed = 0;

			while (row.next()) {
				Card card = cardInfoTracker.get(row.getString(1));

				if (card == null) {
					if (numUniqueCardsParsed == limit) break;

					card = Card.builder()
							.cardID(row.getString(1))
							.cardColor(row.getString(2))
							.cardName(row.getString(3))
							.cardAttribute(row.getString(4))
							.cardEffect(row.getString(5))
							.monsterType(row.getString(6))
							.monsterAttack(row.getObject(7, Integer.class))
							.monsterDefense(row.getObject(8, Integer.class))
							.restrictedIn(new ArrayList<>())
							.build();
					cardInfoTracker.put(card.getCardID(), card);

					numUniqueCardsParsed++;
				}

				try {
					if (row.getString(9) != null) {
						card.getRestrictedIn()
								.add(CardBanListStatus
										.builder()
										.banListDate(dateFormat.parse(row.getString(9)))
										.banStatus(row.getString(10))
										.build());
					}
				} catch (ParseException e) {
					log.error("Error occurred while parsing date for ban list, date: {}.", row.getString(9));
				}
			}

			return cardInfoTracker.values();
		})));
	}



	public boolean isValidBanList(final String banListDate)
	{

		final String query = "select distinct ban_list_date from ban_lists where ban_list_date = :banListDate";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("banListDate", banListDate);


		final List<Object> results = Collections.singletonList(jdbcNamedTemplate.queryForList(query, sqlParams, Object.class));
		return results.size() == 1;

	}


	public Products getAllProductsByType(final ProductType productType, final String locale)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("productType", productType.toString().replaceAll("_", " "));
		System.out.println(sqlParams);
		System.out.println(DBQueryConstants.GET_AVAILABLE_PACKS);


		return jdbcNamedTemplate.query(DBQueryConstants.GET_AVAILABLE_PACKS, sqlParams, (ResultSet row) -> {
			final List<Product> availableProductsList = new ArrayList<>();

			while (row.next())
			{
				try {
					availableProductsList.add( Product
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

			return Products
					.builder()
					.products(availableProductsList)
					.build();
		});
	}



	public Map<String, Integer> getProductRarityCount(final String productId)
	{
		final MapSqlParameterSource queryParams = new MapSqlParameterSource();
		queryParams.addValue("productId", productId);

		return jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_RARITY_INFO, queryParams, (ResultSet row) -> {
			final Map<String, Integer> rarities = new HashMap<>();

			while (row.next())
			{
				rarities.put(row.getString(1), row.getInt(2));
			}

			return rarities;
		});
	}



	public Set<ProductContent> getProductContents(final String packId, final String locale)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("packId", packId);
		sqlParams.addValue("locale", locale);


		final Map<String, Set<String>> rarities = new HashMap<>();
		final Set<ProductContent> productContents = new LinkedHashSet<>(jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_CONTENT, sqlParams, (ResultSet row, int rowNum) -> {
			rarities.computeIfAbsent(row.getString(10), k -> new HashSet<>());
			rarities.get(row.getString(10)).add(row.getString(9));

			MonsterAssociation monsterAssociation = null;
			try {
				if (row.getString(18) != null)
					monsterAssociation = objectMapper.readValue(row.getString(18), MonsterAssociation.class);
			} catch (JsonProcessingException e) {
				log.error("Exception occurred when parsing monster association column, {}", e.toString());
				return null;
			}

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
					.monsterAssociation(monsterAssociation)
					.build();

			return ProductContent
					.builder()
					.productPosition(row.getString(8))
					.card(card)
					.build();
		}));

		for (ProductContent content: productContents)
		{
			content.setRarities(rarities.get(content.getCard().getCardID()));
		}
		return productContents;
	}


	public MonsterTypeStats getMonsterTypeStats(@NonNull final String cardColor)
	{
		final String query = "SELECT monster_type, count(*) AS 'Total' FROM card_info WHERE monster_type IS NOT NULL AND card_color = :cardColor GROUP BY monster_type ORDER BY monster_type";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardColor", cardColor);


		MonsterTypeStats monsterType = MonsterTypeStats.builder()
				.scope(cardColor)
				.monsterTypes(new HashMap<>())
				.build();

		jdbcNamedTemplate.query(query, sqlParams, (ResultSet row, int rowNum) -> {
			monsterType.getMonsterTypes().put(row.getString(1), row.getInt(2));

			return null;
		});

		return monsterType;
	}


	public DatabaseStats getDatabaseStats()
	{
		return jdbcNamedTemplate.queryForObject(DBQueryConstants.GET_DATABASE_TOTALS, (SqlParameterSource) null, (ResultSet row, int rowNum) -> DatabaseStats
				.builder()
				.productTotal(row.getInt(1))
				.cardTotal(row.getInt(2))
				.banListTotal(row.getInt(3))
				.yearsOfBanListCoverage(row.getInt(4))
				.build());
	}


	public Set<Product> getProductDetailsForCard(final String cardId)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);


		final Map<String, Map<String, Set<String>>> rarities = new HashMap<>();
		final Set<Product> products = new LinkedHashSet<>(jdbcNamedTemplate.query(DBQueryConstants.GET_PRODUCT_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
			final String productId = row.getString(1);
			final String cardPosition = row.getString(7);

			rarities.computeIfAbsent(productId, k -> new HashMap<>());

			rarities.get(productId).computeIfAbsent(cardPosition, k -> new HashSet<>());
			rarities.get(productId).get(cardPosition).add(row.getString(8));


			try {
				// TODO: Need to update code block to make sure packContent list contains all occurrences of the specified card, for instance a card can be found in the same pack more than once if it has different rarities within the same set.
				return Product
						.builder()
						.productId(productId)
						.productLocale(row.getString(2))
						.productName(row.getString(3))
						.productReleaseDate(dateFormat.parse(row.getString(4)))
						.productType((row.getString(5)))
						.productSubType(row.getString(6))
						.build();
			} catch (ParseException e) {
				log.error("Cannot parse date from DB when retrieving product info for card {} with exception: {}", cardId, e.toString());
				return null;
			}
		}));


		for (Product product: products)
		{
			product.setProductContent(new ArrayList<>());
			for(Map.Entry<String, Set<String>> cardPositionAndRarityMap: rarities.get(product.getProductId()).entrySet())
			{
				product.getProductContent().add(ProductContent
						.builder()
						.productPosition(cardPositionAndRarityMap.getKey())
						.rarities(cardPositionAndRarityMap.getValue())
						.build());
			}
		}
		return products;
	}


	public List<CardBanListStatus> getBanListDetailsForCard(final String cardId)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);

		return jdbcNamedTemplate.query(DBQueryConstants.GET_BAN_LIST_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
			try {
				return CardBanListStatus
						.builder()
						.banListDate(dateFormat.parse(row.getString(1)))
						.banStatus(row.getString(2))
						.build();
			} catch (ParseException e) {
				log.error("Cannot parse date from DB when retrieving ban list info for card {} with exception: {}", cardId, e.toString());
				return null;
			}
		});
	}


	private String transformCollectionToSQLOr(final Collection<String> monsterAssociationValueSet)
	{

		String monsterAssociationStr = "";

		if (!monsterAssociationValueSet.isEmpty())
		{
			monsterAssociationStr = String.join("|", monsterAssociationValueSet);
		}

		return monsterAssociationStr;

	}


	public CardBrowseResults getBrowseResults(@NonNull final Set<String> cardColors, @NonNull final Set<String> attributeSet, @NonNull final Set<String> monsterTypeSet
			, @NonNull final Set<String> monsterSubTypeSet, @NonNull final Set<String> monsterLevels, @NonNull Set<String> monsterRankSet, @NonNull Set<String> monsterLinkRatingsSet)
	{

		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		final String SQL_TEMPLATE = "SELECT card_number, card_name, card_color, monster_type, card_effect FROM card_info" +
				" WHERE card_color REGEXP :cardColors AND card_attribute REGEXP :attributes AND monster_type REGEXP :monsterTypes AND monster_type REGEXP :monsterSubTypes %s ORDER BY card_name";

		final String cardColorCriteria = (cardColors.isEmpty())? ".*" : String.join("|", cardColors);
		final String attributeCriteria = (attributeSet.isEmpty())? ".*" : String.join("|", attributeSet);
		final String monsterTypeCriteria = (monsterTypeSet.isEmpty())? ".*" : "^" + String.join("|", monsterTypeSet).replace("?", "\\?");
		final String monsterSubTypeCriteria = (monsterSubTypeSet.isEmpty())? ".*" : ".+/" + String.join("|", monsterSubTypeSet).replace("?", "\\?");

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardColors", cardColorCriteria);
		sqlParams.addValue("attributes", attributeCriteria);
		sqlParams.addValue("monsterTypes", monsterTypeCriteria);
		sqlParams.addValue("monsterSubTypes", monsterSubTypeCriteria);

		/*
			Only use where clause for card level if there is a criteria specified by user.
			Unlike other criteria, using the REGEX .* will not work as it will clash with other monster association JSON fields in DB.
		 */
		String monsterAssociationWhereClause;
		if (monsterLevels.isEmpty() && monsterRankSet.isEmpty() && monsterLinkRatingsSet.isEmpty())
		{
			monsterAssociationWhereClause = "";
		} else
		{

			monsterAssociationWhereClause = " AND monster_association REGEXP :monsterAssociation ";
			final String levelCriteria = transformCollectionToSQLOr(monsterLevels);
			final String rankCriteria = transformCollectionToSQLOr(monsterRankSet);
			final String linkRatingCriteria = transformCollectionToSQLOr(monsterLinkRatingsSet);
			final String monsterAssociationCriteria = StringUtil.concatenateStringsWithDelimiter("|", levelCriteria, rankCriteria, linkRatingCriteria);

			sqlParams.addValue("monsterAssociation", monsterAssociationCriteria);

		}

		final String sql = String.format(SQL_TEMPLATE, monsterAssociationWhereClause);
		log.debug("Fetching card browse results from DB using query: ( {} ) with sql params ( {} ).", sql, sqlParams);

		final CardBrowseResults cardBrowseResults =  CardBrowseResults
				.builder()
				.results(jdbcNamedTemplate.query(sql, sqlParams, (ResultSet row, int rowNum) -> Card
						.builder()
						.cardID(row.getString(BrowseQueryDefinition.CARD_ID.toString()))
						.cardName(row.getString(BrowseQueryDefinition.CARD_NAME.toString()))
						.cardColor(row.getString(BrowseQueryDefinition.CARD_COLOR.toString()))
						.monsterType(row.getString(BrowseQueryDefinition.MONSTER_TYPE.toString()))
						.cardEffect(row.getString(BrowseQueryDefinition.CARD_EFFECT.toString()))
						.build()))
				.build();

		stopWatch.stop();
		log.debug("Time taken to retrieve card browse results: {}ms", stopWatch.getTotalTimeMillis());
		return cardBrowseResults;

	}


	public Set<String> getCardColors()
	{

		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		final String sql = "SELECT card_color FROM card_colors WHERE card_color != 'Token'";

		log.debug("Retrieving unique card color values from DB using query {}", sql);
		final Set<String> cardColors =  new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> row.getString(1)));

		stopWatch.stop();
		log.debug("Time taken to retrieve unique card color values: {}ms", stopWatch.getTotalTimeMillis());
		return cardColors;
	}


	public Set<String> getMonsterAttributes()
	{

		final String sql = "SELECT DISTINCT card_attribute FROM cards WHERE card_attribute NOT IN ('Spell', 'Trap', '?') ORDER BY card_attribute";
		return new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> row.getString(1)));

	}

	public Set<String> getMonsterTypes()
	{

		final String sql = "SELECT DISTINCT SUBSTRING_INDEX(monster_type, '/', 1) AS monster_types FROM cards WHERE monster_type IS NOT NULL ORDER BY monster_types";
		return new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> row.getString(1)));

	}

	public Set<String> getMonsterSubTypes()
	{
		final String sql = "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(monster_type, '/', 2), '/', -1) AS monster_sub_types FROM cards WHERE monster_type IS NOT NULL ORDER BY monster_sub_types";

		final Set<String> monsterSubTypes =  new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> row.getString(1).split("/")[0]));
		final Set<String> cardColors = this.getCardColors();

		monsterSubTypes.removeAll(cardColors);
		monsterSubTypes.remove("Pendulum");	// removing pendulum individually as pendulum monster color/name is categorized by cards other color: eg  Pendulum-Normal, Pendulum-Fusion, etc
		return monsterSubTypes;
	}


	public Set<MonsterAssociation> getMonsterAssociationField(final String monsterAssociationField)
	{

//		Below query cannot be used on Remote Servers MySQL software due to version being outdated and having no access to update it - JSON functions are not supported.
//		final String sql = "SELECT CAST(level AS UNSIGNED) AS level FROM (SELECT DISTINCT JSON_EXTRACT(monster_association, '$.level') AS LEVEL FROM cards WHERE monster_association LIKE '%level%') AS levels ORDER BY level";

		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("monsterAssociationField", "%" + monsterAssociationField + "%");

		final String sql = "SELECT DISTINCT monster_association FROM cards WHERE monster_association LIKE :monsterAssociationField";


		final Set<MonsterAssociation> result = new HashSet<>(jdbcNamedTemplate.query(sql, sqlParams, (ResultSet row, int rowNum) -> {
			try {
				return objectMapper.readValue(row.getString(1), MonsterAssociation.class);
			} catch (JsonProcessingException e) {
				log.error(LogConstants.ERROR_READING_OBJECT_USING_OBJECT_MAPPER, e);
				return null;
			}
		}));

		stopWatch.stop();
		log.debug("Time taken to retrieve unique {} from DB was: {}ms", monsterAssociationField, stopWatch.getTotalTimeMillis());

		return result;

	}


	public Product getProductInfo(final String productId, final String locale)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("productId", productId);
		sqlParams.addValue("locale", locale);


		return jdbcNamedTemplate.queryForObject(DBQueryConstants.GET_PRODUCT_DETAILS, sqlParams, (ResultSet row, int rowNum) -> {

			try
			{
				return Product
						.builder()
						.productId(row.getString(1))
						.productLocale(row.getString(2))
						.productName(row.getString(3))
						.productReleaseDate(dateFormat.parse(row.getString(4)))
						.productTotal(row.getInt(5))
						.productType(row.getString(6))
						.productSubType(row.getString(7))
						.productRarityStats(this.getProductRarityCount(row.getString(1)))
						.productContent(new ArrayList<>())
						.build();
			} catch (Exception e)
			{

				log.error("Cannot parse date from DB when retrieving pack {} with exception: {}", productId, e.toString());
				return null;

			}

		});
	}


	public List<Product> getProductsByLocale(final String locale)
	{

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("locale", locale);

		return jdbcNamedTemplate.query(DBQueryConstants.GET_AVAILABLE_PRODUCTS_BY_LOCALE, sqlParams, (ResultSet row, int rowNum) -> {

			Product product = Product
					.builder()
					.productId(row.getString(ProductsTableDefinition.PRODUCT_ID.toString()))
					.productLocale(row.getString(ProductsTableDefinition.PRODUCT_LOCALE.toString()))
					.productName(row.getString(ProductsTableDefinition.PRODUCT_NAME.toString()))
					.productType(row.getString(ProductsTableDefinition.PRODUCT_TYPE.toString()))
					.productSubType(row.getString(ProductsTableDefinition.PRODUCT_SUB_TYPE.toString()))
					.productTotal(row.getInt(ProductViewDefinition.PRODUCT_CONTENT_TOTAL.toString()))
					.build();
			try
			{
				product.setProductReleaseDate(dateFormat.parse(row.getString(ProductsTableDefinition.PRODUCT_RELEASE_DATE.toString())));
			} catch(ParseException e)
			{

				log.error("Cannot parse date from DB when retrieving product {} with exception: {}", product.getProductId(), e.toString());
				return null;

			}

			return product;

		});

	}

}