package org.example.appui;

import org.example.DemoGetRESTAPI;
import org.example.Example;
import org.example.Result;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TriviaGameApp extends Application {

    private DemoGetRESTAPI apiClient = new DemoGetRESTAPI();

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
            Example example = apiClient.fetchResult();
            if (example != null && example.getResults() != null) {
                for (Result question : example.getResults()) {
                    System.out.println("Question: " + question.getQuestion());
                    System.out.println("Correct Answer: " + question.getCorrect_answer());
                    System.out.println("Incorrect Answers: " + question.getIncorrect_answers());
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
