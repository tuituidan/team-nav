package com.tuituidan.openhub.repository;

import com.tuituidan.openhub.bean.entity.Attachment;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
public interface AttachmentRepository extends JpaRepository<Attachment, String> {

    /**
     * deleteByBusinessIdIsNullaAndCreateTimeBefore
     *
     * @param time time
     */
    void deleteByBusinessIdIsNullAndCreateTimeBefore(LocalDateTime time);

    /**
     * deleteByBusinessIdIn
     *
     * @param businessIds businessIds
     */
    void deleteByBusinessIdIn(Collection<String> businessIds);

    /**
     * findByBusinessId
     *
     * @param businessId v
     * @return List
     */
    List<Attachment> findByBusinessIdIn(Collection<String> businessId);

}
