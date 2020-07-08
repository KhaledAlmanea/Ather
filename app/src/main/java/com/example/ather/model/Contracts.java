package com.example.ather.model;

public class Contracts {
    private String stationName, supplierName, contractNumber, validityTerm,
            contractDuration, averageUnitCost, contractTotalValue, discounts,
            departmentName, contractStatus, commencementDate, uId, expiryDate;

    public Contracts() {
    }

    public Contracts(String stationName, String supplierName, String contractNumber, String validityTerm,
                     String contractDuration, String averageUnitCost, String contractTotalValue,
                     String discounts, String departmentName, String contractStatus,
                     String commencementDate, String uId, String expiryDate) {
        this.stationName = stationName;
        this.supplierName = supplierName;
        this.contractNumber = contractNumber;
        this.validityTerm = validityTerm;
        this.contractDuration = contractDuration;
        this.averageUnitCost = averageUnitCost;
        this.contractTotalValue = contractTotalValue;
        this.discounts = discounts;
        this.departmentName = departmentName;
        this.contractStatus = contractStatus;
        this.commencementDate = commencementDate;
        this.uId = uId;
        this.expiryDate = expiryDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getValidityTerm() {
        return validityTerm;
    }

    public void setValidityTerm(String validityTerm) {
        this.validityTerm = validityTerm;
    }

    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    public String getAverageUnitCost() {
        return averageUnitCost;
    }

    public void setAverageUnitCost(String averageUnitCost) {
        this.averageUnitCost = averageUnitCost;
    }

    public String getContractTotalValue() {
        return contractTotalValue;
    }

    public void setContractTotalValue(String contractTotalValue) {
        this.contractTotalValue = contractTotalValue;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(String commencementDate) {
        this.commencementDate = commencementDate;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
