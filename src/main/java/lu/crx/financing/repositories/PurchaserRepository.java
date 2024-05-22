package lu.crx.financing.repositories;

import lu.crx.financing.model.entities.Purchaser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaserRepository extends CrudRepository<Purchaser, Long> {

}