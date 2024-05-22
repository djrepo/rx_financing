package lu.crx.financing.entities.temporary;

import lombok.*;
import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.Purchaser;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmpInvoicePurchaser {
    @EmbeddedId
    private InvoicePurchaserKey invoicePurchaserKey;

    @Basic(optional = false)
    private int financingTerm;
    @Basic(optional = false)
    private double financingRate;
    @Basic(optional = false)
    private int financingRateRank;
    @Getter
    @Setter
    @Embeddable
    public class InvoicePurchaserKey implements Serializable {

        private long invoiceId;
        private long purchaserId;
    }
}
