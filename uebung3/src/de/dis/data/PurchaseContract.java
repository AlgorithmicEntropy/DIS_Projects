package de.dis.data;

public class PurchaseContract extends Contract {
    private int numberOfInstallments;
    private double interestRate;

    public PurchaseContract() {}

    public PurchaseContract(Contract contract) {
        setContractNumber(contract.getContractNumber());
        setDate(contract.getDate());
        setPlace(contract.getPlace());
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
    public String toString() {
        return "PurchaseContract {" +
                "contractNumber=" + contractNumber +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", numberOfInstallments=" + numberOfInstallments +
                ", interestRate=" + interestRate +
                '}';
    }
}
