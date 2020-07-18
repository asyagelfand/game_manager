package com.example.demo.model;

import java.util.Arrays;

public class MockGame {
    public static Game getMockGame() {
        Game game = new Game();

        QuestionData qData1 = new QuestionData(0);
        qData1.setQuestion("1 + 1");
        qData1.setPoints(1);
        qData1.setPossibleAnswers(Arrays.asList("2", "3"));
        qData1.setCorrectAnswerId(0);
        game.getQuestions().put(qData1.getQuestionId(), qData1);

        QuestionData qData2 = new QuestionData(1);
        qData2.setQuestion("1 - 1");
        qData2.setPoints(2);
        qData2.setPossibleAnswers(Arrays.asList("-1", "0"));
        qData2.setCorrectAnswerId(1);
        game.getQuestions().put(qData2.getQuestionId(), qData2);

        return game;
    }
}
