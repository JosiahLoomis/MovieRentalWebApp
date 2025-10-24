/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.josiahloomismovierental.business;

/**
 *
 * @author Imagine
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
