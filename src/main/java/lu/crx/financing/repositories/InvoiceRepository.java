package lu.crx.financing.repositories;

import lu.crx.financing.model.entities.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    @Query("select i " +
            "    from Invoice i " +
            "     where i.id not in (SELECT invoiceId from FactoredInvoice) " +
            "    order by i.id")
    Page<Invoice> findAllNotFinanced(Pageable pageable);

}