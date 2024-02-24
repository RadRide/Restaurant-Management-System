package login;

import cashier.CashierPageController;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.javafx.FontIcon;


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
            checkLogin((Stage) ((Node)event.getSource()).getScene().getWindow());
        }else{
            idField.appendText(((Button)event.getSource()).getText());
        }
    }

    public void checkField(KeyEvent event){
        if(!idField.getText().isEmpty()){
            if(!idField.getText().matches("\\d*")){
                idField.setText(idField.getText().replaceAll("[^\\d]", ""));
                idField.positionCaret(idField.getText().length());
            }
            if(event.getCode() == KeyCode.ENTER){
                checkLogin((Stage) ((Node)event.getSource()).getScene().getWindow());
            }
        }
//        if()
    }

    private void checkLogin(Window window){
        String name = connection.login(idField.getText());
        if(name != null){
            login(name, window);
        }else{
            warningMessage.setVisible(true);
        }
    }

    public void login(String name, Window window) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cashier/CashierPage.fxml"));
            Parent root = loader.load();

            CashierPageController controller = loader.getController();
            controller.setCashierName(name);

            Scene scene = new Scene(root);

            Stage stage = (Stage) window;
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}