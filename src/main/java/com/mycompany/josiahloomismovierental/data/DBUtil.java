package com.mycompany.josiahloomismovierental.data;

import java.sql.*;

/**
 * Class that provides helper methods for safely closing database resources.
 * 
 * @author Josiah Loomis
 * Date: November 27, 2024
 * Course: Java II
 * Final Project - Movie Rental Web App
 */
public class DBUtil {

    public static void closeStatement(Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void closePreparedStatement(Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
