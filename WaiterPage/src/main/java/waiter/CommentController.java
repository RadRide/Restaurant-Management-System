package waiter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Window;
import order.OrderItem;

import java.io.IOException;

public class CommentController {

    @FXML
    private Button spaceButton, eraseButton, enterButton;
    @FXML
    private TextArea commentArea;
    private OrderItem item;
    private FXMLLoader loader;
    private Dialog<ButtonType> dialog;
    private DialogPane commentPane;
    private Alert alert;
    private Window window;

    public void openCommentPane(Window window,OrderItem item){
        this.window = window;

        initializeDialog();
        initializeFields();

        commentArea.setText(item.getComment());

        if(dialog.showAndWait().isPresent()){
            item.setComment(commentArea.getText());
        }
    }

    public void initializeDialog(){
        try{
            loader = new FXMLLoader(getClass().getResource("/waiter/CommentPane.fxml"));
            commentPane = loader.load();
            dialog = new Dialog<>();
            dialog.setDialogPane(commentPane);
            dialog.setTitle("Comment Section");
            dialog.initOwner(window);
        }catch (IOException e){
            e.printStackTrace();
            showAlert("Error Opening Comment Section");
        }
    }

    public void initializeFields(){
        commentArea = (TextArea) commentPane.lookup("#commentArea");
        spaceButton = (Button) commentPane.lookup("spaceButton");
        eraseButton = (Button) commentPane.lookup("eraseButton");
        enterButton = (Button) commentPane.lookup("enterButton");
    }

    public void type(ActionEvent event){
        if(event.getSource() == spaceButton){
            commentArea.appendText(" ");
        }else if(event.getSource() == eraseButton){
            if(!commentArea.getText().isEmpty()){
                commentArea.replaceText(commentArea.getText().length() - 1, commentArea.getText().length(), "");
            }
        }else if(event.getSource() == enterButton){
            commentArea.appendText("\n");
        }else{
            commentArea.appendText(((Button)event.getSource()).getText().toLowerCase());
        }
    }

    private void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

}
