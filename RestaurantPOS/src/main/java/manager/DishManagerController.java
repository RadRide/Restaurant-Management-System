package manager;

import dbConnection.DBConnection;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import menu.Dish;
import menu.Ingredient;
import menu.InventoryItem;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

public class DishManagerController {

    @FXML
    private MFXTextField nameField, priceField, quantityField;
    @FXML
    private MFXButton addButton, removeButton;
    @FXML
    private MFXComboBox<String> categoryChoice;
    @FXML
    private MFXFilterComboBox<InventoryItem> descriptionChoice;
    @FXML
    private MFXListView<Ingredient> ingredientListView;
    @FXML
    private Label costLabel, messageLabel;
    private double cost = 0;
    private ManagerController controller;
    private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    private ArrayList<InventoryItem> inventoryItems;
    private DBConnection connection;
    private FXMLLoader loader;
    private DialogPane dishPane;
    private Dialog<ButtonType> dialog;
    private Optional<ButtonType> clickedButton;
    private DecimalFormat formatter;

    public void setManagerController(ManagerController controller){
        this.controller = controller;
        formatter = new DecimalFormat("0.00");
    }

    public void openAddDishPane(){

        initializeDishDialog("Add Dish");
        initializeDishPane();
//        ingredients = new ArrayList<>();
        addButton.setOnAction(this::addIngredient);
        removeButton.setOnAction(this::removeIngredient);

        ingredientListView.setItems(ingredients);

        dialog.setOnCloseRequest(event ->{
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }else if(ingredients.isEmpty()){
                event.consume();
                messageLabel.setText("At Least One Ingredient Must Be Selected");
                messageLabel.setVisible(true);
            }
        });

        refreshCost();

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent() && !ingredients.isEmpty()){
            connection.insertDish(new Dish(nameField.getText(), categoryChoice.getValue(),
                    Double.parseDouble(priceField.getText()), new ArrayList<Ingredient>(ingredients)));
        }
    }

    public void openEditDishPane(Dish dish){

        initializeDishDialog("Edit Dish");
        initializeDishEditPane();
        addButton.setOnAction(this::addIngredient);
        removeButton.setOnAction(this::removeIngredient);

        dialog.setOnCloseRequest(event ->{
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }else if(ingredients.isEmpty()){
                event.consume();
                messageLabel.setText("At Least One Ingredient Must Be Selected");
                messageLabel.setVisible(true);
            }
        });

        nameField.setText(dish.getName());
//        categoryChoice.setValue("Nfo5o");
        categoryChoice.getSelectionModel().selectItem(dish.getCategory());
        priceField.setText(String.valueOf(dish.getPrice()));
        ingredients.addAll(dish.getIngredients());
        ingredientListView.setItems(ingredients);
        cost = dish.getCost();
        refreshCost();
        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent() && !ingredients.isEmpty()){
            connection.editDish(new Dish(nameField.getText(), categoryChoice.getValue(),
                    Double.parseDouble(priceField.getText()), dish.getId(), dish.getActive(), new ArrayList<Ingredient>(ingredients)));
        }
    }

    public void initializeDishDialog(String title){
        try{
            loader = new FXMLLoader();
            connection = new DBConnection();
            loader.setLocation(getClass().getResource("/manager/ManageDish.fxml"));
            dishPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(dishPane);
            dialog.setTitle(title);
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("/icons/AlphaBeta_Icon.png").openStream()));
            inventoryItems = connection.getInventoryItems();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addIngredient(ActionEvent event){
        // Adds last item in the ingredient arrayList and updates the TextArea
        if(!quantityField.getText().equals("") && !descriptionChoice.getValue().equals("")){
            InventoryItem newIng = descriptionChoice.getValue();
            // Checks if the ingredient is already available and adds the quantity to it
            int index = doesItemExist(newIng.getName());
            if(index >= 0){
                ingredients.get(index).setQuantity(ingredients.get(index).getQuantity() + Double.parseDouble(quantityField.getText()));
            }else{
                newIng.setQuantity(Double.parseDouble(quantityField.getText()));
                this.ingredients.add(new Ingredient(newIng));
            }
            quantityField.clear();
            refreshCost();
        }
    }

    public void checkFields(KeyEvent event){
//        if(event.getCode().getChar().is)
    }

    public void removeIngredient(ActionEvent event){
        // Removes last item in the ingredient arrayList and updates the TextArea
        if(ingredients.size() >= 1){
            ingredients.remove(ingredientListView.getSelectionModel().getSelectedValues().get(0));
            ingredientListView.getSelectionModel().clearSelection();
            refreshCost();
        }
    }

    public void initializeDishPane(){
        initElements();

        categoryChoice.getItems().addAll(connection.getList("category", "category_name"));
        categoryChoice.setValue(categoryChoice.getItems().get(0));

        descriptionChoice.getItems().addAll(inventoryItems);
    }
    public void initializeDishEditPane(){
        initElements();

        categoryChoice.getItems().addAll(connection.getList("category", "category_name"));

        descriptionChoice.getItems().addAll(inventoryItems);
    }

    /**
     * Assigns the elements to their fxml properties
     */
    private void initElements(){
        nameField = (MFXTextField) dishPane.lookup("#nameField");
        priceField = (MFXTextField) dishPane.lookup("#priceField");
        quantityField = (MFXTextField) dishPane.lookup("#quantityField");
        addButton = (MFXButton) dishPane.lookup("#addButton");
        removeButton = (MFXButton) dishPane.lookup("#removeButton");
        categoryChoice = (MFXComboBox<String>) dishPane.lookup("#categoryChoice");
        descriptionChoice = (MFXFilterComboBox<InventoryItem>) dishPane.lookup("#descriptionChoice");
        ingredientListView = (MFXListView<Ingredient>) dishPane.lookup("#ingredientListView");
        costLabel = (Label) dishPane.lookup("#costLabel");
        messageLabel = (Label) dishPane.lookup("#messageLabel");

        ingredientListView.getSelectionModel().setAllowsMultipleSelection(false);
        ingredientListView.getSelectionModel().selectionProperty().addListener(new ChangeListener<ObservableMap<Integer, Ingredient>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableMap<Integer, Ingredient>> observableValue,
                                ObservableMap<Integer, Ingredient> integerIngredientObservableMap, ObservableMap<Integer, Ingredient> t1) {
                if(t1.size() == 0){
                    removeButton.setDisable(true);
                }else{
                    removeButton.setDisable(false);
                }
            }
        });

        descriptionChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InventoryItem>() {
            @Override
            public void changed(ObservableValue<? extends InventoryItem> observableValue, InventoryItem inventoryItem, InventoryItem t1) {
                if(t1 == null){
                    addButton.setDisable(true);
                }else{
                    if(!quantityField.getText().isEmpty()){
                        addButton.setDisable(false);
                    }else{
                        addButton.setDisable(true);
                    }
                }
            }
        });

        quantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(quantityField.getText().isEmpty()){
                    addButton.setDisable(true);
                }else{

                    try{
                        Double.parseDouble(t1);
                    }catch (Exception e){
                        System.out.println("Error");
                    }

                    if(descriptionChoice.getValue() == null){
                        addButton.setDisable(true);
                    }else{
                        addButton.setDisable(false);
                    }
                }
            }
        });
    }

    public void refreshCost(){
        cost = 0;
        for(Ingredient ingredient : ingredients){
            cost += ingredient.getQuantity() * ingredient.getPrice();
        }
        formatter = new DecimalFormat("0.00");
        costLabel.setText(formatter.format(cost));
    }

    public int doesItemExist(String itemName){
        for (int i = 0; i < ingredients.size(); i++){
            if(ingredients.get(i).getName().equals(itemName)){
                return i;
            }
        }
        return -1;
    }

    public boolean areFieldsEmpty(){
        return nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                categoryChoice.getValue().isEmpty();
    }
}