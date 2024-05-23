package lu.crx.financing.model.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Financing settings set by the purchaser for a specific creditor.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaserFinancingSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchaser_financing_settings_seq")
    @SequenceGenerator(name = "purchaser_financing_settings_seq", sequenceName = "purchaser_financing_settings_id_seq", allocationSize = 1)
    private long id;

    @JoinColumn(name = "creditor_id")
    @ManyToOne(targetEntity = Creditor.class, optional = false, fetch = FetchType.LAZY)
    private Creditor creditor;

    @Column(name = "creditor_id", insertable = false, updatable = false)
    private long creditorId;

    /**
     * The annual financing rate set by the purchaser for this creditor.
     */
    @Basic(optional = false)
    private int annualRateInBps;

}
