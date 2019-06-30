package com.rtomyj.yugiohAPI.controller;

import com.rtomyj.yugiohAPI.repository.BanListRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/ban_list")
public class BanListController {
    @Autowired
    BanListRepository userRepo;

    @GetMapping
    public String check()
    {
        String cards = "{ \"cards\": [\n";
        int size = 3;

        for (int ind = 0; ind < size; ind++)
        {
            JSONObject card = new JSONObject();
            try
            {
                card.put("cardName", "Xtra Hero Wonder Driver");
                card.put("cardInfo", "Warrior Link Effect");
                card.put("cardEffect", "Summary");
                cards += card.toString();

                if (ind + 1 != size)
                {
                    cards += ", \n";
                }
            } catch (Exception e)
            {

            }
        }

        cards += "\n ] }";
        System.out.println(cards);
        return cards;
    }

}
