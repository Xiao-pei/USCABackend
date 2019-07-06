package xiaopei.bigdata.model;

import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class AnalysisResult implements Serializable {
    @Id
    @CreationTimestamp
    private Date date;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private String job;

    //private Double score;

    public AnalysisResult() {
    }

    public AnalysisResult(User user, Skill skill, String job) {
        this.user = user;
        this.skill = skill;
        this.job = job;
        date = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisResult that = (AnalysisResult) o;
        return Objects.equal(user, that.user) &&
                Objects.equal(skill, that.skill) &&
                Objects.equal(job, that.job) &&
                Objects.equal(date, that.date);
    }
}
