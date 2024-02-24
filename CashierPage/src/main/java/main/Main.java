package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/LoginPage.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cashier/CashierPage.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Feast Planner");
        stage.getIcons().add(new Image(getClass().getResource("/assets/AlphaBeta_Icon.png").openStream()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}