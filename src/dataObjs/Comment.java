package dataObjs;

import java.io.Serializable;

public class Comment implements Serializable {
    String itemName, text, authorID;

    public Comment(String itemName, String text, String authorID) {//依次为被评价商品的名称，评价文本内容，评价者的ID
        this.authorID = authorID;
        this.text = text;
        this.itemName = itemName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getText() {
        return text;
    }

    public String getItemName() {
        return itemName;
    }
}
