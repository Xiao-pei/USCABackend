package xiaopei.bigdata.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    Job findJobById(Long id);
}
