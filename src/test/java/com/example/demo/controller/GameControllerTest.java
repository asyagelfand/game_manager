package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.demo.model.UserAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameControllerTest {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    protected final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetNextQuestionWrongGameId() throws Exception {
        MvcResult result = this.mvc.perform(get("/getNextQuestion?gameId=5&currQuestionId=-1"))
                .andExpect(status().isBadRequest()).andReturn();
        Assert.assertEquals(result.getResponse().getStatus(), 400);
    }

    @Test
    public void testGetNextQuestionWrongQuestionId() throws Exception {
        MvcResult result = this.mvc.perform(get("/getNextQuestion?gameId=0&currQuestionId=5"))
                .andExpect(status().isBadRequest()).andReturn();
        Assert.assertEquals(result.getResponse().getStatus(), 400);
    }

    @Test
    public void testGetNextQuestionCorrectInput() throws Exception {
        MvcResult result = this.mvc.perform(get("/getNextQuestion?gameId=0&currQuestionId=-1"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotEquals(0, result.getResponse().getContentAsString().length());
    }

    @Test
    public void testAnswerQuestionWrongGameId() throws Exception {
        UserAnswer asnwer = new UserAnswer();
        asnwer.setAnswerId(0);
        asnwer.setQuestionId(0);
        asnwer.setGameId(5);
        asnwer.setUserName("user1");

        MvcResult result = this.mvc.perform(post("/answerQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(asnwer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assert.assertEquals(result.getResponse().getStatus(), 400);
    }

    @Test
    public void testAnswerQuestionDuplicateAnswer() throws Exception {
        UserAnswer asnwer = new UserAnswer();
        asnwer.setAnswerId(0);
        asnwer.setQuestionId(0);
        asnwer.setGameId(0);
        asnwer.setUserName("user1");

        this.mvc.perform(post("/answerQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(asnwer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        MvcResult result = this.mvc.perform(post("/answerQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(asnwer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assert.assertEquals(result.getResponse().getStatus(), 400);
    }

    @Test
    public void testAnswerQuestionCorrectInput() throws Exception {
        UserAnswer asnwer = new UserAnswer();
        asnwer.setAnswerId(0);
        asnwer.setQuestionId(0);
        asnwer.setGameId(1);
        asnwer.setUserName("user1");

        MvcResult result = this.mvc.perform(post("/answerQuestion").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(asnwer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotEquals(0, result.getResponse().getContentAsString().length());
    }

    @Test
    public void testGetLeaderboardWrongGameId() throws Exception {
        MvcResult result = this.mvc.perform(get("/getLeaderboard?gameId=5"))
                .andExpect(status().isBadRequest()).andReturn();
        Assert.assertEquals(result.getResponse().getStatus(), 400);
    }

    @Test
    public void testGetLeaderboardCorrectInput() throws Exception {
        MvcResult result = this.mvc.perform(get("/getLeaderboard?gameId=0"))
                .andExpect(status().isOk()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotEquals(0, result.getResponse().getContentAsString().length());
    }
}
