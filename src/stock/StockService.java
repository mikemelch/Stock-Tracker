package stock;

import account.Account;
import account.AccountService;

import java.io.IOException;
import java.util.List;

public class StockService {
    private AccountService accountService;

    public StockService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void buyStock(Account account, String ticker, double price, int shares) throws IOException {
        account.setBalance(account.getBalance() - price*shares);
        account.getStocks().add(new Stock(ticker, shares, price));

        accountService.updateAccount(account);
    }

    public void sellStock(Account account, String ticker, int shares) throws IOException {
        List<Stock> stocks = account.getStocks();

        for(int i = 0; i < stocks.size(); i++) {
            Stock s = stocks.get(i);
            if(s.getTicker().equalsIgnoreCase(ticker)) {
                if(s.getShares() == shares) {
                    account.setBalance(account.getBalance() + s.modifyStock(shares));
                    stocks.remove(s);
                } else {
                    account.setBalance(account.getBalance() + s.modifyStock(shares));
                }
            }
        }

        accountService.updateAccount(account);
    }
}
