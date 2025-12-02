package com.mycompany.josiahloomismovierental.business;

import java.sql.Date;
/**
 * Class that represents a movie rental having a reference to a movie, a reference to a person, and variables for rental, due, and returned date.
 * 
 * @author Josiah Loomis
 * Date: November 27, 2024
 * Course: Java II
 * Final Project - Movie Rental Web App
 */
public class Rental {
    private long rentalId;
    private Movie movie;        
    private long userId;
    private Date rentalDate;
    private Date dueDate;
    private Date dateReturned;  // null = not returned yet

    public Rental() {}

    public Rental(long rentalId, Movie movie, long userId, Date rentalDate, Date dueDate, Date dateReturned) {
        this.rentalId = rentalId;
        this.movie = movie;
        this.userId = userId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.dateReturned = dateReturned;
    }

    // Getters and Setters
    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public Date getRentalDate() { return rentalDate; }
    public void setRentalDate(Date rentalDate) { this.rentalDate = rentalDate; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getDateReturned() { return dateReturned; }
    public void setDateReturned(Date dateReturned) { this.dateReturned = dateReturned; }

    public boolean isReturned() {
        return dateReturned != null;
    }
}
