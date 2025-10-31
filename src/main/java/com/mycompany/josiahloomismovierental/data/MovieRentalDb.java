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
    
    // Insert a new recommendation
    public static int addRecommendation(Recommendation recommendation) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "INSERT INTO Recommendation (movieId, userId, reason) VALUES (?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, recommendation.getMovie().getMovieId());
            ps.setLong(2, recommendation.getUserId());
            ps.setString(3, recommendation.getReason());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in addRecommendation(): " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // Delete a rental recommendation
    public static int deleteRecommendation(long recommendationId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query = "DELETE FROM Recommendation WHERE recommendationId = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, recommendationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in deleteRecommendation(): " + e.getMessage());
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

        String query = "SELECT r.recommendationId, r.movieId, r.userId, r.reason, "
                     + "m.title, m.genre, m.releaseYear, m.durationInMinutes, m.rating, "
                     + "m.availableCopies, m.timesRented "
                     + "FROM Recommendation r "
                     + "JOIN Movie m ON r.movieId = m.movieId "
                     + "WHERE r.userId = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            List<Recommendation> recommendations = new ArrayList<>();

            while (rs.next()) {
                // Create the Movie object with all its data
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

                // Create Recommendation object with the Movie inside
                Recommendation rec = new Recommendation(
                    rs.getLong("recommendationId"),
                    movie,
                    rs.getLong("userId"),
                    rs.getString("reason")
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
}
