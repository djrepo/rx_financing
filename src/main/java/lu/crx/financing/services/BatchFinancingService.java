package lu.crx.financing.services;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.repositories.InvoiceRepository;
import lu.crx.financing.util.InvoiceFactoringProcess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static lu.crx.financing.helpers.DurationTimeHelper.measure;
import static lu.crx.financing.helpers.DurationTimeHelper.measured;

@Service
@Slf4j
public class BatchFinancingService {

    private final InvoiceRepository invoiceRepository;
    private final FactoredInvoiceService factoredInvoiceService;

    public BatchFinancingService(InvoiceRepository invoiceRepository, FactoredInvoiceService factoredInvoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.factoredInvoiceService = factoredInvoiceService;
    }

    @Value("${crx.batch-size}")
    private int batchSize;

    @Transactional
    public long financeBatch(InvoiceFactoringProcess invoiceFactoringProcess) {
        Page<Invoice> invoicePage =
                measured(() ->
                                invoiceRepository.findAllNotFinanced(PageRequest.of(0, batchSize))
                        , "Loading take ");
        long invoicesSize = invoicePage.stream().count();
        if (invoicesSize == 0) {
            log.info("Nothing to do");
            return 0;
        }
        List<FactoredInvoice> factoredInvoiceList =
                measured(() ->
                                invoicePage.stream().parallel().map(invoice ->
                                        invoiceFactoringProcess.finance(invoice)
                                ).collect(Collectors.toList())
                        , "Conversion take ");
        measure(() -> {
            factoredInvoiceService.saveAll(factoredInvoiceList);
        }, "Storing take ");
        return invoicesSize;
    }

    public long totalNotFinancedInvoices() {
        return invoiceRepository.countNotFinanced();
    }
}
