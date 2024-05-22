package lu.crx.financing.services.impl;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.*;
import lu.crx.financing.repositories.*;
import lu.crx.financing.services.FactoredInvoiceService;
import lu.crx.financing.services.FinancingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Service
public class FinancingServiceImpl implements FinancingService {




    @Autowired
    private InvoiceRepository invoiceBatchRepository;
    @Autowired
    private FactoredInvoiceService factoredInvoiceService;

    @Autowired
    private PurchaserRepository purchaserRepository;



    @Transactional
    public void finance() {
        log.info("Financing started");
        Page<Invoice> invoicePage = invoiceBatchRepository.findAllNotFinanced(PageRequest.of(0,5000));
        List<Invoice> invoices = invoicePage.stream().toList();
        if (invoices.size()==0){
            log.info("Nothing to do");
            return;
        }


        log.info("Cache created");
        int batchNum = 0;
        while (invoices.size()>0) {
            batchNum++;
            log.info("Batch number "+batchNum+" started");
            LocalDate now = LocalDate.now();
            List<FactoredInvoice> factoredInvoiceList = new ArrayList<>();
            for (Invoice invoice : invoices) {
                long financialTerm = DAYS.between(now,invoice.getMaturityDate());
                long creditorId = invoice.getCreditorId();
                Creditor creditor = creditorMap.get(invoice.getCreditorId());
                double minBps = Double.MAX_VALUE;
                Purchaser bestPurchaser = null;
                List<PurchaserSettings> purchaserSettingsList = creditorPurchaserSettingsMap.get(creditorId);
                for (PurchaserSettings purchaserSettings : purchaserSettingsList) {
                    double financingRate = financialTerm * purchaserSettings.annualRateInBps * 1.0 / 365;
                    Purchaser purchaser = purchaserMap.get(purchaserSettings.purchaserId);
                    if (financialTerm > purchaser.getMinimumFinancingTermInDays()) {
                        if (financingRate < minBps) {
                            minBps = financingRate;
                            bestPurchaser = purchaser;
                        }
                    }
                }
                FactoredInvoice factoredInvoice = new FactoredInvoice();
                factoredInvoice.setInvoiceId(invoice.getId());
                factoredInvoice.setDateFulfilment(now);
                factoredInvoice.setFinancingTerm(financialTerm);
                factoredInvoice.setPayable(false);
                factoredInvoice.setViolatesCreditorMaxBps(false);
                if (bestPurchaser!=null) {
                    factoredInvoice.setPurchaserId(bestPurchaser.getId());
                    factoredInvoice.setFinancingRate(Math.round(minBps));
                    factoredInvoice.setEarlyPaymentAmount(Math.round(invoice.getValueInCents() * (1 - minBps / 10000)));
                }
                if (minBps < creditor.getMaxFinancingRateInBps()) {
                    factoredInvoice.setPayable(true);
                }else {
                    factoredInvoice.setViolatesCreditorMaxBps(true);
                }
                factoredInvoiceList.add(factoredInvoice);

            }
            factoredInvoiceService.saveAll(factoredInvoiceList);
            invoicePage = invoiceBatchRepository.findAllNotFinanced(PageRequest.of(1, 5000));
            invoices = invoicePage.stream().toList();
            log.info("Batch number "+batchNum+" finished");
        }


//50 000 000 invoice

//50 000 Purchasers

//10 000 unpaid, 1 000 000 already financed in 30 sec


// natiahnem vsetky invoici do ciklu (50 000 000 invoicov)
// pozrem kto je creditor a ake ma nastavenie
// natiahnem vsetky banky do ciklu (50 000 bank)
// pre kazdu banku vypocitam ci je vhodna, rozdiel dni medzi zaplatenim na dlh a zaplatenim skutocnym je vacsi ako minimum definovane bankou a
// bps vypocitanie je mensie ako preferencia Creditor-a
// Financing.dateFulfilment = today
// Financing.financingTerm = numDays(Invoice.maturityDate - Financing.dateFulfilment)
// if (Financing.financingTerm >= Purchaser.minimumFinancingTermInDays) {
        // Financing.financingRate = Financing.financingTerm * Purchaser.annualRateInBps / 365  eg (30*50)/365 = 4.11 -> 4
//          if (Financing.financingRate<Invoice.creditor.maxFinancingRateInBps) {
//               if (Financing.financingRate < minimumFinancingRate) {
//                  minimumFinancingRate = Financing.financingRate;
//                  minPurchaser =  Purchaser
//               }
//            }
// }

// vyberem tu banku ktora ma bps najnizsie a poznacim ze zafinancuje invoice, poznacnie bude do zvlast tabulky kde bude informacia ktora banka kolko pozicia a s akym bps
// Financing.purchaser = minPurchaser
// Financing.earlyPaymentAmount = Invoice.valueInCents - (Invoice.valueInCents * Financing.financingRate/10000)
        // TODO This is the financing algorithm that needs to be implemented according to the specification.

        log.info("Financing completed");


    }

}
