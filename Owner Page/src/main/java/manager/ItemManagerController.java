package manager;

import dbConnection.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import menu.InventoryItem;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemManagerController implements Initializable {

    @FXML
    private TextField nameField, costField, quantityField;
    @FXML
    private ChoiceBox<String> categoryChoice, unitChoice;
    @FXML
    private Label messageLabel;
    private FXMLLoader loader;
    private DialogPane itemPane;
    private Dialog<ButtonType> dialog;
    private Optional<ButtonType> clickedButton;
    private String[] categoryChoices = {"Vegetables", "Fruits", "Alcohol", "Oil", "Spices", "Juices"};
    private String[] unitChoices = {"kg", "g", "l"};
    private ManagerController controller;
    private DBConnection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setManagerController(ManagerController controller){
        this.controller = controller;
    }

    public void openAddItemPane(){
        initializePane("Add Item");
        initializeFields();

        dialog.setOnCloseRequest(event -> {
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }
        });

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            connection.insertInventoryItem(new InventoryItem(nameField.getText(), unitChoice.getValue(), categoryChoice.getValue(),
                    Double.parseDouble(quantityField.getText()), Double.parseDouble(costField.getText()),
                    connection.getLastId("inventory")));
        }
    }

    public void openEditItemPane(InventoryItem item){
        initializePane("Edit Item");
        initializeFields();

        dialog.setOnCloseRequest(event -> {
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }
        });

        populateFields(item);

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            item.setName(nameField.getText());
            item.setCategory(categoryChoice.getValue());
            item.setUnit(unitChoice.getValue());
            item.setQuantity(Double.parseDouble(quantityField.getText()));
            item.setPrice(Double.parseDouble(costField.getText()));
            connection.editInventoryItem(item);
        }
    }

    public void initializePane(String title){
        try{
            connection = new DBConnection();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/manager/ManageIngredient.fxml"));
            itemPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(itemPane);
            dialog.setTitle(title);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initializeFields(){
        nameField = (TextField) itemPane.lookup("#nameField");
        costField  = (TextField) itemPane.lookup("#costField");
        quantityField = (TextField) itemPane.lookup("#quantityField");
        categoryChoice = (ChoiceBox<String>) itemPane.lookup("#categoryChoice");
        unitChoice = (ChoiceBox<String>) itemPane.lookup("#unitChoice");
        messageLabel = (Label) itemPane.lookup("#messageLabel");

        categoryChoice.getItems().addAll(connection.getList("item_category", "category_name"));
        unitChoice.getItems().addAll(connection.getList("units", "unit"));

        costField.setOnKeyTyped(this::checkFields);
    }

    public void populateFields(InventoryItem item){
        nameField.setText(item.getName());
        costField.setText(item.getPrice() + "");
        quantityField.setText(item.getQuantity() + "");
        unitChoice.setValue(item.getUnit());
        categoryChoice.setValue(item.getCategory());
    }

    public void checkFields(KeyEvent event){
        preventLetters((TextField) event.getSource());
    }

    public void preventLetters(TextField field){
        // Makes textFields only accept doubles or integers
        if(!field.getText().matches("\\d*(\\.\\d*)?")){
            // Prevents the writing of all non digit numbers
            String oldValue = field.getText().substring(0, field.getText().length());
            field.setText(field.getText().replaceAll("[^\\d.]", ""));
            // Puts the caret(writing cursor) at the end of the word.
            field.positionCaret(field.getText().length());
        }
    }

    public boolean areFieldsEmpty(){
        return nameField.getText().isEmpty() || costField.getText().isEmpty() || quantityField.getText().isEmpty() ||
                unitChoice.getValue().isEmpty() || categoryChoice.getValue().isEmpty();
    }

}
