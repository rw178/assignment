package rbccm.com.account.service;

import rbccm.com.account.Account;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;

import java.util.HashMap;
import java.util.Map;

/**
 * A service to keep track of all created accounts and enforce restrictions on the number of accounts
 */
public class AccountsService {
    private final Map<Integer, Account> accounts = new HashMap<>();
    public static final int MAX_NUMBER_OF_ACCOUNTS = 5000;

    public void addAccount(Account account) throws MaxNumberOfAccountsReachedException {
        if (accounts.size() == MAX_NUMBER_OF_ACCOUNTS)
            throw new MaxNumberOfAccountsReachedException("A maximum of " + MAX_NUMBER_OF_ACCOUNTS + " accounts can be created.");
        accounts.putIfAbsent(account.getAccountNumber(), account);
    }

    public Account getAccount(Integer accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean hasAccount(Integer accountNumber) {
        return accounts.containsKey(accountNumber);
    }

}
