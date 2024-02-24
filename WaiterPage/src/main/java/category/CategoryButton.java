package category;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import menu.MenuPane;
import waiter.WaiterPageController;

import javafx.event.ActionEvent;

public class CategoryButton extends Button {

    private String category;
    private WaiterPageController controller;

    public CategoryButton(WaiterPageController controller, String category){
        this.controller = controller;
        this.category = category;
        getStyleClass().add("sideButton");
        setText(category);
        setPrefSize(120, 24);
        setOnAction(this::scrollToPane);
    }

    public void scrollToPane(ActionEvent event){
        controller.getDishScroll().setVvalue(
                controller.getDishBox().getChildren().get(((VBox)getParent()).getChildren().indexOf(event.getSource())).getLayoutY() *
                (1 / (controller.getDishBox().getHeight() - controller.getDishScroll().getViewportBounds().getHeight())));
    }

}
