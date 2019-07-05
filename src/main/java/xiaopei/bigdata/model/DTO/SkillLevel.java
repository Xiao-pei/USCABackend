package xiaopei.bigdata.model.DTO;

import lombok.Data;
import xiaopei.bigdata.model.UserSkill;

/**
 * Data transfer object
 */
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
