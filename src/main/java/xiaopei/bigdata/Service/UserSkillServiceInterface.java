package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.DTO.SkillNameLevel;
import xiaopei.bigdata.model.Skill;
import xiaopei.bigdata.model.User;

import java.util.List;

public interface UserSkillServiceInterface {
    void SaveUserSkill(User user, Skill skill, int level);

    boolean DeleteUserSkill(User user, Skill skill);

    boolean DeleteUserSkill(User user);

    List<SkillNameLevel> getUserSkillNameLevel(User user);
}
