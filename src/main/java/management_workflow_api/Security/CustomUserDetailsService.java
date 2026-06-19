package management_workflow_api.Security;


import lombok.RequiredArgsConstructor;
import management_workflow_api.Entity.WebUser;
import management_workflow_api.Repository.WebUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final WebUserRepository webUserRepository;


    @Override
    public UserDetails loadUserByUsername (String username){

        WebUser webUser = webUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(webUser.getUsername())
                .password(webUser.getPassword())
                .roles(webUser.getRole())
                .build();

    }






}
