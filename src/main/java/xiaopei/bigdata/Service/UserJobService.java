package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.*;

import java.util.List;
import java.util.Set;

@Service
public class UserJobService implements UserJobServiceInterface {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JobRepository jobRepository;

    @Override
    public void SaveUserJob(User user, Job job, double score) {
        UserJob userJob = new UserJob(user, job, score);
        if (user.getUserJobs().contains(userJob))
            return;
        user.getUserJobs().add(userJob);
        jobRepository.save(job);
        userRepository.save(user);
    }

    @Override
    public void SaveUserJobs(List<JobWithScore> jobWithScoreList, User user) {
        for (JobWithScore jobWithScore : jobWithScoreList) {
            Job job = jobRepository.findJobById(jobWithScore.getId());
            SaveUserJob(user, job, jobWithScore.getScore());
        }
    }

    @Transactional
    @Override
    public boolean DeleteUserJob(User user, Job job) {
        Set<UserJob> userJobs = user.getUserJobs();
        for (UserJob userJob : userJobs) {
            if (userJob.getJob().equals(job)) {
                userJobs.remove(userJob);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean DeleteUserJobs(User user) {
        Set<UserJob> userJobs = user.getUserJobs();
        userJobs.clear();
        userRepository.save(user);
        if (user.getUserJobs().size() == 0)
            return true;
        else return false;
    }


}
