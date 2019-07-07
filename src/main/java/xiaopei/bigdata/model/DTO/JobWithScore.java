package xiaopei.bigdata.model.DTO;

import lombok.Data;
import xiaopei.bigdata.model.UserJob;

/**
 * Data transfer object
 */
@Data
public class JobWithScore implements Comparable {
    private Long id;
    private String name;
    private Integer score;

    public JobWithScore(Long id, String name, Double score) {
        this.id = id;
        this.name = name;
        this.score = score.intValue();
    }

    public JobWithScore(UserJob userJob) {
        this.id = userJob.getJob().getId();
        this.name = userJob.getJob().getName();
        this.score = userJob.getScore().intValue();
    }


    @Override
    public int compareTo(Object o) {
        JobWithScore jobWithScore = (JobWithScore) o;
        return jobWithScore.getScore().compareTo(this.getScore());
    }
}
