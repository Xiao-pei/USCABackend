package xiaopei.bigdata.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findSkillByNameLike(String name);

    Skill findSkillById(Long id);

}
