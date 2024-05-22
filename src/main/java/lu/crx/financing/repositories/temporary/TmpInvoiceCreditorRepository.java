package lu.crx.financing.repositories.temporary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

public interface TmpInvoiceCreditorRepository {

    public void truncate();
    public void fillInvoicesAndCreditors(int batchSize);

}
