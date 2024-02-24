module main.cashierpage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.win10;
    requires java.sql;
    requires eu.hansolo.tilesfx;

    opens main to javafx.fxml;
    exports main;
    exports login;
    exports cashier;
    exports order;
    exports properties;
    exports connection;

    opens login to javafx.fxml;
    opens cashier to javafx.fxml;
}