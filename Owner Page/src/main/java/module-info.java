module main.restaurantpos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.win10;
    requires eu.hansolo.tilesfx;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.sql;
    requires java.prefs;

    opens main to javafx.fxml;
    exports main;
    // Temporary for testing.
    opens manager to javafx.fxml;
    opens login to javafx.fxml;
    exports manager;
    exports menu;
    exports qrcode;
    exports dbConnection;
    exports login;
    exports encryption;
    exports section;
    exports settings;
    exports order;
    exports statistics;
}