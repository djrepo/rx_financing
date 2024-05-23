package lu.crx.financing.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lu.crx.financing.model.entities.Invoice;
import lu.crx.financing.model.entities.Purchaser;
import lu.crx.financing.util.FactoredFinancingHelper;
@Getter
@Setter
@Builder
public class PurchaserInfo {

    private Invoice invoice;
    private Purchaser purchaser;
    private double financingRate;
    private long financingTerm;
    private boolean violatesPurchaserMinTerm;
    private boolean violatesCreditorSettings;
    private boolean isPayable;

}
