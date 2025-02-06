module org.example.appui {
    requires javafx.controls;
    requires javafx.fxml;
    requires API;
    requires java.net.http;
    requires org.apache.commons.text;


    opens org.example.appui to javafx.fxml;
    exports org.example.appui;
}