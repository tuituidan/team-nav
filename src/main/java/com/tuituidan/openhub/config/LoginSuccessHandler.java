package com.tuituidan.openhub.config;

import com.tuituidan.openhub.bean.entity.RoleUser;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.entity.UserStar;
import com.tuituidan.openhub.repository.RoleUserRepository;
import com.tuituidan.openhub.repository.UserStarRepository;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
