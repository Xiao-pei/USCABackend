package xiaopei.bigdata.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
public class Skill {
    @Id
    private Long id;
    @NotNull
    @NaturalId
    private String name;
    @OneToMany(mappedBy = "skill")
    private List<UserSkill> userSkills = new ArrayList<>();

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

        if (!Objects.equals(name, skill.name)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
