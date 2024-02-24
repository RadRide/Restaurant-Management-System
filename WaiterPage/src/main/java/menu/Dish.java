package menu;

import properties.Properties;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dish {

    private String name, category;
    private double price;
    private ArrayList<Ingredient> ingredients;
    private int id;
    private DecimalFormat formatter;

    public Dish(String name, String category, double price, ArrayList<Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.ingredients = ingredients;
    }

    public Dish(String name, String category, double price, int id, ArrayList<Ingredient> ingredients) {
        formatter = new DecimalFormat(Properties.PATTERN);
        this.name = name;
        this.category = category;
        this.price = price;
        this.ingredients = ingredients;
        this.id = id;
    }

    public Dish(String name, String category, double price, int id) {
        formatter = new DecimalFormat(Properties.PATTERN);
        this.name = name;
        this.category = category;
        this.price = price;
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return name + "\n$ " + formatter.format(price);
    }
}
