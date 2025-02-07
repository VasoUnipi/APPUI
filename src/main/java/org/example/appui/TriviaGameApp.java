// Ορισμός του package που ανήκει η κλάση
package org.example.appui;

// Βιβλιοθήκη για διαχείριση ειδικών χαρακτήρων στο κείμενο
import org.apache.commons.text.StringEscapeUtils;

// Χρησιμοποιείται για τον καθορισμό θέσης των στοιχείων στο Scene
import javafx.geometry.Pos;

// Εισαγωγή του TriviaApiClient και των σχετικών κλάσεων (Questions.java, Result.java) που βρίσκονται στο αρχείο .jar για την ανάκτηση των ερωτήσεων
import org.example.TriviaApiClient; // Η κλάση για επικοινωνία με το Trivia API
import org.example.Questions; // Το αντικείμενο που περιέχει τις ερωτήσεις του κουίζ
import org.example.Result; // Το αντικείμενο που περιέχει τα δεδομένα κάθε ερώτησης

// Κύριες JavaFX βιβλιοθήκες για την ανάπτυξη GUI εφαρμογών
import javafx.application.Application; // Βασική κλάση JavaFX για την εκκίνηση εφαρμογών GUI
import javafx.scene.Scene; // Αντιπροσωπεύει τη σκηνή της εφαρμογής
import javafx.scene.control.*; // Περιέχει κουμπιά, ετικέτες και άλλα στοιχεία διεπαφής
import javafx.scene.layout.VBox; // Δομή διάταξης κάθετης στοίχισης στοιχείων
import javafx.stage.Stage; // Το "παράθυρο" της εφαρμογής

// Χαρτογράφηση των κατηγοριών και των αντίστοιχων ID τους
import java.util.HashMap; // Χρησιμοποιείται για την αποθήκευση των κατηγοριών ερωτήσεων
import java.util.Map; // Για τη χρήση χαρτών (key-value pairs)

/**
 * Κύρια κλάση του παιχνιδιού Trivia, η οποία δημιουργεί το γραφικό περιβάλλον και
 * διαχειρίζεται τη ροή του κουίζ.
 */
public class TriviaGameApp extends Application {

    // Κλήση στο API που επιστρέφει τις ερωτήσεις
    private TriviaApiClient apiClient = new TriviaApiClient();

    // Χάρτης που αντιστοιχεί τα ονόματα των κατηγοριών με τα αντίστοιχα ID που απαιτεί το API
    private Map<String, Integer> categoryMap = new HashMap<>();

    // Μεταβλητές για το σκορ, το πλήθος παιχνιδιών και τη θέση της τρέχουσας ερώτησης
    private int totalScore = 0; // Συνολικό σκορ από όλες τις φορές που έπαιξε ο χρήστης το παιχνίδι
    private int gamesPlayed = 0; // Αριθμός παιχνιδιών που παίχτηκαν
    private int currentQuestionIndex = 0; // Δείκτης για την τρέχουσα ερώτηση
    private Questions quizData;// Τα δεδομένα των ερωτήσεων

    // GUI Στοιχεία (Σκηνή, Διατάξεις, Ετικέτες, Κουμπιά, Επιλογές)
    private Stage quizStage; // Σκηνή (παράθυρο) για την εμφάνιση του κουίζ
    private VBox quizLayout; // Διάταξη του κουίζ
    private VBox layout;  // Διάταξη για την αρχική οθόνη
    private Label questionLabel; // Ετικέτα για την εμφάνιση της ερώτησης
    private ToggleGroup group; // Ομάδα για την επιλογή απαντήσεων (radio buttons)
    private Button nextButton; // Κουμπί για να προχωρήσει ο χρήστης στην επόμενη ερώτηση
    private VBox optionsBox; // Το κουτί που περιέχει τις επιλογές απαντήσεων
    private ComboBox<String> categoryBox; // Σκοπός: Επιλογή κατηγορίας
    private ComboBox<String> difficultyBox; // Σκοπός: Επιλογή δυσκολίας
    private ComboBox<String> typeBox; // Σκοπός: Επιλογή τύπου ερώτησης (multiple choice ή boolean)
    private Stage primaryStage; // Σκηνή για την αρχική οθόνη
    private TextField numberField; // Πεδίο για την εισαγωγή του αριθμού των ερωτήσεων
    private Label scoreLabel; // Ετικέτα για την εμφάνιση του σκορ
    private int score = 0; // Σκορ για το τρέχον παιχνίδι
    private int totalQuestions = 0; // Σύνολο ερωτήσεων για το τρέχον παιχνίδι

    /**
     * Εκκίνηση της JavaFX εφαρμογής.
     * Δημιουργεί το αρχικό UI που επιτρέπει στον χρήστη να επιλέξει κατηγορία, δυσκολία και αριθμό ερωτήσεων.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Trivia Game"); // Τίτλος παραθύρου

        Label titleLabel = new Label("Select Quiz Options"); // Ετικέτα επικεφαλίδας

        // Δημιουργία του ComboBox για την επιλογή κατηγορίας και προσθήκη των διαθέσιμων κατηγοριών
        categoryBox = new ComboBox<>();
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Entertainment: Books", 10);
        categoryMap.put("Entertainment: Film", 11);
        categoryMap.put("Entertainment: Music", 12);
        categoryMap.put("Entertainment: Musicals & Theatres", 13);
        categoryMap.put("Entertainment: Television", 14);
        categoryMap.put("Entertainment: Video Games", 15);
        categoryMap.put("Entertainment: Board Games", 16);
        categoryMap.put("Science & Nature", 17);
        categoryMap.put("Science: Computers", 18);
        categoryMap.put("Science: Mathematics", 19);
        categoryMap.put("Mythology", 20);
        categoryMap.put("Sports", 21);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);
        categoryMap.put("Politics", 24);
        categoryMap.put("Art", 25);
        categoryMap.put("Celebrities", 26);
        categoryMap.put("Animals", 27);
        categoryMap.put("Vehicles", 28);
        categoryMap.put("Entertainment: Comics", 29);
        categoryMap.put("Science: Gadgets", 30);
        categoryMap.put("Entertainment: Japanese Anime & Manga", 31);
        categoryMap.put("Entertainment: Cartoon & Animations", 32);

        // Προσθέτονται οι κατηγορίες στο ComboBox
        categoryBox.getItems().addAll(categoryMap.keySet()); // Προσθήκη κατηγοριών στο dropdown
        categoryBox.setValue("General Knowledge"); // Προεπιλεγμένη τιμή

        // Δημιουργία ComboBox για την επιλογή δυσκολίας (Easy, Medium, Hard)
        difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("easy", "medium", "hard");
        difficultyBox.setValue("medium"); // Ορισμός της προεπιλεγμένης δυσκολίας

        // Δημιουργία ComboBox για την επιλογή τύπου ερώτησης (πολλαπλών επιλογών ή σωστό/λάθος)
        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("multiple", "boolean");
        typeBox.setValue("multiple"); // Προεπιλεγμένος τύπος ερώτησης

        // Πεδίο κειμένου για την εισαγωγή αριθμού ερωτήσεων
        numberField = new TextField();
        numberField.setPromptText("Number of Questions (1-50)");

        // Κουμπί εκκίνησης του κουίζ
        Button startButton = new Button("Start Quiz");
        startButton.setOnAction(e -> startQuiz());

        // Ετικέτα για την εμφάνιση του σκορ
        scoreLabel = new Label("Score: 0");

        // Δημιουργία του layout για την αρχική οθόνη και οργάνωση των στοιχείων σε κάθετη διάταξη (VBox)
        layout = new VBox(15, titleLabel, categoryBox, difficultyBox, typeBox, numberField, startButton, scoreLabel);
        layout.setAlignment(Pos.CENTER); // Κεντράρισμα των στοιχείων στην οθόνη
        VBox layout = new VBox(10, titleLabel, categoryBox, difficultyBox, typeBox, numberField, startButton, scoreLabel);
        layout.setAlignment(Pos.CENTER); // Κεντράρισμα των στοιχείων στην οθόνη

        // Εμφάνιση της σκηνής με τα παραπάνω στοιχεία
        primaryStage.setScene(new Scene(layout, 400, 300)); // Ορισμός σκηνής παραθύρου
        primaryStage.show();
    }

    /**
     * Ξεκινά το κουίζ λαμβάνοντας δεδομένα από το API, εφόσον οι επιλογές είναι έγκυρες.
     */

    private void startQuiz() {
        try {

            // Ελέγχεται ο αριθμός των ερωτήσεων που εισάγεται από τον χρήστη.
            int numQuestions;
            try {
                numQuestions = Integer.parseInt(numberField.getText());

                // Αν ο αριθμός των ερωτήσεων δεν είναι έγκυρος (π.χ. είναι μικρότερος από 1 ή μεγαλύτερος από 50),
                // εμφανίζεται μήνυμα σφάλματος και επιστρέφει.
                if (numQuestions < 1 || numQuestions > 50) {
                    showAlert("Please enter a number between 1 and 50.");
                    return;
                }
            } catch (NumberFormatException e) {
                // Εμφανίζεται μήνυμα σφάλματος αν η τιμή δεν είναι αριθμός
                showAlert("Invalid number of questions.");  // Ειδοποίηση για μη έγκυρη τιμή
                return;
            }
            // Λαμβάνονται οι επιλογές του χρήστη από τα ComboBox
            String category = categoryBox.getValue();
            int categoryId = categoryMap.get(category);  // Εντοπίζεται το ID της κατηγορίας
            String difficulty = difficultyBox.getValue(); //Λαμβάνεται το επίπεδο δυσκολίας που έχει επιλέξει ο χρήστης
            String type = typeBox.getValue(); //Λαμβάνεται ο τύπος ερωτήσεων που έχει επιλέξει ο χρήστης

            // Κλήση του API για λήψη ερωτήσεων και στη συνέχεια ελέγχεται αν οι ερωτήσεις είναι έγκυρες και εμφανίζονται
            Questions quizData = apiClient.fetchQuestions(numQuestions, categoryId, difficulty, type);

            // Στην περίπτωση που υπάρχει πρόβλημα στην ανάκτηση των ερωτήσεων
            if (quizData != null && quizData.getResults() != null) {

                // Αρχικοποιείται το σκορ και ο αριθμός των ερωτήσεων.
                score = 0;
                totalQuestions = quizData.getResults().size();
                displayQuestions(quizData); // Προβολή ερωτήσεων
            } else {
                showAlert("No questions retrieved.");  // Αν δεν βρέθηκαν λήφθηκαν έγκυρα δεδομένα, εμφανίζεται μήνυμα σφάλματος
            }
        } catch (Exception e) {
            e.printStackTrace(); // Εκτύπωση εξαιρέσεων στην κονσόλα
        }
    }

    /**
     * Αυτή η μέθοδος αρχικοποιεί το παράθυρο για τις ερωτήσεις του quiz
     * Δημιουργεί τα layout και τα στοιχεία που θα εμφανιστούν (ερώτηση, επιλογές, κουμπί "Next")
     * Χρησιμοποιεί ToggleGroup για τη δημιουργία μιας ομάδας από επιλογές,
     * όπου μόνο μία απάντηση μπορεί να είναι επιλεγμένη τη φορά.
     * Η μέθοδος καλεί τη showQuestion(), που είναι υπεύθυνη για την εμφάνιση της πρώτης ερώτησης
     */
    private void displayQuestions(Questions quizData) {

        //Αρχικοποιούνται τα δεδομένα του Quiz
        this.quizData = quizData;
        this.currentQuestionIndex = 0; // Το πρώτο index της ερώτησης
        this.score = 0; // Μηδενίζεται το σκορ για το νέο παιχνίδι
        this.totalQuestions = quizData.getResults().size();  // Σύνολο ερωτήσεων

        // Δημιουργία νέας σκηνής για το κουίζ
        quizStage = new Stage();
        quizLayout = new VBox(20); // Μεγαλύτερο spacing για καλύτερη εμφάνιση
        quizLayout.setAlignment(Pos.CENTER); // Κεντράρισμα στοιχείων

        // Δημιουργία και ρύθμιση της ετικέτας για την ερώτηση
        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(400);
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;"); // Στυλ ερώτησης

        // Ομάδα επιλογών για τις απαντήσεις (radio buttons)
        group = new ToggleGroup();
        optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER); // Κεντράρισμα επιλογών

        // Κουμπί "Next" για να προχωρήσει ο χρήστης στην επόμενη ερώτηση
        nextButton = new Button("Next");
        nextButton.setDisable(true); // Αρχικά, είναι απενεργοποιημένο και ενεργοποιείται μετά την επιλογή της απάντησης
        nextButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        nextButton.setOnAction(e -> nextQuestion());  // Πάτημα κουμπιού για την επόμενη ερώτηση

        // Ετικέτα για να εμφανίζεται το σκορ
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");

        // Προσθήκη όλων των στοιχείων στο Layout
        quizLayout.getChildren().addAll(questionLabel, optionsBox, nextButton, scoreLabel);

        // Εμφάνιση της σκηνής για το κουίζ
        Scene scene = new Scene(quizLayout, 500, 400);
        quizStage.setScene(scene);
        quizStage.setTitle("Quiz Time!");
        quizStage.show();  // Εμφανίζεται το κουίζ

        // Εμφάνιση της πρώτης ερώτησης
        showQuestion();
    }

    /**
     * Εμφανίζει την τρέχουσα ερώτηση και τις επιλογές απαντήσεων
     * Ελέγχει αν υπάρχει επιλεγμένη σωστή απάντηση και την αποθηκεύει
     */
    private void showQuestion() {
        // Εκτύπωση της τρέχουσας θέσης (ευρετηρίου) της ερώτησης για σκοπούς debugging
        System.out.println("Current Question Index: " + currentQuestionIndex + " / " + totalQuestions);

        // Αν ο δείκτης της τρέχουσας ερώτησης είναι μεγαλύτερος ή ίσος με τον συνολικό αριθμό των ερωτήσεων, τερματίζεται το κουίζ
        if (currentQuestionIndex >= totalQuestions) {
            endQuiz();
            return;
        }

        // Λαμβάνεται η τρέχουσα ερώτηση από τα δεδομένα του κουίζ
        Result question = quizData.getResults().get(currentQuestionIndex);

        // Χρήση του StringEscapeUtils για να μην υπάρχουν τυχόν HTML entities (όπως &amp; -> &) στην ερώτηση
        String questionText = StringEscapeUtils.unescapeHtml4(question.getQuestion());
        questionLabel.setText((currentQuestionIndex + 1) + ". " + questionText);

        // Απαλοίφονται οι προηγούμενες επιλογές (αν υπάρχουν)
        group.getToggles().clear();
        optionsBox.getChildren().clear();

        // Δημιουργείται η επιλογή για τη σωστή απάντηση.
        RadioButton correctAnswer = new RadioButton(StringEscapeUtils.unescapeHtml4(question.getCorrectAnswer()));
        correctAnswer.setUserData(true); // Σημειώνει ότι η απάντηση είναι σωστή
        correctAnswer.setToggleGroup(group); // Εξασφαλίζει ότι μόνο μία επιλογή μπορεί να είναι ενεργή
        correctAnswer.setStyle("-fx-font-size: 14px;"); // Ρυθμίζει το μέγεθος γραμματοσειράς
        correctAnswer.setMaxWidth(Double.MAX_VALUE);
        optionsBox.getChildren().add(correctAnswer); // Προσθέτει τη σωστή απάντηση

        // Δημιουργεί και εμφανίζει τις λανθασμένες απαντήσεις
        for (String incorrect : question.getIncorrectAnswers()) {
            RadioButton option = new RadioButton(StringEscapeUtils.unescapeHtml4(incorrect));
            option.setUserData(false); // Σημειώνει ότι αυτές είναι λανθασμένες
            option.setToggleGroup(group); // Όλες οι επιλογές είναι μέρος της ίδιας ομάδας (μόνο μία επιλέξιμη)
            option.setStyle("-fx-font-size: 14px;"); // Ρυθμίζει το μέγεθος γραμματοσειράς
            option.setMaxWidth(Double.MAX_VALUE);
            optionsBox.getChildren().add(option); // Προσθέτει τις λανθασμένες απαντήσεις στις επιλογές
        }

        // Αρχικά απενεργοποιεί το κουμπί "Next", ώστε να ενεργοποιηθεί μόνο μετά την επιλογή μιας απάντησης από το χρήστη
        nextButton.setDisable(true);

        // Για κάθε κουμπί (σωστής ή λανθασμένης απάντησης), ενεργοποιεό το κουμπί "Next" όταν γίνει επιλογή της απάντησης
        for (javafx.scene.Node node : optionsBox.getChildren()) {
            if (node instanceof RadioButton) {
                ((RadioButton) node).setOnAction(e -> nextButton.setDisable(false)); // Όταν ο χρήστης επιλέξει κάποια απάντηση, το κουμπί "Next" ενεργοποιείται
            }
        }
    }

    /**
     *Αυτή η μέθοδος καλείται όταν ο χρήστης επιλέξει μια απάντηση και επιλέξει το κουμπί "Next"
     *Ελέγχεται αν η απάντηση είναι σωστή και αυξάνει το σκορ αν ισχύει.
     *Αφού μεταβεί στην επόμενη ερώτηση, αν έχουν ολοκληρωθεί όλες οι ερωτήσεις,
     *καλεί τη μέθοδο endQuiz() για να ολοκληρωθεί το quiz
     */
    private void nextQuestion() {
        // Αν ο χρήστης έχει φτάσει στην τελευταία ερώτηση, καλείται το endQuiz() και τερματίζει το παιχνίδι
        if (currentQuestionIndex >= totalQuestions - 1) {
            endQuiz();
            return;
        }

        // Ελέγχεται αν υπάρχει επιλεγμένη απάντηση
        RadioButton selected = (RadioButton) optionsBox.getChildren()
                .stream()
                .filter(node -> node instanceof RadioButton && ((RadioButton) node).isSelected())
                .map(node -> (RadioButton) node)
                .findFirst()
                .orElse(null);

        if (selected != null && (boolean) selected.getUserData()) {
            score++;
        }

        //Ενημερώνεται το σκορ
        scoreLabel.setText("Score: " + score + "/" + totalQuestions);

        // Αυξάνεται το currentQuestionIndex **μόνο αν υπάρχουν κι άλλες ερωτήσεις**
        currentQuestionIndex++;

        // Αν υπάρχουν ακόμα ερωτήσεις, δείχνει την επόμενη, αλλιώς τερματίζεται το παιχνίδι
        if (currentQuestionIndex < totalQuestions) {
            showQuestion();
        } else {
            endQuiz();  // Καλείται το endQuiz() αν δεν υπάρχουν όλες τις ερωτήσεις
        }
    }

    /**
     *Αυτή η μέθοδος καλείται όταν ολοκληρωθούν όλες οι ερωτήσεις
     * Εμφανίζει τα αποτελέσματα του παιχνιδιού, υπολογίζει δηλαδή
     *το ποσοστό επιτυχίας και δείχνει το τελικό σκορ,
     *και παρέχει την επιλογή να ξεκινήσει ξανά το quiz μέσω του κουμπιού "Restart"
     */

    private void endQuiz() {
        System.out.println("End of quiz! Final Score: " + score + "/" + totalQuestions);

        // Χρήση του σωστού layout
        quizLayout.getChildren().clear();
        //Υπολογίζεται το ποσοστό επιτυχίας
        double successRate = (totalQuestions > 0) ? ((double) score / totalQuestions) * 100 : 0;
        gamesPlayed++;
        totalScore += score;
        //Δημιουργείται μήνυμα με τα αποτέλεσματα
        Label endMessage = new Label(
                "Quiz Over!\n" +
                        "Your score: " + score + "/" + totalQuestions + "\n" +
                        "Success rate: " + String.format("%.2f", successRate) + "%\n" +
                        "Total score across games: " + totalScore + "\n" +
                        "Games played: " + gamesPlayed
        );
        endMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: blue;");
        endMessage.setAlignment(Pos.CENTER); // Κεντράρισμα στοιχείων

        // Κουμπί για να ξεκινήσει ξανά το quiz
        Button restartButton = new Button("Restart");
        restartButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        restartButton.setOnAction(e -> {
            quizStage.close();  // Κλείνει το παράθυρο με τις ερωτήσεις
            start(primaryStage); // Επιστροφή στην αρχική οθόνη
        });

        // Προσθέτεται το τέλος του quiz στο layout
        quizLayout.getChildren().addAll(endMessage, restartButton);
    }

    /**
     *Δείχνει παράθυρο μηνύματος σφάλματος
     *όταν κάτι δεν πάει καλά (π.χ., λάθος είσοδος αριθμού ερωτήσεων)
     * Αυτή η μέθοδος είναι χρήσιμη για να ενημερωθεί ο χρήστης για σφάλματα ή ελλιπή δεδομένα.
     *
     * @param message Το μήνυμα σφάλματος που θα εμφανιστεί στον χρήστη.
     */
    private void showAlert(String message) {

        // Δημιουργείται ένα νέο παράθυρο τύπου ERROR.
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // Ρυθμίζεται τον τίτλο του παραθύρου.
        alert.setTitle("Error");

        // Ορίζεται το περιεχόμενο του παραθύρου με το μήνυμα σφάλματος.
        alert.setContentText(message);

        // Εμφανίζεται το παράθυρο και περιμένει να το κλείσει ο χρήστης.
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Καλείται η μέθοδος launch() για να ξεκινήσει η εφαρμογή JavaFX.
        launch(args);
    }
}
