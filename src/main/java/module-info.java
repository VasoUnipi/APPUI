module org.example.appui {
    requires javafx.controls; // Για τη χρήση των γραφικών στοιχείων του JavaFX, όπως κουμπιά, ετικέτες, κλπ.
    requires javafx.fxml; // Για τη διαχείριση της αναπαράστασης και φόρτωσης των FXML αρχείων (όταν χρησιμοποιείται FXML για το σχεδιασμό της διεπαφής χρήστη).
    requires API; //Για να παρέχεται πρόσβαση στο αρχείο API.jar ώστε να παρέχονται τα κατάλληλα δεδομένα για τις ερωτήσεις
    requires java.net.http; // Για αιτήματα HTTP (όπως GET ή POST) στον εξωτερικό διακομιστή
    requires org.apache.commons.text; //Για χρήση χρήσιμων κλάσεων που προσφέρουν εργαλεία επεξεργασίας κειμένου, όπως το StringEscapeUtils για αποφυγή προβλημάτων με HTML entities.


    opens org.example.appui to javafx.fxml; //Αναγνώριση του package κατά το φόρτωμα των FXML αρχείων
    exports org.example.appui;
}