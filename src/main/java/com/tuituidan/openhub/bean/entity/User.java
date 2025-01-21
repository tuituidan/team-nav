package com.tuituidan.openhub.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuituidan.openhub.util.SecurityUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * User.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/16
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "nav_user", schema = "team_nav")
@DynamicInsert
@DynamicUpdate
public class User implements UserDetails, OAuth2User, Serializable {

    private static final long serialVersionUID = -4826666254891063669L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "nick_name", length = 100)
    private String nickname;

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Column(name = "user_name", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Transient
    private Set<String> roleIds;

    @Transient
    private Set<String> starCardIds;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ThirdPartUser> thirdPartUsers=new HashSet<>();


    @Override
    public Map<String, Object> getAttributes() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email",getEmail());
        attributes.put("avatar",getAvatar());
        attributes.put("nickname",getNickname());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (SecurityUtils.isAdmin(this)) {
            return AuthorityUtils.createAuthorityList("admin");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return id;// 通常返回用户的唯一标识
    }
}
