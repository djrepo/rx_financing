package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.model.entities.*;
import lu.crx.financing.repositories.InvoiceRepository;
import lu.crx.financing.services.SeedingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Profile("pst")
public class PstSeedingServiceImpl implements SeedingService {

    private EntityManager entityManager;

    private List<Creditor> creditorList;

    private List<Debtor> debtorList;

    private List<Purchaser> purchaserList;
    private List<Invoice> invoiceList;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public PstSeedingServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void seedMasterData() {
        log.info("Seeding master data");
        creditorList = new ArrayList<>();
        for (int i = 0; i< 1000;i++){
            Creditor creditor = Creditor.builder()
                    .name("Generic creditor "+i)
                    .maxFinancingRateInBps(5)
                    .build();
            creditorList.add(creditor);
            entityManager.persist(creditor);
        }

        debtorList = new ArrayList<>();
        // debtors
        for (int i = 0; i< 1000;i++){
            Debtor debtor = Debtor.builder()
                    .name("Generic debtor "+i)
                    .build();
            debtorList.add(debtor);
            entityManager.persist(debtor);
        }

        // purchasers
        purchaserList = new ArrayList<>();
        for (int i = 0; i< 1000;i++) {

            Set<PurchaserFinancingSettings> bankSettings = new HashSet<>();
            for (Creditor creditor : creditorList) {
                PurchaserFinancingSettings purchaserFinancingSettings = PurchaserFinancingSettings.builder()
                        .creditor(creditor)
                        .annualRateInBps(randInt(1, 100))
                        .build();
                bankSettings.add(purchaserFinancingSettings);
            }

            Purchaser purchaser = Purchaser.builder()
                    .name("Generic Bank " + i)
                    .minimumFinancingTermInDays(randInt(1, 200))
                    .build();
            purchaser.setPurchaserFinancingSettings(bankSettings);
            purchaserList.add(purchaser);
            entityManager.persist(purchaser);
            if (i%500==0) {
                log.info("Stored "+ i +" Purchasers");
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Transactional
    public void seedInvoices() {
        log.info("Seeding the invoices");
        invoiceList = new ArrayList<>();
        for (int i = 0; i< 200000;i++) {
            Invoice invoice = Invoice.builder()
                    .creditor(randItem(creditorList))
                    .debtor(randItem(debtorList))
                    .valueInCents(randInt(1,10000000))
                    .maturityDate(LocalDate.now().plusDays(randInt(1,100)))
                    .build();
            invoiceList.add(invoice);
        }

       invoiceRepository.saveAll(invoiceList);
       entityManager.flush();
       entityManager.clear();
    }

    private <T> T randItem(List<T> list){
        int index = randInt(0, list.size());
        return list.get(index);
    }

    private int randInt(int bound1, int bound2) {
        //make sure bound2> bound1
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        //math.random gives random number from 0 to 1
        return (int) (min + (Math.random() * (max - min)));
    }

}