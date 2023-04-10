package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TenancyContract extends Contract {
    private Date startDate;
    private int duration;
    private double additionalCosts;

    public TenancyContract(int contractNumber, Date date, String place, Date startDate, int duration, double additionalCosts) {
        super(contractNumber, date, place);
        this.startDate = startDate;
        this.duration = duration;
        this.additionalCosts = additionalCosts;
    }

    // getters and setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public void save() {
        super.save();
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tenancy_contracts (contract_id, start_date, duration, additional_costs) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, getContractNumber());
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setInt(3, duration);
            stmt.setDouble(4, additionalCosts);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

