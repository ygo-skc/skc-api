package com.rtomyj.yugiohAPI.helper.enumeration.table.definitions;

public enum ProductsTableDefinition
{

    PRODUCT_ID("product_id")
    , PRODUCT_LOCALE("product_locale")
    , PRODUCT_NAME("product_name")
    , PRODUCT_RELEASE_DATE("product_release_date")
    , PRODUCT_TYPE("product_type")
    , PRODUCT_SUB_TYPE("product_sub_type");


    private ProductsTableDefinition(final String columnName) { this.columnName = columnName; }


    private String columnName;


    @Override
    public String toString() {
        return columnName;
    }

}
