package lu.crx.financing.repositories;

import lu.crx.financing.model.entities.FactoredInvoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoredInvoiceRepository extends CrudRepository<FactoredInvoice, Long> {


}