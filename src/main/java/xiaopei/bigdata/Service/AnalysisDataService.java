/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package xiaopei.bigdata.Service;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevel;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;
import xiaopei.bigdata.model.*;

import java.util.*;

@Slf4j
@Service
public class AnalysisDataService implements AnalysisDataServiceInterface {
    @Autowired
    private UserSkillServiceInterface userSkillService;
    @Autowired
    private JavaSparkContext sparkContext;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserJobServiceInterface userJobService;
    @Autowired
    private JobSkillRepository jobSkillRepository;
    @Autowired
    private SkillRepository skillRepository;

    @Override
    public List<JobWithScore> getAnalysisJobData(User user) {
        List<SkillNameLevel> userSkillNameLevels = userSkillService.getUserSkillNameLevel(user);
        StringBuilder skillidAndLevel = new StringBuilder();
        for (SkillNameLevel skillNameLevel : userSkillNameLevels) {
            skillidAndLevel.append(skillNameLevel.getName());
            skillidAndLevel.append(",");
            skillidAndLevel.append(skillNameLevel.getLevel());
            skillidAndLevel.append("\n");
        }
        HashMap<String, Double> hashMap = SparkService.get_scores(SparkService.job_recommend(skillidAndLevel.toString(), sparkContext));
        List<JobWithScore> result = new ArrayList<>();
        Set<String> keyset = hashMap.keySet();
        for (String job : keyset) {
            Job tmp = jobRepository.findJobByName(job);
            double score = hashMap.get(job);
            result.add(new JobWithScore(tmp.getId(), job, score));
        }//Todo
        Collections.sort(result);
        List<JobWithScore> sortedResult = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            sortedResult.add(result.get(i));
        }
        userJobService.SaveUserJobs(sortedResult, user);
        //log.warn(hashMap.toString());
        return sortedResult;
    }

    @Override
    public List<SkillNameLevelScore> getAnalysisSkillData(User user, String job) {
        List<SkillNameLevel> skillNameLevels = userSkillService.getUserSkillNameLevel(user);
        List<JobSkill> jobSkills = jobSkillRepository.findJobSkillsByJobName(job);
        List<SkillNameLevelScore> result = new ArrayList<>();
        for (JobSkill jobSkill : jobSkills) {
            boolean flag = false;
            for (SkillNameLevel skillNameLevel : skillNameLevels) {
                if (skillNameLevel.getName().equals(jobSkill.getSkillName())) {
                    result.add(new SkillNameLevelScore(skillNameLevel, jobSkill.getScore()));
                    flag = true;
                    break;
                }
            }
            if (flag) {
                flag = false;
            } else {
                Skill skill = skillRepository.findSkillByName(jobSkill.getSkillName());
                result.add(new SkillNameLevelScore(jobSkill.getSkillName(), 0, skill.getId(), jobSkill.getScore()));
            }
        }
        return result;
    }
}
