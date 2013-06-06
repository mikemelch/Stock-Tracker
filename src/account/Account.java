package account;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import stock.Stock;

public class Account {
	private String username;
	private String password;
	private double balance;
	private List<Stock> stocks;

    private Account(String username, String password) {
        this.username = username;
        this.password = hashPassword(password);
        this.balance = 100000;
        this.stocks = new ArrayList<Stock>();
    }

    public static Account create(String username, String password) {
        return new Account(username, password);
    }

    public static Account createWithBalance(String username, String password, double balance) {
        Account account = new Account(username, password);
        account.balance = balance;

        return account;
    }

	public void showStocks() throws IOException {
		if(stocks.isEmpty()) {
			System.out.println("You do not have any stocks to list!");
		}
		else {		
			for(Stock s : this.stocks) {
				s.printStock();
			}						
		}
	}
	
	/**
	 * Print the performance of each stock in the user's account and the users total account performance.
	 * Updates each stock to get the latest price before printing.
	 * Format of output is stock, <5 spaces>, open price, <5 spaces>, last price, <5 spaces>, % change
	 */
	public void showAccountPerformance() throws MalformedURLException, IOException {
		if(stocks.isEmpty()) {
			System.out.println("You do not have any stocks to list!");
		}
		else {
			System.out.println();			

            System.out.format("%-10s%-15s%-15s%-10s%n", "Stock", "Open Price", "Last Price", "% Change");

			for(Stock s: stocks) {
				s.updateStock();

                String percentChanged = String.format("%.2f", (s.getPercentChanged() * 100)) + "%";
                System.out.format("%-10s%-15.2f%-15.2f%-10s%n", s.getName(), s.getOpenPrice(), s.getLastPrice(), percentChanged);
			}

			System.out.println();

			System.out.format("%-36s%6.2f%n", "Value of Holdings at Initial Price: $", this.getInitialAccountValue());
			System.out.format("%-36s%6.2f%n", "Value of Holdings at Last Price   : $", this.getLatestAccountValue());
			System.out.format("%-36s%.2f%1s%n", "Account Value Change              : ", ((this.getLatestAccountValue() - this.getInitialAccountValue()) / this.getInitialAccountValue()), "%");
		}
	}
	
	public String hashPassword(String pass) {
		return BCrypt.hashpw(pass, BCrypt.gensalt());	
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

	/**
	 * Return the total value of the account at initial price.
	 * @return the total value of the account (initial value)
	 */
	public double getInitialAccountValue() {
		double sum = 0;

		for(int i = 0; i < stocks.size(); i++) {
			sum += stocks.get(i).getInitialValue();
		}

		return(sum);
	}

	/**
	 * Return the total value of the account at the latest price.
	 * @return the total value of the account (latest value)
	 */
	public double getLatestAccountValue() {
		double sum = 0;

		for(int i = 0; i < stocks.size(); i++) {
			sum += stocks.get(i).getLatestValue();
		}

		return(sum);
	}	
}
