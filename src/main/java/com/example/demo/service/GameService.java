package com.example.demo.service;

import com.example.demo.model.AnswerStatus;
import com.example.demo.model.QuestionData;
import com.example.demo.model.UserAnswer;
import com.example.demo.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public boolean hasGame(int gameId) {
        return gameId < this.repository.getGames().size();
    }

    public QuestionData getNextQuestion(int gameId, int currQuestionId) {
        if (!this.hasGame(gameId)) {
            return null;
        }
        return this.repository.getGames().get(gameId).getNextQuestion(currQuestionId);
    }

    public AnswerStatus registerUserAnswer(UserAnswer answer) {
        if (!this.hasGame(answer.getGameId())) {
            return null;
        }
        return this.repository.getGames().get(answer.getGameId()).answerQuestion(answer);
    }

    public HashMap<String, Integer> getLeaderboard(int gameId) {
        if (!this.hasGame(gameId)) {
            return null;
        }
        HashMap<String, Integer> leaderboard = this.repository.getGames().get(gameId).getLeaderboard();

        return leaderboard.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
    }
}
