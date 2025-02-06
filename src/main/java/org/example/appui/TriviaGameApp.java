package org.example.appui;

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

    private ComboBox<String> categoryBox;
    private ComboBox<String> difficultyBox;
    private ComboBox<String> typeBox;
    private TextField numberField;
    private Label scoreLabel;
    private int score = 0;
    private int totalQuestions = 0;

    @Override
    public void start(Stage primaryStage) {
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

        VBox layout = new VBox(10, titleLabel, categoryBox, difficultyBox, typeBox, numberField, startButton, scoreLabel);
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
        Stage quizStage = new Stage();
        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 500, 400);

        for (Result question : quizData.getResults()) {
            Label questionLabel = new Label(question.getQuestion());
            ToggleGroup group = new ToggleGroup();

            RadioButton correctAnswer = new RadioButton(question.getCorrectAnswer());
            correctAnswer.setUserData(true);
            correctAnswer.setToggleGroup(group);

            VBox optionsBox = new VBox(5, correctAnswer);

            for (String incorrect : question.getIncorrectAnswers()) {
                RadioButton option = new RadioButton(incorrect);
                option.setUserData(false);
                option.setToggleGroup(group);
                optionsBox.getChildren().add(option);
            }

            Button submitButton = new Button("Submit");
            submitButton.setOnAction(e -> {
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                if (selected != null && (boolean) selected.getUserData()) {
                    score++;
                }
                scoreLabel.setText("Score: " + score + "/" + totalQuestions);
                optionsBox.setDisable(true);
                submitButton.setDisable(true);
            });

            layout.getChildren().addAll(questionLabel, optionsBox, submitButton);
        }

        quizStage.setScene(scene);
        quizStage.setTitle("Quiz Time!");
        quizStage.show();
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
