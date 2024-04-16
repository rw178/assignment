package rbccm.com.account;

import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.strategy.ProcessingStrategy;

import java.time.LocalTime;

public class Account {

    private final Integer accountNumber;
    private final ProcessingStrategy strategy;
    public Account(Integer accountNumber, ProcessingStrategy strategy) {
        this.accountNumber = accountNumber;
        this.strategy = strategy;
    }
    public int getAccountNumber() {
        return accountNumber;
    }

    public void processAmount(Long amount, LocalTime time) throws ThresholdBreachedException, MaxNumberOfTransactionsException {
        if (strategy.isAnAlertToBeRaised(amount, time))
            throw new ThresholdBreachedException(time + "," + amount + "," + accountNumber);
    }
}
