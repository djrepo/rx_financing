package lu.crx.financing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.crx.financing.model.entities.Purchaser;

@Getter
public class PurchaserSettings {
    private Purchaser purchaser;
    private int annualRateInBps;

    public PurchaserSettings(Purchaser purchaser, int annualRateInBps){
        this.purchaser = purchaser;
        this.annualRateInBps=annualRateInBps;
    }
}
