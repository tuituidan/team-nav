package com.tuituidan.openhub.bean.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * 第三方平台用户登录方式表
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name= "third_part_user", schema = "team_nav")
@Entity
public class ThirdPartUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String nickname; // 第三方平台的昵称

    private String avatar;   // 第三方平台的头像

    @Column(name = "unique_id" ,nullable = false)
    @Comment("用户在第三方平台的唯一标识，比如微信的OpenId,手机号，邮箱")
    private String uniqueId;


    @Column(name = "credential")
    @Comment("凭证（如密码哈希、微信的 access_token）")
    private String credential;


    @Column(name = "update_time",insertable = false,updatable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Comment("第三方平台唯一标识")
    @Column(name="provider_id",nullable = false)
    private String providerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "update_time")
    @CreationTimestamp
    private LocalDateTime createAt;


    @Transient // 不存储到数据库
    private Collection<? extends GrantedAuthority> authorities; // 权限列表

    @Transient // 不存储到数据库
    private Map<String, Object> attributes; // OAuth2用户属性

}
