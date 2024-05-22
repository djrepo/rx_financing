package lu.crx.financing.util;

import lombok.Getter;
import lombok.Setter;
import lu.crx.financing.model.PurchaserSettings;
import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.model.entities.Invoice;

import java.util.List;
@Setter
@Getter
public class PurchaserFinder {
    private Invoice invoice;
    private List<PurchaserSettings> purchaserSettingsList;
    private Creditor creditor;
    private long financingTerm;
    private double financingRate;
    public PurchaserFinder(Invoice invoice) {
        this.invoice = invoice;
    }
    public PurchaserSettings findPurchaserWithMinBps(){
        double minBps = Double.MAX_VALUE;
        PurchaserSettings bestPurchaserSettings = null;
        for (PurchaserSettings purchaserSettings : purchaserSettingsList) {
            double financingRate = calcFinancingRate(purchaserSettings);
            if (financingTerm > purchaserSettings.getPurchaser().getMinimumFinancingTermInDays()) {
                if (financingRate < minBps) {
                    minBps = financingRate;
                    bestPurchaserSettings = purchaserSettings;
                }
            }
        }
        return bestPurchaserSettings;
    }

    private double calcFinancingRate(PurchaserSettings purchaserSettings){
        return financingTerm * purchaserSettings.getAnnualRateInBps() * 1.0 / 365;
    }
}
