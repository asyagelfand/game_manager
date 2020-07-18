package com.example.demo.repository;

import com.example.demo.model.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class GameRepository {

    private final HashMap<Integer, Game> games = new HashMap<>(); // key: gameId, value: game data
    private final int gamesNumber;
    private final int questionsNumber;

    public GameRepository(@Value("${gamemanager.games-number}")  int gamesNumber, @Value("${gamemanager.questions-number}") int questionsNumber, @Value("${gamemanager.load-from-web}") boolean loadFromWeb) {
        this.gamesNumber = gamesNumber;
        this.questionsNumber = questionsNumber;
        if (loadFromWeb) {
            init();
        }
    }

    public void init() {
        // init games
        for (int i = 0; i < gamesNumber; i++) {
            Game game = new Game();
            this.games.put(i, game);
            game.init(questionsNumber);
        }
    }

    public HashMap<Integer, Game> getGames() {
        return this.games;
    }
}


