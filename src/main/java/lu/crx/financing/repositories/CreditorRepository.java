package lu.crx.financing.repositories;

import lu.crx.financing.model.entities.Creditor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditorRepository extends CrudRepository<Creditor, Long> {

}