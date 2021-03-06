package org.voting.gateway.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.voting.gateway.domain.User;
import org.voting.gateway.domain.Authority;
import org.springframework.stereotype.Repository;
import org.voting.gateway.service.dto.LoginDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public class UserLoginDataRepository {

    private final Logger log = LoggerFactory.getLogger(UserLoginDataRepository.class);

    final String url = "http://msapp/api/loginData/";


    @Autowired
    @Lazy
    @Qualifier("loadBalancedRestTemplate")
    RestTemplate restTemplate;



    public UserLoginDataRepository()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /*    String passHash = encoder.encode("abc");
        this.user = new User();
        this.user.setLogin("abc");
        //user.setPassword(passHash);
        this.user.setActivated(true);
        Authority authority = new Authority();
        authority.setName("ROLE_ADMIN");
        this.user.getAuthorities().add(authority);*/

    }




    public Optional<User> findUserDataByLogin(String login)
    {
        ResponseEntity<LoginDataDTO> responseEntity = restTemplate.getForEntity(url+login,LoginDataDTO.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK)
        {
            log.debug("failed to get login data from msapp, status: {}",
                responseEntity.getStatusCodeValue());

            return Optional.empty();
        }
        LoginDataDTO loginData = responseEntity.getBody();

        User user = new User();

        user.setLogin(login);
        user.setPassword(loginData.getPassHash());


        Authority authority = new Authority();
        authority.setName(loginData.getGroup());
        user.getAuthorities().add(authority);
        if(loginData.getGroup() != null){
            addAuthority(user, "ROLE_USER");
        }
        String group = loginData.getGroup();
        if(group.equals("ROLE_GKW_LEADER")){
            addAuthority(user, "ROLE_GKW_MEMBER");
        }
        if(group.equals("ROLE_OKW_LEADER")){
            addAuthority(user, "ROLE_OKW_MEMBER");
        }

        user.setActivated(!loginData.isDisabled());

        return Optional.of(user);
    }

    private void addAuthority(User user, String name){
        Authority authority = new Authority();
        authority.setName(name);
        user.getAuthorities().add(authority);
    }


}
