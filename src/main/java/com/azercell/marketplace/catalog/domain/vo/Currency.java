package com.azercell.marketplace.catalog.domain.vo;

public enum Currency {
    AZN("azn"),
    USD("usd");

    final String value;

    Currency(String value){
        this.value = value;
    }
}
