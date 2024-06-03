module com.example.kitchentejrib6 {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
        requires org.kordamp.ikonli.javafx;
        requires org.kordamp.ikonli.win10;
        requires eu.hansolo.tilesfx;
    requires java.sql;

    opens kitchen to javafx.fxml;
    exports kitchen;
}