package com.example.tabslayout;

public class TotalExpense {
    String TotalIncome;
    String TotalExpense;
    String Month;

    public TotalExpense() {
    }

    public TotalExpense(String totalIncome, String totalExpense, String month) {
        TotalIncome = totalIncome;
        TotalExpense = totalExpense;
        Month = month;
    }

    public String getTotalIncome() {
        return TotalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        TotalIncome = totalIncome;
    }

    public String getTotalExpense() {
        return TotalExpense;
    }

    public void setTotalExpense(String totalExpense) {
        TotalExpense = totalExpense;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }
}
