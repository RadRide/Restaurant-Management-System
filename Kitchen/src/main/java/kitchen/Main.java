package kitchen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import javafx.scene.image.Image;

public class Main extends Application {

    double x,y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kitchen/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        // to remove the default windows border
        stage.initStyle(StageStyle.UNDECORATED);


//        // to drag the window
//        scene.setOnMousePressed(event ->{
//            x = event.getSceneX();
//            y = event.getSceneY();
//        });
//        scene.setOnMouseDragged(event -> {
//            stage.setX(event.getScreenX() - x);
//            stage.setY(event.getScreenY() - y);
//        });


        stage.setTitle("Kitchen");
        //stage.getIcons().add(new Image("C:\\Users\\96176\\Documents\\javafx projects\\kitchentejrib6\\src\\main\\resources\\com\\example\\kitchentejrib6\\icons\\AlphaBeta_Icon.png"));
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }
}