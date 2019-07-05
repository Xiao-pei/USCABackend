package xiaopei.bigdata.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user_job")
public class UserJob implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private Double score;

    public UserJob() {
    }

    public UserJob(User user, Job job, Double score) {
        this.user = user;
        this.job = job;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJob userJob = (UserJob) o;
        return Objects.equal(user, userJob.user) &&
                Objects.equal(job, userJob.job) &&
                Objects.equal(score, userJob.score);
    }

}
