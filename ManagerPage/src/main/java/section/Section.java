package section;

import io.github.palexdev.materialfx.controls.MFXTreeItem;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;

public class Section {
    private String name;
    private ArrayList<Section> tables;
    private int tableNumber, numberOfSeats;
    private Boolean isRoot;

    public Section(String name, ArrayList<Section> tables) {
        this.name = name;
        this.tables = tables == null ? new ArrayList<>() : tables;
        isRoot = false;
    }

    public Section(int tableNumber, int numberOfSeats){
        this.tableNumber = tableNumber;
        this.numberOfSeats = numberOfSeats;
        isRoot = false;
    }

    public Section(){
        isRoot = true;
    }

    public ArrayList<Section> getTables() {
        return tables;
    }
    public void setTables(ArrayList<Section> tables) {
        this.tables = tables;
    }
    public void addTable(Section table){
        tables.add(table);
    }

    public AbstractMFXTreeItem<Section> getSectionNode(){
        AbstractMFXTreeItem<Section> sectionNode = new MFXTreeItem<>(this);
        for(Section table : tables){
            sectionNode.getItems().add(new MFXTreeItem<Section>(table));
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

    public Boolean isRoot() {
        return isRoot;
    }
    public void setRoot(Boolean root) {
        isRoot = root;
    }

    @Override
    public String toString(){
        if(isRoot)
            return "Restaurant Sections:";
        if(name != null)
            return "Section " + name;
        return "Table: " + tableNumber + ", Number Of Seats: " + numberOfSeats;
    }
}
