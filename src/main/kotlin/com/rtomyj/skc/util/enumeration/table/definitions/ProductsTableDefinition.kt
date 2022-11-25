package com.rtomyj.skc.util.enumeration.table.definitions

enum class ProductsTableDefinition(
    private val columnName: String
) {
    PRODUCT_ID("product_id"),
    PRODUCT_LOCALE("product_locale"),
    PRODUCT_NAME("product_name"),
    PRODUCT_RELEASE_DATE("product_release_date"),
    PRODUCT_CONTENT_TOTAL("product_content_total"),
    PRODUCT_TYPE("product_type"),
    PRODUCT_SUB_TYPE("product_sub_type"),
    PRODUCT_POSITION("product_position"),
    CARD_RARITY("card_rarity"),
    CARD_ID("card_number");

    override fun toString(): String = columnName
}