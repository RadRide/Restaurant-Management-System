package tables;

public class Table {

    private String number;

    public Table(String number){
        this.number = number;
    }

    public int toInt(){
        return Integer.parseInt(number);
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Table " + number;
    }
}
