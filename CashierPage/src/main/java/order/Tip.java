package order;

public class Tip {

    private double usd;
    private int lbp;

    public Tip(double usd, int lbp){
        this.usd = usd;
        this.lbp = lbp;
    }

    public Tip(){
        this.usd = 0;
        this.lbp =  0;
    }

    /**
     * Checks if the tips are empty(equal to 0)
     * @return False if tips are empty else True
     */
    public boolean isEmpty(){
        return usd == 0 && lbp == 0;
    }

    public double getUsd() {
        return usd;
    }
    public void setUsd(double usd) {
        this.usd = usd;
    }

    public int getLbp() {
        return lbp;
    }
    public void setLbp(int lbp) {
        this.lbp = lbp;
    }
}
