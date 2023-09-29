package section;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;

public class Section {
    private String name;
    private ArrayList<Section> tables;
    private int tableNumber, numberOfSeats;

    public Section(String name, ArrayList<Section> tables) {
        this.name = name;
        this.tables = tables;
    }

    public Section(int tableNumber, int numberOfSeats){
        this.tableNumber = tableNumber;
        this.numberOfSeats = numberOfSeats;
    }

    public ArrayList<Section> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Section> tables) {
        this.tables = tables;
    }

    public TreeItem getSectionNode(){
        TreeItem sectionNode = new TreeItem<>(this);
        for(Section table : tables){
            sectionNode.getChildren().add(new TreeItem<Section>(table));
        }
        return sectionNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString(){
        if(name != null)
            return "Section " + name;
        return "Table: " + tableNumber + ", Number Of Seats: " + numberOfSeats;
    }
}
