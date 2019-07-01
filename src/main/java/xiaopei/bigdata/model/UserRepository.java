package xiaopei.bigdata.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);
}
