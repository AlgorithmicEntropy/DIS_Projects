package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PurchaseContract extends Contract {
    public static final String NUM_INSTALLMENTS = "num_installments";
    public static final String INTEREST_RATE = "interest_rate";
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


    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setInt(columns.indexOf(NUM_INSTALLMENTS) + 1, numberOfInstallments);
        stmt.setDouble(columns.indexOf(INTEREST_RATE) + 1, interestRate);
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(NUM_INSTALLMENTS, INTEREST_RATE).stream())
                .collect(Collectors.toList());
    }
}
