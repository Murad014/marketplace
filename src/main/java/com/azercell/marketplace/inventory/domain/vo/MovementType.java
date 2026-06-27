package com.azercell.marketplace.inventory.domain.vo;

public enum MovementType {
    RESTOCK,      // stock added (initial seed, purchase order)
    SALE,         // stock shipped/sold
    RESERVE,      // units reserved against an order
    RELEASE,      // previously reserved units released
    CORRECTION,   // manual stock-take adjustment
    TRANSFER      // moved between warehouses
}