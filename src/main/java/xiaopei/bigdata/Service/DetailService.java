/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package xiaopei.bigdata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xiaopei.bigdata.model.User;
import xiaopei.bigdata.model.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class DetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if (user == null) throw new UsernameNotFoundException("user not found: " + s);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user.getUserType() == 0)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_User"));
        else grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_Admin"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getSecuredPassword(), grantedAuthorities);
    }
}
