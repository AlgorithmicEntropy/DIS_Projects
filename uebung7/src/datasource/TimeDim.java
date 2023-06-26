package datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TimeDim {
    private Date date;
    private int day;
    private int month;
    private int quarter;
    private int year;

    public TimeDim(Date date, int day, int month, int quarter, int year) {
        this.date = date;
        this.day = day;
        this.month = month;
        this.quarter = quarter;
        this.year = year;
    }

    public TimeDim(Date date) {
        this.date = date;
        this.day = date.getDay();
        this.month = date.getMonth();
        this.quarter = getQuarterFromDate(date);
        this.year = date.getYear();
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date dateID) {
        this.date = dateID;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int getQuarterFromDate(Date date)
    {
        int month = date.getMonth();
        if (month <= 2)
            return 1;
        else if (month <= 5)
            return 2;
        else if (month <= 8)
            return 3;
        else
            return 4;
    }

    public void store(Connection con) throws SQLException {
        try {
            String query = "INSERT INTO Time (date, Day, Month, Quarter, Year) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.setInt(2, day);
            statement.setInt(3, month);
            statement.setInt(4, quarter);
            statement.setInt(5, year);
            statement.executeUpdate();
        } catch (
    SQLException e) {
            e.printStackTrace();
        }
    }
}

