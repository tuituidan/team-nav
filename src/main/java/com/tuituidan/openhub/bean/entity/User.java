package com.tuituidan.openhub.bean.entity;

import com.tuituidan.openhub.util.SecurityUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails, Serializable {

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

    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Transient
    private Set<String> roleIds;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (SecurityUtils.isAdmin(this)) {
            return Collections.singletonList(new SimpleGrantedAuthority("admin"));
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

}
