package com.rtomyj.skc.dao.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.constant.DBQueryConstants;
import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.enums.table.definitions.BrowseQueryDefinition;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.card.CardBrowseResults;
import com.rtomyj.skc.model.card.MonsterAssociation;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;
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
import java.util.Arrays;
import java.util.Collection;
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
						.monsterAssociation(MonsterAssociation.parseDBString(row.getString(8), objectMapper))
						.build();
			}

			return null;
		});

		if (card == null) throw new YgoException(String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, cardID), ErrorType.D001);

		return card;

	}



	public List<Card> searchForCardWithCriteria(
			String cardId, String cardName, String cardAttribute, String cardColor, String monsterType, final int limit, final boolean getBanInfo
	)
	{
		return (getBanInfo)? this.searchForCardsIncludeBanInfo(cardId, cardName, cardAttribute, cardColor, monsterType, limit) : this.searchForCards(cardId, cardName, cardAttribute, cardColor, monsterType, limit);
	}

	private void prepSearchParams(String cardId, String cardName, String cardAttribute, String cardColor, String monsterType, final int limit, final MapSqlParameterSource sqlParams)
	{
		cardId = '%' + cardId + '%';
		cardName = '%' + cardName + '%';
		cardAttribute = (cardAttribute.isEmpty())? ".*" : cardAttribute;
		cardColor = (cardColor.isEmpty())? ".*" : cardColor;
		monsterType = (monsterType.isEmpty())? ".*" : monsterType;

		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("cardName", cardName);
		sqlParams.addValue("cardAttribute", cardAttribute);
		sqlParams.addValue("cardColor", cardColor);
		sqlParams.addValue("monsterType", monsterType);
		sqlParams.addValue("limit", limit);
	}


	private List<Card> searchForCards(String cardId, String cardName, String cardAttribute, String cardColor, String monsterType, final int limit)
	{

		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();


		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		final String query = DBQueryConstants.SEARCH_QUERY;
		prepSearchParams(cardId, cardName, cardAttribute, cardColor, monsterType, limit, sqlParams);


		log.debug("Fetching card search results from DB using query: ( {} ) with sql params ( {} ).", query, sqlParams);

		final ArrayList<Card> searchResults = new ArrayList<>(Objects.requireNonNull(jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final Map<String, Card> cardInfoTracker = new LinkedHashMap<>();

			while (row.next()) {
				Card card = cardInfoTracker.get(row.getString(1));

				if (card == null) {

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

				}
			}

			return cardInfoTracker.values();
		})));

		stopwatch.stop();
		log.debug("Time taken to fetch search results from DB: {}ms", stopwatch.getTotalTimeMillis());

		return searchResults;
	}


	private List<Card> searchForCardsIncludeBanInfo(String cardId, String cardName, String cardAttribute, String cardColor, String monsterType, final int limit)
	{

		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();


		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		final String query = DBQueryConstants.SEARCH_QUERY_WITH_BAN_INFO;
		prepSearchParams(cardId, cardName, cardAttribute, cardColor, monsterType, limit, sqlParams);

		log.debug("Fetching card search results from DB using query: ( {} ) with sql params ( {} ).", query, sqlParams);

		final ArrayList<Card> searchResults = new ArrayList<>(Objects.requireNonNull(jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
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
				numUniqueCardsParsed++;
			}

			return cardInfoTracker.values();
		})));

		stopwatch.stop();
		log.debug("Time taken to fetch search results from DB: {}ms", stopwatch.getTotalTimeMillis());

		return searchResults;
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

		final String SQL_TEMPLATE = DBQueryConstants.GET_CARD_BROWSE_RESULTS;

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

			final List<String> monsterAssociationCriteriaList = new ArrayList<>(Arrays.asList(levelCriteria, rankCriteria, linkRatingCriteria));
			monsterAssociationCriteriaList.removeAll(Arrays.asList(null, ""));
			final String monsterAssociationCriteria = String.join("+", monsterAssociationCriteriaList);

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
						.cardAttribute(row.getString(BrowseQueryDefinition.CARD_ATTRIBUTE.toString()))
						.monsterAssociation(
								MonsterAssociation.parseDBString(
										row.getString(BrowseQueryDefinition.MONSTER_ASSOCIATION.toString()), objectMapper)
						)
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
		monsterSubTypes.remove("Pendulum");	// removing pendulum individually as pendulum monster color/name is categorized by cards other color: e.g.  Pendulum-Normal, Pendulum-Fusion, etc
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
			return MonsterAssociation.parseDBString(row.getString(1), objectMapper);
		}));

		stopWatch.stop();
		log.debug("Time taken to retrieve unique {} from DB was: {}ms", monsterAssociationField, stopWatch.getTotalTimeMillis());

		return result;

	}
}