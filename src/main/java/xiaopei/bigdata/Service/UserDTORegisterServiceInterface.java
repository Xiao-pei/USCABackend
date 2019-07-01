package xiaopei.bigdata.Service;

import xiaopei.bigdata.User.User;
import xiaopei.bigdata.User.UserDTO;

public interface UserDTORegisterServiceInterface {
    User registerNewUserAccount(UserDTO accountDto) throws Exception;
}
