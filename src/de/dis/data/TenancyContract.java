package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TenancyContract extends Contract {
    public static final String START_DATE = "start_date";
    public static final String DURATION = "duration";
    public static final String ADDITIONAL_COSTS = "additional_costs";
    private Date startDate;
    private Duration duration;
    private double additionalCosts;

    public TenancyContract() {}

    public TenancyContract(Contract contract) {
        setContractNumber(contract.getContractNumber());
        setDate(contract.getDate());
        setPlace(contract.getPlace());
    }

    // getters and setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setDate(columns.indexOf(START_DATE) + 1, new java.sql.Date(startDate.getTime()));
        stmt.setObject(columns.indexOf(DURATION) + 1, duration);
        stmt.setDouble(columns.indexOf(ADDITIONAL_COSTS) + 1, additionalCosts);
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(START_DATE, DURATION, ADDITIONAL_COSTS).stream())
                .collect(Collectors.toList());
    }

    public int Save() {
        // TODO implement me
        return -1;
    }

    public static TenancyContract load(int id) {
        // TODO implement me
        return new TenancyContract();
    }

    @Override
    public String toString() {
        return "TenancyContract{" +
                "contractNumber=" + contractNumber +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", startDate=" + startDate +
                ", duration=" + duration +
                ", additionalCosts=" + additionalCosts +
                '}';
    }
}

