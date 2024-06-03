package kitchen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;



public class KitchenPageController implements Initializable{

    @FXML
    private ScrollPane scrollPaneMain;
    @FXML
    private VBox vboxMain;
    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private Button closeBtn;
    @FXML
    private Label nameLabel;


    DatabaseConnection connectNow = new DatabaseConnection(this);
    //Connection connectDB = connectNow.openConnection();


    // exit the window
    @FXML
    public void closeBtn(ActionEvent event) {
        System.exit(0);
    }


    //make the recipes bar invisible
    public void recipesBtn(ActionEvent event){
        vboxMain.setVisible(false);
    }
    //make the recipes bar invisible
    public void ordersBtn(ActionEvent event){
        vboxMain.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vboxMain.setVisible(true);

        connectNow.timelineTest();
//        vboxMain.getChildren()

    }
    public VBox getVboxMain() {
        return vboxMain;
    }


    public void setChefName(String name) {
        nameLabel.setText(name);
    }

    public void setName(String name){
    }
}


//    // maximize the window
//    public void maxBtn(ActionEvent event) {
//        Stage stage=(Stage) maxBtn.getScene().getWindow();
//        stage.setMaximized(true);
//    }
//    // minimize the window
//    public void minBtn(ActionEvent event) {
//        Stage stage=(Stage) minBtn.getScene().getWindow();
//        stage.setMaximized(false);
//    }