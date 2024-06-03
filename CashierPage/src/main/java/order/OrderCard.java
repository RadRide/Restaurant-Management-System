package order;

import cashier.CashierPageController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import properties.Properties;

public class OrderCard extends TitledPane {

    private Order order;
    private VBox detailBox;
    private Label sectionLabel, tableLabel, amountLabel;
    private Button orderButton;
    private CashierPageController controller;
    private boolean isCheckedOut;

    public OrderCard(Order order, CashierPageController controller, boolean isCheckedOut){
        this.order = order;
        this.controller = controller;
        this.isCheckedOut = isCheckedOut;

        initCard();
    }

    private void initCard(){
        setText("Order " + order.getId());
        getStyleClass().add("orderPane");

        initContent();

        setContent(detailBox);
    }

    private void initContent(){
        detailBox = new VBox();
        detailBox.getStyleClass().add("orderDetailBox");

        if(order.getTable() == Properties.DELIVERY){
            initDelivery();
        }else{
            initInHouse();
        }

        orderButton = new Button("Open");
        orderButton.getStyleClass().add("orderButton");
        orderButton.setOnAction(this::openOrderDetails);

        detailBox.getChildren().addAll(sectionLabel, tableLabel, amountLabel, orderButton);
    }

    public void initInHouse(){
        sectionLabel = new Label("Section: " + order.getSection());
        sectionLabel.getStyleClass().add("orderDetailLabel");

        tableLabel = new Label("Table: " + order.getTable());
        tableLabel.getStyleClass().add("orderDetailLabel");

        amountLabel = new Label("Amount: $" + order.getTotalUSD());
        amountLabel.getStyleClass().add("orderDetailLabel");
    }

    public void initDelivery(){
        sectionLabel = new Label("Customer: " + ((DeliveryOrder)order).getCustomer().getName());
        sectionLabel.getStyleClass().add("orderDetailLabel");

        tableLabel = new Label("Location: " + ((DeliveryOrder)order).getAddress().getAddress());
        tableLabel.getStyleClass().add("orderDetailLabel");

        amountLabel = new Label("Amount: $" + order.getTotalUSD());
        amountLabel.getStyleClass().add("orderDetailLabel");
    }

    public void openOrderDetails(ActionEvent event){
        if(!isCheckedOut){
            controller.openOrder(order);
        }else{
            controller.openOldOrder(order);
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }
    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }
}