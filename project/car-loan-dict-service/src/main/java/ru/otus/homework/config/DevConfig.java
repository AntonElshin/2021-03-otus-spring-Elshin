package ru.otus.homework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otus.homework.entity.UserAccount;
import ru.otus.homework.repository.UserAccountRepository;
import ru.otus.homework.security.jwt.JwtProvider;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("dev")
@Configuration
public class DevConfig {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @PostConstruct
    public void generateTokens() {

        List<UserAccount> userAccounts = userAccountRepository.findAll();

        StringBuilder tokenStr = new StringBuilder("");
        tokenStr.append("\n*************************************************DEV*TOKENS***********************************************************\n");

        if(userAccounts != null) {
            for(UserAccount userAccount : userAccounts) {
                tokenStr.append("login = " + userAccount.getLogin() + ", token = " + jwtProvider.generateToken(userAccount.getLogin()) + "\n");
            }
        }

        tokenStr.append("**********************************************************************************************************************\n");

        System.out.println(tokenStr.toString());

    }

}
