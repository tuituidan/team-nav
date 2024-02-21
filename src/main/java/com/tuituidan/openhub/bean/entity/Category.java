package com.tuituidan.openhub.bean.entity;

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
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Category.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "nav_category", schema = "team_nav")
@DynamicUpdate
@DynamicInsert
public class Category implements ISortEntity<Category> {
    private static final long serialVersionUID = -369054106198786491L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "pid", length = 32)
    private String pid;

    @Column(name = "icon", length = 400)
    private String icon;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "level")
    private Integer level;

    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

}
