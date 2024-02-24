package staff;

import java.text.DecimalFormat;

public class Tip extends Salary{


    public Tip(double usd, double lbp){
        super(usd, lbp);
    }

    @Override
    public String toString() {
        return "$ " + getFormatter().format(getUsd()) + " / LBP " + getFormatter().format(getLbp());
    }
}
