package manager;

import dbConnection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import menu.Dish;
import menu.Ingredient;
import menu.InventoryItem;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DishManagerController implements Initializable {

    @FXML
    private TextField nameField, priceField, quantityField;
    @FXML
    private Button addButton, removeButton;
    @FXML
    private ChoiceBox<String> categoryChoice;
    @FXML
    private ChoiceBox<InventoryItem> descriptionChoice;
    @FXML
    private TextArea ingredientArea;
    @FXML
    private Label costLabel, messageLabel;
    private double cost = 0;
    private ManagerController controller;
    private String[] categoryChoices = new String[]{"Salads", "Platters", "Sandwiches"};
    private ArrayList<Ingredient> ingredients;
    private ArrayList<InventoryItem> inventoryItems;
    private DBConnection connection;
    private FXMLLoader loader;
    private DialogPane dishPane;
    private Dialog<ButtonType> dialog;
    private Optional<ButtonType> clickedButton;
    private DecimalFormat formatter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ingredients = new ArrayList<>();
    }

    public void setManagerController(ManagerController controller){
        this.controller = controller;
        formatter = new DecimalFormat("0.00");
    }

    public void openAddDishPane(){

        initializeDishDialog("Add Dish");
        initializeDishPane();
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

        refreshCost();
        refresh(ingredients);

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent() && !ingredients.isEmpty()){
            connection.insertDish(new Dish(nameField.getText(), categoryChoice.getValue(), Double.parseDouble(priceField.getText()),
                    ingredients));
        }
    }

    public void openEditDishPane(Dish dish){

        initializeDishEditDialog("Edit Dish");
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
        categoryChoice.setValue(dish.getCategory());
        priceField.setText(String.valueOf(dish.getPrice()));
        ingredients = dish.getIngredients();
        System.out.println(ingredients);
        cost = dish.getCost();
        refreshCost();
//        refreshEdit(ingredients, ingredientArea);
        refresh(ingredients);
        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent() && !ingredients.isEmpty()){
            connection.editDish(new Dish(nameField.getText(), categoryChoice.getValue(),
                    Double.parseDouble(priceField.getText()), dish.getId(), dish.getActive(), ingredients));
        }
    }

    public void initializeDishDialog(String title){
        try{
            loader = new FXMLLoader();
            connection = new DBConnection();
            loader.setLocation(getClass().getResource("/manager/ManageDish.fxml"));
            dishPane = loader.load();
            DishManagerController dishController = loader.getController();
            ingredients = dishController.ingredients;
            this.ingredientArea = dishController.ingredientArea;
            dialog = new Dialog<>();
            dialog.setDialogPane(dishPane);
            dialog.setTitle(title);
            inventoryItems = connection.getInventoryItems();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void initializeDishEditDialog(String title){
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/manager/ManageDish.fxml"));
        try{
            connection = new DBConnection();
            loader.setLocation(getClass().getResource("/manager/ManageDish.fxml"));
            dishPane = loader.load();
            DishManagerController dishController = loader.getController();
            ingredients = dishController.ingredients;
            this.ingredientArea = dishController.ingredientArea;
            dialog = new Dialog<>();
            dialog.setDialogPane(dishPane);
            dialog.setTitle(title);
            inventoryItems = connection.getInventoryItems();
            formatter = new DecimalFormat("0.00");
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
            refresh(ingredients);
            quantityField.clear();
            refreshCost();
        }
    }

    public void removeIngredient(ActionEvent event){
        // Removes last item in the ingredient arrayList and updates the TextArea
        if(ingredients.size() >= 1){
            InventoryItem newIng = ingredients.remove(ingredients.size() - 1);
            refresh(ingredients);
            refreshCost();
        }
    }

    public void initializeDishPane(){
        nameField = (TextField) dishPane.lookup("#nameField");
        priceField = (TextField) dishPane.lookup("#priceField");
        quantityField = (TextField) dishPane.lookup("#quantityField");
        addButton = (Button) dishPane.lookup("#addButton");
        removeButton = (Button) dishPane.lookup("#removeButton");
        categoryChoice = (ChoiceBox<String>) dishPane.lookup("#categoryChoice");
        descriptionChoice = (ChoiceBox<InventoryItem>) dishPane.lookup("#descriptionChoice");
        costLabel = (Label) dishPane.lookup("#costLabel");
        messageLabel = (Label) dishPane.lookup("#messageLabel");

        categoryChoice.getItems().addAll(connection.getList("category", "category_name"));
        categoryChoice.setValue(categoryChoice.getItems().get(0));

        descriptionChoice.getItems().addAll(inventoryItems);
    }
    public void initializeDishEditPane(){
        nameField = (TextField) dishPane.lookup("#nameField");
        priceField = (TextField) dishPane.lookup("#priceField");
        quantityField = (TextField) dishPane.lookup("#quantityField");
        addButton = (Button) dishPane.lookup("#addButton");
        removeButton = (Button) dishPane.lookup("#removeButton");
        categoryChoice = (ChoiceBox<String>) dishPane.lookup("#categoryChoice");
        descriptionChoice = (ChoiceBox<InventoryItem>) dishPane.lookup("#descriptionChoice");
        costLabel = (Label) dishPane.lookup("#costLabel");
        messageLabel = (Label) dishPane.lookup("#messageLabel");

        categoryChoice.getItems().addAll(connection.getList("category", "category_name"));

        descriptionChoice.getItems().addAll(inventoryItems);
    }

    public void refresh(ArrayList<Ingredient> inventoryItems){
        // Updates the TextArea of ingredients
        ingredientArea.setText("");
        for(Ingredient inventoryItem : inventoryItems){
            ingredientArea.appendText(inventoryItem.getName() + "\t" + inventoryItem.getQuantity() + "(" + inventoryItem.getUnit() + ")" + "\n");
        }
    }

    public void refreshEdit(ArrayList<Ingredient> inventoryItems, TextArea ingredientArea) {
        ingredientArea.setText("");
        for (Ingredient inventoryItem : inventoryItems) {
            ingredientArea.appendText(inventoryItem.getName() + "\t" + inventoryItem.getQuantity() + "(" + inventoryItem.getUnit() + ")" + "\n");
        }
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
        return nameField.getText().isEmpty() || priceField.getText().isEmpty();
    }
}