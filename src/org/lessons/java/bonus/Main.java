package org.lessons.java.bonus;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DatabaseService dbService = new DatabaseService();

        try {
            System.out.println("inserisci una stringa: ");
            String userSearchString = scan.nextLine();

            dbService.searchCountries(userSearchString);
// -----------------------------------------------------------------

            System.out.println("inserisci l'id del paese su cui vuoi piu informazioni");
            int countrySearchID = Integer.parseInt(scan.nextLine());

            dbService.searchLanguage(countrySearchID);
// -----------------------------------------------------------------
            System.out.println("Inserisci l'ID del paese per le statistiche più recenti:");
            int statisticSearch = scan.nextInt();
            scan.nextLine(); // Per consumare il resto della linea dopo l'input numerico

            dbService.searchStatistic(statisticSearch);

        } catch (NumberFormatException e) {
            throw new DbException(e.getMessage());
        } catch (DbException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            scan.close();
        }



    }
}
