package lu.crx.financing.model;

import lombok.Getter;
import lu.crx.financing.model.entities.Purchaser;

@Getter
public class PurchaserCreditorBpsSettings {
    private Purchaser purchaser;
    private int annualRateInBps;

    public PurchaserCreditorBpsSettings(Purchaser purchaser, int annualRateInBps){
        this.purchaser = purchaser;
        this.annualRateInBps=annualRateInBps;
    }
}
