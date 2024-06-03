package cashier;

import connection.DBConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import login.LoginPageController;
import order.*;
import properties.Properties;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CashierPageController implements Initializable {

    @FXML
    private BorderPane orderViewerPage, orderHistoryPage;
    @FXML
    private FlowPane inHouseFlow, deliveryFlow, oldInHouseFlow, oldDeliveryFlow;
    @FXML
    private VBox orderVbox, orderDetailsPane, orderHistoryDetailsPane;
    @FXML
    private ScrollPane orderViewerPane, orderHistoryViewerPane;
    @FXML
    private HBox tableBox, sectionBox, customerNameBox, addressBox, oldTableBox, oldSectionBox, oldCustomerNameBox,
            oldAddressBox;
    @FXML
    private Label cashierName, orderIdLabel, orderDiscountLabel, orderTableLabel, orderSectionLabel, customerNameLabel,
            orderAddressLabel, orderTotalLabel, oldOrderIdLabel, oldOrderDiscountLabel, oldOrderTableLabel,
            oldOrderSectionLabel, oldCustomerNameLabel, oldOrderAddressLabel, oldOrderTotalLabel;
    @FXML
    private TextField paidUsdField, paidLbpField, tipUsdField, tipLbpField, oldPaidUsdField, oldPaidLbpField,
            oldTipUsdField, oldTipLbpField;
    @FXML
    private Button backButton, historyBackButton, refreshButton, historyRefreshButton, checkoutButton,
            printInvoiceButton, printOldInvoiceButton, orderViewerButton, orderHistoryButton,
            takeDeliveryButton;
    @FXML
    private TextArea invoiceArea, oldInvoiceArea;

    private DBConnection connection;
    private Order selectedOrder = null;
    private boolean isOrderOpened = false;
    private int rate = 0;
    private DecimalFormat formatter, invoiceFormatter;
    private String name = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {

            onStartup();

        });
    }

    /**
     * @Function Start up initializations
     */
    private void onStartup(){
        formatter = new DecimalFormat(Properties.DOUBLE_PATTERN);
        invoiceFormatter = new DecimalFormat(Properties.PATTERN);

        connection = new DBConnection(this);
        connection.getOrders();
        connection.getOldOrders();

        bindSideButton(orderViewerPage, orderViewerButton);
        bindSideButton(orderHistoryPage, orderHistoryButton);

        bindBackButton(orderDetailsPane, backButton);
        bindBackButton(orderHistoryDetailsPane, historyBackButton);
    }

    /**
     * Binds the side button's styling to its respective pane. if the button is selected a different
     * styling will be applied and if not another one should be applied
     * @param pane The borderPane to be bound with
     * @param button The button to be bound
     */
    private void bindSideButton(BorderPane pane, Button button){
        pane.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(aBoolean){
                    button.getStyleClass().remove("selectedSideButton");
                    button.getStyleClass().add("sideButton");
                }else{
                    button.getStyleClass().remove("sideButton");
                    button.getStyleClass().add("selectedSideButton");
                }
            }
        });
    }

    /**
     * Binds the back button with a pane. For example: if the vbox is visible then it is enabled else it is disabled
     * @param pane The pane to be bound to
     * @param button The button to bind with
     */
    private void bindBackButton(VBox pane, Button button){
        pane.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(pane.isVisible()){
                    button.setDisable(false);
                }else{
                    button.setDisable(true);
                }
            }
        });
    }

    /**
     * Opens the order details viewer page
     * @param selectedOrder The order whom the details should be displayed
     */
    public void openOrder(Order selectedOrder){
        this.selectedOrder = selectedOrder;

        rate = connection.getRate();

        isOrderOpened = true;

        initInvoiceArea(invoiceArea);
        initOrderLabels();

        if(this.selectedOrder.getTable() == Properties.DELIVERY){
            openDeliveryOrder();
        }else{
            openInHouseOrder();
        }

        clearFields();

        paidUsdField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!paidUsdField.getText().isEmpty()){
                    checkoutButton.setDisable(false);
                }else{
                    checkoutButton.setDisable(true);
                }
            }
        });

        orderViewerPane.setVisible(false);
        orderDetailsPane.setVisible(true);
    }

    public void openOldOrder(Order selectedOrder){
        this.selectedOrder = selectedOrder;

        rate = connection.getRate();

        isOrderOpened = true;

        initInvoiceArea(oldInvoiceArea);
        initOrderLabels();

        if(selectedOrder.getTable() == Properties.DELIVERY){
            openDeliveryOrder();
        }else{
            openInHouseOrder();
        }

        initOldFields();

        orderHistoryViewerPane.setVisible(false);
        orderHistoryDetailsPane.setVisible(true);
    }

    /**
     * @Function Shows the Table and Section Boxes and Hides the Customer Name and Address Boxes
     */
    public void openInHouseOrder(){
        setDeliveryVisible(false);
        setInHouseVisible(true);
    }

    /**
     * @Function Shows the Customer Name and Address Boxes and Hides the Table and Section Boxes
     */
    public void openDeliveryOrder(){
        setInHouseVisible(false);
        setDeliveryVisible(true);
    }

    private void initOrderLabels(){
        if(selectedOrder.getStatus() == Properties.CHECKED_OUT){
            oldOrderIdLabel.setText(String.valueOf(selectedOrder.getId()));
            oldOrderDiscountLabel.setText("% " + selectedOrder.getDiscount());
            oldOrderTableLabel.setText(String.valueOf(selectedOrder.getTable()));
            oldOrderSectionLabel.setText(selectedOrder.getSection());
            oldCustomerNameLabel.setText(selectedOrder instanceof DeliveryOrder ? ((DeliveryOrder)selectedOrder).getCustomer().getName() : "");
            oldOrderAddressLabel.setText(selectedOrder instanceof DeliveryOrder ? ((DeliveryOrder)selectedOrder).getAddress().getAddress() : "");
            oldOrderTotalLabel.setText("$ " + selectedOrder.getTotalUSD() + " / LBP " + selectedOrder.getTotalLBP());   
        }else{
            orderIdLabel.setText(String.valueOf(selectedOrder.getId()));
            orderDiscountLabel.setText("% " + selectedOrder.getDiscount());
            orderTableLabel.setText(String.valueOf(selectedOrder.getTable()));
            orderSectionLabel.setText(selectedOrder.getSection());
            customerNameLabel.setText(selectedOrder instanceof DeliveryOrder ? ((DeliveryOrder)selectedOrder).getCustomer().getName() : "");
            orderAddressLabel.setText(selectedOrder instanceof DeliveryOrder ? ((DeliveryOrder)selectedOrder).getAddress().getAddress() : "");
            orderTotalLabel.setText("$ " + selectedOrder.getTotalUSD() + " / LBP " + selectedOrder.getTotalLBP());
        }
    }

    /**
     * @Function Inserts the paid(USD and LBP) and tip values in their respective fields
     */
    private void initOldFields(){
        oldPaidUsdField.setText(invoiceFormatter.format(selectedOrder.getPaidUSD()));
        oldPaidLbpField.setText(invoiceFormatter.format(selectedOrder.getPaidLBP()));
        oldTipUsdField.setText(invoiceFormatter.format(selectedOrder.getTip().getUsd()));
        oldTipLbpField.setText(invoiceFormatter.format(selectedOrder.getTip().getLbp()));
    }

    public void initInvoiceArea(TextArea area){
        int spacing = 32;
        area.setText("\tFEAST PLANNER");
        area.appendText("\n\n Order: " + selectedOrder.getId());

        if(selectedOrder.getTable() != Properties.DELIVERY){
            area.appendText("\n Table: " + selectedOrder.getTable());
        }else{
            area.appendText("\n Location: " + ((DeliveryOrder)selectedOrder).getAddress().getAddress());
        }

        area.appendText("\n\n ORDER ITEMS\t\t    PRICE");
        area.appendText("\n----------------------------------\n");

        for(OrderItem item : selectedOrder.getItems()){
            String price = "$" + (item.getQuantity() * item.getPrice());
            String name = abbreviate(item.getName(), 20) + "(x" + item.getQuantity() + ")";
            String spaces = "";
            for(int i = spacing - (price.length() + name.length()); i > 0; i--){
                spaces += " ";
            }
            area.appendText(" "  + name + spaces + price + "\n");
        }

        area.appendText("----------------------------------\n");

        String spaces = " ";

        if(selectedOrder.getTable() == Properties.DELIVERY){
            String deliveryFee = "$" + invoiceFormatter.format(((DeliveryOrder)selectedOrder).getDeliveryFee());
            area.appendText(" Delivery Fee:" + spaces.repeat(spacing - (13 + deliveryFee.length())) + deliveryFee + "\n");
        }

        if(selectedOrder.getDiscount() != 0){
            String discount = "%" + (int)(selectedOrder.getDiscount() * 100);
            area.appendText(" Discount:" + spaces.repeat(spacing - (9 + discount.length())) + discount + "\n\n");
        }

        String totalUSD = "$" + invoiceFormatter.format(selectedOrder.getTotalUSD());
        String totalLBP = "LBP " + invoiceFormatter.format(selectedOrder.getTotalLBP());
        area.appendText(" TOTAL:" + spaces.repeat(spacing - (6 + totalUSD.length())) + totalUSD +
                "\n " + spaces.repeat(spacing - totalLBP.length()) + totalLBP + "\n");

        if(selectedOrder.getStatus() == Properties.CHECKED_OUT){
            String paidUSD = "$" + invoiceFormatter.format(selectedOrder.getPaidUSD());
            String paidLBP = "LBP " + invoiceFormatter.format(selectedOrder.getPaidLBP());
            area.appendText("\n PAID:" + spaces.repeat(spacing - (5 + paidUSD.length())) + paidUSD +
                    "\n " + spaces.repeat(spacing - paidLBP.length()) + paidLBP + "\n");
            if(!selectedOrder.getTip().isEmpty()){
                String tipUSD = "$ " + invoiceFormatter.format(selectedOrder.getTip().getUsd());
                String tipLBP = "LBP " + invoiceFormatter.format(selectedOrder.getTip().getLbp());
                area.appendText("\n TIP: " + spaces.repeat(spacing - tipUSD.length()) + tipUSD + "\n " +
                        spaces.repeat(spacing - tipLBP.length()) + tipLBP + "\n");
            }
        }

        area.appendText("\n Rate: " + invoiceFormatter.format(connection.getRate()) + " LBP");
    }

    /**
     * Changes the visibility of the customer name and address labels
     * @param visible True for visible and False for invisible
     */
    public void setDeliveryVisible(boolean visible){
        if(selectedOrder.getStatus() == Properties.CHECKED_OUT){
            oldCustomerNameBox.setVisible(visible);
            oldAddressBox.setVisible(visible);
        }else{
            customerNameBox.setVisible(visible);
            addressBox.setVisible(visible);
        }
    }
    /**
     * Changes the visibility of the Table and Section labels
     * @param visible True for visible and False for invisible
     */
    public void setInHouseVisible(boolean visible){
        if(selectedOrder.getStatus() == Properties.CHECKED_OUT){
            oldSectionBox.setVisible(visible);
            oldTableBox.setVisible(visible);
        }else{
            sectionBox.setVisible(visible);
            tableBox.setVisible(visible);
        }
    }

    public void fieldsValueChanged(KeyEvent event){
        if(!((TextField)(event.getSource())).getText().isEmpty()){
            if(!((TextField)(event.getSource())).getText().matches("\\d*\\.?\\d+|[-+]?\\d+\\.\\d*")){
                ((TextField)(event.getSource())).setText(((TextField)(event.getSource())).getText().replaceAll("[^\\d*\\.]", ""));
                ((TextField)(event.getSource())).positionCaret(((TextField)(event.getSource())).getText().length());
            }
            if(event.getSource() == paidUsdField){
                double value = Double.parseDouble(paidUsdField.getText());
                double remaining = (selectedOrder.getTotalUSD() - value) * rate;

                paidLbpField.setText(String.valueOf((int)remaining));
            }else if(event.getSource() == paidLbpField){
                double value = Double.parseDouble(paidLbpField.getText());
                double remaining = (selectedOrder.getTotalLBP() - value) / rate;

                paidUsdField.setText(formatter.format(remaining));
            }
        }
    }

    public void changePage(ActionEvent event){
        if(event.getSource() == orderViewerButton){
            orderHistoryPage.setVisible(false);
            orderViewerPage.setVisible(true);
        }else if(event.getSource() == orderHistoryButton){
            orderViewerPage.setVisible(false);
            orderHistoryPage.setVisible(true);
        }else if(event.getSource() == takeDeliveryButton){
            new Application() {
                @Override
                public void start(Stage stage) throws Exception {this.stop();}
            }.getHostServices().showDocument("www.youtube.com");
        }
    }

    public void refresh(ActionEvent event){
        if(isOrderOpened){
            if(event.getSource() == historyRefreshButton){
                openOldOrder(connection.getSingleOrder(selectedOrder));
            }else{
                openOrder(connection.getSingleOrder(selectedOrder));
            }
        }else{
            if(event.getSource() == refreshButton){
                connection.getOrders();
            }else{
                connection.getOldOrders();
            }
        }
    }
    public void back(ActionEvent event){
        if(event.getSource() == historyBackButton){
            orderHistoryDetailsPane.setVisible(false);
            orderHistoryViewerPane.setVisible(true);
            connection.getOldOrders();
        }else{
            orderDetailsPane.setVisible(false);
            orderViewerPane.setVisible(true);
            connection.getOrders();
        }
        isOrderOpened = false;
    }

    public void printInvoice(ActionEvent event){
        TextArea area;
        if(event.getSource() == printInvoiceButton){
            area = invoiceArea;
        }else{
            area = oldInvoiceArea;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if(job == null){
            return;
        }

        boolean proceed = job.showPrintDialog((((Node)event.getSource()).getScene().getWindow()));
        if(proceed){
            TextFlow textFlow = new TextFlow();

            double contentHeight = textFlow.prefHeight(job.getPrinter().getDefaultPageLayout().getPrintableWidth());

            // Set the height of the TextFlow to the calculated height
            textFlow.setMaxHeight(contentHeight);
            Text text = new Text(area.getText());
            text.setFont(area.getFont());
            textFlow.getChildren().add(text);

            boolean printed = job.printPage(textFlow);
            if(printed){
                job.endJob();
                new Alert(Alert.AlertType.INFORMATION, "Receipt Printed Successfully").showAndWait();
            }
        }
    }

    public void checkout(ActionEvent event){
        double paidUSD = paidUsdField.getText().isEmpty() ? 0 : Double.parseDouble(paidUsdField.getText());
        int paidLBP = paidLbpField.getText().isEmpty() ? 0 : Integer.parseInt(paidLbpField.getText());

        if((double)paidLBP / (double)rate + paidUSD == selectedOrder.getTotalUSD() &&  paidLBP >= 0 && paidUSD >= 0){
            double tipUSD = tipUsdField.getText().isEmpty() ? 0 : Double.parseDouble(tipUsdField.getText());
            int tipLBP = tipLbpField.getText().isEmpty() ? 0 : Integer.parseInt(tipLbpField.getText());

            selectedOrder.setPaidUSD(paidUSD);
            selectedOrder.setPaidLBP(paidLBP);

            connection.checkoutOrder(selectedOrder, new Tip(tipUSD, tipLBP));

            showDialog(Alert.AlertType.INFORMATION, "Operation Successful", "Order Checked Out Successfully");

            back(event);
        }else{
            showDialog(Alert.AlertType.ERROR, "Error", "Total Paid Is Incorrect");
        }
    }

    public void logout(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/LoginPage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FlowPane getInHouseFlow() {
        return inHouseFlow;
    }
    public void setInHouseFlow(ArrayList<OrderCard> orders){
        inHouseFlow.getChildren().clear();
        inHouseFlow.getChildren().addAll(orders);
    }

    public FlowPane getOldInHouseFlow(){
        return oldInHouseFlow;
    }
    public void setOldInHouseFlow(ArrayList<OrderCard> orders){
        oldInHouseFlow.getChildren().clear();
        oldInHouseFlow.getChildren().addAll(orders);
    }

    public FlowPane getDeliveryFlow() {
        return deliveryFlow;
    }
    public void setDeliveryFlow(ArrayList<OrderCard> orders){
        deliveryFlow.getChildren().clear();
        deliveryFlow.getChildren().addAll(orders);
    }

    public FlowPane getOldDeliveryFlow(){
        return oldDeliveryFlow;
    }
    public void setOldDeliveryFlow(ArrayList<OrderCard> orders){
        oldDeliveryFlow.getChildren().clear();
        oldDeliveryFlow.getChildren().addAll(orders);
    }

    public void clearFields(){
        paidUsdField.clear();
        paidLbpField.clear();
        tipUsdField.clear();
        tipLbpField.clear();
    }

    /**
     * Abbreviates a string if its length exceeds its maximum length
     * @param str The string to be abbreviated
     * @param maxLength The maximum length allowed for a string
     * @return The abbreviated string
     */
    public String abbreviate(String str, int maxLength){
        if(str.length() > maxLength){
            return str.substring(0, maxLength - 3) + "...";
        }
        return str;
    }

    public String indent(String message){
        final int space = 21;
        String text = message;
        if(text.length() > space){
            StringBuilder deconstructed = new StringBuilder();
            int index = 0;
            while (!text.equals("")){
                if(index + space >= text.length()){
                    deconstructed.append(text.substring(0, text.length())).append(".");
                    break;
                }else{
                    deconstructed.append(text.substring(0, index + space)).append("\n").append(" ".repeat(10));
                }
                text = text.substring(index + space, text.length());
                index += space;
            }
            return deconstructed.toString();
        }
        return text;
    }

    public void setCashierName(String name){
        this.name = name;
        cashierName.setText(name);
    }

    public void showDialog(Alert.AlertType type, String title, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}