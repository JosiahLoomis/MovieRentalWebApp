/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 * Servlet for the add.jsp which is responsible for adding new movies to the database.
 * 
 * @author Josiah Loomis
 * Date: November 27, 2024
 * Course: Java II
 * Final Project - Movie Rental Web App
 */
@WebServlet(name = "AddMovieServlet", urlPatterns = {"/edit/add"})
public class AddMovieServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/add.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String releaseYearStr = request.getParameter("releaseYear");
        String durationStr = request.getParameter("duration");
        String rating = request.getParameter("rating");
        String copiesStr = request.getParameter("copies");
        
        int releaseYear = Integer.parseInt(releaseYearStr);
        int duration = Integer.parseInt(durationStr);
        int copies = Integer.parseInt(copiesStr);
        
        Movie movie = new Movie(title, genre, releaseYear, duration, rating, copies, 0);
        
        MovieRentalDb.insertMovie(movie);
        
        response.sendRedirect(request.getContextPath() + "/edit");
        return;
    }
}
