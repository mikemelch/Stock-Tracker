package account;

import java.io.IOException;
import java.util.List;

public interface AccountRepository {
    void addAccount(Account account) throws IOException;
    void updateAccount(Account account) throws IOException;
    void clearAccountData() throws IOException;
    List<Account> updateAllAccounts() throws IOException;
    boolean accountExists(String username);
    Account getAccount(String username, String password);
}
