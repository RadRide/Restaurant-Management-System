package menu;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import waiter.WaiterPageController;

import java.util.ArrayList;

public class MenuPane<T> extends FlowPane {

    private Label categoryLabel;
    private ArrayList<MenuButton<T>> buttons;
    private String category;
    private WaiterPageController controller;

    public MenuPane(WaiterPageController controller, String category, ArrayList<MenuButton<T>> buttons){
        this.controller = controller;
        this.category = category;
        this.buttons = buttons;

        setPane();

        setCategoryLabel(category);

        setButtons();
    }

    public void setController(WaiterPageController controller){
        this.controller = controller;
    }

    public void setPane(){
        setHgap(20);
        setVgap(20);

        setPadding(new Insets(10, 0, 0, 20));

        getStyleClass().add("middleBackground");
    }

    public void setCategoryLabel(String category){
        categoryLabel = new Label(category);
        categoryLabel.getStyleClass().add("categoryLabel");
        categoryLabel.prefWidthProperty().bind(controller.getDishScroll().widthProperty().multiply(0.9));

        getChildren().add(categoryLabel);
    }

    public void setButtons(){
        getChildren().addAll(buttons);
    }


    public String getCategory() {
        return category;
    }
}
