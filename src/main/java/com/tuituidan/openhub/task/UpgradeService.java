package com.tuituidan.openhub.task;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.RoleRepository;
import com.tuituidan.openhub.service.CacheService;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
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

    private static final String SQL = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'T_USER'";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.findById(Consts.DEFAULT_ID).isPresent()) {
            return;
        }
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL);
            //判断有没有1.X的表，没有就是全新安装
            if (!resultSet.next()) {
                EncodedResource resource = new EncodedResource(new ClassPathResource("sql/data.sql"),
                        StandardCharsets.UTF_8);
                ScriptUtils.executeSqlScript(connection, resource);
                return;
            }
            EncodedResource resource = new EncodedResource(new ClassPathResource("sql/upgrade.sql"),
                    StandardCharsets.UTF_8);
            ScriptUtils.executeSqlScript(connection, resource);
            //非全新安装有历史数据，重新加载一下缓存
            cacheService.run(null);
        } catch (SQLException e) {
            log.error("执行升级脚本脚本异常！", e);
        }
    }

}
