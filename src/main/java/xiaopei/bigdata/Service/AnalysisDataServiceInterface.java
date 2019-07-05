package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;

import java.util.List;

public interface AnalysisDataServiceInterface {
    List<JobWithScore> getAnalysisJobData(String username);

    List<SkillNameLevelScore> getAnalysisSkillData(String username, String job);
}
