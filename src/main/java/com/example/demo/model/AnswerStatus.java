package com.example.demo.model;

public class AnswerStatus {

    private boolean status = false;

    private int points = 0;

    public boolean isStatus() {
        return status;
    }

    public int getPoints() {
        return points;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
