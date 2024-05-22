package lu.crx.financing.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repositories.CreditorRepository;
import lu.crx.financing.repositories.PurchaserRepository;
import lu.crx.financing.services.impl.FinancingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class PurchaserCacheService {
    @Getter
    @AllArgsConstructor
    private static class PurchaserSettings {
        long purchaserId;
        int annualRateInBps;
    }

    @Autowired
    private PurchaserRepository purchaserRepository;
    private Map<Long, Purchaser> purchaserMap = null;
    private Map<Long, List<PurchaserSettings>> creditorPurchaserSettingsMap = null;
    public void invalidate() {
        purchaserMap = null;
        creditorPurchaserSettingsMap = null;
    }

    public Purchaser getPurchaserById(long id) {
        if (purchaserMap == null) {
            initCache();
        }
        return purchaserMap.get(id);
    }

    public List<PurchaserSettings> getPurchaserSettingsByCreditorId(long creditorId) {
        if (creditorPurchaserSettingsMap == null) {
            initCache();
        }
        return creditorPurchaserSettingsMap.get(creditorId);
    }

    private void initCache() {
        Iterable<Purchaser> purchasers = purchaserRepository.findAll();
        this.purchaserMap = StreamSupport.stream(purchasers.spliterator(), false).collect(Collectors.toMap(Purchaser::getId, Function.identity()));

        creditorPurchaserSettingsMap = new HashMap<>();
        for (Purchaser purchaser : purchasers) {
            Set<PurchaserFinancingSettings> financingSettings = purchaser.getPurchaserFinancingSettings();
            for (PurchaserFinancingSettings financingSetting : financingSettings) {
                List<PurchaserSettings> purchaserSettings = creditorPurchaserSettingsMap.get(financingSetting.getCreditorId());
                if (purchaserSettings == null){
                    creditorPurchaserSettingsMap.put(financingSetting.getCreditorId(),new ArrayList<>());
                }
                creditorPurchaserSettingsMap.get(financingSetting.getCreditorId()).add(new PurchaserSettings(purchaser.getId(),financingSetting.getAnnualRateInBps()));
            }
        }
    }

}

