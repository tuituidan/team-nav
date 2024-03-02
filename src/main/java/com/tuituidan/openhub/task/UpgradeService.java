package com.tuituidan.openhub.task;

import com.tuituidan.openhub.bean.entity.Role;
import com.tuituidan.openhub.repository.RoleRepository;
import com.tuituidan.openhub.service.CacheService;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

/**
 * 1.X自动升级2.0矫正脚本，仅执行一次.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/3/2
 */
@Service
@Slf4j
public class UpgradeService implements ApplicationRunner {

    @Resource
    private DataSource dataSource;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private CacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> roles = roleRepository.findAll();
        if (CollectionUtils.isNotEmpty(roles)) {
            return;
        }
        EncodedResource resource = new EncodedResource(new ClassPathResource("sql/upgrade.sql"),
                StandardCharsets.UTF_8);
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, resource);
        } catch (SQLException e) {
            log.error("执行升级脚本脚本异常！", e);
        }
        // 更新脚本重新加载缓存
        cacheService.run(null);
    }

}
