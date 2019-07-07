package models.purchase;

import models.discounts.DiscountApplier;

public class PurchaseFeeDiscountDecorator implements PurchaseFee {

    protected PurchaseFee purchaseFee;
    protected DiscountApplier discountApplier;

    public PurchaseFeeDiscountDecorator(PurchaseFee purchaseFee, DiscountApplier discountApplier) {
        this.purchaseFee = purchaseFee;
        this.discountApplier = discountApplier;
    }

    @Override
    public Double getFee() {
        if (discountApplier == null) {
            return purchaseFee.getFee();
        }
        return Math.ceil(discountApplier.apply(purchaseFee.getFee()));
    }
}
