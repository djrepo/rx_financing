package lu.crx.financing.util;

import lu.crx.financing.model.PurchaserCreditorBpsSettings;
import lu.crx.financing.model.PurchaserInfo;
import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.model.entities.Invoice;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class FactoredFinancingHelper {

    public static boolean isFinancingTermViolatePurchaserSettings(PurchaserInfo purchaserInfo){
        return purchaserInfo.getFinancingTerm() < purchaserInfo.getPurchaser().getMinimumFinancingTermInDays();
    }

    public static boolean isFinancingRateViolatesCreditorSettings(PurchaserInfo purchaserInfo, Creditor creditor){
        return purchaserInfo.getFinancingRate() >= creditor.getMaxFinancingRateInBps();
    }

    public static double calcFinancingRate(long financingTerm, PurchaserCreditorBpsSettings purchaserCreditorBpsSettings){
        return financingTerm * purchaserCreditorBpsSettings.getAnnualRateInBps() * 1.0 / 365;
    }

    public static long caclEarlyPaymentAmount(PurchaserInfo purchaserInfo) {
        return Math.round(purchaserInfo.getInvoice().getValueInCents() * (1 - purchaserInfo.getFinancingRate() / 10000));
    }

    public static long calcFinancingTerm(Invoice invoice, LocalDate fundingDate){
        return DAYS.between(fundingDate,invoice.getMaturityDate());
    }



}
