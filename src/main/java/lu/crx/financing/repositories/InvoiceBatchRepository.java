package lu.crx.financing.repositories;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class InvoiceBatchRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void saveAll(List<Invoice> invoiceList) {
        jdbcTemplate.batchUpdate("INSERT INTO INVOICE (ID,MATURITY_DATE,VALUE_IN_CENTS,CREDITOR_ID,DEBTOR_ID)" +
                        "values" +
                        "(?,?,?,?,?)", invoiceList, 500,
                new ParameterizedPreparedStatementSetter<Invoice>()  {
                    int i = 0;
                    @Override
                    public void setValues(PreparedStatement ps, Invoice invoice) throws SQLException {
                        i++;
                        int col = 1;
                        ps.setLong(col++, i);
                        ps.setDate(col++, Date.valueOf(invoice.getMaturityDate()));
                        ps.setLong(col++, invoice.getValueInCents());
                        ps.setLong(col++, invoice.getCreditor().getId());
                        ps.setLong(col++, invoice.getDebtor().getId());
                        if (i%5000==0) {
                            log.info("Stored "+ i +" Invoices");
                        }
                    }
                });
    }
}
