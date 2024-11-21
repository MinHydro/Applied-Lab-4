/**
 * Customer.java
 * @author Your name
 * @author Partner's name
 * CIS 22C, Applied Lab 4
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountNum;
    private double cash;
    private BST<MutualFundAccount> fundsValue = new BST<>();
    private BST<MutualFundAccount> fundsName = new BST<>();

    /**CONSTRUCTORS*/

    /**
     * Creates a new Customer when only email
     * and password are known
     * @param email the Customer email
     * @param password the Customer password
     * Assigns firstName to "first name unknown"
     * Assigns lastName to "last name unknown"
     * Assigns cash to 0
     * Assigns accountNum to "none"
     */
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
        this.firstName = "first name unknown";
        this.lastName = "last name unknown";
        this.accountNum = "none";
        
    }

    /**
     * Creates a new Customer with no cash.
     * @param firstName member first name
     * @param lastName member last name
     * Assigns cash to 0
     * Calls getAccountSeed and assigns accountNum to this value
     * after converting it to a String BY USING CONCATENATION (easiest way)
     * Hint: Make sure you get no warnings or you did not call getAccountSeed
     * correctly!
     */
    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cash = 0.0;
        this.accountNum = MutualFundAccount.getAccountSeed() + "";
    }

    /**
     * Creates a new Customer when all information is known.
     * @param firstName member first name
     * @param lastName member last name
     * @param cash the starting amount of cash
     * @param al the MutualFundAccounts owned by this Customer
     * (Hint: add these to the BSTs)
     * Calls getAccountSeed and assigns accountNum to this value
     * after converting it to a String BY USING CONCATENATION (easiest way)
     * Hint: Make sure you get no warnings or you did not call getAccountSeed
     * correctly!
     */
    public Customer(String firstName, String lastName, String email,
            String password, double cash, ArrayList<MutualFundAccount> al) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cash = cash;
        this.accountNum = MutualFundAccount.getAccountSeed() + "";

        for (MutualFundAccount account : al) {
            fundsName.insert(account, new NameComparator());
            fundsValue.insert(account, new ValueComparator());
        }
    }

    /**ACCESORS*/

    /**
     * Accesses the customer first name
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Accesses the customer last name
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Accesses the user's account number
     * @return the account number
     */
    public String getAccountNum() {
        return accountNum;
    }

    /**
     * Accesses the email address
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Determines whether a given password matches the customer password.
     * @param anotherPassword the password to compare
     * @return whether the two passwords match
     */
    public boolean passwordMatch(String anotherPassword) {
        return this.password.equals(anotherPassword);
    }

    /**
     * Accesses a specific fund.
     * @param name of the chosen fund
     * @return the specified mutual fund
     */
    public MutualFundAccount getAccountByName(String name) {
        if (name == null) return null;
        MutualFundAccount dummy = new MutualFundAccount(0, new MutualFund(name, ""));
        try {
            return fundsName.search(dummy, new NameComparator());
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Accesses the amount of cash in your account.
     * @return the amount of cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * Accesses whether any accounts exist for this customer.
     * @return whether the customer currently holds any accounts.
     */
    public boolean hasOpenAccounts() {
        return !fundsName.isEmpty();
    }

    /**MUTATORS*/

    /**
     * Updates the customer first name
     * @param firstName a new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Updates the customer last name
     * @param lastName a new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Updates the value of the email address
     * @param name the Customer's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Updates the value of the password
     * @param password the Customer password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Increases/Decreases the amount of cash by adding to the existing cash.
     * @param cash the amount of cash to add
     */
    public void updateCash(double cash) {
        this.cash += cash;
        if (this.cash < 0) this.cash = 0.0;
    }    

    /**
     * Adds a new mutual fund to the user's list of funds or updates a fund to
     * increase the number of shares held.
     * @param shares the desired number of shares
     * @param mf a new or existing mutual fund
     * @return whether the fund was added to the customer's account
     * - i.e. the customer had sufficient cash to add the MutualFund.
     * Decreases the amount of cash if purchase made
     */
    public boolean addFund(double shares, MutualFund mf) {
        if (shares <= 0 || mf == null) {
            return false; // Invalid input
        }
        
        double totalCost = shares * mf.getPricePerShare();
        if (totalCost > cash) {
            return false; // Not enough cash
        }

        MutualFundAccount existingAccount = getAccountByName(mf.getFundName());
        if (existingAccount != null) {
            // Update existing account
            existingAccount.updateShares(shares);
        } else {
            // Create a new account
            MutualFundAccount newAccount = new MutualFundAccount(shares, mf);
            fundsName.insert(newAccount, new NameComparator());
            fundsValue.insert(newAccount, new ValueComparator());
        }

        // Deduct cash
        updateCash(-totalCost);
        return true; // Fund added successfully
    }

    /**
     * Sells a Mutual Fund and returns (the price per share times
     * the number of shares to cash minus the fee).
     * The fee is % * price per share * number of shares.
     * Returns silently with no changes if the fund does not exist.
     * @param name the name of the fund
     * @throws NoSuchElementException if the fund name does not exist.
     */
    public void sellFund(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Fund name cannot be null or empty.");
        }
    
        // Get the MutualFundAccount by name
        MutualFundAccount fundAccount = getAccountByName(name);
        if (fundAccount == null) {
            throw new NoSuchElementException("No such fund found with the name: " + name);
        }
    
        // Calculate the total value before the fee
        double totalValueBeforeFee = fundAccount.getNumShares() * fundAccount.getMf().getPricePerShare();
       
    
        // Get the trading fee as a percentage from the MutualFund object
        double tradingFeePercent = fundAccount.getMf().getTradingFee();
    
        // Calculate the fee
        double fee = (totalValueBeforeFee * tradingFeePercent) / 100;
        double totalValueAfterFee = totalValueBeforeFee - fee;
    
        
    
        // Update cash balance
        updateCash(totalValueAfterFee);
        
    
    
        
    }

    /**
 * Sells a Mutual Fund and returns (the price per share times
 * the number of shares) to cash minus the fee.
 * The fee is % * price per share * number of shares
 * @param name the name of the fund
 * @param shares the number of shares to sell
 * @throws NoSuchElementException if the fund name does not exist.
 */
public void sellShares(String name, double shares) {
    MutualFundAccount account = getAccountByName(name);
    if (account == null) {
       throw new NoSuchElementException("Fund not found.");
    }
    if (shares > account.getNumShares()) {
       throw new IllegalArgumentException("Insufficient shares to sell.");
    }
    fundsName.remove(account, new NameComparator());
    fundsValue.remove(account, new ValueComparator());
    double saleValue = shares * account.getMf().getPricePerShare();
    double fee = saleValue * (account.getMf().getTradingFee() / 100.0);
    this.cash += (saleValue - fee);
    account.updateShares(-shares);
    if (account.getNumShares() == 0) {
       fundsName.remove(account, new NameComparator());
       fundsValue.remove(account, new ValueComparator());
    }
    else {
    fundsName.insert(account, new NameComparator());
    fundsValue.insert(account, new ValueComparator());
  }
}

    /**ADDITIONAL OPERATIONS*/

    /**
     * Creates a String of customer information in the form
     * Name: <firstName> <lastName>
     * Account Number: <accountNum>
     * Total Cash: $<cash>
     * Note that cash is formatted $XXX,XXX,XXX.XX
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###.00");
        String user = "Name: " + firstName + " " + lastName
            + "\nEmail: " + email + "\nAccount Number: " + accountNum
            + "\nTotal Cash: $" + df.format(cash) + "\n\nCurrent Funds:\n";
        return user;
    }

    /**
     * Prints out all the customer accounts alphabetized by name.
     */
    public void printAccountsByName() {
        System.out.print(fundsName.inOrderString());
    }

    /**
     * Prints out all the customer accounts in increasing order of value.
     */
    public void printAccountsByValue() {
        System.out.print(fundsValue.inOrderString());
    }

    /**
     * Compares this Customer to another Object for equality.
     * You must use the formula presented
     * in class for full credit. (See Lesson 4)
     * @param obj another Object
     * @return true if obj is a Customer and has a matching email and password
     * to this Customer.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        return email.equals(other.email) && password.equals(other.password);
    }

    /**
     * Returns a consistent hash code for each Customer by summing
     * the Unicode values of each character in the key.
     * Key = email + password
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 0;
        String combined = email + password;
        for (char c : combined.toCharArray()) {
            hash += c;
        }
        return hash;
    }
}
