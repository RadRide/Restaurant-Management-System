package order;

import properties.Properties;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class Order {

    private int id, orderTable, status;
    private String orderSource, orderDiscount, orderTotal, orderPaid, orderStatus, orderDateString;
    private double orderTotalUSD, orderTotalLBP, orderPaidUSD, orderPaidLBP, discount;
    private LocalDateTime orderDate;
    private ArrayList<OrderItem> orderItems;
    private DecimalFormat formatter;

    public Order(int id, int orderSource, int status, LocalDateTime orderDate, double orderDiscount, double orderTotalUSD,
                 double orderTotalLBP, double orderPaidUSD, double orderPaidLBP, ArrayList<OrderItem> orderItems) {
        formatter = new DecimalFormat(Properties.PATTERN);
        this.id = id;
        this.orderTable = orderSource;
        this.status = status;
        setOrderStatus(status);
        setOrderSource(orderSource);
        this.orderDate = orderDate;
        setOrderDateString(orderDate);
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
        return orderTotal;
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

    /**
     * @return A string representation of the order's status
     */
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public void setOrderStatus(int status){
        switch (status){
            case 0: orderStatus = "In Kitchen"; break;
            case 1: orderStatus = "Completed"; break;
            case 2: orderStatus = "Checked Out"; break;
        }
    }

    /**
     * @return The status as an integer(0 for <b><i>IN KITCHEN</i></b>, 1 for <b><i>COMPLETED</i></b>, 2 for <b><i>CHECKED OUT</i></b>)
     */
    public int getStatus(){
        return status;
    }
    public void setStatus(int status){
        this.status = status;
    }

    public String getOrderDateString() {
        return orderDateString;
    }
    public void setOrderDateString(LocalDateTime orderDate) {
        this.orderDateString = orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy \n HH:mm:ss"));
    }

    public int getOrderTable() {
        return orderTable;
    }
    public void setOrderTable(int orderTable) {
        this.orderTable = orderTable;
    }
}
