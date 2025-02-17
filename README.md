
```plaintext
🎨 Trivia Game - JavaFX UI

The Trivia Game APPUI is a JavaFX application that uses the API to fetch trivia questions from the Open Trivia DB API and display them in an interactive graphical interface.

🚀 Features
- Allows selection of category, difficulty, number of questions, and type (multiple choice / true-false).
- Graphical User Interface (GUI) built with JavaFX.
- Score management and result display.
- Communicates with the API via the `API.jar` file.

🛠️ Technologies & Dependencies
- Java 17
- Maven (for dependency management)
- JavaFX (for UI)
- API (API.jar) (for fetching questions)

📥 Installation & Execution

1️⃣ Clone the Repository
git clone https://github.com/VasoUnipi/APPUI.git
cd APPUI

2️⃣ Install Dependencies with Maven
mvn clean install

3️⃣ Run the Application
mvn javafx:run

-----------------------------------------------------------------------------------------------------------------------
📂 Project Structure

📁 APPUI

 ┣ 📂 src

 ┃ ┣ 📂 main

 ┃ ┃ ┣ 📂 java

 ┃ ┃ ┃ ┣ 📜 TriviaGameApp.java

 ┃ ┃ ┃ ┣ 📜 HelloController.java

 ┃ ┃ ┃ ┗ 📜 HelloApplication.java

 ┣ 📜 pom.xml

 ┣ 📜 README.md


-----------------------------------------------------------------------------------------------------------------------

📌 How to Use

Select category, difficulty, number of questions, and type.

Click "Start Quiz" to begin.

Answer the questions and proceed by clicking "Next".

View the final score and click "Restart" for a new quiz session.

-----------------------------------------------------------------------------------------------------------------------

🏆 Thank you for using the Trivia Game! 🎉
