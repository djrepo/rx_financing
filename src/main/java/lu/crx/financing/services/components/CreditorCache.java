package lu.crx.financing.services.components;

import lu.crx.financing.model.entities.Creditor;
import lu.crx.financing.repositories.CreditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CreditorCache {


    @Autowired
    private CreditorRepository creditorRepository;
    private Map<Long, Creditor> creditorMap = null;

    @Transactional
    public void init(){
        initCreditorMap();
    }

    public void invalidate() {
        creditorMap = null;
    }

    public Creditor getCreditorById(long id) {
        if (creditorMap == null) {
            initCreditorMap();
        }
        return creditorMap.get(id);
    }

    private void initCreditorMap() {
        Iterable<Creditor> creditors = creditorRepository.findAll();
        this.creditorMap = StreamSupport.stream(creditors.spliterator(), false).collect(Collectors.toMap(Creditor::getId, Function.identity()));
    }

}
