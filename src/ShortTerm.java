
/**
 * Created by DOTIN SCHOOL 4 on 4/28/2015.
 */
public class ShortTerm extends DepositType {
    private static final double SHORT_INTEREST = (float) 10;

    public ShortTerm() {
        super( );
    }
    public double getRate() {
        return SHORT_INTEREST;
    }



}

