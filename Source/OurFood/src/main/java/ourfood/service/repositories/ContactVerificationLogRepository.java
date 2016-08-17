package ourfood.service.repositories;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.ContactVerificationLog;

public interface ContactVerificationLogRepository extends CrudRepository<ContactVerificationLog, Long> {

    /**
     * Find matching OTP for contact info, in the past one hr
     * 
     * @param contactInfo
     * @param contactType
     * @param otp
     * @param userId
     * @param timestamp
     * @return
     */
    int countByContactInfoAndContactTypeAndOtpAndUserIdAndTimestampGreaterThan(String contactInfo, String contactType,
            String otp, Long userId, Date timestamp);
}
