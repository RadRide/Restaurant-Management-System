package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomePageController implements Initializable {

    @FXML
    private Button asCashier, asKitchen, asManager, asWaiter, loginButton, employeeLoginButton,
            number1, number2, number3, number4, number5, number6, number7, number8, number9, eraseButton;
    @FXML
    private FontIcon cashierIcon, loginIcon, managerIcon, waiterIcon, kitchenIcon, employeeLoginIcon, eraseIcon;
    @FXML
    private AnchorPane managerPane, welcomePane, workerPane;
    @FXML
    private TextField username, idField;
    private Color lightGreen = Color.web("#5cd2c6"),
            white = Color.web("#fefefe"),
            darkBlue = Color.web("#363753");
    private boolean managerPressed = false, workerPressed = false;
    private int option; // 0 for cashier, 1 for waiter, 2 for Kitchen crew.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        changeOnHover(asCashier, cashierIcon, lightGreen, darkBlue);
        changeOnHover(asKitchen, kitchenIcon, lightGreen, darkBlue);
        changeOnHover(asManager, managerIcon, lightGreen, darkBlue);
        changeOnHover(asWaiter, waiterIcon, lightGreen, darkBlue);
        changeOnHover(loginButton, loginIcon, darkBlue, white);
        changeOnHover(employeeLoginButton, employeeLoginIcon, darkBlue, white);
        changeOnHover(eraseButton, eraseIcon, darkBlue, white);

    }

    private void changeOnHover(Button button, FontIcon icon, Color color1, Color color2){
        button.setOnMouseEntered(event ->{
            icon.setIconColor(color1);
            icon.setStroke(color1);
        });
        button.setOnMouseExited(event ->{
            icon.setIconColor(color2);
            icon.setStroke(color2);
        });
    }

    public void loginAsManager(ActionEvent event){
        managerPressed = true;
        if(workerPressed){
            workerPane.setVisible(false);
            managerPane.setVisible(true);
            workerPressed = false;
        }else{
            welcomePane.setVisible(false);
            managerPane.setVisible(true);
        }
    }

    public void loginAsWorker(ActionEvent event){
        if(managerPressed){
            managerPane.setVisible(false);
            workerPane.setVisible(true);
            managerPressed = false;
            workerPressed = true;
        }else if(workerPressed){
            getOption(event);
        }else{
            workerPressed = true;
            welcomePane.setVisible(false);
            workerPane.setVisible(true);
        }

    }
    private void getOption(ActionEvent event){
        if(event.getSource() == asCashier){
            option = 0;
        }else if(event.getSource() == asWaiter){
            option = 1;
        }else if(event.getSource() == asKitchen){
            option = 2;
        }
    }

    public void addNumber(ActionEvent event){
        String n = "";

        if(event.getSource() == number1){
            idField.setText(idField.getText() + "1");
        }else if(event.getSource() == number2){
            idField.setText(idField.getText() + "2");
        }else if(event.getSource() == number3){
            idField.setText(idField.getText() + "3");
        }else if(event.getSource() == number4){
            idField.setText(idField.getText() + "4");
        }else if(event.getSource() == number5){
            idField.setText(idField.getText() + "5");
        }else if(event.getSource() == number6){
            idField.setText(idField.getText() + "6");
        }else if(event.getSource() == number7){
            idField.setText(idField.getText() + "7");
        }else if(event.getSource() == number8){
            idField.setText(idField.getText() + "8");
        }else if(event.getSource() == number9){
            idField.setText(idField.getText() + "9");
        }else if(event.getSource() == eraseButton){
            if(idField.getText().length() > 0){
                idField.setText(idField.getText().substring(0, idField.getText().length() - 1));
            }
        }
    }

}