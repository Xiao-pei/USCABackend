/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xiaopei.bigdata.model.DTO.UserDTO;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserRepository;

@Service
public class UserDTORegisterService implements UserDTORegisterServiceInterface {
    @Autowired
    private UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User registerNewUserAccount(UserDTO accountDto) throws Exception {
        if (repository.findUserByUsername(accountDto.getUsername()) != null) {
            throw new Exception("username existed");
        }
        User user = new User(accountDto.getUsername(), bCryptPasswordEncoder.encode(accountDto.getPassword()));
        user.setName(accountDto.getName());
        user.setUserType(0);
        user.setTelephone(accountDto.getPhone());
        return repository.save(user);
    }
}
