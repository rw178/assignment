package rbccm.com.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.strategy.ProcessingStrategy;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountTest {
    @Mock
    private ProcessingStrategy strategy;

    @Test
    public void testAlert() throws MaxNumberOfTransactionsException {
        Account account = new Account(1, strategy);
        when(strategy.isAnAlertToBeRaised(any(), any())).thenReturn(false).thenReturn(true);
        assertThrows(ThresholdBreachedException.class, () -> {
            account.processAmount(1L, LocalTime.NOON);
            account.processAmount(1L, LocalTime.NOON);
        });
    }
}