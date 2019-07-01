package xiaopei.bigdata.Service;

import xiaopei.bigdata.User.User;

public interface UserRegisterServiceInterface {
    void save(User user);

    User findByUsername(String username);
}
