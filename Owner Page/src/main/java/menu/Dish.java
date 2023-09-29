package menu;

import dbConnection.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.ToggleSwitch;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dish {

    private String name, category, status;
    private double price, cost;
    private ArrayList<Ingredient> ingredients;
    private int id, active;
    private ChoiceBox<Ingredient> description;
    private DecimalFormat formatter;
    private ObservableList<Ingredient> ingredientList;
    private ToggleSwitch statusButton;
    private DBConnection connection;

    public Dish(String name, String category, double price, ArrayList<Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.ingredientList = FXCollections.observableArrayList(ingredients);
    }

    public Dish(String name, String category, double price, int id, int active, ArrayList<Ingredient> ingredients) {
        formatter = new DecimalFormat("0.00");
        this.name = name;
        this.category = category;
        this.price = price;
        this.ingredients = ingredients;
        this.ingredientList = FXCollections.observableArrayList(ingredients);
        this.id = id;
        setStatus(active);
        setDescription(ingredients);
        this.active = active;
        connection = new DBConnection();
        setStatusButton();
        setCost();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> inventoryItems) {
        this.ingredients = inventoryItems;
    }

    public ObservableList<Ingredient> getIngredientList(){
        return ingredientList;
    }

    public void setDescription(ArrayList<Ingredient> ingredients) {
        description = new ChoiceBox<>();
        description.getItems().addAll(ingredients);
    }

    public ChoiceBox<Ingredient> getDescription(){
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost() {
        cost = 0;
        for(Ingredient ingredient : ingredients){
            cost += ingredient.getPrice() * ingredient.getQuantity();
        }
        cost = Double.parseDouble(formatter.format(cost));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(int active) {
        if(active == 0){
            status = "Disabled";
        }else{
            status = "Active";
        }
    }

    public int getActive(){
        return active;
    }

    public void setStatusButton(){
        statusButton = new ToggleSwitch(status);
        statusButton.getStylesheets().add(getClass().getResource("/Styles/ManagerStyles.css").toString());
        statusButton.getStyleClass().add("toggleSwitch");
        statusButton.setAlignment(Pos.CENTER_RIGHT);
        if(active == 1){
            statusButton.setSelected(true);
        }
        statusButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(active == 0){
                    active = 1;
                }else{
                    active = 0;
                }
                setStatus(active);
                connection.editDishStatus(id, active);
                statusButton.setText(status);

            }
        });
    }

    public ToggleSwitch getStatusButton(){
        return statusButton;
    }
}
