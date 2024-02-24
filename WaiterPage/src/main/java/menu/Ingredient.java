package menu;

public class Ingredient extends InventoryItem{
    private int id;
    public Ingredient(String name, String unit, double quantity, double price) {
        super(name, unit, quantity, price);
    }

    public Ingredient(String name, String unit, double quantity, double price, int id) {
        super(name, unit, quantity, price);
        this.id = id;
    }

    public Ingredient(InventoryItem item) {
        super(item.getName(), item.getUnit(), item.getCategory(), item.getQuantity(), item.getPrice(), item.getId());
        this.id = item.getId();
        System.out.println(this.id);
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return getName() + "\t(" + getQuantity() + getUnit() + ")";
    }
}
