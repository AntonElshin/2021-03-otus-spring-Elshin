package ru.otus.homework.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.library.domain.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByLoginEqualsAndStatusEquals(String login, String status);
}
