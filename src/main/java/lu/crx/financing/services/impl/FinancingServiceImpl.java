package lu.crx.financing.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.PurchaserInfo;
import lu.crx.financing.model.entities.*;
import lu.crx.financing.repositories.*;
import lu.crx.financing.services.FactoredInvoiceService;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.components.CreditorCache;
import lu.crx.financing.services.components.PurchaserCache;
import lu.crx.financing.util.DurationTimeHelper;
import lu.crx.financing.util.PurchaserFinder;
import lu.crx.financing.util.InvoiceFactoringProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FinancingServiceImpl implements FinancingService {



    @Autowired
    private PurchaserCache purchaserCache;

    @Autowired
    private CreditorCache creditorCache;

    @Autowired
    private BatchFinancingServiceImpl batchFinancingServiceImpl;



    public void createCache(){
            creditorCache.invalidate();
            creditorCache.init();
            purchaserCache.invalidate();
            purchaserCache.init();
    }
    public void finance() {
        log.info("Financing started");
        long start = System.nanoTime();
        createCache();
        InvoiceFactoringProcess invoiceFactoringProcess = new InvoiceFactoringProcess(LocalDate.now(),creditorCache, purchaserCache);
        log.info("Financing Cache created");
        int batchNum = 0;
        long processedInvoices = 0;
        do {
            log.info("Batch number "+batchNum+" started");
            processedInvoices = batchFinancingServiceImpl.financeBatch(invoiceFactoringProcess);
            log.info("Batch number "+batchNum+" finished, processed "+processedInvoices);
            batchNum++;
        } while (processedInvoices>0);
        log.info("Financing completed");
        long stop = System.nanoTime();
        log.info("Financing take "+ DurationTimeHelper.millisToShortDHMS(stop-start));

//50 000 000 invoice

//50 000 Purchasers

//10 000 unpaid, 1 000 000 already financed in 30 sec




    }



}
