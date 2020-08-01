package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.yugiohAPI.dao.DbQueryConstants;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.helper.enumeration.table.definitions.BrowseQueryDefinition;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.BrowseResults;
import com.rtomyj.yugiohAPI.model.MonsterAssociation;
import com.rtomyj.yugiohAPI.model.banlist.BanList;
import com.rtomyj.yugiohAPI.model.banlist.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.banlist.BanListStartDates;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterType;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.ProductContent;
import com.rtomyj.yugiohAPI.model.product.Products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
@Slf4j
public class JDBCDao implements Dao
{
	@Autowired
	private NamedParameterJdbcTemplate jdbcNamedTemplate;

	@Autowired
	@Qualifier("dbSimpleDateFormat")
	private SimpleDateFormat dateFormat;
	private int numUniqueCardsParsed;

	@Autowired
	private ObjectMapper objectMapper;


	@Override
	public BanListStartDates getBanListStartDates()
	{
		return null;
	}



	@Override
	public Card getCardInfo(@NonNull String cardID) throws YgoException
	{
		final String query = DbQueryConstants.GET_CARD_BY_ID;

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardID);


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


				final Card desiredCard = Card
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

				return desiredCard;
			}

			return null;
		});

		if (card == null)	throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format("%s was not found in DB.", cardID));

		return card;
	}



	@Override
	public List<Card> getBanListByBanStatus(@NonNull final String date, @NonNull final Status status)
	{
		final String query = DbQueryConstants.GET_BAN_LIST_BY_STATUS;

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
		return jdbcNamedTemplate.query(query, (ResultSet row) ->  {
			if (row.next())	return row.getInt(1);

			return null;
		});
	}



	public int getBanListPosition(String banListDate)
	{
		String query = new StringBuilder().append("SELECT row_num FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date")
			.append(" FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC) AS dates, (SELECT @row_num:=0) counter)")
			.append(" AS sorted WHERE ban_list_date = :banListDate")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("banListDate", banListDate);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return (int) Float.parseFloat(row.getString(1));	// somehow row_num is treated as a float

			return -1;
		});
	}



	public String getPreviousBanListDate(String currentBanList)
	{
		int currentBanListPosition = this.getBanListPosition(currentBanList);
		if (currentBanListPosition <= 1)	return "";
		int previousBanListPosition = currentBanListPosition - 1;

		String query = new StringBuilder()
			.append("SELECT ban_list_date FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date")
			.append(" FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC)")
			.append(" AS dates, (SELECT @row_num:=0) counter) AS sorted where row_num = :previousBanListPosition")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("previousBanListPosition", previousBanListPosition);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<BanListComparisonResults> getNewContentOfBanList(final String newBanList, final Status status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select new_cards.card_number, cards.card_name from (select new_list.card_number")
			.append(" from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)")
			.append(" as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList")
			.append(" and ban_status = :status) as old_list on new_list.card_number = old_list.card_number")
			.append(" where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number")
			.append(" ORDER BY cards.card_name")
			.toString();

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("status", status.toString());
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<BanListComparisonResults> newCards = new ArrayList<>();

			while (row.next())
			{
				BanListComparisonResults banListComparisonResults = new BanListComparisonResults();
				final String cardID = row.getString(1);
				String previousStatus = this.getCardBanListStatusByDate(cardID, oldBanList);
				previousStatus = ( previousStatus == null ) ? "Unlimited" : previousStatus;


				banListComparisonResults.setId(cardID);
				banListComparisonResults.setPreviousState(previousStatus);
				banListComparisonResults.setName(row.getString(2));

				newCards.add(banListComparisonResults);
			}

			return newCards;
		});
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<BanListComparisonResults> getRemovedContentOfBanList(String newBanList)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select removed_cards.card_number, removed_cards.ban_status, cards.card_name")
			.append(" from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists")
			.append(" where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status")
			.append(" from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number")
			.append(" where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number")
			.append(" ORDER BY cards.card_name")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<BanListComparisonResults> REMOVED_CARDS = new ArrayList<>();

			while(row.next())
			{
				final BanListComparisonResults REMOVED_CARD = new BanListComparisonResults();

				REMOVED_CARD.setId(row.getString(1));
				REMOVED_CARD.setPreviousState(row.getString(2));
				REMOVED_CARD.setName(row.getString(3));

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
			String cardId
			, String cardName
			, String cardAttribute
			, String cardColor
			, String monsterType
			, final int limit
	)
	{
		cardId = '%' + cardId + '%';
		cardName = '%' + cardName + '%';

		cardAttribute = (cardAttribute.isEmpty())? ".*" : cardAttribute;
		cardColor = (cardColor.isEmpty())? ".*" : cardColor;
		monsterType = (monsterType.isEmpty())? ".*" : monsterType;


		final String query = new StringBuilder()
				.append("SELECT card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, ban_list_date, ban_status")
				.append(" FROM search")
				.append(" WHERE card_number LIKE :cardId")
				.append(" AND card_name LIKE :cardName")
				.append(" AND card_attribute REGEXP :cardAttribute")
				.append(" AND card_color REGEXP :cardColor")
				.append(" AND IFNULL(monster_type, '') REGEXP :monsterType")
				.append(" ORDER BY color_id, card_name ASC")
				.toString();
		log.info(query);


		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("cardName", cardName);
		sqlParams.addValue("cardAttribute", cardAttribute);
		sqlParams.addValue("cardColor", cardColor);
		sqlParams.addValue("monsterType", monsterType);

		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
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

			while (row.next())
			{
				Card card = cardInfoTracker.get(row.getString(1));

				if (card == null)
				{
					if (numUniqueCardsParsed == limit)	break;

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

				try
				{
					if (row.getString(9) != null)
					{
						card.getRestrictedIn()
							.add(BanList
								.builder()
								.banListDate(dateFormat.parse(row.getString(9)))
								.banStatus(row.getString(10))
								.build());
					}
				} catch (ParseException e)
				{
					log.error("Error occurred while parsing date for ban list, date: {}", row.getString(9));
				}
			}

			return cardInfoTracker.values();
		}).stream().collect(Collectors.toList());
	}



	public boolean isValidBanList(final String banListDate)
	{
		final String query = "select distinct ban_list_date from ban_lists where ban_list_date = :banListDate";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("banListDate", banListDate);

		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return true;
			else	return false;
		});
	}


	public Products getAllProductsByType(final ProductType productType, final String locale)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("productType", productType.toString().replaceAll("_", " "));
		System.out.println(sqlParams);
		System.out.println(DbQueryConstants.GET_AVAILABLE_PACKS);


		return jdbcNamedTemplate.query(DbQueryConstants.GET_AVAILABLE_PACKS, sqlParams, (ResultSet row) -> {
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
							.productRarityCount(this.getProductRarityCount(row.getString(1)))
							.build());
				} catch (ParseException e) {
					log.error("Cannot parse date from DB when retrieving all packs with exception: {}", e.toString());
				}
			}

			final Products products = Products
					.builder()
					.products(availableProductsList)
					.build();
			return products;
		});
	}



	public Map<String, Integer> getProductRarityCount(final String productId)
	{
		final MapSqlParameterSource queryParams = new MapSqlParameterSource();
		queryParams.addValue("productId", productId);

		return jdbcNamedTemplate.query(DbQueryConstants.GET_PRODUCT_RARITY_INFO, queryParams, (ResultSet row) -> {
			final Map<String, Integer> rarities = new HashMap<>();

			while (row.next())
			{
				rarities.put(row.getString(1), row.getInt(2));
			}

			return rarities;
		});
	}



	public Product getPackContents(final String packId, final String locale)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("packId", packId);
		sqlParams.addValue("locale", locale);

		return jdbcNamedTemplate.query(DbQueryConstants.GET_PRODUCT_DETAILS, sqlParams, (ResultSet row) -> {
			Product pack = null;

			while (row.next())
			{
				if (pack == null)
				{
					try {
						pack = Product
								.builder()
								.productId(row.getString(1))
								.productLocale(row.getString(2))
								.productName(row.getString(3))
								.productReleaseDate(dateFormat.parse(row.getString(4)))
								.productTotal(row.getInt(5))
								.productType(row.getString(6))
								.productSubType(row.getString(7))
								.productRarityCount(this.getProductRarityCount(row.getString(1)))
								.productContent(new ArrayList<ProductContent>())
								.build();
					} catch (Exception e) {
						log.error("Cannot parse date from DB when retrieving pack {} with exception: {}", packId, e.toString());
					}
				}

				MonsterAssociation monsterAssociation = null;
				try
				{
					if (row.getString(18) != null)  monsterAssociation = objectMapper.readValue(row.getString(18), MonsterAssociation.class);
				} catch (JsonProcessingException e)
				{
					log.error("Exception occurred when parsing monster association column, {}", e.toString());
					return null;
				}

				pack.getProductContent()
						.add(ProductContent.builder()
							.position(row.getString(8))
							.rarity(row.getString(9))
							.card(Card
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
									.build()
							).build()
							);
			}

			return pack;
		});
	}


	public MonsterType getMonsterTypeStats(@NonNull final String cardColor)
	{
		final String query = "SELECT monster_type, count(*) AS 'Total' FROM card_info WHERE monster_type IS NOT NULL AND card_color = :cardColor GROUP BY monster_type ORDER BY monster_type";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardColor", cardColor);


		MonsterType monsterType = MonsterType.builder()
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
		return jdbcNamedTemplate.queryForObject(DbQueryConstants.GET_DATABASE_TOTALS, (SqlParameterSource) null, (ResultSet row, int rowNum) -> {
			return DatabaseStats
					.builder()
					.productTotal(row.getInt(1))
					.cardTotal(row.getInt(2))
					.banListTotal(row.getInt(3))
					.yearsOfBanListCoverage(row.getInt(4))
					.build();
		});
	}


	public List<Product> getProductDetailsForCard(final String cardId)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);

		return jdbcNamedTemplate.query(DbQueryConstants.GET_PRODUCT_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
			try {
				// TODO: Need to update code block to make sure packContent list contains all occurences of the specified card, for instance a card can be found in the same pack more than once if it has different rarities within the same set.
				return Product
						.builder()
						.productId(row.getString(1))
						.productLocale(row.getString(2))
						.productName(row.getString(3))
						.productReleaseDate(dateFormat.parse(row.getString(4)))
						.productType((row.getString(5)))
						.productSubType(row.getString(6))
						.productContent(new ArrayList(
								Arrays.asList(ProductContent
										.builder()
										.position(row.getString(7))
										.rarity(row.getString(8))
										.build())))
						.build();
			} catch (ParseException e) {
				log.error("Cannot parse date from DB when retrieving product info for card {} with exception: {}", cardId, e.toString());
				return null;
			}
		});
	}


	public List<BanList> getBanListDetailsForCard(final String cardId)
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);

		return jdbcNamedTemplate.query(DbQueryConstants.GET_BAN_LIST_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
			try {
				return BanList
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


	public BrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> monsterLevels)
	{
		final String sql = "SELECT card_name, card_color, card_effect FROM card_info WHERE card_color REGEXP :cardColors AND monster_association REGEXP :monsterLevels order by card_name";
		final String cc = (cardColors.isEmpty())? ".*" : String.join("|", cardColors);
		final String mm = (monsterLevels.isEmpty())? ".*" : String.join("|", monsterLevels);

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardColors", cc);
		sqlParams.addValue("monsterLevels", mm);


		return BrowseResults
				.builder()
				.results(jdbcNamedTemplate.query(sql, sqlParams, (ResultSet row, int rowNum) -> {
					return Card
							.builder()
							.cardName(row.getString(BrowseQueryDefinition.CARD_NAME.toString()))
							.cardColor(row.getString(BrowseQueryDefinition.CARD_COLOR.toString()))
							.cardEffect(row.getString(BrowseQueryDefinition.CARD_EFFECT.toString()))
							.build();
				}))
				.build();
	}


	public Set<String> getCardColors()
	{

		final String sql = "SELECT card_color FROM card_colors";

		return new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> {
			return row.getString(1);
		}));

	}


	public Set<String> getMonsterAttributes()
	{

		final String sql = "SELECT DISTINCT card_attribute FROM cards WHERE card_attribute NOT IN ('Spell', 'Trap', '?') ORDER BY card_attribute";

		return new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> {
			return row.getString(1);
		}));

	}


	public Set<Integer> getLevels()
	{

		final String sql = "SELECT CAST(level AS UNSIGNED) AS level FROM (SELECT DISTINCT JSON_EXTRACT(monster_association, '$.level') AS LEVEL FROM cards WHERE monster_association LIKE '%level%') AS levels ORDER BY level";

		return new LinkedHashSet<>(jdbcNamedTemplate.query(sql, (ResultSet row, int rowNum) -> {
			return row.getInt(1);
		}));

	}

}