package com.myapps.easybusiness.FachLogic;

public class Item {

    private  String itemTitle;
    private  String itemDate;
    private  String itemPrice;
    private  int itemImageFlag;
    private  int itemImageDeleteButton;

    public Item(String itemTitle, String itemDate, String itemPrice, int itemImageFlag ,int itemImageDeleteButton) {
        this.itemTitle = itemTitle;
        this.itemDate = itemDate;
        this.itemPrice = itemPrice;
        this.itemImageFlag = itemImageFlag;
        this.itemImageDeleteButton = itemImageDeleteButton;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public int getItemImageFlag() {
        return itemImageFlag;
    }

    public String getItemDate() {
        return itemDate;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getItemImageDeleteButton() {
        return itemImageDeleteButton;
    }
}
