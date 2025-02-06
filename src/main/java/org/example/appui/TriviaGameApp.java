package org.example.appui;

import org.example.TriviaApiClient;
import org.example.Questions;
import org.example.Result;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TriviaGameApp extends Application {

    private TriviaApiClient apiClient = new TriviaApiClient();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Trivia Game");

        Label titleLabel = new Label("Select Quiz Options");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("General Knowledge", "Science", "History");
        categoryBox.setValue("General Knowledge");

        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("easy", "medium", "hard");
        difficultyBox.setValue("medium");

        Button startButton = new Button("Start Quiz");
        startButton.setOnAction(e -> startQuiz());

        VBox layout = new VBox(10, titleLabel, categoryBox, difficultyBox, startButton);
        primaryStage.setScene(new Scene(layout, 300, 200));
        primaryStage.show();
    }

    private void startQuiz() {
        try {
            Questions example = apiClient.fetchQuestions();
            if (example != null && example.getResults() != null) {
                for (Result question : example.getResults()) {
                    System.out.println("Question: " + question.getQuestion());
                    System.out.println("Correct Answer: " + question.getCorrectAnswer());
                    System.out.println("Incorrect Answers: " + question.getIncorrectAnswers());
                }
            } else {
                System.out.println("No questions retrieved.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
