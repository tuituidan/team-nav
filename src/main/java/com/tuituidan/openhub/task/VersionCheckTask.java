package com.tuituidan.openhub.task;

import com.tuituidan.openhub.bean.vo.VersionInfo;
import com.tuituidan.openhub.util.HttpUtils;
import com.tuituidan.openhub.util.thread.CompletableUtils;
import java.util.Objects;
import java.util.function.ToIntFunction;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * VersionCheckTask.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/17
 */
@Slf4j
@Component
@DependsOn("completableUtils")
@EnableScheduling
public class VersionCheckTask {

    @Value("v${project.version}")
    private String currentVersion;

    @Value("${project.remote-version-url}")
    private String remoteVersionUrl;

    @Getter
    private VersionInfo versionInfo;

    @PostConstruct
    private void initVersion() {
        versionInfo = new VersionInfo()
                .setRemoteVersion(currentVersion)
                .setCurrentVersion(currentVersion)
                .setHasNewVersion(false);
        CompletableUtils.runAsync(this::check);
    }

    /**
     * check
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void check() {
        Document doc = HttpUtils.getJsoupDoc(remoteVersionUrl);
        if (Objects.isNull(doc)) {
            return;
        }
        String remoteVersion = doc.select("#releases-show .tag-name")
                .attr("data-tag-name");
        if (StringUtils.isBlank(remoteVersion)) {
            return;
        }
        versionInfo.setRemoteVersion(remoteVersion);
        ToIntFunction<String> func = version -> Integer.parseInt(version.replaceAll("\\D", ""));
        versionInfo.setHasNewVersion(func.applyAsInt(versionInfo.getRemoteVersion()) > func.applyAsInt(versionInfo.getCurrentVersion()));
    }

}
