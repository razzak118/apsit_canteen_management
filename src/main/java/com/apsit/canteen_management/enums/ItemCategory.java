package com.apsit.canteen_management.enums;

import lombok.Getter;

@Getter
public enum ItemCategory {
    VEG("Veg"),
    NON_VEG("Non-Veg"),
    BEVERAGE("Beverage"),
    SNACK("Snack"),
    DESERT("Desert"),
    BREAKFAST("Breakfast");

    private final String displayName;

    ItemCategory(String displayName){
        this.displayName=displayName;
    }

}
