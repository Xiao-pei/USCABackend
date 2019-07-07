package xiaopei.bigdata.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class JobSkill {
    @Id
    private Long id;
    @NotNull
    private String jobName;
    @NotNull
    private String skillName;
    @NotNull
    private Double score;

    public JobSkill() {
    }

    public JobSkill(String jobName, String skillName, Double score) {
        this.jobName = jobName;
        this.skillName = skillName;
        this.score = score;
    }
}
