package account;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
}
