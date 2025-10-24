/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.josiahloomismovierental.business;

/**
 *
 * @author Imagine
 */
public class Movie {
    private long movieId;
    private String title;
    private String genre;
    private int year;
    private int duration;
    private String rating;
    private int availableCopies;
    private int timesRented = 0;
    
    // Constructor without movieId (for new movies)
    public Movie(String title, String genre, int year, int duration, String rating, int availableCopies, int timesRented) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.availableCopies = availableCopies;
        this.timesRented = timesRented;
    }
    
    // Constructor with movieId (for movies from database)
    public Movie(long movieId, String title, String genre, int year, int duration, String rating, int availableCopies, int timesRented) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.availableCopies = availableCopies;
        this.timesRented = timesRented;
    }
    
    // Getters
    public long getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public int getDuration() { return duration; }
    public String getRating() { return rating; }
    public int getAvailableCopies() { return availableCopies; }
    public int getTimesRented() { return timesRented; }
    
    // Setters
    public void setMovieId(long movieId) { this.movieId = movieId; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setYear(int year) { this.year = year; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setRating(String rating) { this.rating = rating; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    public void setTimesRented(int timesRented) { this.timesRented = timesRented; }

}
