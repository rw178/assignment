package rbccm.com.account.strategy;

import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;

import java.time.LocalTime;

public interface ProcessingStrategy {

    boolean isAnAlertToBeRaised(Long amount, LocalTime transactionTime) throws MaxNumberOfTransactionsException;

    default Integer getMaxNumberOfTransactions() {
        return 10000;
    }

    default Long getTriggerValue() {
        return 50000L;
    }
    default Long getWindowSizeInSeconds() {
        return 60L;
    }

    Integer getNumberOfRecords();


}
