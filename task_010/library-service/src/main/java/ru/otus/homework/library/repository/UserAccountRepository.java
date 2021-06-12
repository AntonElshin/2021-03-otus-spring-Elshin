package ru.otus.homework.library.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.homework.library.domain.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, Long> {

    Optional<UserAccount> findByLoginEqualsAndStatusEquals(String login, String status);
}
