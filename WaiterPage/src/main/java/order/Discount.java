package order;

public class Discount {

    private double discount;

    public Discount(double discount) {
        this.discount = discount;
    }

    /**
     * Sets The Discount To 0
     */
    public Discount() {
        this.discount = 0;
    }

    public double apply(double total){
        return total * discount / 100.0;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Discount:\t" + (int)discount + "%";
    }
}
