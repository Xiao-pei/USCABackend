package xiaopei.bigdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Skill {
    @Id
    private Long id;
    @NotNull
    @NaturalId
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> userSkills;

    Skill() {
    }

    public Skill(String s) {
        name = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;

        return Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
