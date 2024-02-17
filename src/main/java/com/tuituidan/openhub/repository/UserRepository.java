package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * UserRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * findByUsername
     *
     * @param username username
     * @return User
     */
    User findByUsername(String username);

}
