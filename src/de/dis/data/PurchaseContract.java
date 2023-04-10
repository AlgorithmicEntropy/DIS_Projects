package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class PurchaseContract extends Contract {
    private int numberOfInstallments;
    private double interestRate;

    public PurchaseContract(int contractNumber, Date date, String place, int numberOfInstallments, double interestRate) {
        super(contractNumber, date, place);
        this.numberOfInstallments = numberOfInstallments;
        this.interestRate = interestRate;
    }

    // getters and setters
    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void save() {
        super.save();
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO purchase_contracts (contract_id, num_installments, interest_rate) VALUES (?, ?, ?)");
            stmt.setInt(1, getContractNumber());
            stmt.setInt(2, numberOfInstallments);
            stmt.setDouble(3, interestRate);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
