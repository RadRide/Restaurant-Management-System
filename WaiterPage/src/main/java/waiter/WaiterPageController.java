package waiter;

import connection.DBConnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import menu.Dish;
import order.Discount;
import order.Order;
import order.OrderItem;
import order.OrderItemBox;
import org.controlsfx.control.CheckListView;
import properties.Properties;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class WaiterPageController implements Initializable {

    @FXML
    private VBox categoryBox, dishBox, orderBox;
    @FXML
    private ScrollPane dishScroll, orderScroll;
    @FXML
    private Label nameLabel, totalLabel, tableLabel;
    @FXML
    private Button backButton, logoutButton, confirmOrderButton, changeTableButton;
    @FXML
    private ComboBox<Discount> discountCombo;
    private Alert alert;
    private Order order;
    private OrderItemBox transferringItem;
    private boolean isTransferring, isChangingTable, isManager;
    private DBConnection connection;
    private DecimalFormat formatter;
    private String name;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() ->{
            onStartup();
        });
    }

    /**
     * Populates the sidebar with category buttons
     */
    public void refreshCategoryBox(){
        categoryBox.getChildren().clear();
        categoryBox.getChildren().addAll(connection.getCategoryButtons());
    }
    /**
     * Populates the sidebar with section buttons
     */
    public void refreshSectionBox(){
        categoryBox.getChildren().clear();
        categoryBox.getChildren().addAll(connection.getSectionButtons());
    }

    /**
     * Populates middle section with dish buttons
     */
    public void refreshDishBox(){
        dishBox.getChildren().clear();
        dishBox.getChildren().addAll(connection.getMenu());

        refreshTotal();
    }
    /**
     * Populates middle section with table buttons
     */
    public void refreshTableBox(){
        dishBox.getChildren().clear();
        dishBox.getChildren().addAll(connection.getTablePane());
    }

    public void refreshTotal(){
        totalLabel.setText(formatter.format(order.getOrderTotal()));
    }

    public void onStartup(){
        connection = new DBConnection(this);

        formatter = new DecimalFormat(Properties.PATTERN);

        order = new Order(this);

        isTransferring = false;

        orderBox.getChildren().clear();

        orderBox.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                confirmOrderButton.setDisable(orderBox.getChildren().isEmpty());
            }
        });

        discountCombo.setDisable(!isManager);

        confirmOrderButton.setDisable(true);

        confirmOrderButton.setOnAction(this::confirmOrder);

//        discountCombo.setDisable(true);
        discountCombo.getItems().addAll(new Discount(0),
                new Discount(5),
                new Discount(10),
                new Discount(15),
                new Discount(20),
                new Discount(25));
        discountCombo.setValue(discountCombo.getItems().get(0));
        discountCombo.valueProperty().addListener(new ChangeListener<Discount>() {
            @Override
            public void changed(ObservableValue<? extends Discount> observableValue, Discount discount, Discount t1) {
                order.setDiscount(t1);
                refreshTotal();
            }
        });

        changeTableButton.setDisable(true);
        changeTableButton.setOnAction(this::changeTable);

        setTables();

//        setDishes();

        refreshTotal();

        backButton.setOnAction(this::back);
        logoutButton.setOnAction(this::logout);

        nameLabel.setText(name);
    }

    public void setTables(){
        refreshSectionBox();
        refreshTableBox();

        backButton.setDisable(true);
        changeTableButton.setDisable(true);

        order.clear();
    }
    public void setDishes(){
        refreshCategoryBox();
        refreshDishBox();

        backButton.setDisable(false);
        changeTableButton.setDisable(order.isNew());
    }

    public void backToTables(){
        setTables();
        orderBox.getChildren().clear();
        order.clear();
    }

    public void back(ActionEvent event){
        if(isTransferring || isChangingTable){
            setDishes();
            isChangingTable = false;
            isTransferring = false;

        }else {
            if(!orderBox.getChildren().isEmpty()){
                if(showAlert("Are You Sure You Want To Cancel Order ?",
                        "Canceling Will Clear All Changes To The Order").get() == ButtonType.OK){
                    backToTables();
                }
            }else{
                backToTables();
            }
        }
    }

    public void confirmOrder(ActionEvent event){
        connection.addOrder(order, 1);
        backToTables();
    }

    public void changeTable(ActionEvent event){
        isChangingTable = true;

        launchTransfer();
    }

    public void logout(ActionEvent event){
        logout((Stage) ((Node)event.getSource()).getScene().getWindow());
    }

    /**
     * Sets the pane with tables to choose where to transfer the dish
     */
    public void launchTransfer(){
        refreshSectionBox();
        refreshTableBox();

        backButton.setDisable(false);
        confirmOrderButton.setDisable(true);
        changeTableButton.setDisable(true);
    }

    /**
     * Checks if the inserted dish is already ordered and increments it
     * @param dish
     * @return -1 if the dish is not already ordered, else it returns its index
     */
    public int checkIfPresent(Dish dish){
        for(int i = 0; i < order.getOrderItems().size(); i++){
            if(order.getOrderItems().get(i).getItem() == dish){
                return i;
            }
        }
        return -1;
    }

    /**
     * Enables or disables the discount combo depending on the login account
     * @param disabled true to disable and false for enable
     */
    public void enableDiscount(boolean disabled){
        if(isManager){
            discountCombo.setDisable(disabled);
        }
    }

    private void logout(Window window) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/LoginPage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) window;
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Optional<ButtonType> showAlert(String header, String content){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(backButton.getScene().getWindow());
        alert.setTitle("CANCEL ORDER");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public Window getOwner(){
        return categoryBox.getScene().getWindow();
    }

    public void setWaiterName(String name){
        this.name = name;
    }
    public VBox getCategoryBox() {
        return categoryBox;
    }

    public ScrollPane getDishScroll() {
        return dishScroll;
    }
    public ScrollPane getOrderScroll() {
        return orderScroll;
    }

    public VBox getDishBox() {
        return dishBox;
    }
    public VBox getOrderBox() {
        return orderBox;
    }
    public Label getTableLabel() {
        return tableLabel;
    }
    public void setTableLabel(String table){
        tableLabel.setText(table);
    }
    public ComboBox<Discount> getDiscountCombo() {
        return discountCombo;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isTransferring() {
        return isTransferring;
    }
    public void setTransferring(boolean transferring) {
        isTransferring = transferring;
    }

    public boolean isChangingTable() {
        return isChangingTable;
    }
    public void setChangingTable(boolean changingTable) {
        isChangingTable = changingTable;
    }

    public OrderItemBox getTransferringItem() {
        return transferringItem;
    }
    public void setTransferringItem(OrderItemBox transferringItem) {
        this.transferringItem = transferringItem;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
