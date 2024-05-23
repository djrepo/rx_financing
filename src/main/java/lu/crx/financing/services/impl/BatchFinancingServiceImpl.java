package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.repositories.InvoiceRepository;
import lu.crx.financing.services.BatchFinancingService;
import lu.crx.financing.services.FactoredInvoiceService;
import lu.crx.financing.util.DurationTimeHelper;
import lu.crx.financing.util.InvoiceFactoringProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BatchFinancingServiceImpl implements BatchFinancingService {

    @Autowired
    private InvoiceRepository invoiceBatchRepository;
    @Autowired
    private FactoredInvoiceService factoredInvoiceService;

    @Value("${crx.batch-size}")
    private int batchSize;

    @Transactional
    public int financeBatch(InvoiceFactoringProcess invoiceFactoringProcess) {
        long start = System.nanoTime();
        Page<Invoice> invoicePage = invoiceBatchRepository.findAllNotFinanced(PageRequest.of(0,batchSize));
        List<Invoice> invoices = invoicePage.stream().toList();
        long stop = System.nanoTime();
        log.info("Load take "+ DurationTimeHelper.millisToShortDHMS(stop-start));
        if (invoices.size()==0){
            log.info("Nothing to do");
            return 0;
        }
        List<FactoredInvoice> factoredInvoiceList = new ArrayList<>();
        start = System.nanoTime();
        for (Invoice invoice : invoices) {
            FactoredInvoice factoredInvoice = invoiceFactoringProcess.finance(invoice);
            factoredInvoiceList.add(factoredInvoice);
        }
        stop = System.nanoTime();
        log.info("Conversion take "+ DurationTimeHelper.millisToShortDHMS(stop-start));
        start = System.nanoTime();
        factoredInvoiceService.saveAll(factoredInvoiceList);
        stop = System.nanoTime();
        log.info("Storing take "+ DurationTimeHelper.millisToShortDHMS(stop-start));
        return invoices.size();
    }
}
