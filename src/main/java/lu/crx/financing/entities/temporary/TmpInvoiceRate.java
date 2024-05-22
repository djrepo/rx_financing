package lu.crx.financing.entities.temporary;

import lombok.*;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.PurchaserFinancingSettings;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmpInvoiceRate {

    @EmbeddedId
    private InvoicePurchaserSettingsKey invoicePurchaserSettingsKey;

    @Basic(optional = false)
    private int financingTerm;
    @Basic(optional = false)
    private double financingRate;
    @Getter
    @Setter
    @Embeddable
    public class InvoicePurchaserSettingsKey implements Serializable {

        private long invoiceId;
        private long purchaserSettingsId;
    }

}
