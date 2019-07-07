package models.purchase;

public class NormalFee implements PurchaseFee {

    Double fee;

    public NormalFee(Double fee) {
        this.fee = fee;
    }

    @Override
    public Double getFee() {
        return fee;
    }
}
