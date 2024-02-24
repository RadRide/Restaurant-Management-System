package order;

public class OrderItem {

    private int id, quantity;
    private String name;
    private double price;

    public OrderItem(int id, int quantity, double price, String name){
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return price * quantity;
    }
}