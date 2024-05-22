package lu.crx.financing.repositories.temporary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


public interface TmpInvoicePurchaserRepository {

    public void truncate();
    public void fillInvoicesAndPurchasers();


}
