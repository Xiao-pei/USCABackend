package xiaopei.bigdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
public class Job {
    @Id
    private Long id;
    @NotNull
    @NaturalId
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "job")
    private Set<UserJob> userJobs;

    Job() {
    }

    Job(String name) {
        this.name = name;
    }
}
