package org.lessons.java.bonus;

import java.sql.*;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost:3306/db_nations";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection connect() throws SQLException{
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchCountries(String userInputString){
        try {
            Integer.parseInt(userInputString);
            throw new IllegalArgumentException("È stato inserito un numero, ma era attesa una stringa.");
        } catch (NumberFormatException e) {
            // Se la conversione fallisce, significa che l'input non è un intero, quindi va bene
        }
        String sqlQuery = "SELECT DISTINCT c.name as country_name,c.country_id, r.name as region_name,c2.name as continent_name\n" +
                "FROM countries c\n" +
                "JOIN regions r ON c.region_id = r.region_id\n" +
                "JOIN continents c2 ON r.continent_id = c2.continent_id\n" +
                "WHERE c.name LIKE ?" +
                "ORDER BY c.name;";

        // la connection prepara uno statement sql
        try (PreparedStatement preparedStatement = connect().prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, "%" + userInputString + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new DbException("Nessun paese trovato con la ricerca effettuata.");
                }
                while (resultSet.next()) {
                    System.out.println("Country name: " + resultSet.getString("country_name") + " -" +
                            " Country ID: " + resultSet.getInt("country_id") + " -" +
                            " Region Name: " + resultSet.getString("region_name") + " -" +
                            " Continent Name: " + resultSet.getString("region_name"));
                }
            } catch (SQLException e) {
                throw new DbException("Query Execution Unable to execute query for searching countries by name", e);
            }
        } catch (SQLException e) {
            throw new DbException("Connection Close Error closing the database connection", e);
        }
    }

    public void searchLanguage(int userInputInt){
        String sqlCountrySearchID = "SELECT l.language " +
                "FROM languages l " +
                "JOIN country_languages cl ON l.language_id = cl.language_id " +
                "WHERE cl.country_id = ?";
        try (PreparedStatement langStatement = connect().prepareStatement(sqlCountrySearchID)) {
            langStatement.setInt(1, userInputInt);

            try (ResultSet langResultSet = langStatement.executeQuery()) {
                System.out.println("Lingue parlate:");

                while (langResultSet.next()) {
                    System.out.println("- " + langResultSet.getString("language"));
                }

            } catch (SQLException e) {

                throw new DbException("Unable to execute languages query", e);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public void searchStatistic(int userInputInt){
        String sqlCountryStats =
                "SELECT * " +
                        "FROM country_stats " +
                        "WHERE country_id = ? " +
                        "ORDER BY year DESC " ;

        try (PreparedStatement statsStatement = connect().prepareStatement(sqlCountryStats)) {
            statsStatement.setInt(1, userInputInt);

            try (ResultSet statsResultSet = statsStatement.executeQuery()) {
                if (statsResultSet.next()) {
                    int year = statsResultSet.getInt("year");
                    double population = statsResultSet.getDouble("population");
                    double gdp = statsResultSet.getDouble("gdp");

                    System.out.println("Statistiche più recenti per il paese ID " + userInputInt + ":");
                    System.out.println("Anno: " + year);
                    System.out.println("Popolazione: " + String.format("%,.0f", population));
                    System.out.println("GDP: " + String.format("%,.0f", gdp));

                } else {
                    System.out.println("Non ci sono statistiche disponibili per il paese ID " + userInputInt);
                }
            } catch (SQLException e) {
                throw new DbException("Unable to execute statistics query.", e);

            }
        } catch (SQLException e) {
            throw new DbException("SQL Error.",e);
//            e.printStackTrace();
        }
    }

}
