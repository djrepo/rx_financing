package lu.crx.financing.repositories;

import lu.crx.financing.entities.Creditor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CreditorRepository extends CrudRepository<Creditor, Long> {

}