package com.rtomyj.yugiohAPI.helper.enumeration.table.definitions;

public enum ProductViewDefinition
{
    PRODUCT_CONTENT_TOTAL("product_content_total");


    private ProductViewDefinition(final String columnName) { this.columnName = columnName; }


    private String columnName;


    @Override
    public String toString() {
        return columnName;
    }
}
