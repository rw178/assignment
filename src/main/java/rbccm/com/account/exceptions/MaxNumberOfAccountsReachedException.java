package rbccm.com.account.exceptions;

public class MaxNumberOfAccountsReachedException extends Throwable {
    public MaxNumberOfAccountsReachedException(String msg) {
        super(msg);
    }
}
