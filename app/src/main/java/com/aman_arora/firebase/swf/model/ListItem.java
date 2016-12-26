package com.aman_arora.firebase.swf.model;

public class ListItem {

    private String itemName, owner;

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
    }
}
