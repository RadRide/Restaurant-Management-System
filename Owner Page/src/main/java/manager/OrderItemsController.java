package manager;

import dbConnection.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import order.Order;
import order.OrderItem;

import java.io.IOException;

public class OrderItemsController {

    @FXML
    private Label idLabel;
    @FXML
    private ListView<OrderItem> itemsListView;
    @FXML
    private TextArea commentArea;
    private DialogPane detailPane;
    private Dialog<ButtonType> dialog;
    private FXMLLoader loader;
    private ManagerController controller;

    public void setManagerController(ManagerController controller){
        this.controller = controller;
    }

    public void showDetailsPane(Order order){
        initializeDialog();
        initializeFields();

        idLabel.setText(String.valueOf(order.getId()));
        itemsListView.getItems().addAll(order.getOrderItems());

        dialog.showAndWait();
    }

    public void initializeDialog(){
        try{
            loader = new FXMLLoader(getClass().getResource("/manager/OrderItemsViewer.fxml"));
            detailPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(detailPane);
            dialog.setTitle("Order Details");
        }catch (IOException e){

            e.printStackTrace();
        }
    }

    public void initializeFields(){
        idLabel = (Label) detailPane.lookup("#idLabel");
        itemsListView = (ListView<OrderItem>) detailPane.lookup("#itemsListView");
        commentArea = (TextArea) detailPane.lookup("#commentArea");

        itemsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderItem>() {
            @Override
            public void changed(ObservableValue<? extends OrderItem> observableValue, OrderItem orderItem, OrderItem t1) {
                if(t1 != null){
                    commentArea.setText(t1.getComment());
                }else{
                    commentArea.clear();
                }
            }
        });

    }

}
