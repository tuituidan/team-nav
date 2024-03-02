package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.ChangePassword;
import com.tuituidan.openhub.bean.dto.UserDto;
import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.vo.UserVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.RoleUserRepository;
import com.tuituidan.openhub.repository.UserRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * UserService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/11/16
 */
@Service
public class UserService implements UserDetailsService, ApplicationRunner {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleUserRepository roleUserRepository;

    @Resource
    private CacheService cacheService;

    @Value("${spring.security.user.name}")
    private String defUsername;

    @Value("${spring.security.user.password}")
    private String defPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        insertDefaultUser();
    }

    private void insertDefaultUser() {
        if (userRepository.findById(Consts.DEFAULT_ID).isPresent()) {
            return;
        }
        userRepository.save(new User().setId(Consts.DEFAULT_ID)
                .setUsername(defUsername)
                .setStatus(Consts.DEFAULT_ID)
                .setNickname("管理员")
                .setAvatar("/assets/images/header.png")
                .setPassword(new BCryptPasswordEncoder().encode(defPassword)));
    }

    /**
     * 分页查询用户
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    public Page<UserVo> selectPage(String keywords, Integer pageIndex, Integer pageSize) {
        Specification<User> search = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.isNotBlank(keywords)) {
                predicate.getExpressions().add(builder.or(builder.like(root.get("username"), "%" + keywords + "%"),
                        builder.like(root.get("nickname"), "%" + keywords + "%")));
            }
            return predicate;
        };
        Page<User> users = userRepository.findAll(search,
                PageRequest.of(pageIndex, pageSize, Sort.by("updateTime").descending()));
        Map<String, List<Role>> userRolesMap =
                getUserRolesMap(users.stream().map(User::getId).collect(Collectors.toSet()));
        return users.map(user ->
                BeanExtUtils.convert(user, UserVo::new)
                        .setRoles(userRolesMap.get(user.getId()))
        );
    }

    private Map<String, List<Role>> getUserRolesMap(Collection<String> userIds) {
        List<RoleUser> roleUsers = roleUserRepository.findByUserIdIn(userIds);
        return roleUsers.stream().collect(Collectors.groupingBy(RoleUser::getUserId,
                Collectors.mapping(item -> cacheService.getRole(item.getRoleId()),
                        Collectors.toList())));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return user;
    }

    /**
     * 保存
     *
     * @param id id
     * @param userDto userDto
     */
    public void save(String id, UserDto userDto) {
        Assert.isTrue(!StringUtils.equalsIgnoreCase(userDto.getUsername(), "admin"), "默认管理员不可操作");
        User user;
        User exitUser = userRepository.findByUsername(userDto.getUsername());
        if (StringUtils.isBlank(id)) {
            Assert.isTrue(exitUser == null, "登录账号已存在");
            user = BeanExtUtils.convert(userDto, User::new);
            user.setId(StringExtUtils.getUuid());
            user.setPassword(new BCryptPasswordEncoder().encode(defPassword));
        } else {
            user = userRepository.getReferenceById(id);
            Assert.isTrue(StringUtils.equals(exitUser.getId(), id), "登录账号已存在");
            BeanExtUtils.copyNotNullProperties(userDto, user);
            roleUserRepository.deleteByUserId(user.getId());
        }
        roleUserRepository.saveAll(Arrays.stream(userDto.getRoleIds())
                .map(roleId -> new RoleUser().setId(StringExtUtils.getUuid()).setRoleId(roleId)
                        .setUserId(user.getId())).collect(Collectors.toList()));
        userRepository.save(user);
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String[] id) {
        List<String> ids = Arrays.asList(id);
        Set<String> usernames = userRepository.findAllById(ids).stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
        Assert.isTrue(!CollectionUtils.containsAny(usernames, "admin"), "默认管理员不可操作");
        userRepository.deleteAllById(Arrays.asList(id));
        roleUserRepository.deleteByUserIdIn(ids);
    }

    /**
     * changePassword
     *
     * @param changePassword changePassword
     */
    public void changePassword(ChangePassword changePassword) {
        User user = userRepository.getReferenceById(changePassword.getId());
        Assert.notNull(user, "用户获取失败");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Assert.isTrue(passwordEncoder.matches(changePassword.getOldPassword(),
                user.getPassword()), "原密码不匹配");
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * resetPassword
     *
     * @param ids ids
     */
    public void resetPassword(String[] ids) {
        List<User> users = userRepository.findAllById(Arrays.asList(ids));
        users.forEach(user -> user.setPassword(new BCryptPasswordEncoder().encode(defPassword)));
        userRepository.saveAll(users);
    }

}
