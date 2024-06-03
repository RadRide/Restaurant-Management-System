package manager;

import dbConnection.DBConnection;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import properties.Properties;
import staff.Salary;
import staff.Staff;
import staff.Tip;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class StaffManagerController{

    @FXML
    private Label warningLabel;
    @FXML
    private MFXDatePicker dateJoined, dateOfBirth;
    @FXML
    private MFXComboBox<String> shiftPicker, sectionPicker;
    @FXML
    private MFXTextField nameField, phoneField, usdField, lbpField, nameArea, idArea, managerTips, waiterTips,
            cashierTips, kitchenTips;
    private int rate = 0;
    private DecimalFormat formatter;
    private Staff newStaff;
    private ManagerController managerController;
    private FXMLLoader loader;
    private DialogPane staffPane, idPane, tipsPane;
    private Dialog<ButtonType> dialog;
    private Optional<ButtonType> clickedButton;
    private DBConnection connection;
    private final String pattern = "yyyy-MM-dd";
    
    public void openPane(int type, String tableName){
        // Open add dialog pane
        initializeDialog("Add " + capitalize(tableName));
        initializeFields();
        disableSection(tableName);
        // Prevents the dialog from closing if the fields are not filled
        dialog.setOnCloseRequest(even ->{
            if(areFieldsEmpty()){
                even.consume();
                warningLabel.setVisible(true);
            }
        });

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            connection.insertStaff(tableName, new Staff(capitalize(nameField.getText()), phoneField.getText(),
                        dateJoined.getValue().format(DateTimeFormatter.ofPattern(pattern)), shiftPicker.getValue(),
                        sectionPicker.getValue(), new Salary(Double.parseDouble(usdField.getText()),
                        Double.parseDouble(lbpField.getText())),generateId(type, tableName), Properties.STAFF_DISABLED, dateOfBirth.getValue()));
            managerController.newStaff(type);
        }
    }

    public void openEditPane(Staff staff){
        // Open edit dialog pane
        String tableName = getType(staff.getId());
        initializeDialog("Edit " + capitalize(tableName));
        initializeFields();

        disableSection(tableName);

        nameField.setText(staff.getName());
        dateOfBirth.setValue(staff.getDob());
        usdField.setText(String.valueOf(staff.getSalary().getUsd()));
        lbpField.setText(String.valueOf(staff.getSalary().getLbp()));
        phoneField.setText(staff.getPhoneNumber());
        dateJoined.setValue(LocalDate.parse(staff.getDateJoined(), DateTimeFormatter.ofPattern(pattern)));
        shiftPicker.getSelectionModel().selectItem(staff.getShift());
        if(tableName == "waiter" || tableName == "manager"){
            sectionPicker.getSelectionModel().selectItem(staff.getSection());
        }

        dialog.setOnCloseRequest(even ->{
            if(areFieldsEmpty()){
                even.consume();
                warningLabel.setVisible(true);
            }
        });

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            staff.setName(nameField.getText());
            staff.setDob(dateOfBirth.getValue());
            staff.setSalary(new Salary(Double.parseDouble(usdField.getText()), Double.parseDouble(lbpField.getText())));
            staff.setPhoneNumber(phoneField.getText());
            staff.setShift(shiftPicker.getValue());
            staff.setSection(sectionPicker.getValue());
            staff.setDateJoined(dateJoined.getValue().format(DateTimeFormatter.ofPattern(pattern)));
            connection.editStaff(tableName, staff);
        }
    }

    public void openIdPane(Staff staff){
        String tableName = getType(staff.getId());
        initializeIdDialog();
        initializeIdFields();
        nameArea.setText(staff.getName());
        idArea.setText(String.valueOf(staff.getId()));
        dialog.showAndWait();
    }

    public void openTipsPane(Tip totalTips, int totalManager, int totalWaiter, int totalCashier, int totalKitchen){
        initializeTipsDialog();
        initializeTipsFields();

        Tip[] parts = distributeTips(totalTips, totalManager, totalWaiter, totalCashier, totalKitchen);
        managerTips.setText(parts[0].toString());
        waiterTips.setText(parts[1].toString());
        cashierTips.setText(parts[2].toString());
        kitchenTips.setText(parts[3].toString());

        dialog.showAndWait();
    }

    public void initializeDialog(String title) {

        try{
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/manager/ManageStaff.fxml"));
            staffPane = loader.load();
            connection = new DBConnection();
            dialog = new Dialog<>();
            dialog.setDialogPane(staffPane);
            dialog.setTitle(title);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error initializing DialogPane");
        }
    }

    public void initializeIdDialog(){
        try{
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/manager/ShowIdPane.fxml"));
            idPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(idPane);
            dialog.setTitle("Show ID");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initializeTipsDialog(){
        try{
            connection = new DBConnection();
            rate = connection.getExchangeRate();
            formatter = new DecimalFormat("#,###.00");
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/manager/TipsPane.fxml"));
            tipsPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(tipsPane);
            dialog.setTitle("Tips Distribution");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initializeFields(){
        // Links the Fields to the DialogPane
        nameField = (MFXTextField) staffPane.lookup("#nameField");
        dateOfBirth = (MFXDatePicker) staffPane.lookup("#dateOfBirth");
        usdField = (MFXTextField) staffPane.lookup("#usdField");
        lbpField = (MFXTextField) staffPane.lookup("#lbpField");
        phoneField = (MFXTextField) staffPane.lookup("#phoneField");
        dateJoined = (MFXDatePicker) staffPane.lookup("#dateJoined");
        shiftPicker = (MFXComboBox<String>) staffPane.lookup("#shiftPicker");
        sectionPicker = (MFXComboBox<String>) staffPane.lookup("#sectionPicker");
        warningLabel = (Label) staffPane.lookup("#warningLabel");

        shiftPicker.getItems().addAll(connection.getList("shift", "shift"));
        sectionPicker.getItems().addAll(connection.getList("section", "section_number"));

        usdField.setOnKeyTyped(this::checkSalary);
    }

    public void initializeIdFields(){
        nameArea = (MFXTextField) idPane.lookup("#nameArea");
        idArea = (MFXTextField) idPane.lookup("#idArea");
    }

    public void initializeTipsFields(){
        managerTips = (MFXTextField) tipsPane.lookup("#managerTips");
        waiterTips = (MFXTextField) tipsPane.lookup("#waiterTips");
        cashierTips = (MFXTextField) tipsPane.lookup("#cashierTips");
        kitchenTips = (MFXTextField) tipsPane.lookup("#kitchenTips");
    }

    public void disableSection(String type){
        // Removes the Section picker and label from the pane for staff other than waiters and managers.
        if(!type.equals("waiter") && !type.equals("manager")){
            sectionPicker.setVisible(false);
        }
    }

    public void checkSalary(KeyEvent event){
        if(!usdField.getText().matches("\\d*")){
            // Prevents the writing of all non digit numbers
            usdField.setText(usdField.getText().replaceAll("[^\\d]", ""));
            // Puts the caret(writing cursor) at the end of the word.
            usdField.positionCaret(usdField.getText().length());
        }
    }

    public boolean areFieldsEmpty(){
        return nameField.getText().trim().equals("") || phoneField.getText().trim().equals("") || usdField.getText().trim().equals("") ||
                dateJoined.equals(null);
    }

    public String getType(int id){
        // Determines the Staff Type by looking at his id.
        if(id < 2000){return "manager";}
        else if(id >= 2000 && id < 3000){return "waiter";}
        else if(id >= 3000 && id < 4000){return "cashier";}
        else if(id >= 4000){return "kitchen";}
        return null;
    }

    public String capitalize(String string){
        // Capitalizes all the words in a string
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

    public int generateId(int type, String tableName){
        // Generate an ID and checks if it is not already taken in the database.
        Random random = new Random();
        int min = type * 1000;
        int id = random.nextInt(min, min + 1000);
        if(connection.checkId(id, tableName)){
            return generateId(type, tableName);
        }
        return id;
    }

    public Tip[] distributeTips(Tip total, int totalManager, int totalWaiter, int totalCashier, int totalKitchen){
        // Receives total tips and distribute it to the staff according to the parts allocated to each staff type.
        int forManagers = 4 * totalManager; // Receives 4 parts
        int forWaiters = 2 * totalWaiter; // Receives 2 parts
        int forCashiers = totalCashier; // Receives 1 part
        int forKitchen = 3 * totalKitchen; // Receives 3 parts
        int totalParts = forKitchen + forCashiers + forManagers + forWaiters;
        double partUSD = total.getUsd() / totalParts;
        double partLBP = total.getLbp() / totalParts;
        Tip[] tipsDistributed = {
                new Tip(partUSD * 4, partLBP * 4),
                new Tip(partUSD * 2, partLBP * 2),
                new Tip(partUSD, partLBP),
                new Tip(partUSD * 3, partLBP * 3)
        };
        return tipsDistributed;
    }

    public void setManagerController(ManagerController managerController) {
        this.managerController = managerController;
    }
}
