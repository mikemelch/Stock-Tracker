package stock;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;


public class Stock {

	private String name;
	private int shares;
	private double earnings;
	private double initialPrice;
	private double openPrice;
	private double lastPrice; 
	private double percentChanged; 
		
	public Stock(String name, int shares, double initialPrice){
		this.name = name;
		this.shares = shares;
		this.earnings = 0;
		this.initialPrice = initialPrice;
		this.openPrice = 0;
		this.lastPrice = 0;
		this.percentChanged = 0;
	}
	
	public Stock(){
		this.name = null;
		this.shares = 0;
		this.earnings = 0;
		this.initialPrice = 0;
		this.openPrice = 0;
		this.lastPrice = 0;
		this.percentChanged = 0;
	}
	
	public Stock parseStock(String content){
		StringTokenizer s = new StringTokenizer(content);
		this.name = s.nextToken();
		this.shares = Integer.parseInt(s.nextToken());
		this.earnings = Double.parseDouble(s.nextToken());
		this.initialPrice = Double.parseDouble(s.nextToken());
		return this;
	}
	
	public String writeStock(){
		return (this.name + " " + this.shares + " " + this.earnings + " " + this.initialPrice);
	}
	
	public void printStock() throws NumberFormatException, IOException{
		Reader changed;
		
		InputStream input = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + this.name.toUpperCase() + "&f=nsl1ocghjkr").openStream();
		changed = new InputStreamReader(input, "UTF-8");		
	
	    CSVReader reader = new CSVReader(changed);
	    String [] nextLine;
	    
	    while ((nextLine = reader.readNext()) != null) {
	        System.out.println("\nCompany: " + nextLine[0] + "(" + nextLine[1] + ")\n");
	        System.out.println("\tPrice: " + nextLine[2]);
	        System.out.println("\tOpen: " + nextLine[3]);
	        System.out.println("\tChange: " + nextLine[4]);
	        System.out.println("\tDay's Low: " + nextLine[5]);
	        System.out.println("\tDay's High: " + nextLine[6]);
	        System.out.println("\t52 Week Low: " + nextLine[7]);
	        System.out.println("\t52 Week High: " + nextLine[8]);
	        System.out.println("\tP/E Ratio: " + nextLine[9]);
	    }
	    reader.close();
	    
	    System.out.println("\n\tBought at: $" + this.initialPrice);
	    System.out.println("\n\tShares owned: " + this.shares);
	    System.out.println("\n\tCurrent earnings: $" + this.earnings);
	}
	
	public void updateStock() throws MalformedURLException, IOException{
		Reader changed;
		
		InputStream input = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + this.name.toUpperCase() + "&f=nsl1ocghjkr").openStream();
		changed = new InputStreamReader(input, "UTF-8");		
	
	    CSVReader reader = new CSVReader(changed);
	    String [] nextLine = reader.readNext();
	
		// Store the stock opening and last prices
		this.openPrice = Double.parseDouble(nextLine[3]);
		this.lastPrice = Double.parseDouble(nextLine[2]);	    

	    double currprice = Double.parseDouble(nextLine[2]);		
	    this.earnings = this.shares * (currprice - initialPrice);
	   
		// Calculate and store the percent change for the stock
		this.percentChanged = (this.lastPrice - this.openPrice) / this.openPrice;		
 
	    reader.close();
	    input.close();
	}
	
	public double modifyStock(int shares) throws IOException{
		Reader changed;
		
		InputStream input = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + this.name.toUpperCase() + "&f=nsl1ocghjkr").openStream();
		changed = new InputStreamReader(input, "UTF-8");		
		
	    CSVReader reader = new CSVReader(changed);
	    String [] nextLine = reader.readNext();
	    
	    reader.close();
	    input.close();
	    
	    double currprice = Double.parseDouble(nextLine[2]);
	    this.shares -= shares;
	    this.earnings -= shares * (currprice - initialPrice);
	    return (shares * currprice);
	}
	
	public String getTicker(){
		return this.name;
	}
	
	public int getShares(){
		return this.shares;
	}
	
	public double getEarnings(){
		return this.earnings;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Accessor for the initial price of the stock
	 */
	public double getInitialPrice() {
		return this.initialPrice;
	}

	/**
	 * Accessor for the open price of the stock
	 */
	public double getOpenPrice() {
		return this.openPrice;
	}

	/**
	 * Accessor for the last price of the stock
	 */
	public double getLastPrice() {
		return this.lastPrice;
	}

	/**
	 * Accessor for the percent change of the stock
	 */
	public double getPercentChanged() {
		return this.percentChanged;
	}

	/**
	 * Accessor for the total initial value of the stock
	 */
	public double getInitialValue() {
		return this.initialPrice * this.shares;
	}

	/**
	 * Accessor for the latest value of the stock
	 */
	public double getLatestValue() {
		return this.lastPrice * this.shares;
	}
}
