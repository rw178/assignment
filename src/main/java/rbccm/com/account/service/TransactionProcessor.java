package rbccm.com.account.service;

import rbccm.com.account.Account;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.strategy.AccumulatorStrategy;

import java.time.LocalTime;

public class TransactionProcessor {

    private final AccountsService accountsService;

    public TransactionProcessor(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    public void processTransaction(Integer accountNumber, Long amount, LocalTime time)
            throws MaxNumberOfAccountsReachedException, ThresholdBreachedException, MaxNumberOfTransactionsException {
        if (!accountsService.hasAccount(accountNumber))
            accountsService.addAccount(new Account(accountNumber, new AccumulatorStrategy()));
        accountsService.getAccount(accountNumber).processAmount(amount, time);
    }

}
