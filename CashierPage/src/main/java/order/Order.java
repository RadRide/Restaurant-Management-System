package order;

import properties.Properties;

import java.util.ArrayList;

public class Order {

    private int id, status, table;
    private double totalUSD, totalLBP, paidUSD, paidLBP, discount;
    private String date, section, address;
    private Customer customer;
    private ArrayList<OrderItem> items;
    private Tip tip;

    public Order(int id, int status, int table, double totalUSD, double totalLBP, double paidUSD, double paidLBP,
                 double discount, String date, String section, ArrayList<OrderItem> items, Tip tip){
        this.id = id;
        this.discount = discount;
        this.status = status;
        this.totalLBP = totalLBP;
        this.totalUSD = totalUSD;
        this.paidLBP = paidLBP;
        this.paidUSD = paidUSD;
        this.date = date;
        this.items = items;
        this.tip = tip;
        setTable(table, section);
    }

    public Order(int id, int status, int table, double totalUSD, double totalLBP, double paidUSD, double paidLBP,
                 double discount, String date, String address, Customer customer, ArrayList<OrderItem> items, Tip tip){
        this.id = id;
        this.discount = discount;
        this.status = status;
        this.totalLBP = totalLBP;
        this.totalUSD = totalUSD;
        this.paidLBP = paidLBP;
        this.paidUSD = paidUSD;
        this.date = date;
        this.address = address;
        this.customer = customer;
        this.items = items;
        this.tip = tip;
        setTable(table, null);
    }

    public Order(){
        id = 1;
        totalUSD = 250.03;
        table = 1;
        section = "A";
    }

    /**
     * Sets the table value and checks if the order is a delivery and sets the section to delivery, else it sets the section
     * @param table The table value
     * @param section The section where the table is located. Can be empty String if the order is to be delivered
     */
    public void setTable(int table, String section){
        this.table = table;
        if(table == Properties.DELIVERY){
            this.section = "Delivery";
        }else{
            this.section = section;
        }
    }
    public int getTable() {
        return table;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalUSD() {
        return totalUSD;
    }
    public void setTotalUSD(double totalUSD) {
        this.totalUSD = totalUSD;
    }

    public double getTotalLBP() {
        return totalLBP;
    }
    public void setTotalLBP(double totalLBP) {
        this.totalLBP = totalLBP;
    }

    public double getPaidUSD() {
        return paidUSD;
    }
    public void setPaidUSD(double paidUSD) {
        this.paidUSD = paidUSD;
    }

    public double getPaidLBP() {
        return paidLBP;
    }
    public void setPaidLBP(double paidLBP) {
        this.paidLBP = paidLBP;
    }

    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }
    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public Tip getTip() {
        return tip;
    }
    public void setTip(Tip tip) {
        this.tip = tip;
    }
}