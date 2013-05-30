package account;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import stock.Stock;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVAccountRepository implements AccountRepository {
    private CSVReader csvReader;
    private CSVWriter csvWriter;

    private String csvFile;

    public CSVAccountRepository(String csvFile) throws IOException {
        this.csvFile = csvFile;

        csvReader = new CSVReader(new FileReader(csvFile));
        csvWriter = new CSVWriter(new FileWriter(csvFile, true));
    }

    @Override
    public void addAccount(Account account) throws IOException {
        List<String []> content = csvReader.readAll();
        content.add((account.getUsername() + "#" + account.getPassword() + "#" + account.getBalance()).split("#"));
        csvReader.close();
        csvWriter.writeAll(content);
        csvWriter.close();
    }

    @Override
    public void updateAccount(Account account) throws IOException {
        List<String []> content = csvReader.readAll();
        csvReader.close();

        for(int i = 0; i < content.size(); i++){
            if(content.get(i)[0].equals(account.getUsername())) {
                if(content.get(i)[1].equals(account.getUsername())){
                    String temp[] = new String[account.getStocks().size() + 3];
                    temp[0] = content.get(i)[0];
                    temp[1] = content.get(i)[1];
                    temp[2] = account.getBalance() + "";

                    int j = 3;
                    for(Stock s : account.getStocks()){
                        s.updateStock();
                        temp[j] = s.writeStock();
                        j++;
                    }
                    content.set(i, temp);

                    csvWriter.writeAll(content);
                    csvWriter.close();
                    return;
                }
            }
        }
    }

    @Override
    public List<Account> updateAllAccounts() throws IOException {
        List<String []> content = csvReader.readAll();
        List<Account> accounts = new ArrayList<Account>();
        csvReader.close();

        for(int i = 0; i < content.size(); i++) {
            Account a = Account.createWithBalance(content.get(i)[0], content.get(i)[1], Double.parseDouble(content.get(i)[2]));
            a.setPassword(content.get(i)[1]);

            for(int j = 3; j < content.get(i).length; j++) {
                Stock s = new Stock();
                a.getStocks().add(s.parseStock(content.get(i)[j]));
            }

            accounts.add(a);
        }

        return accounts;
    }

    @Override
    public void clearAccountData() throws IOException {
        csvWriter = new CSVWriter(new FileWriter(csvFile));
        csvWriter.writeAll(new ArrayList<String[]>());
        csvWriter.close();
    }

    @Override
    public boolean accountExists(String username) {
        boolean returnVal = false;

        try {
            List<String []> content = csvReader.readAll();
            for (int i = 0; i < content.size(); i++) {
                if(content.get(i)[0].equals(username)) {
                    returnVal = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnVal;
    }

    @Override
    public Account getAccount(String username, String password) {
        Account account = null;
        List<String []> content = null;
        try {
            content = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < content.size(); i++) {
            if(content.get(i)[0].equals(username)) {
                if(BCrypt.checkpw(password, content.get(i)[1])) {
                    account = Account.createWithBalance(username, password, Double.parseDouble(content.get(i)[2]));
                    account.setPassword(content.get(i)[1]);

                    List<Stock> stocks = new ArrayList<Stock>();

                    for(int j = 3; j < content.get(i).length; j++){
                        Stock s = new Stock();
                        stocks.add(s.parseStock(content.get(i)[j]));
                    }

                    account.setStocks(stocks);
                }
            }
        }

        return account;
    }
}
