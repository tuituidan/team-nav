package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.RoleUserDto;
import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.bean.vo.RoleVo;
import com.tuituidan.openhub.service.RoleService;
import java.util.List;
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
@RequestMapping("/api/v1/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * select
     *
     * @return List
     */
    @GetMapping
    public List<Role> select() {
        return roleService.select();
    }

    /**
     * 分页查询角色
     *
     * @param keywords keywords
     * @param pageIndex pageIndex
     * @param pageSize pageSize
     * @return Page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Role>> selectPage(String keywords,
            @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(roleService.selectPage(keywords, pageIndex - 1, pageSize));
    }

    /**
     * getRoleDetail
     *
     * @param id id
     * @return RoleVo
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleVo> getRoleDetail(@PathVariable String id) {
        return ResponseEntity.ok(roleService.getRoleDetail(id));
    }

    /**
     * add
     *
     * @param role role
     * @return Void
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Role role) {
        roleService.save(null, role.getRoleName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * update
     *
     * @param id id
     * @param role role
     * @return Void
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id,
            @RequestBody Role role) {
        roleService.save(id, role.getRoleName());
        return ResponseEntity.noContent().build();
    }

    /**
     * saveRoleCategories
     *
     * @param id id
     * @param categoryIds categoryIds
     * @return Void
     */
    @PatchMapping("/{id}/category")
    public ResponseEntity<Void> saveRoleCategories(@PathVariable("id") String id,
            @RequestBody String[] categoryIds) {
        roleService.saveRoleCategories(id, categoryIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * updateUser
     *
     * @param id id
     * @param roleUserDto roleUserDto
     * @return Void
     */
    @PatchMapping("/{id}/user")
    public ResponseEntity<Void> saveRoleUser(@PathVariable("id") String id,
            @RequestBody RoleUserDto roleUserDto) {
        roleService.saveRoleUser(id, roleUserDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * delete
     *
     * @param id id
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
