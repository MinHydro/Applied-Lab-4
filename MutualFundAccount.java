/**
 * MutualFundAccount.java
 * @author Minh Long Hang
 * CIS 22C, Applied Lab 3
 */
import java.text.DecimalFormat;
import java.util.Comparator;

public class MutualFundAccount {
    private double numShares;
    private MutualFund mf;

    private static int accountSeed = 100000000;
    /**CONSTRUCTORS*/

    /**
     * One-argument constructor
     * @param mf the mutual fund for this account
     * Assigns numShares to 0
     */
    public MutualFundAccount(MutualFund mf) {
        this.mf = mf;
        this.numShares = 0;
    }

    /**
     * Two-argument constructor
     * @param numShares total shares of the mutual fund
     * @param mf the mutual fund
     */
    public MutualFundAccount(double numShares, MutualFund mf) {
        this.numShares = numShares;
        this.mf = mf;
    }

    /**
     * Two-argument constructor
     * @param mf the mutual fund
     * @param numShares total shares of the mutual fund
     */
    public MutualFundAccount(MutualFund mf, double numShares) {
        this.mf = mf;
        this.numShares = numShares;
    }

    /**ACCESSORS*/

    /**
     * Accesses the total number of shares
     * @return the total shares
     */
    public double getNumShares() {
        return numShares;
    }

    /**
     * Accesses the mutual fund
     * @return the mutual fund
     */
    public MutualFund getMf() {
        return mf;
    }

     /**
     * Increments the account seed and returns
     * the new value
     * @return the incremented account seed
     */
    public static int getAccountSeed() {
        ++accountSeed;
        return accountSeed;
    }
    /**MUTATORS*/

    /**
     * Increases/Decreases the total shares
     * by the given amount
     * @param numShares the amount to increase or decrease.
     */
    public void updateShares(double numShares) {
        this.numShares += numShares;
    }

    public void deductFee() {
        double shareReduction = numShares * mf.getTradingFee();
        numShares = numShares - shareReduction;
    }

    /**
     * Creates a String of the mutual fund
     * account information in the following format:
     * <mf>
     * Total Shares: <numShares>
     * Value: $<numShares>*<pricePerShare>
     */
    @Override
    public String toString() {
        double value = numShares * mf.getPricePerShare();
        if(value >= 1000) {
            return mf.toString() + "\n\nTotal Shares: " + String.format("%.1f",numShares) + "\nValue: $" + String.format("%,.2f", value)+ "\n";
        }
        else {
            return mf.toString() + "\n\nTotal Shares: " + String.format("%.1f", numShares) + "\nValue: $" + String.format("%.2f", value) + "\n";
        }
    }
}

class NameComparator implements Comparator<MutualFundAccount> {
    /**
     * Compares the two mutual fund accounts by name of the fund
     * uses the String compareTo method to make the comparison
     * @param account1 the first MutualFundAccount
     * @param account2 the second MutualFundAccount
     * @return The comparison.
     */
    @Override
    public int compare(MutualFundAccount account1, MutualFundAccount account2) {
        return account1.getMf().getFundName().compareTo(account2.getMf().getFundName());
    }
} // end class NameComparator

class ValueComparator implements Comparator<MutualFundAccount> {
    /**
     * Compares the two mutual fund accounts by total value
     * determines total value as number of shares times price
     * per share
     * uses the static Double compare method to make the
     * comparison
     * @param account1 the first MutualFundAccount
     * @param account2 the second MutualFundAccount
     * @return The comparison.
     */
    @Override
    public int compare(MutualFundAccount account1, MutualFundAccount account2) {
        double value1 = account1.getNumShares() * account1.getMf().getPricePerShare();
        double value2 = account2.getNumShares() * account2.getMf().getPricePerShare();
        return Double.compare(value1, value2);
    }
}
