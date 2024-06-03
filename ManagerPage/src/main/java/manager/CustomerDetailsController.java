package manager;

import customer.Address;
import customer.Customer;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Window;
import order.DeliveryOrder;
import order.OrderItem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class CustomerDetailsController {

    @FXML
    private MFXGenericDialog dialogContent;
    @FXML
    private Label nameLabel, phoneLabel, emailLabel;
    @FXML
    private MFXListView<DeliveryOrder> orderViewer;
    @FXML
    private MFXListView<Address> addressViewer;
    private MFXStageDialog dialog;
    private Window window;
    private FXMLLoader loader;

    public CustomerDetailsController(){}


    public void openCustomerDetails(Window window, Customer customer){
        this.window = window;
        initializeDialog();
        initFields();
        fillFields(customer);

        dialog.showAndWait();
    }

    private void initializeDialog(){
        try{
            loader = new FXMLLoader(getClass().getResource("/manager/CustomerDetailsViewer.fxml"));
            dialogContent = loader.load();
            dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder().initOwner(window)
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(true).setTitle("Customer Details").get();

            MFXButton okButton = new MFXButton("Close");
            okButton.getStyleClass().add("dialogBtn");
            dialogContent.addActions(Map.entry(okButton, event -> {dialog.close();}));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initFields(){
        nameLabel = (Label) dialogContent.lookup("#nameLabel");
        phoneLabel = (Label) dialogContent.lookup("#phoneLabel");
        emailLabel = (Label) dialogContent.lookup("#emailLabel");
        orderViewer = (MFXListView<DeliveryOrder>) dialogContent.lookup("#orderViewer");
        addressViewer = (MFXListView<Address>) dialogContent.lookup("#addressViewer");

        orderViewer.getSelectionModel().setAllowsMultipleSelection(false);
        addressViewer.getSelectionModel().setAllowsMultipleSelection(false);

        orderViewer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!orderViewer.getSelectionModel().getSelectedValues().isEmpty()){
                    if(event.getClickCount() >= 2){
                        OrderItemsController c = new OrderItemsController();
                        c.showDetailsPane(orderViewer.getSelectionModel().getSelectedValues().get(0));
                        orderViewer.getSelectionModel().clearSelection();
                    }
                }
            }
        });
    }

    public void fillFields(Customer customer){
        nameLabel.setText(customer.getName());
        phoneLabel.setText(customer.getPhoneNumber());
        emailLabel.setText(customer.getEmail());

        orderViewer.getItems().clear();
        orderViewer.getItems().addAll(customer.getOrders());

//        ArrayList<OrderItem> test = new ArrayList<>();
//        test.add(new OrderItem("Tabbouleh", "Extra Sour", 2));
//        orderViewer.getItems().add(new DeliveryOrder(1234, 999, 1, LocalDateTime.now(),
//                0, 25, 2000000, 0, 0,
//                test, new Address(1, "Home", "Sin El Fil, Al Ghazal Street"), "Tony Hanna", 3.5));

        addressViewer.getItems().clear();
        addressViewer.getItems().addAll(customer.getAddresses());
    }
}
