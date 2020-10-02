package com.tuituidan.teamnav.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Site.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_SITE")
public class Site implements Serializable {

    private static final long serialVersionUID = -4281175586987550016L;
    /**
     * id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "N_ID")
    private Integer id;

    @Column(name = "C_TAG")
    private String tag;

    @Column(name = "C_ICON")
    private String icon;

    @Column(name = "C_NAME")
    private String name;

    @Column(name = "C_LINK")
    private String link;
}
