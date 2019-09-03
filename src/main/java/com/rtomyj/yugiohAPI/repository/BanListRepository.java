package com.rtomyj.yugiohAPI.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class BanListRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<String> getBanListStartDates() {
		//SELECT DISTINCT ban_list_date from ban_lists;
		return  jdbcTemplate.query("SELECT DISTINCT ban_list_date from ban_lists ORDER BY ban_list_date DESC;",
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet row) throws SQLException, DataAccessException {
						List<String> banListDates = new ArrayList<>();
						while (row.next()) {
							banListDates.add(row.getString(1));
						}
						return banListDates;
					}
				});

	}
}
