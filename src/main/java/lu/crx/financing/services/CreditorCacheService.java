package lu.crx.financing.services;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.repositories.CreditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class CreditorCacheService {


    @Autowired
    private CreditorRepository creditorRepository;
    private Map<Long, Creditor> creditorMap = null;

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
