package menu;

import connection.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import order.OrderItem;
import order.OrderItemBox;
import tables.Table;
import waiter.WaiterPageController;


public class MenuButton<T> extends Button {

    private T item;
    private WaiterPageController controller;
    private DBConnection connection;

    public MenuButton(WaiterPageController controller, T item){
        this.controller = controller;
        this.item = item;
        connection = new DBConnection(controller);
        setButton();
    }

    private void setButton(){
        setText(item.toString());
        getStyleClass().add("dishButton");
        setPrefSize(140, 120);
        setOnAction(this::buttonClicked);
    }

    public void buttonClicked(ActionEvent event){
        if((((MenuButton)event.getSource()).getItem() instanceof Table)){
            if(controller.isTransferring()){
                connection.transferItem(((Table)((MenuButton)event.getSource()).getItem()),
                        controller.getTransferringItem().getItem(), controller.getOrder().getId());
                controller.getOrderBox().getChildren().remove(controller.getTransferringItem());
                controller.setTransferring(false);
                controller.setDishes();
            }else if(controller.isChangingTable()){
                connection.transferOrder(((Table)((MenuButton)event.getSource()).getItem()),
                        controller.getOrder().getId());
                controller.setChangingTable(false);
                controller.setDishes();
            }else{
                connection.loadTable(((Table)((MenuButton)event.getSource()).getItem()));
                controller.getOrder().setOrderSource(((Table)((MenuButton)event.getSource()).getItem()));
                controller.setDishes();
            }
        }else{
            int index = controller.checkIfPresent((Dish) item);
            if(index < 0){
                controller.getOrderBox().getChildren().add(new OrderItemBox(controller, new OrderItem((Dish) item)));
                this.controller.getOrderScroll().setVvalue(1.0);
            }else{
                controller.getOrder().getOrderItems().get(index).increment();
            }
        }
    }

    public T getItem(){
        return item;
    }

}
