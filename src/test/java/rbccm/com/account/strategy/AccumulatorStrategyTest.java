package rbccm.com.account.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class AccumulatorStrategyTest {

    private AccumulatorStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new AccumulatorStrategy();
    }

    @Test
    public void testTransactionSize() {
        LocalTime time = LocalTime.now();
        assertThrows(MaxNumberOfTransactionsException.class, () -> {
            for (long i = 0; i <= strategy.getMaxNumberOfTransactions(); i++) {
                strategy.isAnAlertToBeRaised(123L, time.plusSeconds(i));
            }
        });
    }

    @Test
    public void testTransactionsHappeningAtTheSameTime() throws MaxNumberOfTransactionsException {
        LocalTime time = LocalTime.now();
        for (long i = 0; i <= 10; i++) {
            strategy.isAnAlertToBeRaised(123L, time);
        }
        assertEquals(1, strategy.getNumberOfRecords());
    }


    @Test
    public void testNormalThresholdBreachInFixedWindowWithNegativeAmounts() throws MaxNumberOfTransactionsException {
        LocalTime time = LocalTime.now();
        for (long i = 0; i < 10; i++) {
            strategy.isAnAlertToBeRaised(-1 * strategy.getTriggerValue() / 10, time);
        }
        assertTrue(strategy.isAnAlertToBeRaised(strategy.getTriggerValue() / 2, time));
    }

    @Test
    public void testThresholdBreach() throws MaxNumberOfTransactionsException {
        LocalTime time = LocalTime.now();
        assertFalse(strategy.isAnAlertToBeRaised(10000L, time.plusSeconds(0)));
        assertFalse(strategy.isAnAlertToBeRaised(10000L, time.plusSeconds(10)));
        assertFalse(strategy.isAnAlertToBeRaised(10000L, time.plusSeconds(20)));
        assertFalse(strategy.isAnAlertToBeRaised(10000L, time.plusSeconds(30)));
        assertTrue(strategy.isAnAlertToBeRaised(10000L, time.plusSeconds(40)));
        assertTrue(strategy.isAnAlertToBeRaised(20000L, time.plusSeconds(80)));
        assertFalse(strategy.isAnAlertToBeRaised(25000L, time.plusSeconds(300)));
    }

}