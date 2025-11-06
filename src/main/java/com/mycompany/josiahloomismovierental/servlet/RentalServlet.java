/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.josiahloomismovierental.servlet;

import com.mycompany.josiahloomismovierental.business.Movie;
import com.mycompany.josiahloomismovierental.business.Rental;
import com.mycompany.josiahloomismovierental.business.Recommendation;
import com.mycompany.josiahloomismovierental.data.MovieRentalDb;


import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.Date;
import java.util.Calendar;

@WebServlet(name = "RentalServlet", urlPatterns = {"/index", ""})
public class RentalServlet extends HttpServlet {

    long UserId = 1;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        getData(request, response);
        
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String rentalIdStr;
        int rentalId;
        
        switch (action) {
        case "rent":
            String movieIdStr = request.getParameter("movieId");
            int movieId = Integer.parseInt(movieIdStr);
            rentMovie(movieId);
            break;
            
        case "return":
            rentalIdStr = request.getParameter("rentalId");
            rentalId = Integer.parseInt(rentalIdStr);
            returnMovie(rentalId);
            break;
            
        case "add-coppie":
            movieIdStr = request.getParameter("movieId");
            movieId = Integer.parseInt(movieIdStr);
            addCoppie(movieId);
            break;
            
        case "remove-coppie":
            movieIdStr = request.getParameter("movieId");
            movieId = Integer.parseInt(movieIdStr);
            removeCoppie(movieId);
            break;
            
        case "delete-movie":
            movieIdStr = request.getParameter("movieId");
            movieId = Integer.parseInt(movieIdStr);
            deleteMovie(movieId);
            break;
            
        case "add-movie":
            response.sendRedirect(request.getContextPath() + "/add_movie");
            return;
        }
        
        doGet(request, response);
    }

    void getData(HttpServletRequest request, HttpServletResponse response) {
        
        List<Movie> movies = MovieRentalDb.selectAllMovies();
        request.setAttribute("movies", movies);
        
        List<Recommendation> recommendations = MovieRentalDb.getRecommendationsByUserId(UserId);
        request.setAttribute("recommendations", recommendations);
        
        List<Rental> curRentals = MovieRentalDb.getActiveRentalsByUser(UserId);
        request.setAttribute("curRentals", curRentals);
        
        List<Rental> pastRentals = MovieRentalDb.getReturnedRentalsByUser(UserId);
        request.setAttribute("pastRentals", pastRentals);
    }
    
    private void rentMovie(long movieId) {
        MovieRentalDb.updateMovieAvailability(movieId, -1, true);
        
        Movie movie = MovieRentalDb.selectMovieById(movieId);
        
        Date today = new Date(System.currentTimeMillis());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 14);

        Date dueDate = new Date(cal.getTimeInMillis());
        
        Rental rental = new Rental(0, movie, UserId, today, dueDate, null);
        
        MovieRentalDb.addRental(rental);
    }
    
    private void returnMovie(long rentalId) {
        Rental rental = MovieRentalDb.getRentalById(rentalId);
        
        MovieRentalDb.updateMovieAvailability(rental.getMovie().getMovieId(), 1, false);
        
        Date today = new Date(System.currentTimeMillis());
        
        MovieRentalDb.updateReturnDate(rentalId, today);
    }
    
    private void addCoppie(long movieId) {
        MovieRentalDb.updateMovieAvailability(movieId, 1, false);
    }
    
    private void removeCoppie(long movieId) {
        MovieRentalDb.updateMovieAvailability(movieId, -1, false);
    }
    
    private void deleteMovie(long movieId) {
        MovieRentalDb.deleteMovie(movieId);
    }
}
