package order;

import java.util.ArrayList;

public class DeliveryOrder extends Order{

    private Customer customer;
    private Address address;
    private double deliveryFee;

    public DeliveryOrder(int id, int status, int table, double totalUSD, double totalLBP, double paidUSD, double paidLBP,
                         double discount, String date, ArrayList<OrderItem> items, Tip tip, Customer customer,
                         Address address, double deliveryFee){
        super(id, status, table, totalUSD, totalLBP, paidUSD, paidLBP, discount, date, null, items, tip);

        this.customer = customer;
        this.deliveryFee = deliveryFee;
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }
    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
