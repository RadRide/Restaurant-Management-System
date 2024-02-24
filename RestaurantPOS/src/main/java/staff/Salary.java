package staff;

import java.text.DecimalFormat;

public class Salary {

    private double usd, lbp;
    private DecimalFormat formatter;
    private final String pattern = "#,##0.##";

    public Salary(double usd, double lbp) {
        this.usd = usd;
        this.lbp = lbp;
        formatter = new DecimalFormat(pattern);
    }

    public Salary(){
        usd = 0;
        lbp = 0;
    }

    public void add(Salary salary){
        usd += salary.usd;
        lbp += salary.lbp;
        formatter = new DecimalFormat(pattern);
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getLbp() {
        return lbp;
    }

    public void setLbp(double lbp) {
        this.lbp = lbp;
    }

    public DecimalFormat getFormatter() {
        return formatter;
    }

    public String getTotalString(){
        return "$ " + formatter.format(usd) + " / LBP " + formatter.format(lbp);
    }

    @Override
    public String toString() {
        return "$ " + formatter.format(usd) + "\nLBP " + formatter.format(lbp);
    }
}
