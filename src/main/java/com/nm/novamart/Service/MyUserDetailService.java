package com.nm.novamart.Service;

import com.nm.novamart.Entity.User;
import com.nm.novamart.Entity.UserPrincipal;
import com.nm.novamart.Exeptions.UserNotFoundException;
import com.nm.novamart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

        if(userRepository.existsByUserName(username)){
            User user = userRepository.getUserByUserName(username);
            return new UserPrincipal(user);
        }else {
            throw new UserNotFoundException("User not found with username " + username);
        }

    }
}
