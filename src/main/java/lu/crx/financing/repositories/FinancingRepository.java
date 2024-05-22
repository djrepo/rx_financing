package lu.crx.financing.repositories;

import lu.crx.financing.entities.Creditor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FinancingRepository extends CrudRepository<Creditor, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Financing", nativeQuery = true)
    void truncateTable();
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO FINANCING (INVOICE_ID, PURCHASER_ID, DATE_FULFILMENT, FINANCING_TERM, FINANCING_RATE, EARLY_PAYMENT_AMOUNT)\n" +
            "    SELECT INVOICE_ID, PURCHASER_ID, CURRENT_DATE, FINANCING_TERM, FINANCING_RATE, \n" +
            "           INVOICE.VALUE_IN_CENTS * (1 - FINANCING_RATE / 10000) \n" +
            "    FROM TMP_INVOICE_PURCHASER\n" +
            "    JOIN INVOICE ON INVOICE.ID = TMP_INVOICE_PURCHASER.INVOICE_ID\n" +
            "    WHERE FINANCING_RATE_RANK = 1", nativeQuery = true)
    void populateFromTmpInvoicePurchaser();

}