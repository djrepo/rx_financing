package lu.crx.financing.repositories.temporary.impl.h2;

import lu.crx.financing.repositories.temporary.TmpInvoiceCreditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@Profile("h2")
public class TmpInvoiceCreditorRepositoryImpl implements TmpInvoiceCreditorRepository {

    @Autowired
    private EntityManager entityManager;

    public void truncate() {
        entityManager.createNativeQuery("" +
                "TRUNCATE TABLE TMP_INVOICE_CREDITOR").executeUpdate();
    }
    public void fillInvoicesAndCreditors(int batchSize) {
        entityManager.createNativeQuery("" +
                "INSERT INTO TMP_INVOICE_CREDITOR (INVOICE_ID, CREDITOR_ID, FINANCING_TERM, MAX_FINANCING_RATE_IN_BPS)\n" +
                "    SELECT INVOICE.ID, CREDITOR_ID, DATEDIFF('DAY', CURRENT_DATE, INVOICE.MATURITY_DATE), CREDITOR.MAX_FINANCING_RATE_IN_BPS \n" +
                "    FROM INVOICE\n" +
                "    JOIN CREDITOR ON INVOICE.CREDITOR_ID = CREDITOR.ID\n" +
                "    WHERE INVOICE.ID NOT IN (SELECT INVOICE_ID FROM FINANCING)\n" +
                "    LIMIT "+batchSize).executeUpdate();
    }

    public void fillInvoicesAndCreditorsPostgresSql(int batchSize) {
        entityManager.createNativeQuery("" +
                "INSERT INTO TMP_INVOICE_CREDITOR (INVOICE_ID, CREDITOR_ID, FINANCING_TERM, MAX_FINANCING_RATE_IN_BPS)\n" +
                "    SELECT INVOICE.ID, CREDITOR_ID, (INVOICE.MATURITY_DATE - CURRENT_DATE), CREDITOR.MAX_FINANCING_RATE_IN_BPS \n" +
                "    FROM INVOICE\n" +
                "    JOIN CREDITOR ON INVOICE.CREDITOR_ID = CREDITOR.ID\n" +
                "    WHERE INVOICE.ID NOT IN (SELECT INVOICE_ID FROM FINANCING)\n" +
                "    LIMIT "+batchSize).executeUpdate();
    }


}
