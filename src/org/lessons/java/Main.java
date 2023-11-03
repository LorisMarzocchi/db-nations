package org.lessons.java;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";


        try (Connection connection = DriverManager.getConnection(url, user, password)){
            System.out.println("db connesso");
            System.out.println("inserisci una stringa: ");
            String userSearchString = scanner.nextLine();


            String sqlQuery = "SELECT c.name as country_name,c.country_id, r.name as region_name,c2.name as continent_name\n" +
                    "FROM countries c\n" +
                    "JOIN regions r ON c.region_id = r.region_id\n" +
                    "JOIN continents c2 ON r.continent_id = c2.continent_id\n" +
                    "WHERE c.name LIKE ?" +
                    "ORDER BY c.name;";

            // la connection prepara uno statement sql
            try(PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                // facciamo il binding dei parametri
                preparedStatement.setString(1, "%" + userSearchString + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    while (resultSet.next()){
                        System.out.println("Country name: " + resultSet.getString("country_name") + " -" +
                                " Country ID: " + resultSet.getInt("country_id") + " -" +
                                " Region Name: " + resultSet.getString("region_name") + " -" +
                                " Continent Name: " + resultSet.getString("region_name"));
                    }
                }catch (SQLException e){
                    System.out.println("Unable to execute query (1)");
                    e.printStackTrace();
                }
                System.out.println("inserisci l'id del paese su cui vuoi piu informazioni");
                int countrySearchID = Integer.parseInt(scanner.nextLine());







            }catch (SQLException e){
                System.out.println("unable to prepare state");
                e.printStackTrace();
            }

        }catch (SQLException e ){
                System.out.println("unable connect");
                e.printStackTrace();
            }


        scanner.close();
    }
}
