package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static ConnectionManager instance = null;

    private final Connection dbCon;
    private final Connection dwCon;

    private ConnectionManager() {
        dbCon = buildDBConnection();
        dwCon = builtDWConnection();
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    private Connection buildDBConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "postgres");
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            throw new RuntimeException(e);
        }
    }

    private Connection builtDWConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5433/db", "postgres", "postgres");
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            throw new RuntimeException(e);
        }
    }

    public Connection getDbCon() {
        return dbCon;
    }

    public Connection getDwCon() {
        return dwCon;
    }
}
