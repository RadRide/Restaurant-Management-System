package order;

public class OrderItem {

    private String itemName, comment;
    private int itemQuantity;

    public OrderItem(String itemName, String comment, int itemQuantity) {
        this.itemName = itemName;
        this.comment = comment;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String toString(){
        return itemName + " (" + itemQuantity + ")";
    }
}
