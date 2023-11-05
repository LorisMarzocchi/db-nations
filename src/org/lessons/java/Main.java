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
            System.out.println(connection.getCatalog());

            System.out.println("inserisci una stringa: ");
            String userSearchString = scanner.nextLine();


            String sqlQuery = "SELECT DISTINCT c.name as country_name,c.country_id, r.name as region_name,c2.name as continent_name\n" +
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
//-----------------------------------------------------------------------------------------
                System.out.println("inserisci l'id del paese su cui vuoi piu informazioni");
                int countrySearchID = Integer.parseInt(scanner.nextLine());

                String sqlCountrySearchID = "SELECT l.language " +
                        "FROM languages l " +
                        "JOIN country_languages cl ON l.language_id = cl.language_id " +
                        "WHERE cl.country_id = ?";
                try (PreparedStatement langStatement = connection.prepareStatement(sqlCountrySearchID)){
                    langStatement.setInt(1, countrySearchID);

                    try (ResultSet langResultSet = langStatement.executeQuery()){
                        System.out.println("Lingue parlate:");

                        while (langResultSet.next()) {
                            System.out.println("- " + langResultSet.getString("language"));
                        }

                    }catch (SQLException e) {
                        System.out.println("Unable to execute languages query");
                        e.printStackTrace();
                    }
//   --------------------------------------------------------------------------------------
                    System.out.println("Inserisci l'ID del paese per le statistiche più recenti:");
                    int statisticSearch = scanner.nextInt();
                    scanner.nextLine(); // Per consumare il resto della linea dopo l'input numerico

                    String sqlCountryStats =
                            "SELECT * " +
                                    "FROM country_stats " +
                                    "WHERE country_id = ? " +
                                    "ORDER BY year DESC " ;

                    try (PreparedStatement statsStatement = connection.prepareStatement(sqlCountryStats)) {
                        statsStatement.setInt(1, statisticSearch);

                        try (ResultSet statsResultSet = statsStatement.executeQuery()) {
                            if (statsResultSet.next()) {
                                int year = statsResultSet.getInt("year");
                                double population = statsResultSet.getDouble("population");
                                double gdp = statsResultSet.getDouble("gdp");

                                System.out.println("Statistiche più recenti per il paese ID " + statisticSearch + ":");
                                System.out.println("Anno: " + year);
                                System.out.println("Popolazione: " + String.format("%,.0f", population));
                                System.out.println("GDP: " + String.format("%,.0f", gdp));

                            } else {
                                System.out.println("Non ci sono statistiche disponibili per il paese ID " + statisticSearch);
                            }
                        } catch (SQLException e) {
                            System.out.println("Unable to execute statistics query.");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Error.");
                        e.printStackTrace();
                    }



                }catch (SQLException e){
                    e.printStackTrace();
                }





//                        select c.name as country_name, r.name as region_name,
//                c2.name as country_name, cs.*
//                        from countries c
//                join regions r on r.region_id = c.region_id
//                join continents c2 on c2.continent_id = r.continent_id
//                join country_languages cl on cl.country_id = c.country_id
//                join languages l on cl.language_id = l.language_id
//                join country_stats cs on cs.country_id = c.country_id
//                where c.country_id like 107
//                order by cs.`year` desc

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
