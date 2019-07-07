package models.discounts;

public class MediumDiscountStrategy implements DiscountStrategy {

    @Override
    public double applyDiscount(double price) {
        return (price - (price * 0.40));
    }
}
