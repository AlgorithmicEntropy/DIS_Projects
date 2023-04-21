package de.dis.data;

import java.util.Date;

public class TenancyContract extends Contract {
    private Date startDate;
    private Date duration;
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

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
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

