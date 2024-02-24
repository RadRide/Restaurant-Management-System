package order;

import menu.Dish;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderItem {

    private String comment;
    private int itemQuantity;
    private Dish item;
    private OrderItemBox box;
    private LocalDateTime date;
    private boolean isNew;

    public OrderItem(Dish item){
        this.item = item;
        this.comment = "";
        itemQuantity = 1;
        this.isNew = true;
    }

    public OrderItem(Dish item, String comment, int itemQuantity, LocalDateTime date){
        this.item = item;
        this.comment = comment;
        this.itemQuantity = itemQuantity;
        this.date = date;
        this.isNew = false;
    }

    public OrderItemBox getBox() {
        return box;
    }

    public void increment(){
        box.getQuantitySpinner().getValueFactory().increment(1);
        itemQuantity++;
    }

    public void setBox(OrderItemBox box) {
        this.box = box;
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

    public Dish getItem() {
        return item;
    }
    public void setItem(Dish item) {
        this.item = item;
    }

    public double getTotalPrice() {
        return item.getPrice() * itemQuantity;
    }

    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
