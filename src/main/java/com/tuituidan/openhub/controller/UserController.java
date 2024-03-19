package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.ChangePassword;
import com.tuituidan.openhub.bean.dto.UserDto;
import com.tuituidan.openhub.bean.entity.User;
import com.tuituidan.openhub.bean.vo.UserVo;
import com.tuituidan.openhub.service.UserService;
import com.tuituidan.openhub.util.SecurityUtils;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RoleController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/2
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UserVo>> selectPage(String keywords,
            @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(userService.selectPage(keywords, pageIndex - 1, pageSize));
    }

    /**
     * add
     *
     * @param userDto userDto
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody UserDto userDto) {
        userService.save(null, userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param userDto userDto
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
            @RequestBody UserDto userDto) {
        userService.save(id, userDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String[] id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * changePassword
     *
     * @param changePassword changePassword
     * @return Void
     */
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePassword) {
        userService.changePassword(SecurityUtils.getId(), changePassword);
        return ResponseEntity.noContent().build();
    }

    /**
     * resetPassword
     *
     * @param id id
     * @return Void
     */
    @PatchMapping("/{id}/actions/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable("id") String[] id) {
        userService.resetPassword(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * changeStatus
     *
     * @param id id
     * @param status status
     * @return Void
     */
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") String id,
            @PathVariable("status") String status) {
        userService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * star
     *
     * @param cardIds cardIds
     * @return Void
     */
    @PatchMapping("/star/card")
    public ResponseEntity<Void> star(@RequestBody String[] cardIds) {
        User userInfo = SecurityUtils.getUserInfo();
        if (userInfo != null) {
            userService.userStarCard(userInfo.getId(), cardIds);
        }
        return ResponseEntity.noContent().build();
    }

}
