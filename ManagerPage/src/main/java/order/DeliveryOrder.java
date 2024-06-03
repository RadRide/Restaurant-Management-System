package order;

import customer.Address;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DeliveryOrder extends Order{

    private Address address;
    private String customerName;
    private double deliveryFee;

    public DeliveryOrder(int id, int orderSource, int status, LocalDateTime orderDate, double orderDiscount,
                         double orderTotalUSD, double orderTotalLBP, double orderPaidUSD, double orderPaidLBP,
                         ArrayList<OrderItem> orderItems, Address address, String customerName, double deliveryFee) {
        super(id, orderSource, status, orderDate, orderDiscount, orderTotalUSD, orderTotalLBP, orderPaidUSD, orderPaidLBP, orderItems);
        this.address = address;
        this.customerName = customerName;
        this.deliveryFee = deliveryFee;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString(){
        return "Order ID: " + getId();
    }
}
