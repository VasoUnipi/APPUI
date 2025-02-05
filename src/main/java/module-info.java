module org.example.appui {
    requires javafx.controls;
    requires javafx.fxml;
    requires Triviapp;


    opens org.example.appui to javafx.fxml;
    exports org.example.appui;
}