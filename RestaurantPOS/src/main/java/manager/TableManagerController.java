package manager;

import dbConnection.DBConnection;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTreeItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import section.Section;

import java.io.IOException;
import java.util.Optional;

public class TableManagerController {

    @FXML
    private MFXTextField numberField, seatField;
    @FXML
    private Label messageLabel;

    private ManagerController controller;
    private FXMLLoader loader;
    private DialogPane pane;
    private Dialog<ButtonType> dialog;
    private Optional<ButtonType> clickedButton;
    private DBConnection connection;


    public void setManagerController(ManagerController controller){
        this.controller = controller;
    }

    public void openAddSection(Section parent){
        initializePane("/manager/ManageTable.fxml", "Add Table");
        initializeTableFields();

        dialog.setOnCloseRequest(event -> {
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }
        });

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            connection.insertTable(new Section(Integer.parseInt(numberField.getText()), Integer.parseInt(seatField.getText())), parent.getName());
            controller.getSectionViewer().getSelectionModel().getSelectedItem().getItems().add(new MFXTreeItem<>(
                    new Section(Integer.parseInt(numberField.getText()), Integer.parseInt(seatField.getText()))));
            controller.getSectionViewer().getSelectionModel().getSelectedItem().getData().addTable(new Section(
                    Integer.parseInt(numberField.getText()), Integer.parseInt(seatField.getText())));
        }
    }

    public void openEditTablePane(Section table, String sectionNumber){
        initializePane("/manager/ManageTable.fxml", "Edit Table");
        initializeTableFields();

        dialog.setOnCloseRequest(event -> {
            if(areFieldsEmpty()){
                event.consume();
                messageLabel.setVisible(true);
            }
        });

        numberField.setText(String.valueOf(table.getTableNumber()));
        seatField.setText(String.valueOf(table.getNumberOfSeats()));

        clickedButton = dialog.showAndWait();
        if(clickedButton.isPresent()){
            connection.editTable(new Section(Integer.parseInt(numberField.getText()), Integer.parseInt(seatField.getText())),
                    table.getTableNumber(), sectionNumber);
        }
    }

    public void initializePane(String url, String title){
        try{
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));
            pane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle(title);
            connection = new DBConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initializeTableFields(){
        numberField = (MFXTextField) pane.lookup("#numberField");
        seatField = (MFXTextField) pane.lookup("#seatField");
        messageLabel = (Label) pane.lookup("#messageLabel");
    }

    public boolean areFieldsEmpty(){
        return numberField.getText().isEmpty() || seatField.getText().isEmpty();
    }

}
