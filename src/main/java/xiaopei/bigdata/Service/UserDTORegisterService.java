package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserDTO;
import xiaopei.bigdata.model.UserRepository;

@Service
public class UserDTORegisterService implements UserDTORegisterServiceInterface {
    @Autowired
    private UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public User registerNewUserAccount(UserDTO accountDto) throws Exception {
        if (repository.findUserByUsername(accountDto.getUsername()) != null) {
            throw new Exception("username existed");
        }
        User user = new User(accountDto.getUsername(), bCryptPasswordEncoder.encode(accountDto.getPassword()));
        user.setName(accountDto.getName());
        user.setUserType(0);
        user.setTelephone(accountDto.getTelephone());
        return repository.save(user);
    }
}
