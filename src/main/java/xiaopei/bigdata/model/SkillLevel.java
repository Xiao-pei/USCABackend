package xiaopei.bigdata.model;

import lombok.Data;

@Data
public class SkillLevel {
    private Long id;
    private Integer level;

    public SkillLevel() {
    }

    public SkillLevel(Long id, Integer level) {
        this.id = id;
        this.level = level;
    }

    public SkillLevel(UserSkill userSkill) {
        level = userSkill.getLevel();
        id = userSkill.getSkill().getId();
    }
}
