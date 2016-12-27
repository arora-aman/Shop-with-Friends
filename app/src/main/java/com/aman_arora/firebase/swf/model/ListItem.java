package com.aman_arora.firebase.swf.model;

public class ListItem {

    private String itemName, owner;
    private boolean bought;
    private String boughtBy;

    public ListItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwner() {
        return owner;
    }

    public ListItem(String itemName, String owner) {
        this.itemName = itemName;
        this.owner = owner;
        this.bought = false;
        this.boughtBy = null;
    }

    public boolean isBought() {
        return bought;
    }

    public String getBoughtBy() {
        return boughtBy;
    }
}