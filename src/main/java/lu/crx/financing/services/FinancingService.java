package lu.crx.financing.services;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.repositories.FinancingRepository;
import lu.crx.financing.repositories.PurchaserRepository;
import lu.crx.financing.repositories.temporary.TmpInvoiceCreditorRepository;
import lu.crx.financing.repositories.temporary.TmpInvoicePurchaserRepository;
import lu.crx.financing.repositories.temporary.TmpInvoiceRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinancingService {



    @Autowired
    private FinancingRepository financingRepository;

    @Autowired
    private BatchInvoiceProcessorService batchInvoiceProcessorService;


    @Transactional
    public void finance() {
        log.info("Financing started");
        long numOfRowsBefore = financingRepository.count();
        long numOfRowsAfter = Long.MAX_VALUE;
        while (numOfRowsBefore<numOfRowsAfter) {

            batchInvoiceProcessorService.financeNextBatch();
            financingRepository.populateFromTmpInvoicePurchaser();
            numOfRowsAfter = financingRepository.count();
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
