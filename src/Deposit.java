import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by DOTIN SCHOOL 4 on 4/25/2015.
 */
public  class Deposit implements Comparable<Deposit> {
    private static final int DAYS_OF_YEAR = 36500;
    public String customerNumber;
    private DepositType depositType;
    protected BigDecimal depositInterest;
    protected BigDecimal depositBalance;
    protected long durationDays;


    public Deposit(String customerNumber, BigDecimal depositBalance, long durationDays, DepositType depositType) {
        this.customerNumber = customerNumber;
        this.depositType = depositType;
//        this.depositInterest = depositInterest;
        if(depositBalance.doubleValue()<0)
            throw new NegativeValueExeption("the deposit balance is negative!!");
        else
            this.depositBalance = depositBalance;
        if(durationDays<=0)
            throw new NegativeValueExeption("the number of days is negative or zero!!");
        this.durationDays = durationDays;
    }
    //hghfhg
    //@@@@@@@@@@@@@@@@@@@@@@
//    public BigDecimal getDepositBalance()
//    {
//        return depositBalance;
//    }
//    public Long getDurationDays()
//    {
//        return durationDays;
//    }
    public BigDecimal depositInterestCal() {

        depositInterest = depositBalance.multiply(new BigDecimal(depositType.getRate()))
                .multiply(new BigDecimal(durationDays)).divide(new BigDecimal(DAYS_OF_YEAR), 3, RoundingMode.CEILING);
        return depositInterest;
    }
    //public void setDepositType()
    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
    public int compareTo(Deposit deposit) {
        double compareScore = deposit.depositInterestCal().doubleValue();
        return depositInterest.doubleValue() < compareScore ? 1 : depositInterest.doubleValue() > compareScore ? -1 : 0; //descending comprator!!
    }


}
