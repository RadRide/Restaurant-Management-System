package section;

public class Table {

    private int number, numberOfSeats;

    public Table(int number, int numberOfSeats){
        this.number = number;
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString(){
        return "Table: " + number + ", Number of Seats: " + numberOfSeats;
    }
}
