package lu.crx.financing.repositories.temporary.impl;

import lu.crx.financing.repositories.temporary.TmpInvoicePurchaserRepository;
import lu.crx.financing.repositories.temporary.TmpInvoiceRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TmpInvoiceRateRepositoryImpl implements TmpInvoiceRateRepository {

    @Autowired
    private EntityManager entityManager;

    public void truncate() {
        entityManager.createNativeQuery("" +
                "TRUNCATE TABLE TMP_INVOICE_RATE").executeUpdate();
    }
    public void fillInvoicesAndRates() {
        entityManager.createNativeQuery("" +
                "INSERT INTO TMP_INVOICE_RATE (INVOICE_ID, PURCHASER_SETTINGS_ID, FINANCING_TERM, FINANCING_RATE)\n" +
                "    SELECT INVOICE_ID, PURCHASER_FINANCING_SETTINGS.ID, FINANCING_TERM, \n" +
                "           FINANCING_TERM * PURCHASER_FINANCING_SETTINGS.ANNUAL_RATE_IN_BPS * 1.0 / 365 \n" +
                "    FROM TMP_INVOICE_CREDITOR\n" +
                "    JOIN PURCHASER_FINANCING_SETTINGS ON TMP_INVOICE_CREDITOR.CREDITOR_ID = PURCHASER_FINANCING_SETTINGS.CREDITOR_ID\n" +
                "    WHERE FINANCING_TERM * PURCHASER_FINANCING_SETTINGS.ANNUAL_RATE_IN_BPS * 1.0 / 365 < TMP_INVOICE_CREDITOR.MAX_FINANCING_RATE_IN_BPS").executeUpdate();
    }


}
