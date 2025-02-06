package org.example.appui;
import org.apache.commons.text.StringEscapeUtils;
import javafx.geometry.Pos;
import org.example.TriviaApiClient;
import org.example.Questions;
import org.example.Result;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TriviaGameApp extends Application {

    private TriviaApiClient apiClient = new TriviaApiClient();
    private Map<String, Integer> categoryMap = new HashMap<>();
    private int totalScore = 0;
    private int gamesPlayed = 0;
    private int currentQuestionIndex = 0;
    private Questions quizData;
    private Stage quizStage;
    private VBox quizLayout;
    private VBox layout;
    private Label questionLabel;
    private ToggleGroup group;
    private Button nextButton;
    private VBox optionsBox;
    private ComboBox<String> categoryBox;
    private ComboBox<String> difficultyBox;
    private ComboBox<String> typeBox;
    private Stage primaryStage;
    private TextField numberField;
    private Label scoreLabel;
    private int score = 0;
    private int totalQuestions = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Trivia Game");

        Label titleLabel = new Label("Select Quiz Options");

        // Κατηγορίες από το Open Trivia DB
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

        categoryBox.getItems().addAll(categoryMap.keySet());
        categoryBox.setValue("General Knowledge");

        // Επιλογή δυσκολίας
        difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("easy", "medium", "hard");
        difficultyBox.setValue("medium");

        // Επιλογή τύπου ερωτήσεων
        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("multiple", "boolean");
        typeBox.setValue("multiple");

        // Επιλογή αριθμού ερωτήσεων
        numberField = new TextField();
        numberField.setPromptText("Number of Questions (1-50)");

        // Κουμπί εκκίνησης
        Button startButton = new Button("Start Quiz");
        startButton.setOnAction(e -> startQuiz());

        // Εμφάνιση σκορ
        scoreLabel = new Label("Score: 0");
        layout = new VBox(15, titleLabel, categoryBox, difficultyBox, typeBox, numberField, startButton, scoreLabel);
        layout.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10, titleLabel, categoryBox, difficultyBox, typeBox, numberField, startButton, scoreLabel);
        layout.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.show();
    }

    private void startQuiz() {
        try {
            int numQuestions;
            try {
                numQuestions = Integer.parseInt(numberField.getText());
                if (numQuestions < 1 || numQuestions > 50) {
                    showAlert("Please enter a number between 1 and 50.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid number of questions.");
                return;
            }

            String category = categoryBox.getValue();
            int categoryId = categoryMap.get(category);
            String difficulty = difficultyBox.getValue();
            String type = typeBox.getValue();

            Questions quizData = apiClient.fetchQuestions(numQuestions, categoryId, difficulty, type);

            if (quizData != null && quizData.getResults() != null) {
                score = 0;
                totalQuestions = quizData.getResults().size();
                displayQuestions(quizData);
            } else {
                showAlert("No questions retrieved.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayQuestions(Questions quizData) {
        this.quizData = quizData;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.totalQuestions = quizData.getResults().size();

        quizStage = new Stage();
        quizLayout = new VBox(20); // Μεγαλύτερο spacing για καλύτερη εμφάνιση
        quizLayout.setAlignment(Pos.CENTER); // Κεντράρισμα στοιχείων

        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(400);
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;"); // Στυλ ερώτησης

        group = new ToggleGroup();
        optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER); // Κεντράρισμα επιλογών

        nextButton = new Button("Next");
        nextButton.setDisable(true);
        nextButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        nextButton.setOnAction(e -> nextQuestion());

        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green; -fx-font-weight: bold;");

        quizLayout.getChildren().addAll(questionLabel, optionsBox, nextButton, scoreLabel);

        Scene scene = new Scene(quizLayout, 500, 400);
        quizStage.setScene(scene);
        quizStage.setTitle("Quiz Time!");
        quizStage.show();

        showQuestion();
    }

    private void showQuestion() {
        System.out.println("Current Question Index: " + currentQuestionIndex + " / " + totalQuestions);

        if (currentQuestionIndex >= totalQuestions) {
            endQuiz();
            return;
        }

        Result question = quizData.getResults().get(currentQuestionIndex);
        String questionText = StringEscapeUtils.unescapeHtml4(question.getQuestion());
        questionLabel.setText((currentQuestionIndex + 1) + ". " + questionText);

        group.getToggles().clear();
        optionsBox.getChildren().clear();

        RadioButton correctAnswer = new RadioButton(StringEscapeUtils.unescapeHtml4(question.getCorrectAnswer()));
        correctAnswer.setUserData(true);
        correctAnswer.setToggleGroup(group);
        correctAnswer.setStyle("-fx-font-size: 14px;");
        correctAnswer.setMaxWidth(Double.MAX_VALUE);
        optionsBox.getChildren().add(correctAnswer);

        for (String incorrect : question.getIncorrectAnswers()) {
            RadioButton option = new RadioButton(StringEscapeUtils.unescapeHtml4(incorrect));
            option.setUserData(false);
            option.setToggleGroup(group);
            option.setStyle("-fx-font-size: 14px;");
            option.setMaxWidth(Double.MAX_VALUE);
            optionsBox.getChildren().add(option);
        }

        nextButton.setDisable(true);
        for (javafx.scene.Node node : optionsBox.getChildren()) {
            if (node instanceof RadioButton) {
                ((RadioButton) node).setOnAction(e -> nextButton.setDisable(false));
            }
        }
    }

    private void nextQuestion() {
        // Αν έχουμε φτάσει στην τελευταία ερώτηση, καλούμε το endQuiz() και σταματάμε
        if (currentQuestionIndex >= totalQuestions - 1) {
            endQuiz();
            return;
        }

        // Ελέγχουμε αν υπάρχει επιλεγμένη απάντηση
        RadioButton selected = (RadioButton) optionsBox.getChildren()
                .stream()
                .filter(node -> node instanceof RadioButton && ((RadioButton) node).isSelected())
                .map(node -> (RadioButton) node)
                .findFirst()
                .orElse(null);

        if (selected != null && (boolean) selected.getUserData()) {
            score++;
        }

        scoreLabel.setText("Score: " + score + "/" + totalQuestions);

        // Αυξάνουμε το currentQuestionIndex **μόνο αν υπάρχουν κι άλλες ερωτήσεις**
        currentQuestionIndex++;

        // Αν υπάρχουν ακόμα ερωτήσεις, δείξε την επόμενη, αλλιώς τελείωσε το παιχνίδι
        if (currentQuestionIndex < totalQuestions) {
            showQuestion();
        } else {
            endQuiz();  // Καλούμε το endQuiz() αν τελειώσαμε όλες τις ερωτήσεις
        }
    }

    private void endQuiz() {
        System.out.println("End of quiz! Final Score: " + score + "/" + totalQuestions);

        // Σιγουρευόμαστε ότι χρησιμοποιούμε το σωστό layout
        quizLayout.getChildren().clear();

        double successRate = (totalQuestions > 0) ? ((double) score / totalQuestions) * 100 : 0;
        gamesPlayed++;
        totalScore += score;

        Label endMessage = new Label(
                "Quiz Over!\n" +
                        "Your score: " + score + "/" + totalQuestions + "\n" +
                        "Success rate: " + String.format("%.2f", successRate) + "%\n" +
                        "Total score across games: " + totalScore + "\n" +
                        "Games played: " + gamesPlayed
        );
        endMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: blue;");
        endMessage.setAlignment(Pos.CENTER);

        Button restartButton = new Button("Restart");
        restartButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        restartButton.setOnAction(e -> {
            quizStage.close();  // Κλείνουμε το quiz window
            start(primaryStage); // Επιστροφή στην αρχική οθόνη
        });

        quizLayout.getChildren().addAll(endMessage, restartButton);
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
