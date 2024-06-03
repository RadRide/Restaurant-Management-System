package login;

import dbConnection.DBConnection;
import encryption.Hasher;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import manager.ManagerController;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.*;

public class LoginPageController implements Initializable {

    @FXML
    private MFXTextField usernameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private MFXCheckbox rememberMeCheckBox;
    @FXML
    private Label messageLabel;
    @FXML
    private MFXButton loginButton;
    private DBConnection connection;
    private Stage stage;
    private Parent root;
    private Scene scene;
    private FXMLLoader loader;
    private Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = new DBConnection();

        preferences = Preferences.systemRoot().node("Tabletop").node("Remember");
        preferences.putBoolean("IsRemember", false);
        preferences.put("Username", "");
        preferences.put("Password", "");
        preferences.put("Name", "");

        loginButton.setOnAction(this::login);
    }

    public void login(ActionEvent event){
        // Checks if any field is empty
        if(usernameField.getText().isEmpty()){
            emptyUsername();
        }
        if(passwordField.getText().isEmpty()){
            emptyPassword();
        }
        else{
            String password;
            password = Hasher.hash(passwordField.getText());
            // Checks the credentials in the database
            if(connection.checkOwner(usernameField.getText(), password)){
                if(rememberMeCheckBox.isSelected()){
                    preferences.putBoolean("IsRemember", true);
                    preferences.put("Username", usernameField.getText());
                    preferences.put("Password", password);
                    preferences.put("Name", connection.ownerName);
                }
                openOwnerPage(event);
            }else{
                wrongCredentials();
            }
        }
    }

    public void openOwnerPage(ActionEvent event){
        try{
            loader = new FXMLLoader(getClass().getResource("/manager/ManagerPage.fxml"));
            root = loader.load();

            ManagerController controller = loader.getController();
            controller.setOwnerName(connection.ownerName);

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            stage.setResizable(true);
            stage.setScene(scene);
            stage.setMinWidth(920);
            stage.setMinHeight(650);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void emptyUsername(){
        messageLabel.setText("All Fields Must Be Filled");
        usernameField.setStyle("-fx-border-color: red;" + 
                "-fx-text-fill: red;");
    }
    public void emptyPassword(){
        messageLabel.setText("All Fields Must Be Filled");
        passwordField.setStyle("-fx-border-color: red;" +
                "-fx-text-fill: red;");
    }
    public void wrongCredentials(){
        emptyPassword();
        emptyUsername();
        messageLabel.setText("Wrong Username Or Password");
    }

}
