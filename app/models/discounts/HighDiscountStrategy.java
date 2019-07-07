package models.discounts;

public class HighDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return (price - (price * 0.60));
    }
}
