package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TenancyContract extends Contract {
    public static final String START_DATE = "start_date";
    public static final String DURATION = "duration";
    public static final String ADDITIONAL_COSTS = "additional_costs";
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

    @Override
    protected void setValues(PreparedStatement stmt) throws SQLException {
        super.setValues(stmt);
        List<String> columns = getDBFields();
        stmt.setDate(columns.indexOf(START_DATE) + 1, new java.sql.Date(startDate.getTime()));
        stmt.setInt(columns.indexOf(DURATION) + 1, duration);
        stmt.setDouble(columns.indexOf(ADDITIONAL_COSTS) + 1, additionalCosts);
    }

    @Override
    public List<String> getDBFields() {
        return Stream
                .concat(super.getDBFields().stream(),
                        List.of(START_DATE, DURATION, ADDITIONAL_COSTS).stream())
                .collect(Collectors.toList());
    }
}

