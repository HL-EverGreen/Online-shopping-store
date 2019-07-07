package models.discounts;

public class DiscountApplier {
    private DiscountStrategy discountStrategy;
    private double price;

    public DiscountApplier(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double apply(double price) {
        return discountStrategy.applyDiscount(price);
    }
}
