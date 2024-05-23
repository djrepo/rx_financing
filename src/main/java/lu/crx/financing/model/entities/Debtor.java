package lu.crx.financing.model.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A debtor is an entity that purchased some goods from the {@link Creditor}, received an {@link Invoice}
 * and is obliged to pay for the invoice at the specified date called maturity date
 * (see {@link Invoice#getMaturityDate()}).
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Debtor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debtor_seq")
    @SequenceGenerator(name = "debtor_seq", sequenceName = "debtor_id_seq", allocationSize = 1)
    private long id;

    @Basic(optional = false)
    private String name;

}
