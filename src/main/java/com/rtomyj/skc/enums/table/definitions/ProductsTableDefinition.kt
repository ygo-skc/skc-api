package com.rtomyj.skc.enums.table.definitions

enum class ProductsTableDefinition(
    private val columnName: String
) {
    PRODUCT_ID("product_id"),
    PRODUCT_LOCALE("product_locale"),
    PRODUCT_NAME("product_name"),
    PRODUCT_RELEASE_DATE("product_release_date"),
    PRODUCT_CONTENT_TOTAL("product_content_total"),
    PRODUCT_TYPE("product_type"),
    PRODUCT_SUB_TYPE("product_sub_type");

    override fun toString(): String = columnName
}