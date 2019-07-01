package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserDTO;

public interface UserDTORegisterServiceInterface {
    User registerNewUserAccount(UserDTO accountDto) throws Exception;
}
