package domain.value;

import java.math.BigDecimal;

public final class Money {

    private final BigDecimal amount;

    private Money(BigDecimal value) {
        this.amount = value;
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public boolean isPositiveOrZero(){
        return this.amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public Money minus(Money money){
        return new Money(this.amount.subtract(money.amount));
    }

    public Double value() {
        return this.amount.doubleValue();
    }

    public Money plus(Money money){
        return new Money(this.amount.add(money.amount));
    }
}
