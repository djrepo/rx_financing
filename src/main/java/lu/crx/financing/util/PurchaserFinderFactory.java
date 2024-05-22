package lu.crx.financing.util;

import lu.crx.financing.model.PurchaserSettings;
import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.services.components.CreditorCache;
import lu.crx.financing.services.components.PurchaserCache;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


public class PurchaserFinderFactory {

    private final LocalDate fulfilmentDate;
    private final CreditorCache creditorCache;
    private final PurchaserCache purchaserCache;
    public PurchaserFinderFactory(LocalDate fulfilmentDate, CreditorCache creditorCache, PurchaserCache purchaserCache){
        this.fulfilmentDate = fulfilmentDate;
        this.creditorCache=creditorCache;
        this.purchaserCache=purchaserCache;
    }

    public PurchaserFinder createPurchaserFinder(Invoice invoice){
        PurchaserFinder purchaserFinder = new PurchaserFinder(invoice);
        purchaserFinder.setPurchaserSettingsList(purchaserCache.getPurchaserSettingsByCreditorId(invoice.getCreditorId()));
        purchaserFinder.setCreditor(creditorCache.getCreditorById(invoice.getCreditorId()));
        purchaserFinder.setFinancingTerm(calcFinancingTerm(invoice));
        return purchaserFinder;
    }

    public FactoredInvoice createFactoredInvoice(PurchaserFinder purchaserFinder, PurchaserSettings purchaserSettings){
        FactoredInvoice factoredInvoice = new FactoredInvoice();
        factoredInvoice.setInvoiceId(purchaserFinder.getInvoice().getId());
        factoredInvoice.setDateFulfilment(fulfilmentDate);
        factoredInvoice.setFinancingTerm(purchaserFinder.getFinancingTerm());
        factoredInvoice.setPayable(false);
        factoredInvoice.setViolatesCreditorMaxBps(false);
        double financingRate = purchaserFinder.getFinancingRate();
        if (purchaserSettings!=null) {
            factoredInvoice.setPurchaserId(purchaserSettings.getPurchaser().getId());
            factoredInvoice.setFinancingRate(Math.round(financingRate));
            factoredInvoice.setEarlyPaymentAmount(Math.round(purchaserFinder.getInvoice().getValueInCents() * (1 - financingRate / 10000)));
        }
        if (financingRate < purchaserFinder.getCreditor().getMaxFinancingRateInBps()) {
            factoredInvoice.setPayable(true);
        }else {
            factoredInvoice.setViolatesCreditorMaxBps(true);
        }
        return factoredInvoice;
    }

    public long calcFinancingTerm(Invoice invoice){
        return DAYS.between(fulfilmentDate,invoice.getMaturityDate());
    }


}
