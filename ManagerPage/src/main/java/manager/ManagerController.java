package manager;

import customer.Customer;
import dbConnection.DBConnection;
import export.ExcelConverter;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTreeItem;
import io.github.palexdev.materialfx.controls.MFXTreeView;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import menu.Dish;
import menu.Ingredient;
import menu.InventoryItem;
import order.Order;
import org.controlsfx.control.ToggleSwitch;
import org.kordamp.ikonli.javafx.FontIcon;
import properties.Properties;
import qrcode.QrGenerator;
import section.Section;
import settings.Category;
import settings.Unit;
import staff.Salary;
import staff.Staff;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ManagerController implements Initializable {

    @FXML
    private BorderPane waiterPane, cashierPane, kitchenPane, managerPane, ordersPane, menuPane, inventoryPane,
            qrCodePane, statPane, settingsPane, customerPane;
    @FXML
    private ScrollPane settingsScrollPane;
    @FXML
    private HBox tableBox, inventoryBox;
    @FXML
    private LineChart<String, Double> revenueChart;
    @FXML
    private BarChart<String, Integer> topDishChart;
    @FXML
    private PieChart salaryChartUSD, salaryChartLBP;
    @FXML
    private Label managerName, totalWaiter, totalCashier, totalKitchen, totalManager, totalMenu, totalItems,
            totalOrders, totalCustomer;
    @FXML
    private Button waiterButton, cashierButton, kitchenButton, managerButton, ordersButton, statButton, menuButton,
            inventoryButton, customerButton, qrCodeButton, reportButton, settingsButton, logoutButton,
            addWaiter, addCashier, addKitchen, addManager, addMenu, addItem,
            removeWaiter, removeCashier, removeKitchen, removeManager, removeMenu, removeItem,
            editWaiter, editCashier, editKitchen, editManager, editMenu, editItem,
            searchWaiters, searchCashiers, searchKitchen, searchManagers, searchMenu, searchInventory, searchCustomer,
            refreshWaiter, refreshCashier, refreshKitchen, refreshManager, refreshMenu, refreshInventory, refreshCustomer,
            showOrderDetail, refreshOrder, searchOrder,
            idWaiter, idCashier, idKitchen, idManager,
            generateButton, chooseSaveLocationButton, saveButton, saveItemButton,
            showTipsDistribution, exportInventoryButton, showCustomerDetails,
            restoreRateButton, saveRateButton, restoreDeliveryFeeButton, saveDeliveryFeeButton,
            addSection, addTable, deleteSection, editSection, editTable,
            addDishCategory, editDishCategory, deleteDishCategory,
            addItemCategory, editItemCategory, deleteItemCategory,
            addUnit, editUnit, deleteUnit;
    @FXML
    private RadioButton tableRadio, inventoryRadio;
    @FXML
    private ChoiceBox<String> waiterSearchChoice, cashierSearchChoice, kitchenSearchChoice, managerSearchChoice,
            menuSearchChoice, inventorySearchChoice, sectionChoice, tableNumberChoice, itemChoice, orderSearchChoice,
            customerSearchChoice;
    @FXML
    private TextField waiterSearchField, cashierSearchField, kitchenSearchField, managerSearchField, menuSearchField,
            inventorySearchField, quantityField, rateField, deliveryFeeField, sectionField, dishCategoryField,
            itemCategoryField, unitField, orderSearchField, customerSearchField;
    @FXML
    private MFXTextField totalTipsField;
    @FXML
    private MFXComboBox<String> tipIntervalCombo;
    @FXML
    private ListView<Category> dishCategoryViewer, itemCategoryViewer;
    @FXML
    private ListView<Unit> unitViewer;
    @FXML
    private ImageView qrCodeImage;
    @FXML
    private TableView<Staff> waiterTable, cashierTable, kitchenTable, managerTable;
    @FXML
    private TableView<Dish> menuTable;
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Staff, String> waiterNameColumn, cashierNameColumn, kitchenNameColumn, managerNameColumn,
            waiterSectionColumn, managerSectionColumn;
    @FXML
    private TableColumn waiterAgeColumn, cashierAgeColumn, kitchenAgeColumn, managerAgeColumn;
    @FXML
    private TableColumn waiterPhoneColumn, cashierPhoneColumn, kitchenPhoneColumn, managerPhoneColumn;
    @FXML
    private TableColumn waiterSalaryColumn, cashierSalaryColumn, kitchenSalaryColumn, managerSalaryColumn;
    @FXML
    private TableColumn waiterDateColumn, cashierDateColumn, kitchenDateColumn, managerDateColumn;
    @FXML
    private TableColumn waiterShiftColumn, cashierShiftColumn, kitchenShiftColumn, managerShiftColumn;
    @FXML
    private TableColumn waiterStatusColumn, cashierStatusColumn, kitchenStatusColumn, managerStatusColumn;
    @FXML
    private TableColumn menuNameColumn, menuCategoryColumn;
    @FXML
    private TableColumn menuDescriptionColumn, menuStatusColumn, orderIdColumn, orderDateColumn, orderDiscountColumn,
            orderTotalColumn, orderTotalPaidColumn, orderSourceColumn;
    @FXML
    private TableColumn menuPriceColumn, menuCostColumn;
    @FXML
    private TableColumn customerNameColumn, customerIdColumn, customerPhoneColumn, customerEmailColumn,
            customerFavoriteColumn;
    @FXML
    private TableColumn<InventoryItem, String> itemNameColumn, itemCategoryColumn, itemUnitColumn;
    @FXML
    private TableColumn<InventoryItem, Integer> itemQuantityColumn;
    @FXML
    private TableColumn<InventoryItem, Double> itemCostColumn;
    @FXML
    private MFXTreeView<Section> sectionViewer;

    private ObservableList<Staff> waiters = FXCollections.observableArrayList(),
            cashiers = FXCollections.observableArrayList(),
            kitchens = FXCollections.observableArrayList(),
            managers = FXCollections.observableArrayList();
    private ObservableList<Dish> dishes = FXCollections.observableArrayList();
    private ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<String> tipIntervalChoices = FXCollections.observableArrayList("This Month",
            "Last Month", "Last 30 Days", "This Week", "Last 7 days", "Today");

    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<BorderPane> panes = new ArrayList<>();
    private ArrayList<Category> dishCategory = new ArrayList<>(), itemCategory = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private final String[] staffChoices = {"Name", "Age", "Salary", "Phone Number", "Date Joined", "Shift"};
    private final String[] menuChoices = {"Name", "Category", "Cost","Price", "Description"};
    private final String[] inventoryChoices = {"Name", "Category", "Cost", "Unit", "Quantity"};
    private final String[] orderChoices = {"ID", "Discount", "Total", "Total Paid", "Source"};
    private final String[] customerChoices = {"Name", "ID", "Phone Number", "Email"};
    private String qrData = "", qrName = "";
    private String ownerName = "";
    private double totalTips;
    private int rate = 0;
    private DecimalFormat formatter;
    private String[] itemNameList;
    private File saveLocation;
    private QrGenerator generator;
    private DBConnection connection;
    private Preferences preferences;
    private Stage stage;
    private Parent root;
    private Scene scene;
    private FXMLLoader loader;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() ->{
            initializeSideBar();

            initializeWaiterPane();

            initializeCashierPane();

            initializeKitchenPane();

            initializeManagerPane();

            initializeMenuPane();

            initializeInventoryPane();

            initializeQrCodePane();

            initializeStatPane();

            initializeSettingsPane();

            initializeOrderPane();

            initCustomerPane();
        });
        buttons.addAll(Arrays.asList(waiterButton, cashierButton, kitchenButton, managerButton, statButton, menuButton,
                inventoryButton, qrCodeButton, settingsButton, ordersButton, customerButton));
        panes.addAll(Arrays.asList(waiterPane, cashierPane, kitchenPane, managerPane, statPane, menuPane, inventoryPane,
                qrCodePane, settingsPane, ordersPane, customerPane));
    }

    /**
     * All the add buttons are linked to it and handles each add operation depending on pressed button
     * @param event The button which is pressed
     */
    public void addStaff(ActionEvent event){

        StaffManagerController controller = new StaffManagerController();
        controller.setManagerController(this);
        if (event.getSource() == addWaiter) {
            controller.openPane(2, "waiter");
            disableWaiterButtons();
        }
        if (event.getSource() == addCashier) {
            controller.openPane(3, "cashier");
            disableCashierButtons();
        }
        if (event.getSource() == addKitchen) {
            controller.openPane(4, "kitchen");
            disableKitchenButtons();
        }
        if (event.getSource() == addManager) {
            controller.openPane(1, "manager");
            disableManagerButtons();
        }
        if (event.getSource() == addMenu) {
            DishManagerController dishManagerController = new DishManagerController();
            dishManagerController.setManagerController(this);
            dishManagerController.openAddDishPane();
            refreshDish();
            disableMenuButtons();
            countDishes();
        }
        if(event.getSource() == addItem){
            ItemManagerController itemManagerController = new ItemManagerController();
            itemManagerController.setManagerController(this);
            itemManagerController.openAddItemPane();
            refreshInventory();
            disableInventoryButtons();
        }
        if(event.getSource() == addSection){
            if(!sectionField.getText().isEmpty()){
                connection.insertSection(sectionField.getText());
                refreshSectionViewer();
            }
        }
        if(event.getSource() == addTable){
            TableManagerController tableController = new TableManagerController();
            tableController.setManagerController(this);
            tableController.openAddSection(sectionViewer.getSelectionModel().getSelectedItem().getData());
//            refreshSectionViewer();
        }
        if(event.getSource() == addDishCategory){
            double vtemp = settingsScrollPane.getVvalue();
            connection.insertCategory("category", new Category(dishCategoryField.getText()));
            dishCategoryViewer.getSelectionModel().clearSelection();
            refreshDishViewer();
            settingsScrollPane.setVvalue(vtemp);
        }
        if(event.getSource() == addItemCategory){
            connection.insertCategory("item_category", new Category(itemCategoryField.getText()));
            itemCategoryViewer.getSelectionModel().clearSelection();
            refreshItemCategory();
        }
        if(event.getSource() == addUnit){
            connection.insertUnit(new Unit(unitField.getText()));
            unitViewer.getSelectionModel().clearSelection();
            refreshUnitViewer();
        }
    }

    /**
     * Refreshes the staff table depending on which staff type was edited
     * @param type: the first digit of their id represents the staff type: 1-supervisors, 2-waiters, 3-cashiers, 4-kitchen crew
     */
    public void newStaff(int type) {
        switch (type) {
            case 1:
                refreshStaff(managerTable, managers, "manager");
                getManagerTotalSalaries();
                break;
            case 2:
                refreshStaff(waiterTable, waiters, "waiter");
                getWaitersTotalSalaries();
                break;
            case 3:
                refreshStaff(cashierTable, cashiers, "cashier");
                getCashierTotalSalaries();
                break;
            case 4:
                refreshStaff(kitchenTable, kitchens, "kitchen");
                getKitchenTotalSalaries();
                break;
        }
    }

    /**
     * Handles all the delete buttons in the system
     * @param event
     */
    public void remove(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (event.getSource() == removeWaiter) {
            // Continue from here.
            removeStaff("waiter", waiterTable, waiters, alert);
            // Disable the deleteButton after deleting the row
            disableWaiterButtons();
            getWaitersTotalSalaries();
        }
        if (event.getSource() == removeCashier) {
            removeStaff("cashier", cashierTable, cashiers, alert);
            disableCashierButtons();
            getCashierTotalSalaries();
        }
        if (event.getSource() == removeKitchen) {
            removeStaff("kitchen", kitchenTable, kitchens, alert);
            disableKitchenButtons();
            getKitchenTotalSalaries();
        }
        if (event.getSource() == removeManager) {
            removeStaff("manager", managerTable, managers, alert);
            disableManagerButtons();
            getManagerTotalSalaries();
        }
        if (event.getSource() == removeMenu) {
            removeDish("Dish", menuTable, alert);
            disableMenuButtons();
            countDishes();
        }
        if (event.getSource() == removeItem) {
            removeItem("Item", inventoryTable, alert);
            disableInventoryButtons();
            countItems();
        }
        if(event.getSource() == deleteSection){
            connection.deleteSectionOrTable(sectionViewer.getSelectionModel().getSelectedItem().getData());
            refreshSectionViewer();
            sectionViewer.getSelectionModel().clearSelection();
            sectionField.clear();
        }
        if(event.getSource() == deleteDishCategory){
            double vtemp = settingsScrollPane.getVvalue();
            System.out.println("Before " + settingsScrollPane.getVvalue());
            removeCategory("category", dishCategoryViewer, alert);
            refreshDishViewer();
            settingsScrollPane.setVvalue(vtemp);
        }
        if(event.getSource() == deleteItemCategory){
            removeCategory("item_category", itemCategoryViewer, alert);
            refreshItemCategory();
        }
        if(event.getSource() == deleteUnit){
            connection.deleteUnit(unitViewer.getSelectionModel().getSelectedItem());
            unitViewer.getSelectionModel().clearSelection();
            refreshUnitViewer();
        }
    }

    /**
     * Displays an alert and if the OK button was clicked, it deletes the staff depending on its type
     * @param tableName The type of the staff to be deleted (waiter, manager, cashier, kitchen)<br>
     * @param table The TableView from where we selected the staff member<br>
     * @param list The list containing the staff member<br>
     * @param alert The alert to be shown
     */
    private void removeStaff(String tableName, TableView<Staff> table, ObservableList<Staff> list , Alert alert) {
        alert.setTitle("Remove " + tableName);
        alert.setHeaderText("You are about to remove a " + tableName);
        alert.setContentText("Are You sure you want to remove this staff ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Get the selected item
            Staff selectedItem = table.getSelectionModel().getSelectedItem();

            // Delete the row from the Database
            connection.delete(tableName, selectedItem.getId());
            // Refreshes the Table
            refreshStaff(table, list, tableName);
        }
        table.getSelectionModel().clearSelection();
    }

    /**
     * Displays an alert and if the OK button was clicked, it deletes the dish from the table and the database
     * @param position The name of the dish
     * @param table The table where we selected the dish and where it was displayed
     * @param alert The alert to be shown
     */
    public void removeDish(String position, TableView<Dish> table, Alert alert) {
        alert.setTitle("Remove " + position);
        alert.setHeaderText("You are about to remove a " + position);
        alert.setContentText("Are You sure you want to remove this " + position + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Get the selected item
            connection.delete("dish", table.getSelectionModel().getSelectedItem().getId());

            refreshDish();
        }
        // Clear the selection
        table.getSelectionModel().clearSelection();
    }

    public void removeItem(String position, TableView<InventoryItem> table, Alert alert) {
        alert.setTitle("Remove Inventory Item");
        alert.setHeaderText("You are about to remove " + position);
        alert.setContentText("Are You sure you want to remove " + position + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Get the selected item
            InventoryItem selectedItem = table.getSelectionModel().getSelectedItem();

            // Delete the row from the TableView's data source
            connection.delete("inventory", selectedItem.getId());
            refreshInventory();
        }
        // Clear the selection
        table.getSelectionModel().clearSelection();
    }

    public void removeCategory(String tableName, ListView<Category> listView, Alert alert){
        alert.setTitle("Delete Category");
        alert.setHeaderText("Are you sure you want to delete this category");

        if(alert.showAndWait().get() == ButtonType.OK){
            connection.deleteCategory(tableName, listView.getSelectionModel().getSelectedItem());

        }
        listView.getSelectionModel().clearSelection();
    }

    /**
     * Handles the Pages switching after clicking any of the side buttons
     * @param event Which button was clicked
     */
    public void switchPanes(ActionEvent event){
        Pane targetPane = null;

        if (event.getSource() == waiterButton) {
            targetPane = waiterPane;
        } else if (event.getSource() == cashierButton) {
            targetPane = cashierPane;
        } else if (event.getSource() == kitchenButton) {
            targetPane = kitchenPane;
        } else if (event.getSource() == managerButton) {
            targetPane = managerPane;
        } else if (event.getSource() == menuButton) {
            targetPane = menuPane;
        } else if (event.getSource() == inventoryButton) {
            targetPane = inventoryPane;
        } else if (event.getSource() == qrCodeButton) {
            targetPane = qrCodePane;
        } else if (event.getSource() == statButton) {
            targetPane = statPane;
        } else if (event.getSource() == settingsButton) {
            targetPane = settingsPane;
        } else if (event.getSource() == ordersButton){
            targetPane = ordersPane;
        }else if (event.getSource() == customerButton){
            targetPane = customerPane;
        }

        if(targetPane != null){
            if(!targetPane.isVisible()){
                changePane(targetPane);
            }
        }
    }

    /**
     * Sets all the panes to invisible and sets the selected pane to visible
     * @param selectedPane
     */
    public void changePane(Pane selectedPane){
        for(int i = 0; i < panes.size(); i++){
            panes.get(i).setVisible(false);
        }
        selectedPane.setVisible(true);

        if(selectedPane == statPane){
            refreshSalaryChart();
        }
    }

    public void openEditPane(ActionEvent event) {
        if (event.getSource() == editWaiter) {
            StaffManagerController controller = new StaffManagerController();
            controller.setManagerController(this);
            controller.openEditPane(waiterTable.getSelectionModel().getSelectedItem());
            refreshStaff(waiterTable, waiters, "waiter");
            waiterTable.getSelectionModel().clearSelection();
            disableWaiterButtons();
            getWaitersTotalSalaries();
        }
        if (event.getSource() == editCashier) {
            StaffManagerController controller = new StaffManagerController();
            controller.setManagerController(this);
            controller.openEditPane(cashierTable.getSelectionModel().getSelectedItem());
            refreshStaff(cashierTable, cashiers, "cashier");
            cashierTable.getSelectionModel().clearSelection();
            disableCashierButtons();
            getCashierTotalSalaries();
        }
        if(event.getSource() == editKitchen){
            StaffManagerController controller = new StaffManagerController();
            controller.setManagerController(this);
            controller.openEditPane(kitchenTable.getSelectionModel().getSelectedItem());
            refreshStaff(kitchenTable, kitchens, "kitchen");
            kitchenTable.getSelectionModel().clearSelection();
            disableKitchenButtons();
            getKitchenTotalSalaries();
        }
        if (event.getSource() == editManager) {
            StaffManagerController controller = new StaffManagerController();
            controller.setManagerController(this);
            controller.openEditPane(managerTable.getSelectionModel().getSelectedItem());
            refreshStaff(managerTable, managers, "manager");
            managerTable.getSelectionModel().clearSelection();
            getManagerTotalSalaries();
            disableManagerButtons();
        }
        if (event.getSource() == editMenu) {
            DishManagerController dishController = new DishManagerController();
            dishController.setManagerController(this);
            dishController.openEditDishPane(menuTable.getSelectionModel().getSelectedItem());
            refreshDish();
            disableMenuButtons();
        }
        if(event.getSource() == editItem){
            ItemManagerController itemController = new ItemManagerController();
            itemController.setManagerController(this);
            itemController.openEditItemPane(inventoryTable.getSelectionModel().getSelectedItem());
            inventoryTable.getSelectionModel().clearSelection();
            refreshInventory();
            disableInventoryButtons();
        }
        if(event.getSource() == editSection){
            if(!sectionField.getText().isEmpty()){
                connection.editSection(sectionViewer.getSelectionModel().getSelectedItem().getData(), sectionField.getText());
                sectionViewer.getSelectionModel().clearSelection();
                refreshSectionViewer();
                sectionField.clear();
            }
        }
        if(event.getSource() == editTable){
            TableManagerController tableController = new TableManagerController();
            tableController.setManagerController(this);
            tableController.openEditTablePane(sectionViewer.getSelectionModel().getSelectedItem().getData(),
                    sectionViewer.getSelectionModel().getSelectedItem().getItemParent().getData().getName());
            sectionViewer.getSelectionModel().clearSelection();
            refreshSectionViewer();
        }
        if(event.getSource() == editDishCategory){
            connection.editCategory("category", new Category(dishCategoryField.getText()),
                    dishCategoryViewer.getSelectionModel().getSelectedItem().getName());
            dishCategoryViewer.getSelectionModel().clearSelection();
            refreshDishViewer();
        }
        if(event.getSource() == editItemCategory){
            connection.editCategory("item_category", new Category(itemCategoryField.getText()),
                    itemCategoryViewer.getSelectionModel().getSelectedItem().getName());
            dishCategoryViewer.getSelectionModel().clearSelection();
            refreshItemCategory();
        }
        if(event.getSource() == editUnit){
            connection.editUnit(unitViewer.getSelectionModel().getSelectedItem(), unitField.getText());
            unitViewer.getSelectionModel().clearSelection();
            refreshUnitViewer();
        }
    }

    public void search(ActionEvent event) {
        if (event.getSource() == searchWaiters) {
            if (!waiterSearchField.getText().equals("")) {
                String search = waiterSearchField.getText();
                ObservableList<Staff> searchResult = FXCollections.observableArrayList();
                staffSearch(search, waiterSearchChoice, waiters, searchResult);
                waiterTable.setItems(searchResult);
            }
        }
        if (event.getSource() == searchCashiers) {
            if (!waiterSearchField.getText().equals("")) {
                String search = cashierSearchField.getText();
                ObservableList<Staff> searchResult = FXCollections.observableArrayList();
                staffSearch(search, cashierSearchChoice, cashiers, searchResult);
                cashierTable.setItems(searchResult);
            }
        }
        if (event.getSource() == searchKitchen) {
            if (!kitchenSearchField.getText().equals("")) {
                String search = kitchenSearchField.getText();
                ObservableList<Staff> searchResult = FXCollections.observableArrayList();
                staffSearch(search, kitchenSearchChoice, kitchens, searchResult);
                kitchenTable.setItems(searchResult);
            }
        }
        if (event.getSource() == searchManagers) {
            if (!managerSearchField.getText().equals("")) {
                String search = managerSearchField.getText();
                ObservableList<Staff> searchResult = FXCollections.observableArrayList();
                staffSearch(search, managerSearchChoice, managers, searchResult);
                managerTable.setItems(searchResult);
            }
        }
    }

    public void refresh(ActionEvent event) {
        if (event.getSource() == refreshWaiter) {
            refreshStaff(waiterTable, waiters, "waiter");
            getWaitersTotalSalaries();
            disableWaiterButtons();
        }else if (event.getSource() == refreshCashier) {
            refreshStaff(cashierTable, cashiers, "cashier");
            getCashierTotalSalaries();
            disableCashierButtons();
        }else if (event.getSource() == refreshKitchen) {
            refreshStaff(kitchenTable, kitchens, "kitchen");
            getKitchenTotalSalaries();
            disableKitchenButtons();
        }else if (event.getSource() == refreshManager) {
            refreshStaff(managerTable, managers, "manager");
            getManagerTotalSalaries();
            disableManagerButtons();
        }else if (event.getSource() == refreshMenu) {
            refreshDish();
            disableMenuButtons();
        }else if (event.getSource() == refreshInventory) {
            refreshInventory();
            disableInventoryButtons();
        }else if(event.getSource() == refreshOrder){
            refreshOrderTable();
        }else if(event.getSource() == refreshCustomer){
            refreshCustomerTable();
        }
    }
    private void refreshStaff(TableView<Staff> table,ObservableList<Staff> list, String tableName){
        list.clear();
        list.addAll(connection.getEmployees(tableName));
        table.setItems(list);
    }
    private void refreshInventory(){
        items.clear();
        items.addAll(connection.getInventoryItems());
        inventoryTable.setItems(items);
        countItems();
    }
    public void refreshDish(){
        dishes.clear();
        dishes.addAll(connection.getDishes());
        menuTable.setItems(dishes);
        countDishes();
    }
    public void refreshAll(){
        refreshStaff(waiterTable, waiters, "waiter");
        refreshStaff(cashierTable, cashiers, "cashier");
        refreshStaff(kitchenTable, kitchens, "kitchen");
        refreshDish();
        refreshInventory();
    }

    public void showId(ActionEvent event) {
        StaffManagerController controller = new StaffManagerController();
        controller.setManagerController(this);
        if (event.getSource() == idWaiter) {
            controller.openIdPane(waiterTable.getSelectionModel().getSelectedItem());
            waiterTable.getSelectionModel().clearSelection();
            disableWaiterButtons();
        }
        if (event.getSource() == idCashier) {
            controller.openIdPane(cashierTable.getSelectionModel().getSelectedItem());
            cashierTable.getSelectionModel().clearSelection();
            disableCashierButtons();
        }
        if (event.getSource() == idKitchen) {
            controller.openIdPane(kitchenTable.getSelectionModel().getSelectedItem());
            kitchenTable.getSelectionModel().clearSelection();
            disableKitchenButtons();
        }
        if (event.getSource() == idManager) {
            controller.openIdPane(managerTable.getSelectionModel().getSelectedItem());
            managerTable.getSelectionModel().clearSelection();
            disableManagerButtons();
        }
    }

    public void staffSearch(String search, ChoiceBox<String> choice, ObservableList<Staff> searchList, ObservableList<Staff> searchResult) {
        switch (choice.getValue()) {
            case "Name":
                String[] name = search.split(" ");
                search = capitalize(search);
                for (Staff staff : searchList) {
                    if (staff.getName().contains(search))
                        searchResult.add(staff);
                }
                break;
            case "Age":
                for (Staff staff : searchList) {
                    if (staff.getAge() == Integer.parseInt(search))
                        searchResult.add(staff);
                }
                break;
            case "Salary":
                for (Staff staff : searchList) {
                    if (staff.getSalary().getUsd() == Double.parseDouble(search) ||
                            staff.getSalary().getLbp() == Double.parseDouble(search))
                        searchResult.add(staff);
                }
                break;
            case "Phone Number":
                for (Staff staff : searchList) {
                    if (staff.getPhoneNumber().contains(search))
                        searchResult.add(staff);
                }
                break;
            case "Date Joined":
                for (Staff staff : searchList) {
                    if (staff.getDateJoined().contains(search))
                        searchResult.add(staff);
                }
                break;
            case "Shift":
                search = search.toUpperCase();
                for (Staff staff : searchList) {
                    if (staff.getShift().contains(search.toUpperCase()))
                        searchResult.add(staff);
                }
                break;
        }
    }
    public void dishSearch(ActionEvent event) {
        if (!menuSearchField.getText().equals("")) {
            ObservableList<Dish> searchResult = FXCollections.observableArrayList();
            String input = menuSearchField.getText();
            switch (menuSearchChoice.getValue()) {
                case "Name":
                    input = capitalize(input);
                    for (Dish dish : dishes) {
                        if (dish.getName().equals(input))
                            searchResult.add(dish);
                    }
                    break;
                case "Cost":
                    double cost = Double.parseDouble(input);
                    for (Dish dish : dishes) {
                        if (dish.getCost() == cost)
                            searchResult.add(dish);
                    }
                    break;
                case "Price":
                    double price = Double.parseDouble(input);
                    for (Dish dish : dishes) {
                        if (dish.getPrice() == price)
                            searchResult.add(dish);
                    }
                    break;
                case "Category":
                    input = capitalize(input);
                    for (Dish dish : dishes) {
                        if (dish.getCategory().equals(input))
                            searchResult.add(dish);
                    }
                    break;
            }
            menuTable.setItems(searchResult);
        }
    }
    public void itemSearch(ActionEvent event) {
        if (!inventorySearchField.getText().equals("")) {
            ObservableList<InventoryItem> searchResult = FXCollections.observableArrayList();
            String input = inventorySearchField.getText();
            switch (inventorySearchChoice.getValue()) {
                case "Name":
                    input = capitalize(input);
                    for (InventoryItem item : items) {
                        if (item.getName().equals(input))
                            searchResult.add(item);
                    }
                    break;
                case "Category":
                    input = capitalize(input);
                    for (InventoryItem item : items) {
                        if (item.getCategory().equals(input))
                            searchResult.add(item);
                    }
                    break;
                case "Cost":
                    double price = Double.parseDouble(input);
                    for (InventoryItem item : items) {
                        if (item.getPrice() == price)
                            searchResult.add(item);
                    }
                    break;
                case "Unit":
                    for (InventoryItem item : items) {
                        if (item.getUnit().equals(input))
                            searchResult.add(item);
                    }
                    break;
                case "Quantity":
                    int quantity = Integer.parseInt(input);
                    for (InventoryItem item : items)
                        if (item.getQuantity() == quantity)
                            searchResult.add(item);
            }
            inventoryTable.setItems(searchResult);
        }
    }
    public void orderSearch(ActionEvent event){
        if(!orderSearchField.getText().isEmpty()){
            ObservableList<Order> searchResult = FXCollections.observableArrayList();
            switch (orderSearchChoice.getValue()){
                case "ID":
                    for(Order order : orders){
                        if(order.getId() == Integer.parseInt(orderSearchField.getText()))
                            searchResult.add(order);
                    }
                    break;
                case "Date":
                    for(Order order : orders){
                        if(order.getOrderDate().isEqual(LocalDateTime.parse(orderSearchField.getText()))){
                            searchResult.add(order);
                        }
                    }
                    break;
                case "Discount":
                    for(Order order : orders){
                        if(order.getDiscount() == Double.parseDouble(orderSearchField.getText())){
                            searchResult.add(order);
                        }
                    }
                    break;
                case "Total":
                    double search = Double.parseDouble(orderSearchField.getText());
                     for(Order order : orders){
                         if(order.getOrderTotalLBP() == search || order.getOrderTotalUSD() == search){
                             searchResult.add(order);
                         }
                     }
                     break;
                case "Total Paid":
                    search = Double.parseDouble(orderSearchField.getText());
                    for(Order order : orders){
                        if(order.getOrderPaidLBP() == search || order.getOrderPaidUSD() == search){
                            searchResult.add(order);
                        }
                    }
                    break;
                case "Source":
                    for(Order order : orders){
                        if(order.getOrderSource().equals(orderSearchField.getText()));
                    }
                    break;
            }
            orderTable.setItems(searchResult);
        }
    }
    public void searchCustomers(ActionEvent event){
        if(!customerSearchField.getText().isEmpty()){
            ObservableList<Customer> searchResult = FXCollections.observableArrayList();
            switch (customerSearchChoice.getValue()){
                case "Name":
                    for(Customer customer : customers){
                        if(customer.getName().equals(capitalize(customerSearchField.getText()))){
                            searchResult.add(customer);
                        }
                    }
                    break;
                case "ID":
                    for(Customer customer : customers){
                        if(customer.getId() == Integer.parseInt(customerSearchField.getText())){
                            searchResult.add(customer);
                        }
                    }
                    break;
                case "Phone Number":
                    for(Customer customer : customers){
                        if(customer.getPhoneNumber().equals(customerSearchField.getText())){
                            searchResult.add(customer);
                        }
                    }
                    break;
                case "Email":
                    for(Customer customer : customers){
                        if(customer.getEmail().equals(customerSearchField.getText())){
                            searchResult.add(customer);
                        }
                    }
                    break;
            }
            customerTable.setItems(searchResult);
        }
    }
    public String capitalize(String string){
        String[] name = string.split(" ");
        String capitalized = "";
        for(int i = 0; i < name.length; i++){
            if(i == name.length - 1){
                capitalized += name[i].substring(0, 1).toUpperCase() + name[i].substring(1);
            }else{
                capitalized += name[i].substring(0, 1).toUpperCase() + name[i].substring(1) + " ";
            }
        }
        return capitalized;
    }

    public void launchAI(ActionEvent event){
        try{
            FXMLLoader aiLoader = new FXMLLoader(getClass().getResource("/ai/AIPage.fxml"));
            Parent root = aiLoader.load();
            Scene aiScene = new Scene(root);
            Stage aiStage = new Stage();
            aiStage.setScene(aiScene);
            aiStage.setTitle("Feast Planner Assistant");
            aiStage.getIcons().add(new Image(getClass().getResource("/icons/AlphaBeta_Icon.png").openStream()));
            aiStage.show();
        }catch (IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error Launching AI Assistant").showAndWait();
        }
    }

    public void logout(ActionEvent event){
        preferences = Preferences.systemRoot().node("Tabletop").node("Remember");
        preferences.putBoolean("IsRemember", false);
        preferences.put("Username", "");
        preferences.put("Password", "");
        preferences.put("Name", "");

        try{
            loader = new FXMLLoader(getClass().getResource("/main/LoginPage.fxml"));
            root = loader.load();

            scene = new Scene(root);

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void refreshRate(){
        rate = connection.getExchangeRate();
    }

    /****** QR CODE SECTION ******/
    public void generateQrCode(ActionEvent event){
        qrCodeImage.setImage(generator.generateQrCode(qrData));
        chooseSaveLocationButton.setDisable(false);
        saveButton.setDisable(true);
    }

    public void chooseSaveLoction(ActionEvent event){
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose Save Location");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Directories","*"));
        saveLocation = fileChooser.showDialog((Stage) chooseSaveLocationButton.getScene().getWindow());
        if(saveLocation != null){
            saveButton.setDisable(false);
        }else{
            saveButton.setDisable(true);
        }
    }

    public void saveQrCode(ActionEvent event){
        generator.saveQrCode(saveLocation, qrName);
    }

    public void selectType(ActionEvent event){
        if(tableRadio.isSelected()){
            generateButton.setDisable(true);
            tableBox.setVisible(true);
            inventoryBox.setVisible(false);
        }else{
            qrData = "itemId:";
            generateButton.setDisable(true);
            tableBox.setVisible(false);
            inventoryBox.setVisible(true);
            getItemNameList(items);
            itemChoice.getItems().addAll(itemNameList);
        }
    }

    public void chooseSection(ActionEvent event){
        tableNumberChoice.getItems().clear();
        tableNumberChoice.getItems().addAll(connection.getTables(sectionChoice.getValue()));
        generateButton.setDisable(true);
        tableNumberChoice.setDisable(false);
    }
    public void chooseTableNumber(ActionEvent event){
        if(tableNumberChoice.getValue() != null){
            qrData = Properties.RESTAURANT_URL + tableNumberChoice.getValue();
            qrName = "Section " + sectionChoice.getValue() + ", Table Number " + tableNumberChoice.getValue() + ".jpg";
            generateButton.setDisable(false);
        }
    }

    public void chooseItem(ActionEvent event){
        quantityField.setText("");
        quantityField.setDisable(false);
        saveItemButton.setDisable(true);
        generateButton.setDisable(true);
    }
    public void quantityChanged(KeyEvent event){
        if(!quantityField.getText().matches("\\d*")){
            // Prevents the writing of all non digit numbers
            quantityField.setText(quantityField.getText().replaceAll("[^\\d]", ""));
            // Puts the caret(writing cursor) at the end of the word.
            quantityField.positionCaret(quantityField.getText().length());
        }
        if(quantityField.getText().equals("")){
            saveItemButton.setDisable(true);
        }else{
            saveItemButton.setDisable(false);
        }
    }
    public void saveItemData(ActionEvent event){
        // Saves entered data for the qr code and enables the generateQrCode button
        qrData = "itemName=" + itemChoice.getValue() + "/quantity=" + quantityField.getText();
        qrName = "Item Name " + itemChoice.getValue() + ", Quantity " + quantityField.getText() + ".jpg";
        generateButton.setDisable(false);
    }
    /****** QR CODE SECTION ******/

    /****** STATISTICS SECTION ******/
    public void openTipsDialog(ActionEvent event){
        StaffManagerController controller = new StaffManagerController();
        controller.setManagerController(this);
        controller.openTipsPane(connection.getTotalTips(tipIntervalCombo.getSelectedItem()), managers.size(), waiters.size(), cashiers.size(), kitchens.size());
    }
    /****** STATISTICS SECTION ******/

    /****** SETTINGS SECTION ******/

    public void initializeRateSection(){
        fillRateField();
        restoreRateButton.setOnAction(this::restoreField);
        saveRateButton.setOnAction(this::changeRate);
    }
    private void initializeDeliveryFeeSection(){
        fillDeliveryFeeField();
        restoreDeliveryFeeButton.setOnAction(this::restoreField);
        saveDeliveryFeeButton.setOnAction(this::changeRate);
    }
    private void fillRateField(){
        rateField.setText(String.valueOf(rate));
    }
    public void restoreField(ActionEvent event){
        if(event.getSource() == restoreRateButton){
            fillRateField();
        }else if(event.getSource() == restoreDeliveryFeeButton){
            fillDeliveryFeeField();
        }
    }
    public void changeRate(ActionEvent event){
        if(event.getSource() == saveRateButton){
            if(!rateField.getText().isEmpty()){
                connection.changeRate(Integer.parseInt(rateField.getText()));
                refreshRate();
                refreshAll();
            }
        }else if(event.getSource() == saveDeliveryFeeButton){
            if(!deliveryFeeField.getText().isEmpty()){
                connection.insertDeliveryFee(Double.parseDouble(deliveryFeeField.getText()));
                fillDeliveryFeeField();
            }
        }
    }
    public void fillDeliveryFeeField(){
        deliveryFeeField.setText(String.valueOf(connection.getDeliveryFee()));
    }

    public void initializeTreeViewSection(){

        refreshSectionViewer();

        sectionViewer.setShowRoot(true);
        sectionViewer.getRoot().setStartExpanded(true);
        sectionViewer.getSelectionModel().setAllowsMultipleSelection(false);
        sectionViewer.getSelectionModel().getSelectedItems().addListener(new ChangeListener<ObservableList<AbstractMFXTreeItem<Section>>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<AbstractMFXTreeItem<Section>>> observableValue, ObservableList<AbstractMFXTreeItem<Section>> abstractMFXTreeItems, ObservableList<AbstractMFXTreeItem<Section>> t1) {
                if(t1 != null && !t1.isEmpty()){
                    if(t1.get(0).getData().isRoot()){
                        disableAllSectionButtons(true);
                        sectionField.clear();
                    }else{
                        if(t1.get(0).getData().getName() == null){
                            disableSectionButtons(true);
                            disableTableButtons(false);
                            sectionField.clear();
                        }else{
                            sectionField.setText(t1.get(0).getData().getName());
                            disableTableButtons(true);
                            disableSectionButtons(false);
                        }
                    }
                }else{
                    disableAllSectionButtons(true);
                }
            }
        });

        addSection.setOnAction(this::addStaff);
        sectionField.setOnKeyTyped(this::checkTextField);
        editSection.setOnAction(this::openEditPane);
        deleteSection.setOnAction(this::remove);
        addTable.setOnAction(this::addStaff);
        editTable.setOnAction(this::openEditPane);
    }
    public void refreshSectionViewer(){
        sectionViewer.setRoot(null);
        ArrayList<Section> sections = connection.getSections();
        MFXTreeItem<Section> rootItem = new MFXTreeItem<>(new Section());
        for(Section section : sections){
            rootItem.getItems().add(section.getSectionNode());
        }
        sectionViewer.setRoot(rootItem);
//        sectionViewer.setRoot(null);
//        ArrayList<Section> sections = connection.getSections();
//        MFXTreeItem rootItem = new MFXTreeItem<>(new Section("", null));
//        for(Section section : sections){
//            MFXTreeItem<Section> sectionTreeItem = new MFXTreeItem<>(section);
//            for(Section table : section.getTables()){
//                sectionTreeItem.getItems().add(new MFXTreeItem<Section>(table));
//            }
//            rootItem.getItems().add(sectionTreeItem);
//        }
//        sectionViewer.setRoot(rootItem);
    }

    public void initializeDishCategorySection(){
        refreshDishViewer();

        dishCategoryViewer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observableValue, Category category, Category t1) {
                if(t1 != null){
                    disableDishCategoryButtons(false);
                    dishCategoryField.setText(dishCategoryViewer.getSelectionModel().getSelectedItem().getName());
                }else{
                    dishCategoryField.clear();
                    disableDishCategoryButtons(true);
                }
            }
        });

        dishCategoryField.setOnKeyTyped(this::checkTextField);
        addDishCategory.setOnAction(this::addStaff);
        deleteDishCategory.setOnAction(this::remove);
        editDishCategory.setOnAction(this::openEditPane);
    }
    public void refreshDishViewer(){
        dishCategory = connection.getCategory("category");
        dishCategoryViewer.getItems().clear();
        dishCategoryViewer.getItems().addAll(dishCategory);
    }

    public void initializeItemCategorySection(){
        itemCategoryViewer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category t1) {
                if(t1 != null){
                    disableItemCategoryButtons(false);
                    itemCategoryField.setText(itemCategoryViewer.getSelectionModel().getSelectedItem().getName());
                }else{
                    itemCategoryField.clear();
                    disableItemCategoryButtons(true);
                }
            }
        });

        refreshItemCategory();

        addItemCategory.setOnAction(this::addStaff);
        editItemCategory.setOnAction(this::openEditPane);
        deleteItemCategory.setOnAction(this::remove);
        itemCategoryField.setOnKeyTyped(this::checkTextField);
    }

    public void refreshItemCategory(){
        itemCategory = connection.getCategory("item_category");
        itemCategoryViewer.getItems().clear();
        itemCategoryViewer.getItems().addAll(itemCategory);
    }

    public void initializeUnitSection(){
        unitViewer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Unit>() {
            @Override
            public void changed(ObservableValue<? extends Unit> observable, Unit oldValue, Unit newValue) {
                if(newValue != null){
                    unitField.setText(newValue.getUnit());
                    disableUnitButtons(false);
                }else{
                    unitField.clear();
                    disableUnitButtons(true);
                }
            }
        });

        refreshUnitViewer();

        addUnit.setOnAction(this::addStaff);
        editUnit.setOnAction(this::openEditPane);
        deleteUnit.setOnAction(this::remove);
        unitField.setOnKeyTyped(this::checkTextField);
    }
    public void refreshUnitViewer(){
        units = connection.getUnits();

        unitViewer.getItems().clear();
        unitViewer.getItems().addAll(units);
    }

    public void checkTextField(KeyEvent event){
        if(event.getSource() == sectionField){
            if(sectionField.getText().isEmpty()){
                addSection.setDisable(true);
                editSection.setDisable(true);
            }else{
                addSection.setDisable(false);
                if(sectionViewer.getSelectionModel().getSelectedItem() != null){
                    if(sectionViewer.getSelectionModel().getSelectedItem().getData().getName() != null){
                        editSection.setDisable(false);
                    }
                }
            }
        }
        if(event.getSource() == dishCategoryField){
            if(dishCategoryField.getText().isEmpty()){
                addDishCategory.setDisable(true);
                editDishCategory.setDisable(true);
            }else{
                disableDishCategoryButtons(false);
                if(dishCategoryViewer.getSelectionModel().getSelectedItem() == null){
                    editDishCategory.setDisable(true);
                    deleteDishCategory.setDisable(true);
                }
            }
        }
        if(event.getSource() == itemCategoryField){
            if(itemCategoryField.getText().isEmpty()){
                addItemCategory.setDisable(true);
                editItemCategory.setDisable(true);
            }else{
                disableItemCategoryButtons(false);
                if(itemCategoryViewer.getSelectionModel().getSelectedItem() == null){
                    editItemCategory.setDisable(true);
                    deleteItemCategory.setDisable(true);
                }
            }
        }
        if(event.getSource() == unitField){
            if(unitField.getText().isEmpty()){
                addUnit.setDisable(true);
                editUnit.setDisable(true);
            }else {
                disableUnitButtons(false);
                if(unitViewer.getSelectionModel().getSelectedItem() == null){
                    editUnit.setDisable(true);
                    deleteUnit.setDisable(true);
                }
            }
        }
    }

    /****** SETTINGS SECTION ******/

    /***** OWNER PANE INITIALIZATION ******/
    public void initializeWaiterPane(){

        waiterSearchChoice.getItems().addAll(staffChoices);
        waiterSearchChoice.setValue(staffChoices[0]);

        initializeWaiterTable();

        disableWaiterButtons();

        // Add a listener to the selectedItemProperty of the TableView
        waiterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A row is selected, enable the Button
                removeWaiter.setDisable(false);
                editWaiter.setDisable(false);
                idWaiter.setDisable(false);
            } else {
                // No row is selected, disable the Button
                disableWaiterButtons();
            }
        });

        initStaffButtons(addWaiter, searchWaiters, removeWaiter, editWaiter, idWaiter);
        getWaitersTotalSalaries();

    }

    public void initializeWaiterTable(){
        initStaffColumns(waiterNameColumn, waiterAgeColumn, waiterPhoneColumn, waiterSalaryColumn,
                waiterShiftColumn, waiterDateColumn, waiterSectionColumn, waiterStatusColumn);

        refreshStaff(waiterTable, waiters, "waiter");
    }

    public void initializeCashierPane(){

        cashierSearchChoice.getItems().addAll(staffChoices);
        cashierSearchChoice.setValue(staffChoices[0]);

        initializeCashierTable();

        disableCashierButtons();

        cashierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                removeCashier.setDisable(false);
                editCashier.setDisable(false);
                idCashier.setDisable(false);
            }else{
                disableCashierButtons();
            }
        });

        initStaffButtons(addCashier, searchCashiers, removeCashier, editCashier, idCashier);

        getCashierTotalSalaries();

    }

    public void initializeCashierTable(){
         initStaffColumns(cashierNameColumn, cashierAgeColumn, cashierPhoneColumn, cashierSalaryColumn,
                 cashierShiftColumn, cashierDateColumn, null, cashierStatusColumn);

        refreshStaff(cashierTable, cashiers, "cashier");
    }

    public void initializeKitchenPane(){
        kitchenSearchChoice.getItems().addAll(staffChoices);
        kitchenSearchChoice.setValue(staffChoices[0]);

        initializeKitchenTable();

        disableKitchenButtons();

        kitchenTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                removeKitchen.setDisable(false);
                editKitchen.setDisable(false);
                idKitchen.setDisable(false);
            }else{
                disableKitchenButtons();
            }
        });

        initStaffButtons(addKitchen, searchKitchen, removeKitchen, editKitchen, idKitchen);

        getKitchenTotalSalaries();
    }
    public void initializeKitchenTable(){
        initStaffColumns(kitchenNameColumn, kitchenAgeColumn, kitchenPhoneColumn, kitchenSalaryColumn,
                kitchenShiftColumn, kitchenDateColumn, null, kitchenStatusColumn);

        refreshStaff(kitchenTable, kitchens, "kitchen");
    }

    public void initializeManagerPane(){
        managerSearchChoice.getItems().addAll(staffChoices);
        managerSearchChoice.setValue(staffChoices[0]);

        initializeManagerTable();

        disableManagerButtons();

        managerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A row is selected, enable the Button
                removeManager.setDisable(false);
                editManager.setDisable(false);
                idManager.setDisable(false);
            } else {
                // No row is selected, disable the Button
                disableManagerButtons();
            }
        });

        initStaffButtons(addManager, searchManagers, removeManager, editManager, idManager);

        getManagerTotalSalaries();

    }
    private void initializeManagerTable(){
        initStaffColumns(managerNameColumn, managerAgeColumn, managerPhoneColumn, managerSalaryColumn,
                managerDateColumn, managerShiftColumn, managerSectionColumn, managerStatusColumn);

        refreshStaff(managerTable, managers, "manager");
    }

    /**
     * Assigns each staff's button to a specific action
     * @param add
     * @param search
     * @param remove
     * @param edit
     * @param id
     */
    private void initStaffButtons(Button add, Button search, Button remove, Button edit, Button id){
        add.setOnAction(this::addStaff);
        search.setOnAction(this::search);
        remove.setOnAction(this::remove);
        edit.setOnAction(this::openEditPane);
        id.setOnAction(this::showId);
    }

    /**
     * Initializes each column in the staff's tables
     * @param nameColumn
     * @param ageColumn
     * @param phoneColumn
     * @param salaryColumn
     * @param shiftColumn
     * @param sectionColumn
     * @param statusColumn
     */
    private void initStaffColumns(TableColumn nameColumn, TableColumn ageColumn, TableColumn phoneColumn,
                                  TableColumn salaryColumn, TableColumn shiftColumn, TableColumn dateColumn,TableColumn sectionColumn,
                                  TableColumn statusColumn){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Staff, Integer>("age"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("phoneNumber"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<Staff, Salary>("salary"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("dateJoined"));
        shiftColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("shift"));

        if(sectionColumn != null){
            sectionColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("section"));
        }

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusButton"));

        statusColumn.setCellFactory(column -> {
            return new TableCell<Staff, ToggleSwitch>(){
                @Override
                public void updateItem(ToggleSwitch statusButton, boolean empty){
                    if(empty){
                        setText(null);
                        setGraphic(null);
                        setStyle(null);
                    }else{
                        setGraphic(statusButton);
                        setStyle("-fx-border-width: 0 0 2 0;" +
                                "-fx-border-style: solid;" +
                                "-fx-border-color: #fefefe;" +
                                "-fx-alignment: center;");
                    }
                }
            };
        });
    }

    public void initializeMenuPane(){
        menuSearchChoice.getItems().addAll(menuChoices);
        menuSearchChoice.setValue(menuChoices[0]);

        initializeMenuTable();

        disableMenuButtons();

        menuTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if(newSelection != null){
                removeMenu.setDisable(false);
                editMenu.setDisable(false);
            }else{
                disableMenuButtons();
            }
        });

        editMenu.setOnAction(this::openEditPane);
        removeMenu.setOnAction(this::remove);
        searchMenu.setOnAction(this::dishSearch);

        countDishes();

    }
    public void initializeMenuTable(){
        menuNameColumn.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        menuCategoryColumn.setCellValueFactory(new PropertyValueFactory<Dish, String>("category"));
        menuCostColumn.setCellValueFactory(new PropertyValueFactory<Dish, Double>("cost"));
        menuPriceColumn.setCellValueFactory(new PropertyValueFactory<Dish, Double>("price"));
        menuDescriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        menuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusButton"));

        menuDescriptionColumn.setCellFactory(column -> {
            return new TableCell<Dish, ChoiceBox<Ingredient>>(){
                @Override
                public void updateItem(ChoiceBox<Ingredient> ingredients, boolean empty){
                    super.updateItem(ingredients, empty);
                    if (empty || ingredients == null) {
                        setText(null);
                        setGraphic(null);
                    }else if(ingredients.getItems().isEmpty()){
                        setText("No Ingredients");
                    }else {
                        ingredients.setValue(ingredients.getItems().get(0));
                        setGraphic(ingredients);
                    }
                }
            };
        });
        menuStatusColumn.setCellFactory(column -> {
            return new TableCell<Dish, ToggleSwitch>(){
                @Override
                public void updateItem(ToggleSwitch statusButton, boolean empty){
                    super.updateItem(statusButton, empty);
                    if(empty){
                        setText(null);
                        setGraphic(null);
                    }else{
                        setGraphic(statusButton);
                    }
                }
            };
        });

        refreshDish();
    }

    public void initializeInventoryPane(){
        inventorySearchChoice.getItems().addAll(inventoryChoices);
        inventorySearchChoice.setValue(inventoryChoices[0]);

        initializeInventoryTable();

        disableInventoryButtons();

        inventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if(newSelection != null){
                removeItem.setDisable(false);
                editItem.setDisable(false);
            }else{
                disableInventoryButtons();
            }
        });

        editItem.setOnAction(this::openEditPane);
        removeItem.setOnAction(this::remove);
        searchInventory.setOnAction(this::itemSearch);
        exportInventoryButton.setOnAction(this::exportToExcel);

        countItems();
    }
    public void initializeInventoryTable(){
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, String>("name"));
        itemCategoryColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, String>("category"));
        itemUnitColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, String>("unit"));
        itemCostColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, Double>("price"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<InventoryItem, Integer>("quantity"));

        refreshInventory();
    }

    public void exportToExcel(ActionEvent event){
        ExcelConverter converter = new ExcelConverter(connection.getInventoryItems());
        converter.exportToExcel(((Node)event.getSource()).getScene().getWindow());
    }

    public void initializeQrCodePane(){
        generator = new QrGenerator();

        sectionChoice.getItems().addAll(connection.getList("section", "section_number"));

        sectionChoice.setOnAction(this::chooseSection);
        tableNumberChoice.setOnAction(this::chooseTableNumber);

        itemChoice.setOnAction(this::chooseItem);
        quantityField.setOnKeyTyped(this::quantityChanged);
        saveItemButton.setOnAction(this::saveItemData);
        chooseSaveLocationButton.setOnAction(this::chooseSaveLoction);
        saveButton.setOnAction(this::saveQrCode);

        generateButton.setOnAction(this::generateQrCode);
    }

    public void initializeStatPane(){
        initializeCharts();
        initializeTips();

        showTipsDistribution.setOnAction(this::openTipsDialog);
    }
    public void initializeCharts(){

        refreshSalaryChart();
        salaryChartUSD.setLegendVisible(false);
        salaryChartLBP.setLegendVisible(false);

        revenueChart.getData().addAll(connection.getRevenue());
        revenueChart.setLegendVisible(true);
        for(int i = 0; i < revenueChart.getData().size(); i++){
            String currency;
            int mult = 1;
            if(i == 0){
                currency = "$ ";
                mult = 1;
            }else{
                currency = "LBP ";
                mult = 1_000_0;
            }
            for(XYChart.Data<String, Double> d : revenueChart.getData().get(i).getData()){
                Tooltip tp = new Tooltip(currency + formatter.format(d.getYValue() * mult));
                tp.setFont(new Font(14));
                tp.setShowDelay(Duration.ZERO);
                Tooltip.install(d.getNode(), tp);
            }
        }

        topDishChart.getData().add(connection.getTopDishes());
        topDishChart.setLegendVisible(false);
        for(XYChart.Data<String, Integer> d : topDishChart.getData().get(0).getData()){
            Tooltip tp = new Tooltip(d.getXValue() + ": " + formatter.format(d.getYValue()));
            tp.setFont(new Font(14));
            tp.setShowDelay(Duration.ZERO);
            Tooltip.install(d.getNode(), tp);
        }
    }
    public void refreshSalaryChart(){
        Salary totalWaiters = getWaitersTotalSalaries();
        Salary totalCashiers = getCashierTotalSalaries();
        Salary totalKitchen = getKitchenTotalSalaries();
        Salary totalManagers = getManagerTotalSalaries();
        Salary totalSalaries = new Salary();
        totalSalaries.add(totalWaiters);
        totalSalaries.add(totalCashiers);
        totalSalaries.add(totalKitchen);
        totalSalaries.add(totalManagers);

        ObservableList<PieChart.Data> salaryDataUSD = FXCollections.observableArrayList(
                new PieChart.Data("Supervisors\n$" + formatter.format(totalManagers.getUsd()), totalManagers.getUsd()),
                new PieChart.Data("Waiters\n$" + formatter.format(totalWaiters.getUsd()), totalWaiters.getUsd()),
                new PieChart.Data("Cashiers\n$" + formatter.format(totalCashiers.getUsd()), totalCashiers.getUsd()),
                new PieChart.Data("Kitchen Staff\n$" + formatter.format(totalKitchen.getUsd()), totalKitchen.getUsd())
        );
        salaryChartUSD.getData().clear();
        salaryChartUSD.getData().addAll(salaryDataUSD);
        salaryChartUSD.setTitle("Total Salaries: $ " + formatter.format(totalSalaries.getUsd()));

        ObservableList<PieChart.Data> salaryDataLBP = FXCollections.observableArrayList(
                new PieChart.Data("Supervisors\nLBP" + formatter.format(totalManagers.getLbp()), totalManagers.getLbp()),
                new PieChart.Data("Waiters\nLBP" + formatter.format(totalWaiters.getLbp()), totalWaiters.getLbp()),
                new PieChart.Data("Cashiers\nLBP" + formatter.format(totalCashiers.getLbp()), totalCashiers.getLbp()),
                new PieChart.Data("Kitchen Staff\nLBP" + formatter.format(totalKitchen.getLbp()), totalKitchen.getLbp())
        );
        salaryChartLBP.getData().clear();
        salaryChartLBP.getData().addAll(salaryDataLBP);
        salaryChartLBP.setTitle("Total Salaries: LBP " + formatter.format(totalSalaries.getLbp()));
    }

    public void initializeTips(){
        totalTips = 2457.69;
        tipIntervalCombo.setItems(tipIntervalChoices);
        tipIntervalCombo.getSelectionModel().selectFirst();
        tipIntervalCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1 != null){
                    totalTipsField.setText(connection.getTotalTips(tipIntervalCombo.getSelectedItem()).toString());
                }
            }
        });
        totalTipsField.setText(connection.getTotalTips(tipIntervalCombo.getSelectedItem()).toString());
    }

    public void initializeSideBar(){
        connection = new DBConnection();
        // Gets exchange rate from the database
        refreshRate();

        formatter = new DecimalFormat(Properties.PATTERN);

        managerName.setText(ownerName);

        initSideButtons();

        waiterButton.setOnAction(this::switchPanes);
        cashierButton.setOnAction(this::switchPanes);
        kitchenButton.setOnAction(this::switchPanes);
        managerButton.setOnAction(this::switchPanes);
        ordersButton.setOnAction(this::switchPanes);
        menuButton.setOnAction(this::switchPanes);
        inventoryButton.setOnAction(this::switchPanes);
        qrCodeButton.setOnAction(this::switchPanes);
        statButton.setOnAction(this::switchPanes);
        settingsButton.setOnAction(this::switchPanes);
        logoutButton.setOnAction(this::logout);
        customerButton.setOnAction(this::switchPanes);
        reportButton.setOnAction(this::launchAI);
    }

    private void initSideButtons(){
        for(int i = 0; i < panes.size(); i++){
            bindSideButton(panes.get(i), buttons.get(i));
        }
    }

    public void initializeSettingsPane(){
        initializeRateSection();
        initializeDeliveryFeeSection();
        initializeTreeViewSection();
        initializeDishCategorySection();
        initializeItemCategorySection();
        initializeUnitSection();
    }

    public void initializeOrderPane(){
        initializeOrderTable();

        getTodayOrders();

        disableOrderButtons(true);

        orderSearchChoice.getItems().addAll(orderChoices);
        orderSearchChoice.setValue(orderChoices[0]);

        searchOrder.setOnAction(this::orderSearch);
        showOrderDetail.setOnAction(this::showOrderDetails);
    }
    public void initializeOrderTable(){
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDateString"));
        orderDiscountColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDiscount"));
        orderTotalColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderTotal"));
        orderTotalPaidColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderPaid"));
        orderSourceColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderSource"));

        orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue<? extends Order> observableValue, Order order, Order t1) {
                if(t1 != null){
                    disableOrderButtons(false);
                }else{
                    disableOrderButtons(true);
                }
            }
        });

        refreshOrderTable();
    }
    public void refreshOrderTable(){
        orders.clear();
        orders.addAll(connection.getOrders());
        orderTable.getSelectionModel().clearSelection();
        orderTable.setItems(orders);
    }

    public void showOrderDetails(ActionEvent event){
        OrderItemsController controller = new OrderItemsController();
        controller.setManagerController(this);
        controller.showDetailsPane(orderTable.getSelectionModel().getSelectedItem());
    }

    private void initCustomerPane(){
        initCustomerTable();

//        CustomerDetailsController controller = new CustomerDetailsController();
//        controller.openCustomerDetails(customerButton.getScene().getWindow(), null);

        customerSearchChoice.getItems().addAll(customerChoices);
        customerSearchChoice.setValue(customerChoices[0]);

        showCustomerDetails.setDisable(true);

        showCustomerDetails.setOnAction(this::showCustomerDetails);
        searchCustomer.setOnAction(this::searchCustomers);
    }

    private void initCustomerTable(){
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
        customerFavoriteColumn.setCellValueFactory(new PropertyValueFactory<>("favoriteDishesCombo"));

        customerFavoriteColumn.setCellFactory(column -> {
            return new TableCell<Customer, ChoiceBox<Dish>>() {
                @Override
                public void updateItem(ChoiceBox<Dish> favoriteDishes, boolean empty){
                    super.updateItem(favoriteDishes, empty);
                    if(empty){
                        setText(null);
                        setGraphic(null);
                    }else if(favoriteDishes.getItems().isEmpty()){
                        setText("No Favorite Dishes");
                    }else{
                        setGraphic(favoriteDishes);
                    }
                }
            };
        });

        customerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observableValue, Customer customer, Customer t1) {
                if(t1 != null){
                    showCustomerDetails.setDisable(false);
                }else{
                    showCustomerDetails.setDisable(true);
                }
            }
        });

        refreshCustomerTable();
    }

    public void refreshCustomerTable(){
        customerTable.getSelectionModel().clearSelection();
        customers.clear();
        customers.addAll(connection.getCustomers());
        customerTable.setItems(customers);
        countCustomers();
    }

    public void showCustomerDetails(ActionEvent event){
        CustomerDetailsController controller = new CustomerDetailsController();
        controller.openCustomerDetails(((Node) event.getSource()).getScene().getWindow(),
                customerTable.getSelectionModel().getSelectedItem());
    }

    /***** OWNER PANE INITIALIZATION ******/

    /***** TOTALS CALCULATION *****/
    public Salary getWaitersTotalSalaries() {
        Salary total = new Salary();
        for (Staff waiter : waiters) {
            total.add(waiter.getSalary());
        }
        totalWaiter.setText(total.getTotalString());
        return total;
    }
    public Salary getCashierTotalSalaries() {
        Salary total = new Salary();
        for (Staff cashier : cashiers) {
            total.add(cashier.getSalary());
        }
        totalCashier.setText(total.getTotalString());
        return total;
    }
    public Salary getKitchenTotalSalaries() {
        Salary total = new Salary();
        for (Staff kitchen : kitchens) {
            total.add(kitchen.getSalary());
        }
        totalKitchen.setText(total.getTotalString());
        return total;
    }
    public Salary getManagerTotalSalaries() {
        Salary total = new Salary();
        for (Staff manager : managers) {
            total.add(manager.getSalary());
        }
        totalManager.setText(total.getTotalString());
        return total;
    }

    public void countDishes(){
        totalMenu.setText(dishes.size() + "");
    }
    public void countItems() {
        totalItems.setText(items.size() + "");
    }
    public void getTodayOrders(){
        int numberOfOrders = 0;
        double totalLBP = 0;
        double totalUSD = 0;
        Date date = new Date();
        LocalDateTime today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        for(Order order : orders){
            if(order.getOrderDate().getYear() == today.getYear() &&
                    order.getOrderDate().getMonth() == today.getMonth() &&
                    order.getOrderDate().getDayOfMonth() == today.getDayOfMonth()){
                numberOfOrders++;
                totalUSD += order.getOrderTotalUSD();
                totalLBP += order.getOrderTotalLBP();
            }
        }
        totalOrders.setText(numberOfOrders + " / $ " + formatter.format(totalUSD) + " / LBP " + formatter.format(totalLBP));
    }
    public void countCustomers(){
        totalCustomer.setText(String.valueOf(customers.size()));
    }
    /***** TOTALS CALCULATION *****/

    /***** DISABLE BUTTONS FUNCTION *****/

    /***** DISABLE BUTTONS FUNCTION *****/
    public void disableWaiterButtons() {
        removeWaiter.setDisable(true);
        editWaiter.setDisable(true);
        idWaiter.setDisable(true);
    }
    public void disableCashierButtons() {
        removeCashier.setDisable(true);
        editCashier.setDisable(true);
        idCashier.setDisable(true);
    }
    public void disableKitchenButtons() {
        removeKitchen.setDisable(true);
        editKitchen.setDisable(true);
        idKitchen.setDisable(true);
    }
    public void disableManagerButtons() {
        removeManager.setDisable(true);
        editManager.setDisable(true);
        idManager.setDisable(true);
    }
    public void disableOrderButtons(boolean disabled){
        showOrderDetail.setDisable(disabled);
    }
    public void disableMenuButtons() {
        removeMenu.setDisable(true);
        editMenu.setDisable(true);
    }
    public void disableInventoryButtons() {
        removeItem.setDisable(true);
        editItem.setDisable(true);
    }
    public void disableSectionButtons(boolean disabled){
        addSection.setDisable(disabled);
        editSection.setDisable(disabled);
        addTable.setDisable(disabled);
        deleteSection.setDisable(disabled);
    }
    public void disableTableButtons(boolean disabled){
        editTable.setDisable(disabled);
        deleteSection.setDisable(disabled);
    }
    public void disableAllSectionButtons(boolean disabled){
        disableSectionButtons(disabled);
        disableTableButtons(disabled);
    }
    public void disableDishCategoryButtons(boolean disabled){
        addDishCategory.setDisable(disabled);
        editDishCategory.setDisable(disabled);
        deleteDishCategory.setDisable(disabled);
    }
    public void disableItemCategoryButtons(boolean disabled){
        addItemCategory.setDisable(disabled);
        editItemCategory.setDisable(disabled);
        deleteItemCategory.setDisable(disabled);
    }
    public void disableUnitButtons(boolean disabled){
        addUnit.setDisable(disabled);
        editUnit.setDisable(disabled);
        deleteUnit.setDisable(disabled);
    }
    /***** CHANGE BUTTON STATE *****/

    /**
     * Binds the border pane to the respective button. the styling of the button will change depending
     * on the visibility of the borderPane
     * @param pane The BorderPane to be bound to
     * @param button The button to bind
     */
    private void bindSideButton(BorderPane pane, Button button){
        String normal = "sideButton";
        String selected = "selectedSideButton";
        if(pane == settingsPane){
            normal = "settingsButton";
            selected = "selectedSettingsButton";
        }
        String finalSelected = selected;
        String finalNormal = normal;
        pane.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(aBoolean){
                    button.getStyleClass().remove(finalSelected);
                    button.getStyleClass().add(finalNormal);
                }else{
                    button.getStyleClass().remove(finalNormal);
                    button.getStyleClass().add(finalSelected);
                }
            }
        });
    }
    /***** CHANGE BUTTON STATE *****/

    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }
    public void getItemNameList(ObservableList<InventoryItem> list){
        // Converts available Items in the inventory to an array for the qr code generator.
        itemNameList = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            itemNameList[i] = list.get(i).getName();
        }
    }

    public MFXTreeView<Section> getSectionViewer(){
        return sectionViewer;
    }

}
