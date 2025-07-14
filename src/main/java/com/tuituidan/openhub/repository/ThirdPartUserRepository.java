package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.ThirdPartUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThirdPartUserRepository extends JpaRepository<ThirdPartUser, Long> {

    Optional<ThirdPartUser> findByUniqueIdAndProviderId(String uniqueId, String providerId);


}
