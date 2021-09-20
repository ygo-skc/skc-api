package com.rtomyj.skc.helper.enumeration.table.definitions;

public enum ProductViewDefinition
{
    PRODUCT_CONTENT_TOTAL("product_content_total");


    ProductViewDefinition(final String columnName) { this.columnName = columnName; }


    private final String columnName;


    @Override
    public String toString() {
        return columnName;
    }
}
