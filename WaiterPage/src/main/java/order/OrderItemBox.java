package order;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import waiter.CommentController;
import waiter.WaiterPageController;

public class OrderItemBox extends HBox {

    private Label itemLabel;
    private Button deletebutton, editButton;
    private FontIcon deleteIcon, editIcon;
    private Spinner<Integer> quantitySpinner;
    private ContextMenu editMenu;
    private MenuItem commentButton, transferButton;
    private OrderItem item;
    private WaiterPageController controller;
    private ListView checkedList;

    public OrderItemBox(WaiterPageController controller, OrderItem item){
        this.controller = controller;
        this.item = item;

        this.item.setBox(this);
        this.controller.getOrder().addItem(item);

        setContextMenu();

        setButtons();

        setItemLabel();

        setQuantitySpinner();

        setHBox();

        disableButtons();
    }

    public void setHBox(){
        setPrefSize(348, 60);
        setMinSize(getPrefWidth(), getPrefHeight());
        getStyleClass().add("itemBox");
        getChildren().addAll(deletebutton, itemLabel, quantitySpinner, editButton);
    }

    public void setItemLabel(){
        itemLabel = new Label(item.getItem().getName());

        itemLabel.setPrefSize(160, 60);

        itemLabel.getStyleClass().add("dishLabel");
    }

    public void setQuantitySpinner(){
        quantitySpinner = new Spinner<>();

        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(item.getItemQuantity(), 100, item.getItemQuantity()));

        quantitySpinner.setPrefSize(80, 60);

        quantitySpinner.getStyleClass().add("quantitySpinner");

        quantitySpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                item.setItemQuantity(quantitySpinner.getValue());
                controller.refreshTotal();
            }
        });
    }

    public void setContextMenu(){
        editMenu = new ContextMenu();

        commentButton = new MenuItem("Add Comment");
        transferButton = new MenuItem("Transfer Dish");

        commentButton.setOnAction(this::openCommentSection);
        transferButton.setOnAction(this::transfer);

        editMenu.getItems().addAll(commentButton, transferButton);
    }

    public void setButtons(){
        deletebutton = new Button();
        editButton = new Button();

        deletebutton.setPrefSize(45, 60);
        editButton.setPrefSize(55, 60);

        deletebutton.getStyleClass().add("deleteButton");
        editButton.getStyleClass().add("editDish");

        setIcons();

        deletebutton.setOnAction(this::delete);

        editButton.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                editMenu.show(editButton, event.getScreenX(), event.getScreenY());
            }
        });
        editButton.setOnTouchPressed(event -> {
            editMenu.show(editButton, event.getTouchPoint().getScreenX(), event.getTouchPoint().getScreenY());
        });
    }
    private void setIcons(){
        deleteIcon = new FontIcon("win10-cancel");
        editIcon = new FontIcon("win10-pencil");

        deletebutton.setGraphic(deleteIcon);
        editButton.setGraphic(editIcon);
    }

    public void delete(ActionEvent event){
        controller.getOrderBox().getChildren().remove(this);
        controller.getOrder().removeItem(item);
    }

    public void openCommentSection(ActionEvent event){
        CommentController controller = new CommentController();
        controller.openCommentPane((((MenuItem)event.getSource()).getParentPopup().getOwnerWindow()), item);
    }

    public void transfer(ActionEvent event){
        controller.setTransferringItem(this);
        controller.setTransferring(true);
        controller.launchTransfer();
    }

    /**
     * Checks If the Ordered item is newly added and disables the transfer button else it disables the delete button
     */
    private void disableButtons(){
        if(item.isNew()){
            transferButton.setDisable(true);
        }else{
            deletebutton.setDisable(true);
            quantitySpinner.setDisable(true);
        }
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }

    public OrderItem getItem() {
        return item;
    }
}
