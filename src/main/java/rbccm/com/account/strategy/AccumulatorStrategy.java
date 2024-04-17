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
        Long prevCumulative = 0L;
        Long newAmount = Math.abs(amount);
        if (!map.isEmpty()) {
            TransactionDetail previousItem = map.get(timeStampOfLastTransaction);
            prevCumulative = previousItem.amount() + previousItem.prevCumulative();
            if (previousItem.time().equals(transactionTime)) {
                map.remove(transactionTime); //remove the previous transaction if it occurred at the same time and replace with a (new) cumulative transaction
            } else {
                LocalTime timeOfClosestTransaction = map.ceilingKey(transactionTime.minusSeconds(getWindowSizeInSeconds())); //Find the first record in the given time frame
                if (timeOfClosestTransaction != null) {
                    if (timeOfClosestTransaction.plusSeconds(getWindowSizeInSeconds()).equals(transactionTime)) //An exact match, i.e. it falls withing the window
                        prevCumulative = prevCumulative - map.get(timeOfClosestTransaction).prevCumulative() + map.get(timeOfClosestTransaction).amount();
                    if (timeOfClosestTransaction.plusSeconds(getWindowSizeInSeconds()).isAfter(transactionTime)) //There is a record in the window
                        prevCumulative = prevCumulative - map.get(timeOfClosestTransaction).prevCumulative();
                } else {
                    prevCumulative = 0L; //No records fall within the window
                }
            }
        }
        map.put(transactionTime, new TransactionDetail(transactionTime, newAmount, prevCumulative));
        timeStampOfLastTransaction = transactionTime;
        return prevCumulative + Math.abs(amount) >= getTriggerValue();
    }

    @Override
    public Integer getNumberOfRecords() {
        return map.size();
    }
}
