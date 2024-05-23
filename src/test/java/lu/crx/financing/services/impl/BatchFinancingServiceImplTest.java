package lu.crx.financing.services.impl;

import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.repositories.FactoredInvoiceRepository;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.SeedingService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BatchFinancingServiceImplTest {

    @Autowired
    private BasicSeedingServiceImpl seedingService;
    @Autowired
    private FinancingService financingService;
    @Autowired
    private FactoredInvoiceRepository factoredInvoiceRepository;
    @Test
    public void financeBatchTest(){
        seedingService.seedMasterData();
        seedingService.seedInvoices();
        financingService.finance();
        Iterable<FactoredInvoice> invoices = factoredInvoiceRepository.findAll();

    }
}
