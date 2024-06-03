package customer;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import menu.Dish;
import order.DeliveryOrder;
import order.Order;

import java.util.ArrayList;

public class Customer {

    private int id;
    private String name, email, phoneNumber;
    private ArrayList<DeliveryOrder> orders;
    private ArrayList<Address> addresses;
    private ArrayList<Dish> favoriteDishes;
    private ChoiceBox<Dish> favoriteDishesCombo;
    public Customer(int id, String name, String email, String phoneNumber, ArrayList<DeliveryOrder> orders,
                    ArrayList<Address> addresses, ArrayList<Dish> favoriteDishes){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
        this.orders = orders;
        this.favoriteDishes = favoriteDishes;
        this.favoriteDishesCombo = new ChoiceBox<>();
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<DeliveryOrder> getOrders() {
        return orders;
    }
    public void setOrders(ArrayList<DeliveryOrder> orders) {
        this.orders = orders;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }
    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<Dish> getFavoriteDishes() {
        return favoriteDishes;
    }
    public void setFavoriteDishes(ArrayList<Dish> favoriteDishes) {
        this.favoriteDishes = favoriteDishes;
    }

    public ChoiceBox<Dish> getFavoriteDishesCombo() {
        favoriteDishesCombo.getItems().addAll(favoriteDishes);
        favoriteDishesCombo.setValue(favoriteDishesCombo.getItems().get(0));
        return favoriteDishesCombo;
    }
    public void setFavoriteDishesCombo(ChoiceBox<Dish> favoriteDishesCombo) {
        this.favoriteDishesCombo = favoriteDishesCombo;
    }
}
