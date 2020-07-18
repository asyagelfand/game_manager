package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.model.MockGame;
import com.example.demo.model.QuestionData;
import com.example.demo.repository.GameRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameServiceTest {

    private static GameService service;

    @BeforeClass
    public static void setUp() {
        GameRepository repository = new GameRepository(2, 2, false);
        Game game1 = MockGame.getMockGame();
        Game game2 = MockGame.getMockGame();
        repository.getGames().put(0, game1);
        repository.getGames().put(1, game2);
        service = new GameService(repository);
    }

    @Test
    public void testGetNextQuestionWrongInput() {
        QuestionData result = service.getNextQuestion(2, -1);
        Assert.assertNull(result);
    }

    @Test
    public void testGetNextQuestionCorrectInput() {
        QuestionData result = service.getNextQuestion(0, -1);
        Assert.assertNotNull(result);
    }
}