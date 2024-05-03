package edu.unimagdalena.demospringsecurity.services.security;

import edu.unimagdalena.demospringsecurity.controllers.dto.UserInfo;
import edu.unimagdalena.demospringsecurity.entities.User;
import edu.unimagdalena.demospringsecurity.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByEmail(email);
        return userDetail.map(UserInfoDetail::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    public UserInfo addUser(UserInfo userInfo) {
        User user = new User(null, userInfo.name(), userInfo.email(), passwordEncoder.encode(userInfo.password()), userInfo.roles() );
        user = userRepository.save(user);
        return new UserInfo(user.getName(), user.getEmail(), userInfo.password(), user.getRoles());

    }
}
