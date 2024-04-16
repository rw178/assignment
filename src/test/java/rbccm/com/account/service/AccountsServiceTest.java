package rbccm.com.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rbccm.com.account.Account;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.strategy.ProcessingStrategy;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AccountsServiceTest {

    private AccountsService accountsService;
    @Mock
    private ProcessingStrategy strategy;

    @BeforeEach
    void setUp() {
        accountsService = new AccountsService();
    }

    @Test
    public void testSize() {
        assertThrows(MaxNumberOfAccountsReachedException.class, () -> {
            for (int i = 0; i <= AccountsService.MAX_NUMBER_OF_ACCOUNTS; i++) {
                accountsService.addAccount(new Account(i, strategy));
            }
        });
    }
}