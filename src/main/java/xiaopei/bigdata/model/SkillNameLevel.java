package xiaopei.bigdata.model;

import lombok.Data;

@Data
public class SkillNameLevel {
    private String name;
    private Integer level;
    private Long id;

    public SkillNameLevel(String name, Integer level, Long id) {
        this.name = name;
        this.level = level;
        this.id = id;
    }

    public SkillNameLevel(Skill skill, Integer level) {
        name = skill.getName();
        id = skill.getId();
        this.level = level;
    }
}
