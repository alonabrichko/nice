package com.wealthrating.request;

import java.math.BigDecimal;

public class FinancialInfo {
    private BigDecimal cash; // in case cash is in trillions and it can be decimal number
    private int numberOfAssets;

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
}
