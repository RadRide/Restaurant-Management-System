package manager;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import order.DeliveryOrder;
import order.Order;
import order.OrderItem;
import properties.Properties;

import java.io.IOException;

public class OrderItemsController {

    @FXML
    private Label idLabel, tableLabel, tableNumberLabel, locationLabel, locationValueLabel;
    @FXML
    private MFXListView<OrderItem> itemsListView;
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

        if(order.getOrderTable() == Properties.DELIVERY){
            DeliveryOrder deliveryOrder = (DeliveryOrder) order;
            tableLabel.setText("Customer Name:");
            tableNumberLabel.setText(deliveryOrder.getCustomerName());
            locationValueLabel.setText(deliveryOrder.getAddress().getAddress());
        }else{
            locationLabel.setVisible(false);
            locationValueLabel.setVisible(false);
            tableNumberLabel.setText(String.valueOf(order.getOrderTable()));
        }

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
        tableLabel = (Label) detailPane.lookup("#tableLabel");
        tableNumberLabel = (Label) detailPane.lookup("#tableNumberLabel");
        locationLabel = (Label) detailPane.lookup("#locationLabel");
        locationValueLabel = (Label) detailPane.lookup("#locationValueLabel");
        itemsListView = (MFXListView<OrderItem>) detailPane.lookup("#itemsListView");
        commentArea = (TextArea) detailPane.lookup("#commentArea");

        itemsListView.getSelectionModel().setAllowsMultipleSelection(false);
        itemsListView.getSelectionModel().selectionProperty().addListener(new ChangeListener<ObservableMap<Integer, OrderItem>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableMap<Integer, OrderItem>> observableValue, ObservableMap<Integer, OrderItem> integerOrderItemObservableMap, ObservableMap<Integer, OrderItem> t1) {
                if(t1 != null){
                    commentArea.setText(itemsListView.getSelectionModel().getSelectedValues().get(0).getComment());
                }else{
                    commentArea.clear();
                }
            }
        });

    }

}
