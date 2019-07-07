package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;
import xiaopei.bigdata.model.User;

import java.util.List;

public interface AnalysisDataServiceInterface {
    List<JobWithScore> getAnalysisJobData(User user);

    List<SkillNameLevelScore> getAnalysisSkillData(User user, String jobName);
}
