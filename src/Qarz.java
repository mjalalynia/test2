

/**
 * Created by DOTIN SCHOOL 4 on 4/28/2015.
 */
public class Qarz extends DepositType {
    private static final double QARZ_INTEREST = (float) 0;

    public Qarz() {
        super( );
    }

    public double getRate() {
        return QARZ_INTEREST;
    }
}
