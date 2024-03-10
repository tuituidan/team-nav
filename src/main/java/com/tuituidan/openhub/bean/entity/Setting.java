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
@Table(name = "nav_setting", schema = "team_nav")
@DynamicInsert
@DynamicUpdate
public class Setting implements Serializable {

    private static final long serialVersionUID = -6313930717204077701L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "nginx_open")
    private Boolean nginxOpen;

    @Column(name = "nginx_url", length = 200)
    private String nginxUrl;

    @Column(name = "nav_name", length = 200)
    private String navName;

    @Column(name = "cut_over_speed")
    private Integer cutOverSpeed;

    @Column(name = "logo_path", length = 400)
    private String logoPath;

    @Column(name = "logo_to_favicon")
    private Boolean logoToFavicon;

    @Column(name = "show_doc")
    private Boolean showDoc;
}
