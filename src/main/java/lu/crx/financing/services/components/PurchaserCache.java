package lu.crx.financing.services.components;

import lu.crx.financing.model.PurchaserCreditorBpsSettings;
import lu.crx.financing.model.entities.Purchaser;
import lu.crx.financing.model.entities.PurchaserFinancingSettings;
import lu.crx.financing.repositories.PurchaserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Component
public class PurchaserCache {

    @Autowired
    private PurchaserRepository purchaserRepository;
    private Map<Long, Purchaser> purchaserMap = null;

    private Map<Long, List<PurchaserCreditorBpsSettings>> creditorPurchaserSettingsMap = null;
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

    public List<PurchaserCreditorBpsSettings> getPurchaserSettingsByCreditorId(long creditorId) {
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
                List<PurchaserCreditorBpsSettings> purchaserSettings = creditorPurchaserSettingsMap.get(financingSetting.getCreditorId());
                if (purchaserSettings == null){
                    creditorPurchaserSettingsMap.put(financingSetting.getCreditorId(),new ArrayList<>());
                }
                creditorPurchaserSettingsMap.get(financingSetting.getCreditorId()).add(new PurchaserCreditorBpsSettings(purchaser,financingSetting.getAnnualRateInBps()));
            }
        }
    }

}

