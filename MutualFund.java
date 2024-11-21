/**
 * MutualFund.java
 * @author Minh Long Hang
 * CIS 22C, Applied Lab 4
 */
import java.text.DecimalFormat;
public class MutualFund {
    private final String fundName;
    private final String ticker;
    private double pricePerShare;
    private double tradingFee;
    
    /**CONSTRUCTORS*/
    
    /**
     * One-argument constructor, when only ticker is known.
     * @param ticker the ticker symbol sets fundName to "No name"
     * and assigns 0 to pricePerShare and tradingFee.
     */
    public MutualFund(String ticker) {
        this.fundName = "No name";
        this.ticker = ticker;
        this.pricePerShare = 0;
        this.tradingFee = 0;
    }

    /**
     * Two-argument constructor, when only name and ticker are known.
     * @param name the name of the fund
     * @param ticker the ticker symbol
     * Assigns 0 to pricePerShare
     * and tradingFee
     */
    public MutualFund(String name, String ticker) {
        this.fundName = name;
        this.ticker = ticker;
        this.pricePerShare = 0;
        this.tradingFee = 0;
    }

    /**
     * Multi-argument constructor when all MutualFund information is known.
     * @param fundName the name of the fund
     * @param ticker the ticker symbol
     * @param pricePerShare the price per share
     * @param tradingFee the trading fee as a percent
     */
    public MutualFund(String fundName, String ticker, double pricePerShare,
            double tradingFee) {
        this.fundName = fundName;
        this.ticker = ticker;
        this.pricePerShare = pricePerShare;
        this.tradingFee = tradingFee;
    }

    /**ACCESSORS*/

    /**
     * Accesses the name of the fund
     * @return the fund name
     */
    public String getFundName() {
        return fundName;
    }

    /**
     * Accesses the ticker symbol
     * @return the ticker symbol
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * Accesses the price per share
     * @return the price per share
     */
    public double getPricePerShare() {
        return pricePerShare;
    }

    /**
     * Accesses the trading fee
     * @return the trading fee
     */
    public double getTradingFee() {
        return tradingFee;
    }

    /**MUTATORS*/

    /**
     * Updates the share price
     * @param pricePerShare the new share price
     */
    public void setPricePerShare(double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    /**
     * Updates the trading fee
     * @param tradingFee the new trading fee
     */
    public void setTradingFee(double tradingFee) {
        this.tradingFee = tradingFee;
    }

    /**ADDITIONAL OPERATIONS*/

    /**
 * Creates a String of the mutual fund information in the format:
 * <fundName>
 * <ticker>
 * Share Price: $<pricePerShare>
 * Trading Fee: <tradingFee>%
 * @return The fund information.
 */
@Override
public String toString() {
    String priceStr;
    if (pricePerShare == 0) {
        priceStr = "$.00";
    } else if (pricePerShare >= 1000) {
        priceStr = String.format("$%,d.%02d", 
            (int)pricePerShare, 
            (int)((pricePerShare * 100) % 100));
    } else {
        priceStr = String.format("$%.2f", pricePerShare);
    }
    
        DecimalFormat feeFormat = new DecimalFormat("0.0#");
    
        return fundName + "\n" + ticker + "\n" +  "Share Price: " + priceStr + "\n" + 
        "Trading Fee: " + feeFormat.format(tradingFee) + "%";
    }
    

    /**
     * Compares this MutualFund to
     * another Object for equality
     * You must use the formula presented
     * in class materials for full credit.
     * @param o another Object
     * (MutualFund or otherwise)
     * @return whether o is a MutualFund
     * and has the same ticker as this MutualFund
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MutualFund)) {
            return false;
        }
        MutualFund m = (MutualFund) o;
        return this.ticker.equals(m.ticker);
    }

    /**
     * Returns a consistent hash code for
     * each MutualFund by summing the Unicode values
     * of each character in the key
     * Key = ticker
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < ticker.length(); i++) {
            sum += (int) ticker.charAt(i);
        }
        return sum;
    }
}