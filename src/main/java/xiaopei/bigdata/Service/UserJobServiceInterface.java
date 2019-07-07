package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.DTO.JobWithScore;
import xiaopei.bigdata.model.Job;
import xiaopei.bigdata.model.User;

import java.util.List;

public interface UserJobServiceInterface {
    void SaveUserJob(User user, Job job, double score);

    void SaveUserJobs(List<JobWithScore> jobWithScoreList, User user);

    boolean DeleteUserJob(User user, Job job);

    boolean DeleteUserJobs(User user);
}
