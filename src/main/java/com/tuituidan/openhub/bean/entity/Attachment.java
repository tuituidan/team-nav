package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Attachment.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/27
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "nav_attachment", schema = "team_nav")
@DynamicInsert
@DynamicUpdate
public class Attachment implements Serializable {

    private static final long serialVersionUID = -832892436518171512L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "business_id", length = 32)
    private String businessId;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "ext", length = 10)
    private String ext;

    @Column(name = "path", length = 200)
    private String path;

    @Column(name = "size")
    private Long size;

    @Column(name = "create_time")
    @CreationTimestamp
    private LocalDateTime createTime;

}
