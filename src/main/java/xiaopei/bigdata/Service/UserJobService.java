package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xiaopei.bigdata.model.*;

import java.util.Set;

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

}
