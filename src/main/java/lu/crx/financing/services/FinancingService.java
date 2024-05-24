package lu.crx.financing.services;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.services.components.CreditorCache;
import lu.crx.financing.services.components.PurchaserCache;
import lu.crx.financing.util.InvoiceFactoringProcess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static lu.crx.financing.helpers.DurationTimeHelper.measure;

@Slf4j
@Service
public class FinancingService {

    private final PurchaserCache purchaserCache;
    private final CreditorCache creditorCache;
    private final BatchFinancingService batchFinancingService;

    @Value("${crx.max-batch-iterations}")
    private int maxBatchIterations;

    public FinancingService(PurchaserCache purchaserCache, CreditorCache creditorCache, BatchFinancingService batchFinancingServiceImpl) {
        this.purchaserCache = purchaserCache;
        this.creditorCache = creditorCache;
        this.batchFinancingService = batchFinancingServiceImpl;
    }

    public void createCache() {
        creditorCache.invalidate();
        creditorCache.init();
        purchaserCache.invalidate();
        purchaserCache.init();
    }

    public void finance() {

        log.info("Financing started");
        measure(()-> {
                    createCache();
                },"Cache load take ");
        final long totalNotFinancedInvoices = batchFinancingService.totalNotFinancedInvoices();

        measure(()-> {
                    InvoiceFactoringProcess invoiceFactoringProcess = new InvoiceFactoringProcess(LocalDate.now(), creditorCache, purchaserCache);
                    log.info("Financing Cache created");
                    int batchNum = 0;
                    long totalProcessedInvoices = 0;
                    while (totalProcessedInvoices < totalNotFinancedInvoices && batchNum<maxBatchIterations) {
                        log.info("Batch number " + batchNum + " started");
                        long processedInvoices = batchFinancingService.financeBatch(invoiceFactoringProcess);
                        log.info("Batch number " + batchNum + " finished, processed " + processedInvoices);
                        totalProcessedInvoices = totalProcessedInvoices + processedInvoices;
                        batchNum++;
                    }
                    if (batchNum >= maxBatchIterations){
                        throw new RuntimeException("Number of batches too high.");
                    }
                    log.info("Financing completed");
                },"Financing take ");


//50 000 000 invoice

//50 000 Purchasers

//10 000 unpaid, 1 000 000 already financed in 30 sec


    }


}
