package rbccm.com.account.exceptions;

public class ThresholdBreachedException extends Throwable {
    public ThresholdBreachedException(String msg) {
        super(msg);
    }
}
