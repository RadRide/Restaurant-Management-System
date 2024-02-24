package order;

import properties.Properties;
import tables.Table;
import waiter.WaiterPageController;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class Order {

    private int id;
    private String orderStatus, orderDateString;
    private Table orderSource;
    private double orderTotal;
    private Discount discount;
    private LocalDateTime orderDate;
    private ArrayList<OrderItem> orderItems;
    private boolean isNew;
    private DecimalFormat formatter;
    private WaiterPageController controller;

    public Order(WaiterPageController controller, int id, Table orderSource, Discount orderDiscount, double orderTotal,
                 ArrayList<OrderItem> orderItems) {
        this.controller = controller;
        this.id = id;
        this.discount = orderDiscount;
        this.orderItems = orderItems;

        setOrderSource(orderSource);
        getOrderTotal();
        this.controller.getDiscountCombo().setValue(this.discount);

        isNew = false;
    }

    public Order(Table orderSource, OrderItem item){
        this.orderSource = orderSource;
        this.orderItems = new ArrayList<>();
        orderItems.add(item);
        this.isNew = true;
        this.discount = new Discount();
    }

    public Order(WaiterPageController controller){
        this.controller = controller;
        isNew = true;
        clear();
    }

    public void clear(){
        orderItems = new ArrayList<>();
        orderTotal = 0;
        discount = new Discount();
        if(!controller.getDiscountCombo().getItems().isEmpty()){
            controller.getDiscountCombo().setValue(controller.getDiscountCombo().getItems().get(0));
        }
        setOrderSource(new Table(Properties.NO_TABLE));
        if(controller.getOrder() != null){
            controller.refreshTotal();
        }
    }

    public void addItem(OrderItem item){
        orderItems.add(item);
        controller.refreshTotal();
    }

    public void removeItem(OrderItem item){
        orderItems.remove(item);
        controller.refreshTotal();
    }

    public double getOrderTotal(){
        orderTotal = 0;
        for(OrderItem dish : orderItems){
            orderTotal += dish.getTotalPrice();
        }
        orderTotal -= discount.apply(orderTotal);
        return orderTotal;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Discount getDiscount(){
        return discount;
    }
    public void setDiscount(Discount discount) {
        this.discount = discount;
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

    public String getOrderDateString() {
        return orderDateString;
    }
    public void setOrderDateString(LocalDateTime orderDate) {
        this.orderDateString = orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy \n HH:mm:ss"));
    }

    public Table getOrderSource() {
        return orderSource;
    }
    public void setOrderSource(Table orderSource) {
        this.orderSource = orderSource;
        controller.setTableLabel(this.orderSource.getNumber());
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
