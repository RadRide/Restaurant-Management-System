package ai;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MessageBox extends HBox{

    /**
     * The label containing message that is going to be displayed
     */
    private Label messageLabel;

    public MessageBox(String message, String styleClass){
        // Creating a label and adding the message and styles to it
        messageLabel = new Label(message);
        messageLabel.getStylesheets().add(getClass().getResource("/Styles/AIStyles.css").toString());
        messageLabel.getStyleClass().add("label");


        getStylesheets().add(getClass().getResource("/Styles/AIStyles.css").toString());
        getStyleClass().add(styleClass);

        getChildren().add(messageLabel);
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }
}
