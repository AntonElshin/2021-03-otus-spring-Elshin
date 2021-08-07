package ru.otus.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.SmsVerification;

import java.util.Optional;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long>{
    Optional<SmsVerification> findBySecretCodeAndProcessGuidAndStatus(
            String secretCode, String processGuid, String status);

    Optional<SmsVerification> findByPhoneNumberEquals(String phoneNumber);

    @Transactional
    @Modifying
    @Query("update SmsVerification v set status = ?1 where processGuid = ?2")
    int updateStatusByProcessGuid(String status, String processGuid);

}
