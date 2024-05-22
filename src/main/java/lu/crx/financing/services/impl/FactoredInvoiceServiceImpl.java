package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.FactoredInvoice;
import lu.crx.financing.repositories.FactoredInvoiceRepository;
import lu.crx.financing.services.FactoredInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.xml.stream.events.EntityReference;
import java.util.List;

@Slf4j
@Service
public class FactoredInvoiceServiceImpl implements FactoredInvoiceService {

    private final EntityManager entityManager;
    private final FactoredInvoiceRepository factoredInvoiceRepository;

    public FactoredInvoiceServiceImpl(EntityManager entityManager, FactoredInvoiceRepository factoredInvoiceRepository) {
        this.entityManager = entityManager;
        this.factoredInvoiceRepository = factoredInvoiceRepository;
    }

    @Transactional
    @Override
    public void saveAll(List<FactoredInvoice> factoredInvoiceList) {
        factoredInvoiceRepository.saveAll(factoredInvoiceList);
        entityManager.flush();
    }
}
