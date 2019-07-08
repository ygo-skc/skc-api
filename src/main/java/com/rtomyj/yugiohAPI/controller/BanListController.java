package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.Card;
import com.rtomyj.yugiohAPI.repository.BanListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/ban_list")
public class BanListController {
    @Autowired
    BanListRepository banListRepository;

    @GetMapping
    public String check()
    {
        String cards = "{ \"bannedCards\": {\n";
        ArrayList<Card> bannedCards = (ArrayList) banListRepository.getForbiddenCards();

        /*  Forbidden cards */
        cards += "\"forbidden\": [\n";

        for (int ind = 0; ind < bannedCards.size(); ind++)
        {
            cards += bannedCards.get(ind).toJSON();

            if (ind + 1 != bannedCards.size())  cards += ", \n";
        }
        cards += "\n ] ";

        /*  Limited cards */
        cards += ", \"limited\": [\n";

        bannedCards = (ArrayList) banListRepository.getLimitedCards();
        for (int ind = 0; ind < bannedCards.size(); ind++)
        {
            cards += bannedCards.get(ind).toJSON();

            if (ind + 1 != bannedCards.size())  cards += ", \n";
        }
        cards += "\n ] ";

        /*  Semi-Limited cards */
        cards += ", \"semiLimited\": [\n";

        bannedCards = (ArrayList) banListRepository.getSemiLimitedCards();
        for (int ind = 0; ind < bannedCards.size(); ind++)
        {
            cards += bannedCards.get(ind).toJSON();

            if (ind + 1 != bannedCards.size())  cards += ", \n";
        }
        cards += "\n ] ";

        cards += "\n } }";
        System.out.println(cards);
        return cards;
    }

}
