package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Countdown;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CountdownRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface CountdownRepository extends JpaRepository<Countdown, String> {

}
