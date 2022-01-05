package com.rtomyj.skc.service.banlist;

import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.dao.Dao.Status;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListNewContent;
import com.rtomyj.skc.model.banlist.BanListRemovedContent;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DiffService
{
	private final Dao dao;


	@Autowired
	public DiffService(@Qualifier("jdbc") final Dao dao) {
		this.dao = dao;
	}



	public BanListNewContent getNewContentForGivenBanList(final String banListStartDate)
		throws YgoException {
		log.info("Fetching new content for ban list from DB w/ start date: ({}).", banListStartDate);

		if ( !dao.isValidBanList(banListStartDate) )
			throw new YgoException(String.format(ErrConstants.NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate), ErrorType.D001);


		// builds meta data object for new cards request
		final List<CardsPreviousBanListStatus> forbidden = dao.getNewContentOfBanList(banListStartDate, Status.FORBIDDEN);
		final List<CardsPreviousBanListStatus> limited = dao.getNewContentOfBanList(banListStartDate, Status.LIMITED);
		final List<CardsPreviousBanListStatus> semiLimited = dao.getNewContentOfBanList(banListStartDate, Status.SEMI_LIMITED);

		final BanListNewContent newCardsMeta = BanListNewContent.builder()
				.listRequested(banListStartDate)
				.comparedTo(this.getPreviousBanListDate(banListStartDate))
				.numNewForbidden(forbidden.size())
				.numNewLimited(limited.size())
				.numNewSemiLimited(semiLimited.size())
				.newForbidden(forbidden)
				.newLimited(limited)
				.newSemiLimited(semiLimited)
				.build();

		newCardsMeta.setLinks();

		return newCardsMeta;
	}


	public BanListRemovedContent getRemovedContentForGivenBanList(final String banListStartDate)
			throws YgoException {
		log.info("Fetching removed content for ban list from DB w/ start date: ( {} ).", banListStartDate);

		if ( !dao.isValidBanList(banListStartDate) )
			throw new YgoException(String.format(ErrConstants.NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE, banListStartDate), ErrorType.D001);


		final List<CardsPreviousBanListStatus> removedCards = dao.getRemovedContentOfBanList(banListStartDate);

		// builds meta data object for removed cards request
		final BanListRemovedContent removedCardsMeta = BanListRemovedContent.builder()
				.listRequested(banListStartDate)
				.comparedTo(this.getPreviousBanListDate(banListStartDate))
				.removedCards(removedCards)
				.numRemoved(removedCards.size())
				.build();
		removedCardsMeta.setLinks();


		return removedCardsMeta;
	}


	private String getPreviousBanListDate(final String banList)	{ return dao.getPreviousBanListDate(banList); }
}