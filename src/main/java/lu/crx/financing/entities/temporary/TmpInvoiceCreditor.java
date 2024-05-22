package lu.crx.financing.entities.temporary;

import lombok.*;
import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Invoice;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmpInvoiceCreditor {

    @Id
    private long invoiceId;
    @Basic(optional = false)
    private long creditorId;
    @Basic(optional = false)
    private int financingTerm;
    @Basic(optional = false)
    private int maxFinancingRateInBps;
}
