package kitchen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckListView;

public class OrderPane extends HBox {

    private Label orderPaneLabel;
    private Button orderPaneButton;
    private CheckListView<OrderItem> orderPaneCheckListView;
    private TextArea orderPaneTextArea;
    private Region orderPaneRegion;

    private VBox orderPaneVbox;
    private KitchenPageController kitchenController;
    private Order order;
    private OrderItem orderItem;
    DatabaseConnection connectNow = new DatabaseConnection(kitchenController);



    public OrderPane(KitchenPageController controller, Order order){

        this.kitchenController = controller;
        this.order = order;
        //this.orderItem = orderItem;



        orderPaneLabel = new Label();
        orderPaneLabel.setText("Order Id: " + order.getId());
        orderPaneLabel.getStyleClass().add("orderPaneLabel");
//        orderPaneLabel.setAlignment(Pos.TOP_LEFT);
//        orderPaneLabel.setPadding(new Insets(0,0,0,5));
        orderPaneLabel.setPadding(new Insets(5,0,0,5));



        orderPaneRegion = new Region();
        orderPaneRegion.getStyleClass().add("orderPaneRegion");
        //orderPaneRegion.setvgrow(); ma zabatet 2m 7atayt height
        orderPaneRegion.setPrefHeight(80);
        orderPaneRegion.setMinHeight(orderPaneRegion.getPrefHeight());

        orderPaneButton = new Button();
        orderPaneButton.getStyleClass().add("orderPaneButton");
        orderPaneButton.setText("Order Complete");
        orderPaneButton.setAlignment(Pos.CENTER);
        orderPaneButton.setPrefSize(160,60);
        orderPaneButton.setMinSize(orderPaneButton.getPrefWidth(),orderPaneButton.getPrefHeight());
        //orderPaneButton.setPadding(new Insets(30,10,10,10));
        orderPaneButton.setOnAction(this::completeOrder);



        orderPaneVbox = new VBox();
        orderPaneVbox.getStyleClass().add("orderPaneVbox");
        orderPaneVbox.setAlignment(Pos.BASELINE_LEFT);
        orderPaneVbox.setPrefWidth(200);
        orderPaneVbox.setMinWidth(orderPaneVbox.getPrefWidth());
        orderPaneVbox.getChildren().addAll(orderPaneLabel, orderPaneRegion, orderPaneButton );
        //orderPaneVbox.setPadding(new Insets(0,20,0,20));

        orderPaneCheckListView = new CheckListView<>();
        orderPaneCheckListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderItem>() {
            @Override
            public void changed(ObservableValue<? extends OrderItem> observableValue, OrderItem orderItem, OrderItem t1) {
                //System.out.println(t1.getComment());

                orderPaneTextArea.setText(t1.getComment());
            }
        });
        orderPaneCheckListView.getStyleClass().add("orderPaneCheckListView");
        setHgrow(orderPaneCheckListView, Priority.ALWAYS);
        orderPaneCheckListView.setPrefWidth(300);
        orderPaneCheckListView.setPadding(new Insets(5,8,5,8));
        orderPaneCheckListView.getItems().addAll(order.getOrderItems());




        orderPaneTextArea = new TextArea();
        //orderPaneTextArea.setText(orderItem.getComment());
        orderPaneTextArea.getStyleClass().add("orderPaneTextArea");
        orderPaneTextArea.setEditable(false);
        orderPaneTextArea.autosize();
        orderPaneTextArea.setPadding(new Insets(0,11,0,11));
        orderPaneTextArea.setPrefWidth(300);
        setHgrow(orderPaneTextArea,Priority.ALWAYS);



        setMargin(orderPaneVbox, new Insets(10,10,10,20));
        setMargin(orderPaneCheckListView,new Insets(10,10,10,10));
        setMargin(orderPaneTextArea, new Insets(10,10,10,10));
        getStyleClass().add("orderPaneHbox");
        setPrefHeight(260);
        setMinHeight(getPrefHeight());




        getChildren().addAll(orderPaneVbox, orderPaneCheckListView, orderPaneTextArea);

    }


    public void completeOrder(ActionEvent event){



        System.out.println("complete button clicked");
        orderPaneCheckListView.getCheckModel().checkAll();

        //methods to set order_status and ordercontent_status = 2
        connectNow.orderComplete(order.getId());

        System.out.println(this.toString());
        kitchenController.getVboxMain().getChildren().remove(this);

        connectNow.increaseIngredients(order.getId());
        System.out.println(order.getId());

    }





}
