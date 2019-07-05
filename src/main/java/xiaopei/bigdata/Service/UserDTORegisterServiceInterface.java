package xiaopei.bigdata.Service;

import xiaopei.bigdata.model.DTO.UserDTO;
import xiaopei.bigdata.model.User;

public interface UserDTORegisterServiceInterface {
    User registerNewUserAccount(UserDTO accountDto) throws Exception;
}
