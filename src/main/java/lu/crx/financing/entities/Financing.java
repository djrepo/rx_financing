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
public class Financing {

    @Id
    private long invoiceId;
    @Basic(optional = false)
    private long purchaserId;
    @Basic(optional = false)
    private LocalDate dateFulfilment;
    @Basic(optional = false)
    private int financingTerm;
    @Basic(optional = false)
    private int financingRate;
    @Basic(optional = false)
    private long earlyPaymentAmount;
}
