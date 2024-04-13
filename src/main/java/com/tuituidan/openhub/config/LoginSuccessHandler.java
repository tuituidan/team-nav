package com.tuituidan.openhub.config;

import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.entity.UserStar;
import com.tuituidan.openhub.repository.RoleUserRepository;
import com.tuituidan.openhub.repository.UserStarRepository;
import com.tuituidan.openhub.util.SecurityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * LoginSuccessHandler.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/10/26
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Resource
    private UserStarRepository userStarRepository;

    @Resource
    private RoleUserRepository roleUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        user.setPassword(null);
        user.setStarCardIds(userStarRepository.findByUserId(user.getId()).stream()
                .map(UserStar::getCardId).collect(Collectors.toSet()));
        user.setRoleIds(roleUserRepository.findByUserId(user.getId()).stream()
                .map(RoleUser::getRoleId).collect(Collectors.toSet()));
        if (!SecurityUtils.isAdmin(user)) {
//            super.onAuthenticationSuccess(request, response, authentication);
            // 不执行重定向
            super.clearAuthenticationAttributes(request);
            return;
        }
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(),
                AuthorityUtils.createAuthorityList("admin"));
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
//        super.onAuthenticationSuccess(request, response, newAuthentication);
    }

}
