package lu.crx.financing.services;

import lu.crx.financing.repositories.temporary.TmpInvoiceCreditorRepository;
import lu.crx.financing.repositories.temporary.impl.postgres.TmpInvoiceCreditorRepositoryImpl;
import lu.crx.financing.repositories.temporary.TmpInvoicePurchaserRepository;
import lu.crx.financing.repositories.temporary.TmpInvoiceRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BatchInvoiceProcessorService {

    @Autowired
    private TmpInvoiceCreditorRepository tmpInvoiceCreditorRepository;

    @Autowired
    private TmpInvoiceRateRepository tmpInvoiceRateRepository;

    @Autowired
    private TmpInvoicePurchaserRepository tmpInvoicePurchaserRepository;

    private int BATCH_SIZE = 5000;
    @Transactional
    public void financeNextBatch() {
        tmpInvoiceCreditorRepository.truncate();
        tmpInvoiceRateRepository.truncate();
        tmpInvoicePurchaserRepository.truncate();

        tmpInvoiceCreditorRepository.fillInvoicesAndCreditors(BATCH_SIZE);
        tmpInvoiceRateRepository.fillInvoicesAndRates();
        tmpInvoicePurchaserRepository.fillInvoicesAndPurchasers();
    }
}
