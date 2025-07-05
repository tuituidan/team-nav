package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysDatasource.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/7/4
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "nav_datasource", schema = "team_nav")
public class SysDatasource implements Serializable {

    private static final long serialVersionUID = 4619475624006387908L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "desc", length = 100)
    private String desc;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "driver_class_name", length = 100)
    private String driverClassName;

    @Column(name = "jdbc_url", length = 400)
    private String jdbcUrl;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

}
