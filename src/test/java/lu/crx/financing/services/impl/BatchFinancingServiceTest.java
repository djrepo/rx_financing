package lu.crx.financing.services.impl;

import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.repositories.FactoredInvoiceRepository;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.SeedingService;
import lu.crx.financing.util.CsvReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"h2mem","basic","junit"})
public class BatchFinancingServiceTest {

    @Autowired
    private SeedingService seedingService;
    @Autowired
    private FinancingService financingService;
    @Autowired
    private FactoredInvoiceRepository factoredInvoiceRepository;
    @Test
    public void financeBatchTest() throws IOException {
        seedingService.seedMasterData();
        seedingService.seedInvoices();
        financingService.finance();
        Iterable<FactoredInvoice> invoices = factoredInvoiceRepository.findAll();
        List<FactoredInvoice> currentFactoredInvoices = StreamSupport.stream(invoices.spliterator(), false).sorted(Comparator.comparingLong(FactoredInvoice::getInvoiceId)).collect(Collectors.toList());
        List<FactoredInvoice> expectedFactoredInvoices = loadExpectedFactoredInvoice();
        assertEquals(currentFactoredInvoices.size(), expectedFactoredInvoices.size(),"Invoice list size differ");
        for (int i = 0;i<currentFactoredInvoices.size();i++){
            FactoredInvoice currentFactoredInvoice = currentFactoredInvoices.get(i);
            FactoredInvoice expectedFactoredInvoice = expectedFactoredInvoices.get(i);
            assertFactoredInvoiceEquals(currentFactoredInvoice,expectedFactoredInvoice);
        }
    }

    private void assertFactoredInvoiceEquals(FactoredInvoice currentFactoredInvoice, FactoredInvoice expectedFactoredInvoice) {
        String msg = "Invoice id "+currentFactoredInvoice.getInvoiceId();
        if (currentFactoredInvoice.getInvoiceId() != expectedFactoredInvoice.getInvoiceId()){
            throw new AssertionFailedError(msg+" invoiceId not matched");
        }
        if (currentFactoredInvoice.getFinancingRate() != expectedFactoredInvoice.getFinancingRate()){
            throw new AssertionFailedError(msg+" financingRate not matched");
        }
        if (currentFactoredInvoice.getFinancingTerm() != expectedFactoredInvoice.getFinancingTerm()){
            throw new AssertionFailedError(msg+" financingTerm not matched");
        }
        if (currentFactoredInvoice.isPayable() != expectedFactoredInvoice.isPayable()){
            throw new AssertionFailedError(msg+" payable not matched");
        }
        if (currentFactoredInvoice.getPurchaserId() != expectedFactoredInvoice.getPurchaserId()){
            throw new AssertionFailedError(msg+" purchaserId not matched");
        }
        if (currentFactoredInvoice.getEarlyPaymentAmount() != expectedFactoredInvoice.getEarlyPaymentAmount()){
            throw new AssertionFailedError(msg+" earlyPaymentAmount not matched");
        }
    }

    private List<FactoredInvoice> loadExpectedFactoredInvoice() throws IOException {
        final Path resourcePath = Paths.get("src", "test","resources", "expectedFactoredInvoices.csv");
        return CsvReader.load(resourcePath.toFile(), FactoredInvoice.class);
    }
}
