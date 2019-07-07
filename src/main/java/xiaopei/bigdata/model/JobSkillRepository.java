package xiaopei.bigdata.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    List<JobSkill> findJobSkillsByJobName(String jobName);
}
