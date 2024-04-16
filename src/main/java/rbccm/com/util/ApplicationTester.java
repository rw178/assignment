package rbccm.com.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import rbccm.com.account.exceptions.MaxNumberOfAccountsReachedException;
import rbccm.com.account.exceptions.MaxNumberOfTransactionsException;
import rbccm.com.account.exceptions.ThresholdBreachedException;
import rbccm.com.account.service.AccountsService;
import rbccm.com.account.service.TransactionProcessor;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApplicationTester {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");

    public static void main(String[] args) throws IOException {
        TransactionProcessor transactionProcessor = new TransactionProcessor(new AccountsService());
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(args[0])).withSkipLines(1).build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                LocalTime time = LocalTime.parse(values[0], formatter);
                Long amount = Long.valueOf(values[1]);
                Integer accountNumber = Integer.valueOf(values[2]);
                try {
                    transactionProcessor.processTransaction(accountNumber, amount, time);
                    System.out.println(time + "," + amount + "," + accountNumber + ",N");

                    //exceptions would always be logged as well using the relevant logging framework (e.g. log4j)
                } catch (ThresholdBreachedException ex) {
                    System.out.println(time + "," + amount + "," + accountNumber + ",Y");
                } catch (MaxNumberOfTransactionsException e) {
                    System.out.println(time + "," + amount + "," + accountNumber + ",S"); //Skipped
                } catch (MaxNumberOfAccountsReachedException ex) {
                    System.out.println(time + "," + amount + "," + accountNumber + ",S"); //Skipped
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}