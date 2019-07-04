package xiaopei.bigdata.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user_skill")
public class UserSkill implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;


    private Integer level;

    private UserSkill() {
    }

    public UserSkill(User user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, skill);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        UserSkill that = (UserSkill) obj;
        return Objects.equals(user, that.user) &&
                Objects.equals(skill, that.skill);
    }
}
