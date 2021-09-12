package ru.otus.homework.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.homework.entity.UserAccount;
import ru.otus.homework.entity.UserRole;
import ru.otus.homework.repository.UserAccountRepository;
import ru.otus.homework.security.User;
import ru.otus.homework.security.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /*@PostConstruct
    public void init() {
        Optional<UserEntity> optional = userRepository.findByLoginEqualsAndStatusEquals("admin", "ACTIVE");

        if(optional.isPresent()) {
            UserEntity userEntity = optional.get();
            userEntity.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(userEntity);
        }
    }*/

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        Optional<UserAccount> optional = userAccountRepository.findByLoginEqualsAndStatusEquals(username, "ACTIVE");

        if(optional.isPresent()) {

            UserAccount userAccount = optional.get();
            Boolean enabled = userAccount.getStatus().equalsIgnoreCase("active");

            List<GrantedAuthority> autorities = new ArrayList<GrantedAuthority>();

            for(UserRole userRole : userAccount.getUserRoles()) {
                autorities.add(new Role(userRole.getName()));
            }

            return User.builder()
                    .username(userAccount.getLogin())
                    .password(userAccount.getPassword())
                    .authorities(autorities)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(enabled)
                    .build();
        }
        else {
            throw new UsernameNotFoundException("user " + username + " was not found!");
        }

    }


}
