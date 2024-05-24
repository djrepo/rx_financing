package lu.crx.financing.services;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.entities.FactoredInvoice;
import lu.crx.financing.repositories.FactoredInvoiceRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class FactoredInvoiceService {

    private final EntityManager entityManager;
    private final FactoredInvoiceRepository factoredInvoiceRepository;

    public FactoredInvoiceService(EntityManager entityManager, FactoredInvoiceRepository factoredInvoiceRepository) {
        this.entityManager = entityManager;
        this.factoredInvoiceRepository = factoredInvoiceRepository;
    }

    @Transactional
    public void saveAll(List<FactoredInvoice> factoredInvoiceList) {
        factoredInvoiceRepository.saveAll(factoredInvoiceList);
        entityManager.flush();
        entityManager.clear();
    }

}
