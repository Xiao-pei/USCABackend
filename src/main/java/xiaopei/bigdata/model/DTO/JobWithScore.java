package xiaopei.bigdata.model.DTO;

import lombok.Data;
import xiaopei.bigdata.model.UserJob;

/**
 * Data transfer object
 */
@Data
public class JobWithScore {
    private Long id;
    private String name;
    private Double score;

    public JobWithScore(Long id, String name, Double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public JobWithScore(UserJob userJob) {
        this.id = userJob.getJob().getId();
        this.name = userJob.getJob().getName();
        this.score = userJob.getScore();
    }
}
