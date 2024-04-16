package rbccm.com.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rbccm.com.account.Account;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionProcessorTest {

    @Mock
    private AccountsService accountsService;

    @Mock
    private Account account;

    private TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        transactionProcessor = new TransactionProcessor(accountsService);
    }

    @Test
    public void testTypicalFlow() throws MaxNumberOfAccountsReachedException, ThresholdBreachedException, MaxNumberOfTransactionsException {
        when(accountsService.hasAccount(any())).thenReturn(false);
        when(accountsService.getAccount(any())).thenReturn(account);

        transactionProcessor.processTransaction(1, 1L, LocalTime.now());
        verify(accountsService).addAccount(any());
        verify(account).processAmount(any(), any());
    }

}