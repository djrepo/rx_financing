package lu.crx.financing.services;

import lu.crx.financing.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

public class BaseSeedingService {
/*
    @Autowired
    private CreditorRepository creditorRepository;
    @Autowired
    private DebtorRepository debtorRepository;
    @Autowired
    private PurchaserRepository purchaserRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private FinancingRepository financingRepository;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    //@PostConstruct
    public void init(){
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        invoiceRepository.truncateTable();
        debtorRepository.truncateTable();
        purchaserRepository.truncateTable();
        creditorRepository.truncateTable();
        financingRepository.truncateTable();

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
*/

}
