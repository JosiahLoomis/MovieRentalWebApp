package com.mycompany.josiahloomismovierental.business;

/**
 * Class that represents a movie recommendation having a reference to a movie, a reference to a person, and a reason for the recommendation.
 * 
 * @author Josiah Loomis
 * Date: November 27, 2024
 * Course: Java II
 * Final Project - Movie Rental Web App
 */
public class Recommendation {
    private long recommendationId;
    private Movie movie;
    private long userId;
    private String reason;
    
    public Recommendation(long recommendationId, Movie movie, long userId, String reason) {
        this.recommendationId = recommendationId;
        this.movie = movie;
        this.userId = userId;
        this.reason = reason;
    }
    
    // Getters
    public long getRecommendationId() { return recommendationId; }
    public Movie getMovie() { return movie; }
    public long getUserId() { return userId; }
    public String getReason() { return reason; }
}
