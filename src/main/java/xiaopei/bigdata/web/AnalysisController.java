package xiaopei.bigdata.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xiaopei.bigdata.MyException;
import xiaopei.bigdata.Service.AnalysisDataServiceInterface;
import xiaopei.bigdata.Service.SecurityServiceInterface;
import xiaopei.bigdata.Service.UserJobServiceInterface;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;
import xiaopei.bigdata.model.Job;
import xiaopei.bigdata.model.JobRepository;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AnalysisController {
    private final AnalysisDataServiceInterface analysisDataService;
    private final SecurityServiceInterface securityService;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final UserJobServiceInterface userJobService;

    public AnalysisController(AnalysisDataServiceInterface analysisDataService, SecurityServiceInterface securityService, JobRepository jobRepository, UserRepository userRepository, UserJobServiceInterface userJobService) {
        this.analysisDataService = analysisDataService;
        this.securityService = securityService;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.userJobService = userJobService;
    }

    @GetMapping(path = "/analysis/jobs")
    public Map<String, Object> getJobAnalysis() {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findUserByUsername(username);
        userJobService.DeleteUserJobs(user);
        List<JobWithScore> jobWithScoreList = analysisDataService.getAnalysisJobData(user);
        user.setBestMatchJobName(jobWithScoreList.get(0).getName());
        userRepository.save(user);
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", jobWithScoreList);
        return map;
    }

    @GetMapping(path = "/analysis/tags")
    public Map<String, Object> getSkillAnalysis(@RequestParam long job) {
        Job jobById = jobRepository.findJobById(job);
        String username = securityService.findLoggedInUsername();
        if (jobById == null) {
            throw new MyException(404, "No Such Job!");
        }
        User user = userRepository.findUserByUsername(username);
        List<SkillNameLevelScore> skillNameLevelScores = analysisDataService.getAnalysisSkillData(user, jobById.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", skillNameLevelScores);
        return map;
    }

    @GetMapping(path = "/test")
    public String count() {
        //User user = userRepository.findUserByUsername("xiaopei");
        //Skill skill = skillRepository.findSkillById(Integer.toUnsignedLong(6));
        // userSkillService.SaveUserSkill(user,skill,2);
        //userSkillAnalysisService.SaveUserSkillJobAnalysisResult(user,skill,"带数据",89);
        return "ok";
    }
}
