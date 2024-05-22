package lu.crx.financing.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactoredInvoice {

    @Id
    private long invoiceId;
    @Basic(optional = true)
    private long purchaserId;
    @Basic(optional = false)
    private LocalDate dateFulfilment;
    @Basic(optional = true)
    private long financingTerm;
    @Basic(optional = true)
    private long financingRate;
    @Basic(optional = true)
    private long earlyPaymentAmount;
    @Basic(optional = false)
    private boolean isPayable;
    @Basic(optional = false)
    private boolean violatesCreditorMaxBps;
}
