package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaopei.bigdata.model.*;

@Service
public class UserSkillAnalysisService implements UserSkillAnalysisServiceInterface {

    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void SaveUserSkillJobAnalysisResult(User user, Skill skill, String job, double score) {
        AnalysisResult analysisResult = new AnalysisResult(user, skill, job);
        if (user.getAnalysisResults().contains(analysisResult))
            return;
        user.getAnalysisResults().add(analysisResult);
        skillRepository.save(skill);
        userRepository.save(user);
    }
}
