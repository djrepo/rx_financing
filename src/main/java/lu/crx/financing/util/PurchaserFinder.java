package lu.crx.financing.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lu.crx.financing.model.PurchaserCreditorBpsSettings;
import lu.crx.financing.model.PurchaserInfo;
import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.model.entities.Invoice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
public class PurchaserFinder {
    private Invoice invoice;
    private List<PurchaserCreditorBpsSettings> availablePurchasers;
    private Creditor creditor;
    private LocalDate fundingDate;

    public List<PurchaserInfo> getSortedPurchaserByBps(){
        long financingTerm = FactoredFinancingHelper.calcFinancingTerm(invoice, fundingDate);
        List<PurchaserInfo> purchaserInfos = availablePurchasers.stream()
                .map(p -> PurchaserInfo.builder()
                        .invoice(invoice)
                        .purchaser(p.getPurchaser())
                        .financingTerm(financingTerm)
                        .financingRate(FactoredFinancingHelper.calcFinancingRate(financingTerm, p))
                        .build()
                ).collect(Collectors.toList());
        return purchaserInfos.stream().sorted(Comparator.comparingDouble(PurchaserInfo::getFinancingRate)).collect(Collectors.toList());
    }

    public PurchaserInfo findFirstPayable(List<PurchaserInfo> sortedPurchaserInfos) {
        for (PurchaserInfo purchaserInfo : sortedPurchaserInfos) {
            evaluatePurchaserInfos(purchaserInfo);
            if (purchaserInfo.isPayable()){
                return purchaserInfo;
            }
        }
        return null;
    }

    private void evaluatePurchaserInfos(PurchaserInfo purchaserInfo) {
        boolean isPayable = true;
        if (FactoredFinancingHelper.isFinancingTermViolatePurchaserSettings(purchaserInfo)){
            purchaserInfo.setViolatesPurchaserMinTerm(true);
            isPayable = false;
        }
        if (FactoredFinancingHelper.isFinancingRateViolatesCreditorSettings(purchaserInfo, creditor)){
            purchaserInfo.setViolatesCreditorSettings(true);
            isPayable = false;
        }
        purchaserInfo.setPayable(isPayable);
    }


}
