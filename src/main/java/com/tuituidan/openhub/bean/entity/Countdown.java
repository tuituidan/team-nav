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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Countdown.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/20
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_COUNTDOWN")
@DynamicUpdate
@DynamicInsert
public class Countdown implements Serializable {

    private static final long serialVersionUID = 5686996589966592484L;

    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_PREFIX_TEXT", length = 100)
    private String prefixText;

    @Column(name = "DT_ENDTIME")
    private LocalDateTime endTime;
}
