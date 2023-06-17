package med.helper.entitys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "active_substance_interaction")
public class ActiveSubstanceInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "active_substance_id1")
    private ActiveSubstance activeSubstance1;

    @ManyToOne
    @JoinColumn(name = "active_substance_id2")
    private ActiveSubstance activeSubstance2;

    @Temporal(TemporalType.TIME)
    Date interactionTime;

    private String status;
}
