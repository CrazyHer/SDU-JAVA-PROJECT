package server.dataObjs;

import java.io.Serializable;

public class ItemData implements Serializable {
    String name, introduction, ownerID, itemID;
    int quantity;
    double price;
    boolean auction;

    public ItemData(String name, Double price, boolean auction, int quantity, String introduction, String ownerID) {//参数依次为商品名、价格、是否为拍卖形式（布林值）、数量、商品介绍、出售人的ID（学号）
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.introduction = introduction;
        this.auction = auction;
        this.ownerID = ownerID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public boolean isAuction() {
        return auction;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
