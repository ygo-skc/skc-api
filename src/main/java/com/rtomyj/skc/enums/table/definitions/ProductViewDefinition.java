package com.rtomyj.skc.enums.table.definitions;

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
