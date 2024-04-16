package rbccm.com.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.service.AccountsService;
import rbccm.com.account.service.TransactionProcessor;

import java.time.LocalTime;

import static rbccm.com.account.service.AccountsService.MAX_NUMBER_OF_ACCOUNTS;

@ExtendWith(MockitoExtension.class)
public class IntegrationTest {

    private TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        transactionProcessor = new TransactionProcessor(new AccountsService());
    }

    @Test
    @Disabled("Unable to run this test on the GitHub runner")
    public void stressTest() throws MaxNumberOfAccountsReachedException, MaxNumberOfTransactionsException, ThresholdBreachedException {
        for (int i = 0; i < MAX_NUMBER_OF_ACCOUNTS; i++) {
            for (int j = 0; j < 10000; j++) {
                transactionProcessor.processTransaction(i, 1L, LocalTime.now());
            }
        }
    }
}
