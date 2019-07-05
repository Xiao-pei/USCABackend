/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package xiaopei.bigdata.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevel;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;

import java.util.List;

@Slf4j
@Service
public class AnalysisDataService implements AnalysisDataServiceInterface {
    @Autowired
    private UserSkillServiceInterface userSkillService;
    @Autowired
    private SparkService sparkService;

    @Override
    public List<JobWithScore> getAnalysisJobData(String username) {
        List<SkillNameLevel> userSkillNameLevels = userSkillService.getUserSkillNameLevel(username);
        StringBuilder skillidAndLevel = new StringBuilder();
        for (SkillNameLevel skillNameLevel : userSkillNameLevels) {
            skillidAndLevel.append(skillNameLevel.getName());
            skillidAndLevel.append(",");
            skillidAndLevel.append(skillNameLevel.getLevel());
            skillidAndLevel.append("\n");
        }
        log.info(sparkService.job_recommend(skillidAndLevel.toString()).toString());
        //Todo
        return null;
    }

    @Override
    public List<SkillNameLevelScore> getAnalysisSkillData(String username, String job) {
        //Todo
        return null;
    }
}
