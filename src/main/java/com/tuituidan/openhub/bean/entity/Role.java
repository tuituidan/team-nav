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
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Role.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "nav_role", schema = "team_nav")
public class Role implements Serializable {

    private static final long serialVersionUID = 5210879308812671741L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "role_name", length = 100)
    private String roleName;

    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
