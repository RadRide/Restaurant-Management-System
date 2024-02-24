package menu;

public class InventoryItem {

    private String name, unit, category;
    private double quantity;
    private double price;
    private int id;

    public InventoryItem(String name, String unit, String category, double quantity, double price, int id) {
        this.name = name;
        this.unit = unit;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }

    public InventoryItem(String name, String unit, double quantity, double price) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
    }

    public String getChoice(){
        return name + "(" + unit + ")";
    }

    public String getIngredientString(){
        return "(" + quantity + unit + ")" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return name + "\t" +"(" + unit + ")";
    }
}
