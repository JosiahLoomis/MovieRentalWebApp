/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.josiahloomismovierental.servlet;

import com.mycompany.josiahloomismovierental.business.Movie;
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

@WebServlet(name = "EditServlet", urlPatterns = {"/edit"})
public class EditServlet extends HttpServlet {

    long UserId = 1;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        getData(request, response);
        
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String rentalIdStr;
        int rentalId;
        String movieIdStr;
        int movieId;
        
        switch (action) {
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
            response.sendRedirect(request.getContextPath() + "/edit/add");
            return;
        }
        
        doGet(request, response);
    }

    void getData(HttpServletRequest request, HttpServletResponse response) {
        
        List<Movie> movies = MovieRentalDb.selectAllMovies();
        request.setAttribute("movies", movies);
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
