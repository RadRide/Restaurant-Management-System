package kitchen;

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
//import waiter.WaiterPageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {
    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7,
            button8, button9, button0, eraseButton,loginButton;
    @FXML
    private FontIcon eraseIcon;
    @FXML
    private TextField idField;
    @FXML
    private Label warningMessage;
    private DatabaseConnection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = new DatabaseConnection();
    }

    public void dialPressed(ActionEvent event){
        if(event.getSource() == eraseButton){
            if(!idField.getText().isEmpty()){
                idField.setText(idField.getText(0, idField.getText().length() - 1));
            }
        }else if(event.getSource() == loginButton){
            String name = connection.login(idField.getText());
            if(name != null){
                login(name, ((Node)event.getSource()).getScene().getWindow());
            }else{
                warningMessage.setVisible(true);
            }
        }else{
            idField.appendText(((Button)event.getSource()).getText());
        }
    }

    public void login(String name, Window window) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kitchen/KitchenPage.fxml"));
            Parent root = loader.load();

            KitchenPageController controller = loader.getController();
            controller.setChefName(name);


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

