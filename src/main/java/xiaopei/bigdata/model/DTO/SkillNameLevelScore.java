package xiaopei.bigdata.model.DTO;

import lombok.Data;

/**
 * Data transfer object
 */
@Data
public class SkillNameLevelScore {
    private String name;
    private Integer level;
    private Long id;
    private Float score;

    public SkillNameLevelScore(String name, Integer level, Long id, Float score) {
        this.name = name;
        this.level = level;
        this.id = id;
        this.score = score;
    }

    public SkillNameLevelScore(SkillNameLevel skillNameLevel, float score) {
        this.id = skillNameLevel.getId();
        this.level = skillNameLevel.getLevel();
        this.name = skillNameLevel.getName();
        this.score = score;
    }
}
