package statistics;

import java.text.DecimalFormat;

public class Tip {

    private double usd, lbp;
    private DecimalFormat formatter;

    public Tip(double usd, double lbp){
        this.usd = usd;
        this.lbp = lbp;
        formatter = new DecimalFormat("#,###.00");
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

    @Override
    public String toString() {
        return "$ " + formatter.format(usd) + " / LBP " + formatter.format(lbp);
    }
}
