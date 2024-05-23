package lu.crx.financing.model.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * An invoice issued by the {@link Creditor} to the {@link Debtor} for shipped goods.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Creditor is the entity that issued the invoice.
     */
    @JoinColumn(name = "creditor_id")
    @ManyToOne(targetEntity = Creditor.class, optional = false, fetch = FetchType.LAZY)
    private Creditor creditor;

    @Column(name = "creditor_id", insertable = false, updatable = false)
    private Long creditorId;
    /**
     * Debtor is the entity obliged to pay according to the invoice.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Debtor debtor;

    /**
     * Maturity date is the date on which the {@link #debtor} is to pay for the invoice.
     * In case the invoice was financed, the money will be paid in full on this date to the purchaser of the invoice.
     */
    @Basic(optional = false)
    private LocalDate maturityDate;

    /**
     * The value is the amount to be paid for the shipment by the Debtor.
     */
    @Basic(optional = false)
    private long valueInCents;
}
