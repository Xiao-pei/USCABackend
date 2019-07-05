package xiaopei.bigdata.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xiaopei.bigdata.MyException;
import xiaopei.bigdata.Service.AnalysisDataServiceInterface;
import xiaopei.bigdata.Service.SecurityServiceInterface;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.DTO.SkillNameLevelScore;
import xiaopei.bigdata.model.Job;
import xiaopei.bigdata.model.JobRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AnalysisController {
    private final AnalysisDataServiceInterface analysisDataService;
    private final SecurityServiceInterface securityService;
    private final JobRepository jobRepository;

    public AnalysisController(AnalysisDataServiceInterface analysisDataService, SecurityServiceInterface securityService, JobRepository jobRepository) {
        this.analysisDataService = analysisDataService;
        this.securityService = securityService;
        this.jobRepository = jobRepository;
    }

    @GetMapping(path = "/analysis/jobs")
    public Map<String, Object> getJobAnalysis() {
        String username = securityService.findLoggedInUsername();
        List<JobWithScore> jobWithScoreList = analysisDataService.getAnalysisJobData(username);
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", jobWithScoreList);
        return map;
    }

    @GetMapping(path = "/analysis/tags")
    public Map<String, Object> getSkillAnalysis(@RequestParam Long jobid) {
        Job job = jobRepository.findJobById(jobid);
        String username = securityService.findLoggedInUsername();
        if (job == null) {
            throw new MyException(404, "No Such Job!");
        }
        List<SkillNameLevelScore> skillNameLevelScores = analysisDataService.getAnalysisSkillData(username, job.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("error", 0);
        map.put("errmsg", "nothing");
        map.put("data", skillNameLevelScores);
        return map;
    }

    @PostMapping(path = "/test")
    public String count() {
        analysisDataService.getAnalysisJobData("xiaopei");
        //User user = userRepository.findUserByUsername("xiaopei");
        //Skill skill = skillRepository.findSkillById(Integer.toUnsignedLong(6));
        // userSkillService.SaveUserSkill(user,skill,2);
        //userSkillAnalysisService.SaveUserSkillJobAnalysisResult(user,skill,"带数据",89);
        return "ok";
    }
}
