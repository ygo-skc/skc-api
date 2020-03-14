package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.sql.SQLException;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.TestConstants;
import com.rtomyj.yugiohAPI.model.Card;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class JdbcDaoTest
{
	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	private static Card stratos;


	@BeforeAll
	public static void before()
	{
		stratos = Card
			.builder()
			.cardID(TestConstants.STRATOS_ID)
			.cardName(TestConstants.STRATOS_NAME)
			.monsterType(TestConstants.STRATOS_TYPE)
			.cardColor(TestConstants.STRATOS_COLOR)
			.cardAttribute(TestConstants.STRATOS_ATTRIBUTE)
			.monsterAttack(TestConstants.STRATOS_ATK)
			.monsterDefense(TestConstants.STRATOS_DEF)
			.cardEffect(TestConstants.STRATOS_TRIMMED_EFFECT)
			.build();
	}



	@Test
	public void testFetchingCardById_Success() throws YgoException, SQLException
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", stratos.getCardID());


		System.out.println(dao.getCardInfo(stratos.getCardID()));
	}



	public void testFetchingCardById_Failure() throws YgoException
	{
		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", stratos.getCardID());


		dao.getCardInfo(stratos.getCardID());
	}
}