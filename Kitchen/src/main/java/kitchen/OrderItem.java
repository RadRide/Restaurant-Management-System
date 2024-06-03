package kitchen;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderItem{

    private String itemName, comment;
    private int itemQuantity;
    private LocalDateTime date;
    private int status;

    //la 3ayetla
    //OrderItem a = new OrderItem(asdf,ads,2);
    public OrderItem(String itemName, String comment, int itemQuantity, LocalDateTime date, int status) {
        this.itemName = itemName;
        this.comment = comment;
        this.itemQuantity = itemQuantity;
        this.date = date;
        this.status = status;
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

        return "\t" + itemName + "\n\t(" + itemQuantity + ")";
    }

}



