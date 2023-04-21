package de.dis.data;

import java.util.Date;

public class Contract extends Entity {
    protected int contractNumber = -1;
    protected Date date;
    protected String place;

    // getters and setters
    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Contract {" +
                "contractNumber=" + contractNumber +
                ", date=" + date +
                ", place='" + place + '\'' +
                '}';
    }
}


