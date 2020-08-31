package com.rtomyj.yugiohAPI.dao.database.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.dao.database.Dao.Status;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.model.card.Card;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")	// Loading test props with H2 in memory DB configurations
@SqlGroup({
	@Sql("classpath:drop.sql")
	, @Sql("classpath:schema.sql")
	, @Sql("classpath:data.sql")
	, @Sql("classpath:views.sql")
})
public class JdbcDaoTest
{

	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	private static Card stratosTestCard;
	private static Card aHeroLivesTestCard;
	private static Card dMaliTestCard;

	private final String TEST_BAN_LIST_DATE = "2015-11-09";


	@BeforeAll
	public static void before()
	{
		stratosTestCard = Card
			.builder()
			.cardID(TestConstants.STRATOS_ID)
			.cardName(TestConstants.STRATOS_NAME)
			.monsterType(TestConstants.STRATOS_TYPE)
			.cardColor(TestConstants.STRATOS_COLOR)
			.cardAttribute(TestConstants.STRATOS_ATTRIBUTE)
			.monsterAttack(TestConstants.STRATOS_ATK)
			.monsterDefense(TestConstants.STRATOS_DEF)
			.cardEffect(TestConstants.STRATOS_FULL_EFFECT)
			.build();

		aHeroLivesTestCard = Card
			.builder()
			.cardID(TestConstants.A_HERO_LIVES_ID)
			.cardName(TestConstants.A_HERO_LIVES_NAME)
			.monsterType(TestConstants.A_HERO_LIVES_TYPE)
			.cardColor(TestConstants.A_HERO_LIVES_COLOR)
			.cardAttribute(TestConstants.A_HERO_LIVES_ATTRIBUTE)
			.monsterAttack(TestConstants.A_HERO_LIVES_ATK)
			.monsterDefense(TestConstants.A_HERO_LIVES_DEF)
			.cardEffect(TestConstants.A_HERO_LIVES_FULL_EFFECT)
			.build();

		dMaliTestCard = Card
			.builder()
			.cardID(TestConstants.D_MALICIOUS_ID)
			.cardName(TestConstants.D_MALICIOUS_NAME)
			.monsterType(TestConstants.D_MALICIOUS_TYPE)
			.cardColor(TestConstants.D_MALICIOUS_COLOR)
			.cardAttribute(TestConstants.D_MALICIOUS_ATTRIBUTE)
			.monsterAttack(TestConstants.D_MALICIOUS_ATK)
			.monsterDefense(TestConstants.D_MALICIOUS_DEF)
			.cardEffect(TestConstants.D_MALICIOUS_FULL_EFFECT)
			.build();
	}



	@Test
	public void testFetchingCardById_Success()
		throws YgoException, SQLException
	{
		final Card stratosDbResult = dao.getCardInfo(stratosTestCard.getCardID());
		final Card aHeroLivesDbResult = dao.getCardInfo(aHeroLivesTestCard.getCardID());
		final Card dMaliDbResult = dao.getCardInfo(dMaliTestCard.getCardID());


		assertNotEquals(null, stratosDbResult);

		assertEquals(stratosTestCard.getCardID(), stratosDbResult.getCardID());
		assertEquals(stratosTestCard.getCardName(), stratosDbResult.getCardName());
		assertEquals(stratosTestCard.getMonsterType(), stratosDbResult.getMonsterType());
		assertEquals(stratosTestCard.getCardColor(), stratosDbResult.getCardColor());
		assertEquals(stratosTestCard.getCardAttribute(), stratosDbResult.getCardAttribute());
		assertEquals(stratosTestCard.getMonsterAttack(), stratosDbResult.getMonsterAttack());
		assertEquals(stratosTestCard.getMonsterDefense(), stratosDbResult.getMonsterDefense());
		assertEquals(stratosTestCard.getCardEffect(), stratosDbResult.getCardEffect());


		assertNotEquals(null, aHeroLivesDbResult);

		assertEquals(aHeroLivesTestCard.getCardID(), aHeroLivesDbResult.getCardID());
		assertEquals(aHeroLivesTestCard.getCardName(), aHeroLivesDbResult.getCardName());
		assertEquals(aHeroLivesTestCard.getMonsterType(), aHeroLivesDbResult.getMonsterType());
		assertEquals(aHeroLivesTestCard.getCardColor(), aHeroLivesDbResult.getCardColor());
		assertEquals(aHeroLivesTestCard.getCardAttribute(), aHeroLivesDbResult.getCardAttribute());
		assertEquals(aHeroLivesTestCard.getMonsterAttack(), aHeroLivesDbResult.getMonsterAttack());
		assertEquals(aHeroLivesTestCard.getMonsterDefense(), aHeroLivesDbResult.getMonsterDefense());
		assertEquals(aHeroLivesTestCard.getCardEffect(), aHeroLivesDbResult.getCardEffect());


		assertNotEquals(null, dMaliDbResult);

		assertEquals(dMaliTestCard.getCardID(), dMaliDbResult.getCardID());
		assertEquals(dMaliTestCard.getCardName(), dMaliDbResult.getCardName());
		assertEquals(dMaliTestCard.getMonsterType(), dMaliDbResult.getMonsterType());
		assertEquals(dMaliTestCard.getCardColor(), dMaliDbResult.getCardColor());
		assertEquals(dMaliTestCard.getCardAttribute(), dMaliDbResult.getCardAttribute());
		assertEquals(dMaliTestCard.getMonsterAttack(), dMaliDbResult.getMonsterAttack());
		assertEquals(dMaliTestCard.getMonsterDefense(), dMaliDbResult.getMonsterDefense());
		assertEquals(dMaliTestCard.getCardEffect(), dMaliDbResult.getCardEffect());
	}



	@Test
	public void testFetchingCardById_Failure()
		throws YgoException, SQLException
	{
		assertThrows(NullPointerException.class, () -> dao.getCardInfo(null));
		assertThrows(YgoException.class, () -> dao.getCardInfo("12345678"));
	}



	@Test
	public void testFetchingBanListByStatus_Success()
		throws YgoException, SQLException
	{
		final List<Card> forbiddenDbResult = dao.getBanListByBanStatus(TEST_BAN_LIST_DATE, Status.FORBIDDEN);
		final List<Card> limitedDbResult = dao.getBanListByBanStatus(TEST_BAN_LIST_DATE, Status.LIMITED);
		final List<Card> semiLimitedDbResult = dao.getBanListByBanStatus(TEST_BAN_LIST_DATE, Status.SEMI_LIMITED);


		assertNotEquals(null, forbiddenDbResult);
		assertNotEquals(null, limitedDbResult);
		assertNotEquals(null, semiLimitedDbResult);
		System.out.println(dao.getCardInfo(stratosTestCard.getCardID()));
		System.out.println(forbiddenDbResult);
		System.out.println(limitedDbResult);
		System.out.println(semiLimitedDbResult);

		assertEquals(1, forbiddenDbResult.size());
		assertEquals(2, limitedDbResult.size());
		assertEquals(0, semiLimitedDbResult.size());

		assertEquals(stratosTestCard.getCardName(), forbiddenDbResult.get(0).getCardName());
		assertEquals(dMaliTestCard.getCardName(), limitedDbResult.get(0).getCardName());
		assertEquals(aHeroLivesTestCard.getCardName(), limitedDbResult.get(1).getCardName());
	}

}