package main;

import java.io.BufferedReader;
import java.io.FileReader;

public class InitMain {
    public static void main(String[] args) {
        fillProductionDB();
        setupDW();
    }

    public static void fillProductionDB() {
        try {
            var con = ConnectionManager.getInstance().getDbCon();
            var statement = con.createStatement();
            var reader = new BufferedReader(new FileReader("files/stores-and-products.sql"));
            String line;
            StringBuilder query = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                query.append(line);

                // If a query ends with a semicolon, execute it
                if (line.trim().endsWith(";")) {
                    String sqlQuery = query.toString();
                    statement.executeUpdate(sqlQuery);
                    query.setLength(0); // Reset the query
                }
            }
        } catch (Exception e) {
            System.out.println("Error while filling the production DB.");
            throw new RuntimeException(e);
        }
    }

    public static void setupDW() {
        try {
            var con = ConnectionManager.getInstance().getDwCon();
            var statement = con.createStatement();
            var reader = new BufferedReader(new FileReader("files/createDWSchema.sql"));
            String line;
            StringBuilder query = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                query.append(line);

                // If a query ends with a semicolon, execute it
                if (line.trim().endsWith(";")) {
                    String sqlQuery = query.toString();
                    statement.executeUpdate(sqlQuery);
                    query.setLength(0); // Reset the query
                }
            }
        } catch (Exception e) {
            System.out.println("Error while filling the production DB.");
            throw new RuntimeException(e);
        }
    }
}