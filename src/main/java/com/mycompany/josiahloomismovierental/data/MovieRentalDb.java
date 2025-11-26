package com.mycompany.josiahloomismovierental.data;

import com.mycompany.josiahloomismovierental.business.Movie;
import com.mycompany.josiahloomismovierental.business.Recommendation;
import com.mycompany.josiahloomismovierental.business.Rental;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRentalDb {
    
    // Insert a new movie
    public static int insertMovie(Movie movie) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO Movie (title, genre, releaseYear, durationInMinutes, rating, availableCopies, timesRented) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getYear());  // Use setInt instead of setString
            ps.setInt(4, movie.getDuration());
            ps.setString(5, movie.getRating());
            ps.setInt(6, movie.getAvailableCopies());
            ps.setInt(7, movie.getTimesRented());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    // Delete a movie record
    public static int deleteMovie(long movieId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "DELETE FROM Movie WHERE movieId = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, movieId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in deleteMovie(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    // Select all movies
    public static List<Movie> selectAllMovies() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Movie";  // Fixed: was "Checkout"
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getLong("movieId"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("releaseYear"),
                    rs.getInt("durationInMinutes"),
                    rs.getString("rating"),
                    rs.getInt("availableCopies"),
                    rs.getInt("timesRented")
                );
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            System.out.println("SQL Exception in selectAllMovies(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    // Select a movie by ID
    public static Movie selectMovieById(Long movieId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Movie WHERE movieId = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, movieId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Movie movie = new Movie(
                    rs.getLong("movieId"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("releaseYear"),
                    rs.getInt("durationInMinutes"),
                    rs.getString("rating"),
                    rs.getInt("availableCopies"),
                    rs.getInt("timesRented")
                );
                return movie;
            } else {
                return null; // Movie not found
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception in selectMovieById(): " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    // Update movie availability (when rented/returned)
    public static int updateMovieAvailability(long movieId, int change, boolean updateTimesRented) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query;
    
        if (updateTimesRented) {
            query = "UPDATE Movie " +
                "SET availableCopies = availableCopies + ?, " +
                "timesRented = timesRented + 1 " +
                "WHERE movieId = ?";
        } else {
            query = "UPDATE Movie " +
                "SET availableCopies = availableCopies + ? " +
                "WHERE movieId = ?";
        }
    
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, change);
            ps.setLong(2, movieId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Movie not found or update failed for movieId: " + movieId);
            }
            return rowsUpdated;
        } catch (SQLException e) {
            System.out.println("SQL Exception in updateMovieAvailability(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Get all recommendations by user 
    public static List<Recommendation> getRecommendationsByUserId(long userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Step 1: Find the most common genre from user's rental history
            String genreQuery = "SELECT m.genre, COUNT(*) as genreCount " +
                              "FROM Rental ren " +
                              "JOIN Movie m ON ren.movieId = m.movieId " +
                              "WHERE ren.userId = ? " +
                              "GROUP BY m.genre " +
                              "ORDER BY genreCount DESC " +
                              "LIMIT 1";

            ps = connection.prepareStatement(genreQuery);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            String favoriteGenre = null;
            if (rs.next()) {
                favoriteGenre = rs.getString("genre");
            }
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);

            List<Movie> recommendedMovies = new ArrayList<>();

            if (favoriteGenre != null) {
                // Step 2: Get up to 3 movies in that genre that the user hasn't rented
                String movieQuery = "SELECT m.movieId, m.title, m.genre, m.releaseYear, " +
                                  "m.durationInMinutes, m.rating, m.availableCopies, m.timesRented " +
                                  "FROM Movie m " +
                                  "WHERE m.genre = ? " +
                                  "AND m.movieId NOT IN (" +
                                  "    SELECT movieId FROM Rental WHERE userId = ?" +
                                  ") " +
                                  "ORDER BY m.timesRented DESC " +
                                  "LIMIT 3";

                ps = connection.prepareStatement(movieQuery);
                ps.setString(1, favoriteGenre);
                ps.setLong(2, userId);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Movie movie = new Movie(
                        rs.getLong("movieId"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("releaseYear"),
                        rs.getInt("durationInMinutes"),
                        rs.getString("rating"),
                        rs.getInt("availableCopies"),
                        rs.getInt("timesRented")
                    );
                    recommendedMovies.add(movie);
                }
                DBUtil.closeResultSet(rs);
                DBUtil.closePreparedStatement(ps);
            }

            // Step 3: If we don't have 3 movies yet, fill with random unrented movies
            if (recommendedMovies.size() < 3) {
                int needed = 3 - recommendedMovies.size();

                String randomQuery = "SELECT m.movieId, m.title, m.genre, m.releaseYear, " +
                                   "m.durationInMinutes, m.rating, m.availableCopies, m.timesRented " +
                                   "FROM Movie m " +
                                   "WHERE m.movieId NOT IN (" +
                                   "    SELECT movieId FROM Rental WHERE userId = ?" +
                                   ") ";

                // Exclude already recommended movies
                if (!recommendedMovies.isEmpty()) {
                    randomQuery += "AND m.movieId NOT IN (" +
                                 recommendedMovies.stream()
                                     .map(m -> String.valueOf(m.getMovieId()))
                                     .collect(java.util.stream.Collectors.joining(",")) +
                                 ") ";
                }

                randomQuery += "ORDER BY RAND() LIMIT ?";

                ps = connection.prepareStatement(randomQuery);
                ps.setLong(1, userId);
                ps.setInt(2, needed);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Movie movie = new Movie(
                        rs.getLong("movieId"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("releaseYear"),
                        rs.getInt("durationInMinutes"),
                        rs.getString("rating"),
                        rs.getInt("availableCopies"),
                        rs.getInt("timesRented")
                    );
                    recommendedMovies.add(movie);
                }
            }

            // Step 4: Convert Movie list to Recommendation list
            List<Recommendation> recommendations = new ArrayList<>();

            for (Movie movie : recommendedMovies) {
                // Give each movie a personalized reason based on whether it matches favorite genre
                String reason;
                if (favoriteGenre != null && movie.getGenre().equalsIgnoreCase(favoriteGenre)) {
                    reason = "Based on your love of " + favoriteGenre + " movies";
                } else {
                    reason = "Popular movie you might enjoy";
                }

                Recommendation rec = new Recommendation(
                    0L, // recommendationId can be 0 for dynamic recommendations
                    movie,
                    userId,
                    reason
                );
                recommendations.add(rec);
            }

            return recommendations;

        } catch (SQLException e) {
            System.out.println("SQL Exception in getRecommendationsByUserId(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Insert a new rental
    public static int addRental(Rental rental) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO Rental (movieId, userId, rentalDate, dueDate, dateReturned) VALUES (?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, rental.getMovie().getMovieId());
            ps.setLong(2, rental.getUserId());
            ps.setDate(3, rental.getRentalDate());
            ps.setDate(4, rental.getDueDate());
            ps.setDate(5, rental.getDateReturned()); // can be null
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in addRental(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Delete a rental record
    public static int deleteRental(long rentalId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "DELETE FROM Rental WHERE rentalId = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, rentalId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in deleteRental(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Get all rentals by user that have been returned
    public static List<Rental> getReturnedRentalsByUser(long userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT r.*, m.* "
                     + "FROM Rental r "
                     + "JOIN Movie m ON r.movieId = m.movieId "
                     + "WHERE r.userId = ? AND r.dateReturned IS NOT NULL";

        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            List<Rental> rentals = new ArrayList<>();

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getLong("movieId"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("releaseYear"),
                    rs.getInt("durationInMinutes"),
                    rs.getString("rating"),
                    rs.getInt("availableCopies"),
                    rs.getInt("timesRented")
                );

                Rental rental = new Rental(
                    rs.getLong("rentalId"),
                    movie,
                    rs.getLong("userId"),
                    rs.getDate("rentalDate"),
                    rs.getDate("dueDate"),
                    rs.getDate("dateReturned")
                );

                rentals.add(rental);
            }

            return rentals;
        } catch (SQLException e) {
            System.out.println("SQL Exception in getReturnedRentalsByUser(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Get all rentals by user that have NOT been returned
    public static List<Rental> getActiveRentalsByUser(long userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT r.*, m.* "
                     + "FROM Rental r "
                     + "JOIN Movie m ON r.movieId = m.movieId "
                     + "WHERE r.userId = ? AND r.dateReturned IS NULL";

        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            List<Rental> rentals = new ArrayList<>();

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getLong("movieId"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("releaseYear"),
                    rs.getInt("durationInMinutes"),
                    rs.getString("rating"),
                    rs.getInt("availableCopies"),
                    rs.getInt("timesRented")
                );

                Rental rental = new Rental(
                    rs.getLong("rentalId"),
                    movie,
                    rs.getLong("userId"),
                    rs.getDate("rentalDate"),
                    rs.getDate("dueDate"),
                    rs.getDate("dateReturned")
                );

                rentals.add(rental);
            }

            return rentals;
        } catch (SQLException e) {
            System.out.println("SQL Exception in getActiveRentalsByUser(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        } 
    }
    
    // Get a rental by rental id
    public static Rental getRentalById(long rentalId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT r.*, m.* "
                     + "FROM Rental r "
                     + "JOIN Movie m ON r.movieId = m.movieId "
                     + "WHERE r.rentalId = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, rentalId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Movie movie = new Movie(
                    rs.getLong("movieId"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("releaseYear"),
                    rs.getInt("durationInMinutes"),
                    rs.getString("rating"),
                    rs.getInt("availableCopies"),
                    rs.getInt("timesRented")
                );
                Rental rental = new Rental(
                    rs.getLong("rentalId"),
                    movie,
                    rs.getLong("userId"),
                    rs.getDate("rentalDate"),
                    rs.getDate("dueDate"),
                    rs.getDate("dateReturned")
                );
                return rental;
            } else {
                return null; // Rental not found
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception in getRentalById(): " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    // Set the return date on a rental by id
    public static int updateReturnDate(long rentalId, java.sql.Date returnDate) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "UPDATE Rental SET dateReturned = ? WHERE rentalId = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setDate(1, returnDate);
            ps.setLong(2, rentalId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Rental not found for rentalId: " + rentalId);
            }
            return rowsUpdated;
        } catch (SQLException e) {
            System.out.println("SQL Exception in updateReturnDate(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static long getUserIdByUsername(String username) {
    ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
    String sql = "SELECT userId FROM User WHERE username = ?";
    try {
        ps = connection.prepareStatement(sql);
        
        ps.setString(1, username);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getLong("userId");
        }
    } catch (SQLException e) {
        System.err.println("Error getting user ID: " + e);
    }
        return -1;
    }
}
