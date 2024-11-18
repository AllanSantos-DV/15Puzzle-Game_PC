module com.allan._15puzzle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.allan._15puzzle to javafx.fxml;
    exports com.allan._15puzzle;
}