package models.discounts;

public class LowDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return price - (price * 0.20);
    }
}
