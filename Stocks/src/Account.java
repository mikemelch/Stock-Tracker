import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class Account {
	
	private String username;
	private String password;
	private double balance;
	private ArrayList<Stock> stocks;
	
	public Account(String username, String password) throws IOException{
		
		this.username = username;
		this.password = password;
		this.balance = 100000;	   
		this.stocks = new ArrayList<Stock>();

		addAccount();
	}
	public Account(String username, String password, double balance) throws IOException{

		this.username = username;
		this.password = password;
		this.balance = balance;	   
		this.stocks = new ArrayList<Stock>();
		
		addAccount();
	}
	public Account(){
		this.username = null;
		this.password = null;
	}

	private void addAccount() throws IOException{
		
		CSVReader csvReader = new CSVReader(new FileReader("accounts.csv"));
		List<String []> content = csvReader.readAll();
		content.add((this.username + "#" + this.password + "#" + this.balance).split("#"));
		csvReader.close();
		CSVWriter writer = new CSVWriter(new FileWriter("accounts.csv"));
	    writer.writeAll(content);
	    writer.close();
	}
	
	public boolean accountLookup(String user, String pass) throws IOException{
		
		CSVReader csvReader = new CSVReader(new FileReader("accounts.csv"));
		List<String []> content = csvReader.readAll();
		
		for(int i = 0; i < content.size(); i++){
			if(content.get(i)[0].equals(user)){
				if(content.get(i)[1].equals(pass)){
					this.username = user;
					this.password = pass;
					this.balance = Double.parseDouble(content.get(i)[2]);
					this.stocks = new ArrayList<Stock>();
					for(int j = 3; j < content.get(i).length; j++){
						Stock s = new Stock();
						this.stocks.add(s.parseStock(content.get(i)[j]));
					}
					return true;
				}
			}
		}
			
		return false;
	}
	
	public String getUsername(){
		return this.username;
	}
	public double getBalance(){
		return this.balance;
	}
	public void buyStock(String ticker, double price, int shares) throws IOException {
		this.balance -= (price * shares);
		this.stocks.add(new Stock(ticker, shares, price));
		this.updateAccount();
	}
	public void updateAccount() throws IOException{
		CSVReader csvReader = new CSVReader(new FileReader("accounts.csv"));
		List<String []> content = csvReader.readAll();
		
		for(int i = 0; i < content.size(); i++){
			if(content.get(i)[0].equals(this.username)){
				if(content.get(i)[1].equals(this.password)){
					
					String temp[] = new String[this.stocks.size() + 3];
					temp[0] = content.get(i)[0];
					temp[1] = content.get(i)[1];
					temp[2] = this.balance + "";
					
					int j = 3;
					for(Stock s : this.stocks){
						s.updateStock();
						temp[j] = s.writeStock();
						j++;
					}
					content.set(i, temp);
					csvReader.close();
					
					CSVWriter writer = new CSVWriter(new FileWriter("accounts.csv"));
				    writer.writeAll(content);
				    writer.close();
				    return;
				}
			}
		}
		
	}
	
	public void showStocks() throws NumberFormatException, IOException{
		for(Stock s : this.stocks){
			s.printStock();
		}
	}
	
	public void sellStock(String ticker, int shares) throws IOException{
		for(int i = 0; i < this.stocks.size(); i++){
			Stock s = this.stocks.get(i);
			if(s.getTicker().equalsIgnoreCase(ticker)){
				if(s.getShares() == shares){
					this.balance += s.modifyStock(shares);
					this.stocks.remove(s);
				}
				else{
					balance += s.modifyStock(shares);
				}
			}
		}
		this.updateAccount();
	}
	
}
