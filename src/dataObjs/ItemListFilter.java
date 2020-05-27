package dataObjs;

import java.io.Serializable;

public class ItemListFilter implements Serializable {
    public static final int ORDER_BY_DEFAULT = 0;
    public static final int ORDER_BY_SALE = 1;
    public static final int ORDER_BY_PRICE = 2;
    String keyWord;
    int ODER_TYPE;

    public ItemListFilter(String keyWord, int ODER_TYPE) {//依次为搜索关键字（不搜索请输入通配符*）、排序类型，输入上面的常量名即可
        this.keyWord = keyWord;
        this.ODER_TYPE = ODER_TYPE;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getODER_TYPE() {
        switch (ODER_TYPE){
            case 1:
                return "sale";
            case 2:
                return "ItemPrice";
            default:
                return "ItemID";
        }
    }
}
