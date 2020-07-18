package com.example.demo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Game {
    private final HashMap<String, Integer> leaderboard = new HashMap<>(); // key: userName, value: total points
    private final HashMap<Integer, QuestionData> questions = new HashMap<>(); // key: questionId, value: question data
    private final HashMap<String, List<Integer>> questionTracker = new HashMap<>(); // key: userName, value: questions answered
    private static final int DEFAULT_POINTS = 1;

    public void init(int questionsNumber) {
        try {
            URL url = new URL(String.format("https://opentdb.com/api.php?amount=%s&type=multiple", questionsNumber));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                System.out.println("Questions successfully fetched from external API");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject json = new JSONObject(sb.toString());
                Iterator<String> it = json.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    if (json.get(key) instanceof JSONArray) {
                        JSONArray arr = (JSONArray) json.get(key);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject q = arr.getJSONObject(i);
                            QuestionData qData = new QuestionData(i);
                            qData.setPoints(DEFAULT_POINTS);
                            qData.setQuestion(q.getString("question"));
                            List<String> answers = new ArrayList<>();
                            String correctAnswer = q.getString("correct_answer");
                            answers.add(correctAnswer);
                            JSONArray iAnswers = q.getJSONArray("incorrect_answers");
                            for (int j = 0; j < iAnswers.length(); j++) {
                                answers.add(iAnswers.getString(j));
                            }
                            answers.sort(Comparator.comparing(String::toString));
                            qData.setPossibleAnswers(answers);
                            qData.setCorrectAnswerId(answers.indexOf(correctAnswer));

                            this.questions.put(i, qData);
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public QuestionData getNextQuestion(int currQuestionId) {
        if ((currQuestionId < -1) || (currQuestionId > this.questions.size() - 2)) {
            return null;
        }
        return this.questions.get(currQuestionId + 1);
    }

    public AnswerStatus answerQuestion(UserAnswer answer) {
        // validate
        AnswerStatus status = new AnswerStatus();
        if (answer.getQuestionId() > this.questions.size() - 1) {
            System.out.println(String.format("User %s sent the answer for question that does't exist", answer.getUserName(), answer.getQuestionId()));
            return status;
        }
        List<Integer> questions = this.questionTracker.get(answer.getUserName());
        if (questions != null && questions.contains(answer.getQuestionId())) {
            System.out.println(String.format("User %s sent the answer for question %s repeatedly", answer.getUserName(), answer.getQuestionId()));
            return null;
        }
        // check answer
        QuestionData question = this.questions.get(answer.getQuestionId());
        Integer totalPoints = this.leaderboard.get(answer.getUserName());
        if (totalPoints == null) {
            totalPoints = 0;
        }
        if (question.getCorrectAnswerId() == answer.getAnswerId()) {
            status.setStatus(true);
            status.setPoints(question.getPoints());
            // update user earned points
            totalPoints += question.getPoints();
        }
        this.leaderboard.put(answer.getUserName(), totalPoints);
        // update questions tracker
        if (questions == null) {
            questions = new ArrayList<>();
            this.questionTracker.put(answer.getUserName(), questions);
        }
        questions.add(answer.getQuestionId());
        return status;
    }

    public HashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public HashMap<Integer, QuestionData> getQuestions() {
        return questions;
    }
}
