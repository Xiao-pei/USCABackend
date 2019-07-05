package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.Skill;
import xiaopei.bigdata.model.User;

public interface UserSkillAnalysisServiceInterface {
    void SaveUserSkillJobAnalysisResult(User user, Skill skill, String job, double score);
}
