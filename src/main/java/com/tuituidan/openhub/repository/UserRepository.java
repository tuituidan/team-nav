package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface UserRepository extends JpaRepository<User, String> {

}
