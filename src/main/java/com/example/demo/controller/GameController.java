package com.example.demo.controller;
import com.example.demo.model.AnswerStatus;
import com.example.demo.model.QuestionData;
import com.example.demo.model.UserAnswer;
import com.example.demo.service.GameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = "/getNextQuestion", produces = "application/json")
    @ApiOperation(value = "Return next question for game and current question specified")
    public ResponseEntity<QuestionData> getNextQuestion(@RequestParam(value = "gameId") int gameId, @RequestParam(value = "currQuestionId") int currQuestionId) {
        if (gameService.hasGame(gameId)) {
            QuestionData res = gameService.getNextQuestion(gameId, currQuestionId);
            if (res != null) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/answerQuestion", produces = "application/json")
    @ApiOperation(value = "Register user answer for user/game/question specified")
    public ResponseEntity<AnswerStatus> answerQuestion(@RequestBody UserAnswer request) {
        if (gameService.hasGame(request.getGameId())) {
            AnswerStatus res = gameService.registerUserAnswer(request);
            if (res != null) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/getLeaderboard", produces = "application/json")
    @ApiOperation(value = "Get leaderboard for the game specified")
    public ResponseEntity<HashMap<String, Integer>> getLeaderboard(@RequestParam(value = "gameId") int gameId) {
        if (gameService.hasGame(gameId)) {
            return new ResponseEntity<>(gameService.getLeaderboard(gameId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
