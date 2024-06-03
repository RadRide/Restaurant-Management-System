package ai;

import dbConnection.DBConnection;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AIController implements Initializable {

    @FXML
    private MFXScrollPane chatScroll;
    @FXML
    private VBox chatBox;
    @FXML
    private MFXTextField messageField;
    @FXML
    private MFXButton sendButton;
    @FXML
    private MFXProgressBar loadingBar;

    private DBConnection connection;
    private String instructions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        connection = new DBConnection();

        instructions = connection.getGeneralReport();

        loadingBar.setVisible(false);
        chatBox.getChildren().clear();

        chatBox.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                chatScroll.setVvalue(1D);
            }
        });

        messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    sendMessage(null);
                }
            }
        });

        sendButton.setOnAction(this::sendMessage);

        startComms("Hello, who are you?");
    }

    public void sendMessage(ActionEvent event){
        if(!messageField.getText().isEmpty()){
            chatBox.getChildren().add(new MessageBox(messageField.getText(), "requestBox"));
            startComms(messageField.getText());
            messageField.clear();
        }
    }

    private void startComms(String message){
        Communicator.sendMessage(message, instructions, loadingBar, sendButton, chatBox);
    }

    public static void startLoading(MFXProgressBar loadingBar, MFXButton sendButton,VBox chatBox, MessageBox messageBox){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisible(true);
                sendButton.setDisable(true);
                chatBox.getChildren().add(messageBox);
            }
        });
    }

    public static void streamAnswer(MessageBox messageBox, String answer){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageBox.getMessageLabel().setText(answer);
            }
        });
    }

    public static void finishLoading(MFXProgressBar loadingBar, MFXButton sendButton){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisible(false);
                sendButton.setDisable(false);
            }
        });
    }

    public static void showAlert(String errorMessage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(errorMessage);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        });
    }
}
