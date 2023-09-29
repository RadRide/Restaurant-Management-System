package order;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {

    private int id;
    private String orderSource, orderDiscount, orderTotal, orderPaid, orderStatus;
    private double orderTotalUSD, orderTotalLBP, orderPaidUSD, orderPaidLBP, discount;
    private LocalDateTime orderDate;
    private ArrayList<OrderItem> orderItems;
    private DecimalFormat formatter;

    public Order(int id, int orderSource, LocalDateTime orderDate, double orderDiscount, double orderTotalUSD,
                 double orderTotalLBP, double orderPaidUSD, double orderPaidLBP, ArrayList<OrderItem> orderItems) {
        formatter = new DecimalFormat("#,###.00");
        this.id = id;
        setOrderSource(orderSource);
        this.orderDate = orderDate;
        this.discount = orderDiscount;
        setOrderDiscount(orderDiscount);
        this.orderTotalUSD = orderTotalUSD;
        this.orderTotalLBP = orderTotalLBP;
        this.orderPaidUSD = orderPaidUSD;
        this.orderPaidLBP = orderPaidLBP;
        setOrderTotal();
        setOrderPaid();
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(double orderDiscount) {
        this.orderDiscount = "%" + formatter.format(orderDiscount);
    }

    public String getOrderTotal() {
        return formatter.format(orderTotal);
    }

    public void setOrderTotal() {
        this.orderTotal = "$ " + orderTotalUSD + " /\nLBP " + orderTotalLBP;
    }

    public String getOrderPaid() {
        return orderPaid;
    }
    public double getDiscount(){
        return discount;
    }

    public void setOrderPaid() {
        this.orderPaid = "$ " + orderPaidUSD + " /\nLBP " + orderPaidLBP;
    }

    public double getOrderTotalUSD(){
        return orderTotalUSD;
    }
    public double getOrderTotalLBP() {
        return orderTotalLBP;
    }
    public double getOrderPaidUSD() {
        return orderPaidUSD;
    }
    public double getOrderPaidLBP() {
        return orderPaidLBP;
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
