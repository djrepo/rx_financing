package lu.crx.financing.repositories.temporary.impl;

import lu.crx.financing.repositories.temporary.TmpInvoicePurchaserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TmpInvoicePurchaserRepositoryImpl implements TmpInvoicePurchaserRepository {

    @Autowired
    private EntityManager entityManager;

    public void truncate() {
        entityManager.createNativeQuery("" +
                "TRUNCATE TABLE TMP_INVOICE_PURCHASER").executeUpdate();
    }
    public void fillInvoicesAndPurchasers() {
        entityManager.createNativeQuery("" +
                "INSERT INTO TMP_INVOICE_PURCHASER (INVOICE_ID, PURCHASER_ID, FINANCING_TERM, FINANCING_RATE, FINANCING_RATE_RANK)\n" +
                "    SELECT INVOICE_ID, PURCHASER.ID, FINANCING_TERM, FINANCING_RATE, \n" +
                "           ROW_NUMBER() OVER(PARTITION BY INVOICE_ID ORDER BY FINANCING_RATE) AS FINANCING_RATE_RANK \n" +
                "    FROM TMP_INVOICE_RATE\n" +
                "    JOIN PURCHASER_PURCHASER_FINANCING_SETTINGS ON TMP_INVOICE_RATE.PURCHASER_SETTINGS_ID = PURCHASER_PURCHASER_FINANCING_SETTINGS.PURCHASER_FINANCING_SETTINGS_ID\n" +
                "    JOIN PURCHASER ON PURCHASER.ID = PURCHASER_PURCHASER_FINANCING_SETTINGS.PURCHASER_ID\n" +
                "    WHERE FINANCING_TERM > PURCHASER.MINIMUM_FINANCING_TERM_IN_DAYS").executeUpdate();
    }


}
