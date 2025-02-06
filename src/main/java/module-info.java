module org.example.appui {
    requires javafx.controls;
    requires javafx.fxml;
    requires API;
    requires java.net.http;



    opens org.example.appui to javafx.fxml;
    exports org.example.appui;
}