package lu.crx.financing.repositories;

import lu.crx.financing.model.entities.FactoredInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.SQLType;
import java.sql.Types;
import java.util.List;
@Repository
public class FactoredInvoiceJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void saveAll(List<FactoredInvoice> invoices){
        jdbcTemplate.batchUpdate("INSERT INTO PUBLIC.FACTORED_INVOICE\n" +
                "(INVOICE_ID, CREATED_ON, EARLY_PAYMENT_AMOUNT, FINANCING_RATE, FINANCING_TERM, FUNDING_DATE, IS_PAYABLE, PURCHASER_ID)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)",invoices,500, (ps, inv) -> {
            int col = 1;
            ps.setLong(col++, inv.getInvoiceId());
            ps.setDate(col++, Date.valueOf(inv.getCreatedOn()));
            ps.setLong(col++, inv.getEarlyPaymentAmount());
            ps.setDouble(col++, inv.getFinancingRate());
            ps.setLong(col++, inv.getFinancingTerm());
            if (inv.getFundingDate() == null){
                ps.setNull(col++, Types.DATE);
            }else {
                ps.setDate(col++, Date.valueOf(inv.getFundingDate()));
            }
            ps.setBoolean(col++, inv.isPayable());
            ps.setLong(col++, inv.getPurchaserId());
        });
    }
}
