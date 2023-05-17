package med.helper.entitys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String status;

    @Temporal(TemporalType.TIME)
    Date start_day;
    @Temporal(TemporalType.TIME)
    Date end_day;
    @Temporal(TemporalType.TIME)
    Date breakfast;
    @Temporal(TemporalType.TIME)
    Date lunch;
    @Temporal(TemporalType.TIME)
    Date dinner;
}
