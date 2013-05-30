package account;

import java.io.IOException;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addAccount(Account account) throws IOException {
        accountRepository.addAccount(account);
    }

    public void updateAccount(Account account) throws IOException {
        accountRepository.updateAccount(account);
    }

    public Account getAccount(String username, String password) {
        return accountRepository.getAccount(username, password);
    }

    public boolean accountExists(String username) {
        return accountRepository.accountExists(username);
    }

    public void clearAccountData() throws IOException {
        accountRepository.clearAccountData();
    }

    public void updateAllAccounts() throws IOException {
        List<Account> accounts = accountRepository.updateAllAccounts();

        for (Account account : accounts) {
            updateAccount(account);
        }
    }
}
