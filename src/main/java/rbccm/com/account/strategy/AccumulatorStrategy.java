package rbccm.com.account.strategy;

import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;

import java.time.LocalTime;
import java.util.TreeMap;

public class AccumulatorStrategy implements ProcessingStrategy {


    private final TreeMap<LocalTime, TransactionDetail> map = new TreeMap<>();

    private LocalTime timeStampOfLastTransaction = null;

    @Override
    public boolean isAnAlertToBeRaised(Long amount, LocalTime transactionTime) throws MaxNumberOfTransactionsException {
        if (map.keySet().size() >= getMaxNumberOfTransactions()) {
            throw new MaxNumberOfTransactionsException("Maximum number of transactions of " + getMaxNumberOfTransactions() + " reached");
        }
        Long runningTotal = 0L;
        Long newAmount = Math.abs(amount);
        if (!map.isEmpty()) {
            TransactionDetail previousItem = map.get(timeStampOfLastTransaction);
            runningTotal = previousItem.amount() + previousItem.runningTotal();
            if (previousItem.time().equals(transactionTime)) {
                map.remove(transactionTime); //remove the previous transaction if it occurred at the same time and replace with a (new) cumulative transaction
            } else {
                //Find the "closest" record in the given time frame
                LocalTime timeOfClosestTransaction = map.floorKey(transactionTime.minusSeconds(getWindowSizeInSeconds()));
                if (timeOfClosestTransaction != null) {
                    if (timeOfClosestTransaction.plusSeconds(getWindowSizeInSeconds()).equals(transactionTime)) //An exact match, i.e. it falls withing the window
                        runningTotal = runningTotal - map.get(timeOfClosestTransaction).runningTotal() + map.get(timeOfClosestTransaction).amount();
                    else
                        runningTotal = runningTotal - map.get(timeOfClosestTransaction).runningTotal() - map.get(timeOfClosestTransaction).amount();
                }
            }
        }
        map.put(transactionTime, new TransactionDetail(transactionTime, newAmount, runningTotal));
        timeStampOfLastTransaction = transactionTime;
        return runningTotal + Math.abs(amount) >= getTriggerValue();
    }

    @Override
    public Integer getNumberOfTransactions() {
        return map.size();
    }
}
