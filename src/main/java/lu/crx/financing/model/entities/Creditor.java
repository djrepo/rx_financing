package lu.crx.financing.model.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A creditor is a company that shipped some goods to the {@link Debtor}, issued an {@link Invoice} for the shipment
 * and is waiting for this invoice to be paid by the debtor.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Creditor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditor_seq")
    @SequenceGenerator(name = "creditor_seq", sequenceName = "creditor_id_seq", allocationSize = 1)
    private long id;

    @Basic(optional = false)
    private String name;

    /**
     * Maximum acceptable financing rate for this creditor.
     */
    @Basic(optional = false)
    private int maxFinancingRateInBps;

}
