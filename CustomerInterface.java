import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CustomerInterface {
    public static void main(String[] args) throws IOException {
        final int NUM_MUTUAL_FUNDS = 7;
        final int NUM_CUSTOMERS = 100;
        HashTable<MutualFund> funds = new HashTable<>(NUM_MUTUAL_FUNDS * 2);
        HashTable<Customer> customers = new HashTable<>(NUM_CUSTOMERS);

        DecimalFormat df = new DecimalFormat("###,##0.00");
        
        loadMutualFunds(funds);
        loadCustomers(customers, funds);
        
        Scanner scanner = new Scanner(System.in);
        String choice = "";
                
        displayMenu(scanner, choice, funds, customers, df);
        
        scanner.close();
    }

    private static void loadMutualFunds(HashTable<MutualFund> funds) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("mutual_funds.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String mutualName = line.trim();
                String ticker = reader.readLine().trim();
                double sharePrice = Double.parseDouble(reader.readLine().trim());
                double fee = Double.parseDouble(reader.readLine().trim());
                funds.add(new MutualFund(mutualName, ticker, sharePrice, fee));
            }
        }
    }

    private static void loadCustomers(HashTable<Customer> customers, HashTable<MutualFund> funds) throws FileNotFoundException {
        File file2 = new File("customers.txt");
        Scanner input = new Scanner(file2);
        
        while (input.hasNextLine()) {
            String firstName = input.next();
            String lastName = input.nextLine().trim();
            String email = input.nextLine().trim();
            String password = input.nextLine().trim();
            double cash = Double.parseDouble(input.nextLine().trim());
            int numFunds = Integer.parseInt(input.nextLine().trim());
            
            ArrayList<MutualFundAccount> customerFunds = new ArrayList<>(numFunds);
            
            for (int i = 0; i < numFunds; i++) {
                String ticker = input.nextLine().trim();
                double shares = Double.parseDouble(input.nextLine().trim());
                
                MutualFund dummyFund = new MutualFund(ticker);
                MutualFund actualFund = funds.get(dummyFund);
                
                if (actualFund != null) {
                    MutualFundAccount account = new MutualFundAccount(shares, actualFund);
                    customerFunds.add(account);
                }
            }
            
            Customer customer = new Customer(firstName, lastName, email, password, cash, customerFunds);
            customers.add(customer);
            
            if (input.hasNextLine()) {
                input.nextLine();
            }
        }
        input.close();
    }
    
    private static void displayMenu(Scanner scanner, String choice, HashTable<MutualFund> funds, HashTable<Customer> customers, DecimalFormat df) {
        try {
            System.out.print("Welcome to Mutual Fund InvestorTrack (TM)!");
            System.out.print("\n\nPlease enter your email address: ");
            String email = scanner.next();
            System.out.print("Please enter your password: ");
            String password = scanner.next();
            
            Customer currentCustomer = customers.get(new Customer(email, password));
            
            if (currentCustomer == null) {
                System.out.println("\nWe don't have your account on file...");
                System.out.println("\nLet's create an account for you!");
                System.out.print("Enter your first name: ");
                String firstName = scanner.next();
                System.out.print("Enter your last name: ");
                String lastName = scanner.next();
                currentCustomer = new Customer(firstName, lastName, email, password);
                customers.add(currentCustomer);
            }
            
            System.out.println("\nWelcome, " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "!\n");

            do {
                System.out.println("\nPlease select from the following options:\n");
                System.out.println("A. Purchase a Fund");
                System.out.println("B. Sell a Fund");
                System.out.println("C. Add Cash");
                System.out.println("D. Display Your Current Funds");
                System.out.println("X. Exit\n");
                System.out.print("Enter your choice: ");

                choice = scanner.next().toUpperCase();
                System.out.println(); 

                switch (choice) {
                    case "A":
                        purchaseFund(currentCustomer, funds, scanner);
                        break;
                    case "B":
                        sellFund(scanner, currentCustomer, df);
                        break;
                    case "C":
                        addCash(scanner, currentCustomer, df);
                        break;
                    case "D":
                        displayCurrentFunds(currentCustomer, scanner);
                        break;
                    case "X":
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid menu option. Please enter A-D or X to exit.");
                }
            } while (!choice.equals("X"));
        } catch (NoSuchElementException e) {
            System.out.println("Error reading input. Please try again.");
        }
    }
    private static void purchaseFund(Customer currentCustomer, HashTable<MutualFund> funds, Scanner scanner) {
        System.out.println("Please select from the options below:\n");
        System.out.println(funds.toString());
    
        System.out.print("Enter the ticker of the fund to purchase: ");
        String ticker = scanner.next().trim();
    
        System.out.print("Enter the number of shares to purchase: ");
        try {
            double shares = Double.parseDouble(scanner.next().trim());
    
            MutualFund dummyFund = new MutualFund(ticker);
            MutualFund actualFund = funds.get(dummyFund);
    
            if (actualFund != null) {
                double cost = actualFund.getPricePerShare() * shares;
    
                if (currentCustomer.getCash() >= cost) {
                    currentCustomer.addFund(shares, actualFund);
                    System.out.println("You successfully added shares of the following fund:");
                    System.out.println(actualFund);
                    System.out.println("Number of shares added: " + shares);
                } else {
                    System.out.println("You don't have enough cash to purchase that fund.\nPlease add cash to make a purchase");
                }
            } else {
                System.out.println("Fund ticker not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number of shares.");
        }
    }
    
    private static void displayFunds(Customer customer){
        System.out.println("You own the following mutual funds:\n");
       customer.printAccountsByName();
    }
    private static void sellFund(Scanner scanner, Customer customer, DecimalFormat df) {
        if (!customer.hasOpenAccounts()) {
            System.out.println("You don't have any funds to sell at this time.");
            return;
        }
        displayFunds(customer);
    

        System.out.print("Enter the name of the fund to sell: ");
        scanner.nextLine(); // Consume newline
        String fundName = scanner.nextLine();
        
        MutualFundAccount account = customer.getAccountByName(fundName);
        if (account == null) {
            System.out.println("Sorry you don't own an account by that name.");
            return;
        }
        
        System.out.print("Enter the number of shares to sell or \"all\" to sell everything: ");
        String shareInput = scanner.next();
        
        try {
            double sharesToSell = shareInput.equalsIgnoreCase("all") ? account.getNumShares() : Double.parseDouble(shareInput);
            customer.sellShares(fundName,sharesToSell);
            

            System.out.println("You own the following funds:\n");
            customer.printAccountsByName();
            System.out.println("\nYour current cash balance is $" + df.format(customer.getCash()));
            
        } catch (NoSuchElementException e) {
            System.out.println("No such fund found with the name: " + fundName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayCurrentFunds(Customer customer, Scanner scanner) {
        if (!customer.hasOpenAccounts()) {
            System.out.println("You don't have any funds to display at this time.");
            return;
        }
        
        System.out.println("View Your Mutual Funds By:");
        System.out.println("1. Name");
        System.out.println("2. Value");
        System.out.print("Enter your choice (1 or 2): ");
        
        int choice = Integer.parseInt(scanner.next().trim());
        if (choice == 1) {
            customer.printAccountsByName();
        } else if (choice == 2) {
            customer.printAccountsByValue();
        } else {
            System.out.println("Invalid Choice!");
        }
    }
    

    
    private static void addCash(Scanner scanner, Customer customer, DecimalFormat df) {
        try {
            System.out.println("\nYour current cash balance is $" + df.format(customer.getCash()));
            System.out.print("\n\nEnter the amount of cash to add: $");
            
            double amount = Double.parseDouble(scanner.next().trim());
            if (amount <= 0) {
                System.out.println("Error: Amount must be greater than zero.");
                return;
            }
            
            customer.updateCash(amount);
            System.out.println("\nYour current cash balance is $" + df.format(customer.getCash()));
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        }
    }
}
