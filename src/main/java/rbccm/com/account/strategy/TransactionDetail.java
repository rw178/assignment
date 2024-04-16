package rbccm.com.account.strategy;

import java.time.LocalTime;

public record TransactionDetail(LocalTime time,
                                Long amount,
                                Long runningTotal) {
}
