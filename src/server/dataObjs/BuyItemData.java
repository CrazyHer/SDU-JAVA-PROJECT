package server.dataObjs;

public class BuyItemData {
    String buyerID, itemName;

    public BuyItemData(String buyerID, String itemName) {//依次为买家ID，所购物品名称
        this.buyerID = buyerID;
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBuyerID() {
        return buyerID;
    }
}
