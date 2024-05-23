package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.repositories.FactoredInvoiceJdbcRepository;
import lu.crx.financing.repositories.FactoredInvoiceRepository;
import lu.crx.financing.services.FactoredInvoiceService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class FactoredInvoiceServiceImpl implements FactoredInvoiceService {

    private final EntityManager entityManager;
    private final FactoredInvoiceRepository factoredInvoiceRepository;

    private final FactoredInvoiceJdbcRepository factoredInvoiceJdbcRepository;

    public FactoredInvoiceServiceImpl(EntityManager entityManager, FactoredInvoiceRepository factoredInvoiceRepository, FactoredInvoiceJdbcRepository factoredInvoiceJdbcRepository) {
        this.entityManager = entityManager;
        this.factoredInvoiceRepository = factoredInvoiceRepository;
        this.factoredInvoiceJdbcRepository = factoredInvoiceJdbcRepository;
    }

    @Transactional
    @Override
    public void saveAll(List<FactoredInvoice> factoredInvoiceList) {
        factoredInvoiceRepository.saveAll(factoredInvoiceList);
        entityManager.flush();
        entityManager.clear();
    }

/*
    @Override
    public void saveAll(List<FactoredInvoice> factoredInvoiceList) {
        factoredInvoiceJdbcRepository.saveAll(factoredInvoiceList);
    }

 */
/*
    @Transactional
    @Override
    public void saveAll(List<FactoredInvoice> factoredInvoices) {
        int batchSize = 5000;
        for (int i = 0; i < factoredInvoices.size(); i++) {
            entityManager.persist(factoredInvoices.get(i));
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }*/
}
