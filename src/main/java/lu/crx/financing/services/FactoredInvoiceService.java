package lu.crx.financing.services;

import lu.crx.financing.entities.FactoredInvoice;

import java.util.List;

public interface FactoredInvoiceService {

    void saveAll(List<FactoredInvoice> factoredInvoiceList);
}
