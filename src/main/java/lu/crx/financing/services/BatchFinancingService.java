package lu.crx.financing.services;

import lu.crx.financing.util.InvoiceFactoringProcess;

public interface BatchFinancingService {
    long financeBatch(InvoiceFactoringProcess invoiceFactoringProcess);
}
