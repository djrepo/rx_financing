package lu.crx.financing.util;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.helpers.FactoredFinancingHelper;
import lu.crx.financing.model.PurchaserInfo;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.services.components.CreditorCache;
import lu.crx.financing.services.components.PurchaserCache;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class InvoiceFactoringProcess {

    private final LocalDate fundingDate;
    private final CreditorCache creditorCache;
    private final PurchaserCache purchaserCache;

    public InvoiceFactoringProcess(LocalDate fundingDate, CreditorCache creditorCache, PurchaserCache purchaserCache) {
        this.fundingDate = fundingDate;
        this.creditorCache = creditorCache;
        this.purchaserCache = purchaserCache;
    }


    private PurchaserFinder createPurchaserFinder(Invoice invoice) {
        return PurchaserFinder.builder()
                .invoice(invoice)
                .availablePurchasers(purchaserCache.getPurchaserSettingsByCreditorId(invoice.getCreditorId()))
                .creditor(creditorCache.getCreditorById(invoice.getCreditorId()))
                .fundingDate(fundingDate)
                .build();
    }

    private FactoredInvoice createFactoredInvoice(Invoice invoice, PurchaserInfo purchaserInfo) {
        FactoredInvoice factoredInvoice = FactoredInvoice.builder()
                .payable(false)
                .createdOn(LocalDate.now())
                .invoiceId(invoice.getId())
                .build();
        if (purchaserInfo != null) {
            factoredInvoice.setPayable(true);
            factoredInvoice.setFinancingTerm(purchaserInfo.getFinancingTerm());
            factoredInvoice.setFundingDate(fundingDate);
            factoredInvoice.setPurchaserId(purchaserInfo.getPurchaser().getId());
            factoredInvoice.setFinancingRate(purchaserInfo.getFinancingRate());
            factoredInvoice.setEarlyPaymentAmount(FactoredFinancingHelper.calcEarlyPaymentAmount(purchaserInfo));
        }
        return factoredInvoice;
    }


    public FactoredInvoice finance(Invoice invoice) {
        PurchaserFinder purchaserFinder = createPurchaserFinder(invoice);
        List<PurchaserInfo> sortedPurchaserByBps = purchaserFinder.getSortedPurchaserByBps();
        PurchaserInfo purchaserInfo = purchaserFinder.findFirstPayable(sortedPurchaserByBps);
        return createFactoredInvoice(invoice, purchaserInfo);
    }
}
