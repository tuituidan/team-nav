package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
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
@Table(name = "T_USER")
@DynamicInsert
@DynamicUpdate
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = -4826666254891063669L;

    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_NICKNAME", length = 100)
    private String nickname;

    @Column(name = "C_AVATAR", length = 200)
    private String avatar;

    @Column(name = "C_USERNAME", length = 100)
    private String username;

    @Column(name = "C_PASSWORD", length = 100)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
