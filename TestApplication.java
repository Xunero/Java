import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Question {
    private int questionNumber;
    private String questionContent;
    private List<String> options;
    private int correctOption;

    public Question(int questionNumber, String questionContent, List<String> options, int correctOption) {
        this.questionNumber = questionNumber;
        this.questionContent = questionContent;
        this.options = options;
        this.correctOption = correctOption;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }
}

class Test implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private List<Integer> selectedAnswers;

    public Test(String userId) {
        this.userId = userId;
        selectedAnswers = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void addAnswer(int answer) {
        selectedAnswers.add(answer);
    }
}

public class TestApplication extends JFrame implements ActionListener {
    private List<Question> questions;
    private int currentQuestionIndex;
    private JLabel questionLabel;
    private List<JRadioButton> optionButtons;
    private JButton nextButton;
    private JButton checkResultsButton;
    private Test currentTest;
    private static final String TEST_RESULTS_FILE = "test_results.bin";
    private static final String ADMIN_PASSWORD = "Admin"; // Hasło administratora

    public TestApplication(List<Question> questions) {
        this.questions = questions;
        currentQuestionIndex = 0;
        currentTest = null;

        setTitle("Test Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        questionLabel = new JLabel();
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1));
        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JRadioButton button = new JRadioButton();
            button.addActionListener(this);
            optionsPanel.add(button);
            optionButtons.add(button);
        }
        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);

        checkResultsButton = new JButton("Check Results");
        checkResultsButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        buttonPanel.add(checkResultsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displayQuestion();
    }

    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionLabel.setText(question.getQuestionContent());
        List<String> options = question.getOptions();
        Collections.shuffle(options);
        for (int i = 0; i < 4; i++) {
            optionButtons.get(i).setText(options.get(i));
            optionButtons.get(i).setEnabled(true);
        }
        if (currentTest != null && currentTest.getSelectedAnswers().size() > currentQuestionIndex) {
            optionButtons.get(currentTest.getSelectedAnswers().get(currentQuestionIndex)).setSelected(true);
        } else {
            clearSelection();
        }
    }

    private void clearSelection() {
        for (JRadioButton button : optionButtons) {
            button.setSelected(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            JRadioButton selectedButton = (JRadioButton) e.getSource();
            for (int i = 0; i < 4; i++) {
                if (selectedButton == optionButtons.get(i)) {
                    if (currentTest == null) {
                        currentTest = new Test(getUserId());
                    }
                    clearSelectionExcept(i);
                    currentTest.addAnswer(i);
                    break;
                }
            }
        } else if (e.getSource() == nextButton) {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                saveTestResults();
                JOptionPane.showMessageDialog(this, "Test completed!");
                dispose();
            }
        } else if (e.getSource() == checkResultsButton) {
            String password = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if (password != null && password.equals(ADMIN_PASSWORD)) {
                displayResults();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Password!");
            }
        }
    }

    private void saveTestResults() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(TEST_RESULTS_FILE))) {
            outputStream.writeObject(currentTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayResults() {
        if (currentTest == null) {
            JOptionPane.showMessageDialog(this, "No test results available.");
            return;
        }
    
        int correctAnswers = 0;
        int totalQuestions = questions.size() - 1; // excluding the last line
        int passThreshold = questions.get(totalQuestions).getQuestionNumber();
        char result;
        List<Integer> selectedAnswers = currentTest.getSelectedAnswers();
        for (int i = 0; i < totalQuestions; i++) {
            if (i < selectedAnswers.size() && questions.get(i).getCorrectOption() == selectedAnswers.get(i)) {
                correctAnswers++;
            }
        }
        if (correctAnswers >= passThreshold) {
            result = 'P'; // Passed
        } else {
            result = 'F'; // Failed
        }
    
        double percentage = (double) correctAnswers / totalQuestions * 100;
        JOptionPane.showMessageDialog(this, "Total Questions: " + totalQuestions +
                "\nCorrect Answers: " + correctAnswers +
                "\nPercentage: " + percentage + "%" +
                "\nResult: " + result);
    }

    private String getUserId() {
        return JOptionPane.showInputDialog(this, "Enter your unique login (identifier):");
    }

    public static void main(String[] args) {
        List<Question> questions = loadQuestionsFromFile("questions.txt");
        if (questions != null && !questions.isEmpty()) {
            TestApplication testApp = new TestApplication(questions);
            testApp.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error loading questions!");
        }
    }

    private static List<Question> loadQuestionsFromFile(String fileName) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int questionNumber = Integer.parseInt(parts[0].trim());
                String questionContent = parts[1].trim();
                List<String> options = new ArrayList<>();
                for (int i = 2; i < 6; i++) {
                    options.add(parts[i].trim());
                }
                int correctOption = Integer.parseInt(parts[6].trim());
                questions.add(new Question(questionNumber, questionContent, options, correctOption));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Collections.shuffle(questions);
        return questions;
    }

    private void clearSelectionExcept(int indexToKeep) {
        for (int i = 0; i < 4; i++) {
            if (i != indexToKeep) {
                optionButtons.get(i).setSelected(false);
            }
        }
    }
}
