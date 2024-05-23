package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.components.CreditorCache;
import lu.crx.financing.services.components.PurchaserCache;
import lu.crx.financing.util.DurationTimeHelper;
import lu.crx.financing.util.InvoiceFactoringProcess;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class FinancingServiceImpl implements FinancingService {

    private final PurchaserCache purchaserCache;
    private final CreditorCache creditorCache;
    private final BatchFinancingServiceImpl batchFinancingServiceImpl;

    public FinancingServiceImpl(PurchaserCache purchaserCache, CreditorCache creditorCache, BatchFinancingServiceImpl batchFinancingServiceImpl) {
        this.purchaserCache = purchaserCache;
        this.creditorCache = creditorCache;
        this.batchFinancingServiceImpl = batchFinancingServiceImpl;
    }

    public void createCache() {
        creditorCache.invalidate();
        creditorCache.init();
        purchaserCache.invalidate();
        purchaserCache.init();
    }

    public void finance() {
        log.info("Financing started");
        long start = System.nanoTime();
        createCache();
        InvoiceFactoringProcess invoiceFactoringProcess = new InvoiceFactoringProcess(LocalDate.now(), creditorCache, purchaserCache);
        log.info("Financing Cache created");
        int batchNum = 0;
        long processedInvoices = 0;
        do {
            log.info("Batch number " + batchNum + " started");
            processedInvoices = batchFinancingServiceImpl.financeBatch(invoiceFactoringProcess);
            log.info("Batch number " + batchNum + " finished, processed " + processedInvoices);
            batchNum++;
        } while (processedInvoices > 0);
        log.info("Financing completed");
        long stop = System.nanoTime();
        log.info("Financing take " + DurationTimeHelper.millisToShortDHMS(stop - start));

//50 000 000 invoice

//50 000 Purchasers

//10 000 unpaid, 1 000 000 already financed in 30 sec


    }


}
