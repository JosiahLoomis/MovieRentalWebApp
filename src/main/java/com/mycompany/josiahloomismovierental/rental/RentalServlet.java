/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.josiahloomismovierental.rental;

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

//Temp classes will replace with data base later
@WebServlet(name = "RentalServlet", urlPatterns = {"/index", ""})
public class RentalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        getData(request, response, 2);
        
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    void getData(HttpServletRequest request, HttpServletResponse response, long UserId) {
        
        List<Movie> movies = MovieRentalDb.selectAllMovies();
        request.setAttribute("movies", movies);
        
        List<Recommendation> recommendations = MovieRentalDb.getRecommendationsByUserId(UserId);
        request.setAttribute("recommendations", recommendations);
        
        List<Rental> curRentals = MovieRentalDb.getActiveRentalsByUser(UserId);
        request.setAttribute("curRentals", curRentals);
        
        List<Rental> pastRentals = MovieRentalDb.getReturnedRentalsByUser(UserId);
        request.setAttribute("pastRentals", pastRentals);
    }
}
