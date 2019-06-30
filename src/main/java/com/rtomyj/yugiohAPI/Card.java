package com.rtomyj.yugiohAPI;

public class Card {

    public Card(String card_name, String rarity, int quantity)
    {
        this.card_name = card_name;
        this.rarity = rarity;
        this.quantity = quantity;
    }

    private String card_name, rarity;
    private int quantity;

    public String toString()
    {
        return String.join(" | ", card_name, rarity, Integer.toString(quantity));
    }
}
