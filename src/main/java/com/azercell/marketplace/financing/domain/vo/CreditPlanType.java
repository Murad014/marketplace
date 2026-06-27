package com.azercell.marketplace.financing.domain.vo;

public enum CreditPlanType {
    DEFAULT,    // part of the standard offering — auto-applied to products that don't specify plans
    OPTIONAL    // opt-in — only applied to a product when explicitly assigned
}