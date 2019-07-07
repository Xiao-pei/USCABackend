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
    private Integer expected;

    public SkillNameLevelScore(String name, Integer level, Long id, Double score) {
        this.name = name;
        this.level = level;
        this.id = id;
        this.expected = score.intValue();
    }

    public SkillNameLevelScore(SkillNameLevel skillNameLevel, Double score) {
        this.id = skillNameLevel.getId();
        this.level = skillNameLevel.getLevel();
        this.name = skillNameLevel.getName();
        this.expected = score.intValue();
    }
}
