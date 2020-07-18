package com.example.demo.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameTest {

    private static Game game;

    @BeforeClass
    public static void setUp() {
        game = MockGame.getMockGame();
    }

    @Test
    public void testAnswerQuestionWrongInput() {
        UserAnswer answer = new UserAnswer();
        answer.setUserName("user1");
        answer.setQuestionId(2);
        answer.setAnswerId(0);
        AnswerStatus status = game.answerQuestion(answer);
        Assert.assertNotNull(status);
        Assert.assertEquals(0, status.getPoints());
        Assert.assertFalse(status.isStatus());
    }

    @Test
    public void testAnswerQuestionWrongAnswer() {
        UserAnswer answer = new UserAnswer();
        answer.setUserName("user1");
        answer.setQuestionId(0);
        answer.setAnswerId(1);
        AnswerStatus status = game.answerQuestion(answer);
        Assert.assertNotNull(status);
        Assert.assertEquals(0, status.getPoints());
        Assert.assertFalse(status.isStatus());
        Assert.assertEquals(0, (int)game.getLeaderboard().get("user1"));
    }

    @Test
    public void testAnswerQuestionCorrectAnswer() {
        UserAnswer answer = new UserAnswer();
        answer.setUserName("user2");
        answer.setQuestionId(1);
        answer.setAnswerId(1);
        AnswerStatus status = game.answerQuestion(answer);
        Assert.assertNotNull(status);
        Assert.assertEquals(2, status.getPoints());
        Assert.assertTrue(status.isStatus());
        Assert.assertEquals(2, (int)game.getLeaderboard().get("user2"));
    }

    @Test
    public void testAnswerQuestionDuplicateAnswer() {
        UserAnswer answer = new UserAnswer();
        answer.setUserName("user3");
        answer.setQuestionId(0);
        answer.setAnswerId(1);
        game.answerQuestion(answer);

        answer.setAnswerId(0);
        AnswerStatus status = game.answerQuestion(answer);
        Assert.assertNull(status);
        Assert.assertEquals(0, (int)game.getLeaderboard().get("user3"));
    }

    @Test
    public void testGetNextQuestionWrongInput() {
        QuestionData result = game.getNextQuestion(2);
        Assert.assertNull(result);
    }

    @Test
    public void testGetNextQuestionCorrectInput() {
        QuestionData result = game.getNextQuestion(-1);
        Assert.assertNotNull(result);
    }
}
