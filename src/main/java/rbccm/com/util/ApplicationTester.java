package rbccm.com.util;

import rbccm.com.account.Account;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.service.AccountsService;
import rbccm.com.account.strategy.AccumulatorStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApplicationTester {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");

    public static void main(String[] args) throws IOException {
        AccountsService accountsService = new AccountsService();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ApplicationTester.class.getResourceAsStream("/input2.csv")))) {
            String line = br.readLine(); //skip the header
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                LocalTime time = LocalTime.parse(lineItems[0], formatter);
                Long amount = Long.valueOf(lineItems[1]);
                Integer accountNumber = Integer.valueOf(lineItems[2]);

                try {
                    if (!accountsService.hasAccount(accountNumber))
                        accountsService.addAccount(new Account(accountNumber, new AccumulatorStrategy()));
                    Account account = accountsService.getAccount(accountNumber);
                    try {
                        account.processAmount(amount, time);
                        System.out.println(time + "," + amount + "," + accountNumber + ",N");
                    } catch (ThresholdBreachedException ex) {
                        System.out.println(time + "," + amount + "," + accountNumber + ",Y");
                    } catch (MaxNumberOfTransactionsException e) {
                        System.out.println(time + "," + amount + "," + accountNumber + ",S"); //Skipped
                    }
                } catch (MaxNumberOfAccountsReachedException ex) {
                    System.out.println(time + "," + amount + "," + accountNumber + ",S"); //Skipped
                }
            }
        }
    }
}