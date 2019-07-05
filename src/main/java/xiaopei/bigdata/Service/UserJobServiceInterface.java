package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.Job;
import xiaopei.bigdata.model.User;

public interface UserJobServiceInterface {
    void SaveUserJob(User user, Job job, double score);

    boolean DeleteUserJob(User user, Job job);
}
