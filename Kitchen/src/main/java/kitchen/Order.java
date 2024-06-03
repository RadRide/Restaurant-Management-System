package kitchen;

import java.util.ArrayList;

public class Order {

    private int id;
    private String orderSource, orderStatus;
    private ArrayList<OrderItem> orderItems;

    public Order(int id, int orderSource, ArrayList<OrderItem> orderItems) {

        this.id = id;
        setOrderSource(orderSource);
        this.orderItems = orderItems;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        if(orderSource == 999){
            this.orderSource = "Delivery";
        }else{
            this.orderSource = String.valueOf(orderSource);
        }
    }




    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


}


