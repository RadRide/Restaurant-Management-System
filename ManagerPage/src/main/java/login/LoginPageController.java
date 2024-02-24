package login;

import dbConnection.DBConnection;
import encryption.Hasher;
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
    private TextField usernameField, showPasswordField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox showPasswordCheckBox, rememberMeCheckBox;
    @FXML
    private Label messageLabel;
    @FXML
    private Button loginButton;
    @FXML
    private FontIcon loginIcon;
    private DBConnection connection;
    private Stage stage;
    private Parent root;
    private Scene scene;
    private FXMLLoader loader;
    private Preferences preferences;

    private final Color lightGreen = Color.web("#5cd2c6"),
            white = Color.web("#fefefe"),
            darkBlue = Color.web("#363753");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = new DBConnection();

        preferences = Preferences.systemRoot().node("Tabletop").node("Remember");
        preferences.putBoolean("IsRemember", false);
        preferences.put("Username", "");
        preferences.put("Password", "");
        preferences.put("Name", "");

        changeOnHover(loginButton, loginIcon);

        showPasswordCheckBox.setOnAction(this::showPassword);
        loginButton.setOnAction(this::login);
    }

    public void showPassword(ActionEvent event){
        // Replaces the passwordField with a TextField if the show password is selected and vice versa.
        if(showPasswordCheckBox.isSelected()){
            String password = passwordField.getText();
            passwordField.setVisible(false);
            passwordField.setDisable(true);
            showPasswordField.setText(password);
            showPasswordField.setDisable(false);
            showPasswordField.setVisible(true);
            passwordField.clear();
        }else{
            String password = showPasswordField.getText();
            showPasswordField.setVisible(false);
            showPasswordField.setDisable(true);
            passwordField.setText(password);
            passwordField.setDisable(false);
            passwordField.setVisible(true);
            showPasswordField.clear();
        }
    }

    public void login(ActionEvent event){
        // Checks if any field is empty
        if(usernameField.getText().isEmpty()){
            emptyUsername();
        }
        if(passwordField.getText().isEmpty() && showPasswordField.getText().isEmpty()){
            emptyPassword();
        }
        else{
            // Checks to see which password field if used and gets the password from it
            String password = "";
            if(showPasswordCheckBox.isSelected()){
                password = showPasswordField.getText();
            }else{
                password = passwordField.getText();
            }
            password = Hasher.hash(password);
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
        showPasswordField.setStyle("-fx-border-color: red;" +
                "-fx-text-fill: red;");
    }
    public void wrongCredentials(){
        emptyPassword();
        emptyUsername();
        messageLabel.setText("Wrong Username Or Password");
    }

    private void changeOnHover(Button button, FontIcon icon) {
        button.setStyle("-fx-border-color: #363753;" +
                "-fx-text-fill: #363753;");
        icon.setIconColor(darkBlue);
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-border-color: #5cd2c6;" +
                    "-fx-text-fill: #5cd2c6;");
            icon.setIconColor(lightGreen);
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-border-color: #363753;" +
                    "-fx-text-fill: #363753;");
            icon.setIconColor(darkBlue);
        });
    }

}
