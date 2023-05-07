package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;
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
 * Setting.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_SETTING")
@DynamicInsert
@DynamicUpdate
public class Setting implements Serializable {

    private static final long serialVersionUID = -6313930717204077701L;

    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_NGINX_OPEN")
    private Boolean nginxOpen;

    @Column(name = "C_NGINX_URL", length = 200)
    private String nginxUrl;

    @Column(name = "C_NAV_NAME", length = 200)
    private String navName;

    @Column(name = "C_COUNTDOWN")
    private Boolean countdown;

    @Column(name = "N_CUTOVER_SPEED")
    private Integer cutOverSpeed;

    @Column(name = "C_LOGO_PATH", length = 400)
    private String logoPath;
}
