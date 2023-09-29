package main;

import dbConnection.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import manager.ManagerController;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Preferences preferences = Preferences.systemRoot().node("Tabletop").node("Remember");
        DBConnection connection = new DBConnection();
        Image icon = new Image(getClass().getResource("/icons/AlphaBeta_Icon.png").openStream());
//        Parent root = FXMLLoader.load(getClass().getResource("/main/WelcomePage.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/manager/ManagerPage.fxml"));

        Parent root = null;

        if(preferences.getBoolean("IsRemember", false)){
            if(connection.checkOwner(preferences.get("Username", ""), preferences.get("Password", ""))){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/manager/ManagerPage.fxml"));
                root = loader.load();
                ManagerController controller = loader.getController();
                controller.setOwnerName(preferences.get("Name", ""));
                stage.setResizable(true);
                stage.setMinWidth(920);
                stage.setMinHeight(650);
            }else {
                root = FXMLLoader.load(getClass().getResource("/main/LoginPage.fxml"));
                stage.setResizable(false);
            }
        }else {
            root = FXMLLoader.load(getClass().getResource("/main/LoginPage.fxml"));
            stage.setResizable(false);
        }

        Scene scene = new Scene(root);
        stage.setTitle("Feast Planner");
        stage.setScene(scene);
        stage.getIcons().add(icon);
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}