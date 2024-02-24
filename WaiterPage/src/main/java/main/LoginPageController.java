package main;

import connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.javafx.FontIcon;
import waiter.WaiterPageController;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7,
            button8, button9, button0, eraseButton,loginButton;
    @FXML
    private FontIcon eraseIcon;
    @FXML
    private TextField idField;
    @FXML
    private Label warningMessage;
    private DBConnection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = new DBConnection();
    }

    public void dialPressed(ActionEvent event){
        if(event.getSource() == eraseButton){
            if(!idField.getText().isEmpty()){
                idField.setText(idField.getText(0, idField.getText().length() - 1));
            }
        }else if(event.getSource() == loginButton){
            String name = connection.login(idField.getText());
            if(name != null){
                if(idField.getText().charAt(0) == '1'){
                    login(name, (Stage) ((Node)event.getSource()).getScene().getWindow(), true);
                }else{
                    login(name, (Stage) ((Node)event.getSource()).getScene().getWindow(), false);
                }
            }else{
                warningMessage.setVisible(true);
            }
        }else{
            idField.appendText(((Button)event.getSource()).getText());
        }
    }

    public void login(String name, Window window, boolean isManager) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waiter/WaiterPage.fxml"));
            Parent root = loader.load();

            WaiterPageController controller = loader.getController();
            controller.setWaiterName(name);
            controller.setManager(isManager);

            Scene scene = new Scene(root);

            Stage stage = (Stage) window;
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}