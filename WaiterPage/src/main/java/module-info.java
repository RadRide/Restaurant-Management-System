module main.waiterpage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.win10;
    requires java.sql;
    requires eu.hansolo.tilesfx;
    requires MaterialFX;

    opens main to javafx.fxml;
    opens waiter to javafx.fxml;
    exports main;
    exports waiter;
    exports menu;
    exports properties;
    exports order;
}