package lu.crx.financing.helpers;

import lu.crx.financing.model.PurchaserCreditorBpsSettings;
import lu.crx.financing.model.PurchaserInfo;
import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.model.entities.Purchaser;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class FactoredFinancingHelper {

    public static boolean isFinancingTermViolatePurchaserSettings(PurchaserInfo purchaserInfo, Purchaser purchaser) {
        return purchaserInfo.getFinancingTerm() < purchaser.getMinimumFinancingTermInDays();
    }

    public static boolean isFinancingRateViolatesCreditorSettings(PurchaserInfo purchaserInfo, Creditor creditor) {
        return purchaserInfo.getFinancingRate() >= creditor.getMaxFinancingRateInBps();
    }

    public static double calcFinancingRate(long financingTerm, PurchaserCreditorBpsSettings purchaserCreditorBpsSettings) {
        return financingTerm * purchaserCreditorBpsSettings.getAnnualRateInBps() * 1.0 / 365;
    }

    public static long calcEarlyPaymentAmount(PurchaserInfo purchaserInfo, long financingRate) {
        return Math.round(purchaserInfo.getInvoice().getValueInCents() * (1 - financingRate * 1.0 / 10000));
    }

    public static long calcFinancingTerm(Invoice invoice, LocalDate fundingDate) {
        return DAYS.between(fundingDate, invoice.getMaturityDate());
    }


}
