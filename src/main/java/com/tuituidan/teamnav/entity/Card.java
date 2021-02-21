package com.tuituidan.teamnav.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

/**
 * Card.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_CARD")
public class Card implements Serializable {

    private static final long serialVersionUID = -4281175586987550016L;

    @Id
    @GeneratedValue(generator = "sys_uid")
    @GenericGenerator(name = "sys_uid", strategy = "uuid")
    @Column(name = "C_ID")
    private String id;

    @Column(name = "C_CATEGORY")
    private String category;

    @Column(name = "C_ICON")
    private String icon;

    @Column(name = "C_NAME")
    private String name;

    @Column(name = "C_CONTENT")
    private String content;
}
