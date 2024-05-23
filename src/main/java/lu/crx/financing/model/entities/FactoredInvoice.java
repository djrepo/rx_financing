package lu.crx.financing.model.entities;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    @Basic(optional = false)
    private boolean payable;
    @Basic(optional = false)
    private LocalDate createdOn;

    private long purchaserId;
    @Column(nullable = true)
    private LocalDate fundingDate;
    private long financingTerm;
    private double financingRate;
    private long earlyPaymentAmount;


}
