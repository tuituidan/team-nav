package com.tuituidan.openhub.task;

import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.AttachmentRepository;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.service.SettingService;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时删除没有使用的文件
 * 虽然删除数据时有清理对应文件的操作，但修改时为了逻辑更简单，并未涉及文件删除，可能就会产生无用文件
 * 这里提供定时去清理
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/12
 */
@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(value = "clear-useless-files.enable", havingValue = "true")
public class ClearUselessFilesTask {

    @Resource
    private CardRepository cardRepository;

    @Resource
    private SettingService settingService;

    @Resource
    private AttachmentRepository attachmentRepository;

    @Scheduled(cron = "${clear-useless-files.cron}")
    private void clear() {
        Set<String> existFileIds = getExistFileIds();
        deleteUselessFiles("/ext-resources/images", existFileIds);
        deleteUselessFiles("/ext-resources/modules", existFileIds);
        deleteUselessFiles("/ext-resources/attachments", getExistAttachment());
    }

    private void deleteUselessFiles(String path, Set<String> existFileIds) {
        File root = new File(Consts.ROOT_DIR + path);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            // 只删除文件名是日期类型的 images下的default和modules下的已解压的网站文件都忽略
            if (file.isDirectory() && file.getName().length() == 8) {
                deleteUselessFiles(file, existFileIds);
            }
        }
    }

    private void deleteUselessFiles(File parentFile, Set<String> existFileIds) {
        File[] files = parentFile.listFiles();
        if (files == null || files.length <= 0) {
            forceDeleteFile(parentFile);
            return;
        }
        for (File file : files) {
            if (!existFileIds.contains(FilenameUtils.getBaseName(file.getName()))) {
                forceDeleteFile(file);
            }
        }
        // 重新listFiles一次，避免前面删除的过程中有新的文件进入添加进parentFile，虽然可能性很小
        File[] newFiles = parentFile.listFiles();
        if (newFiles == null || newFiles.length <= 0) {
            forceDeleteFile(parentFile);
        }
    }

    private void forceDeleteFile(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException ex) {
            log.error("文件删除失败", ex);
        }
    }

    /**
     * 获取正在使用的文件ID，每个上传的文件（不论是图标还是网站压缩包）都是new的新的uuid
     * 根据uuid的文件名即可唯一确定一个文件，不需要完整路径
     *
     * @return Set
     */
    private Set<String> getExistFileIds() {
        Set<String> fileIds = new HashSet<>();
        List<Card> cardList = cardRepository.findAll();
        for (Card card : cardList) {
            CardIconDto icon = card.getIcon();
            if (icon != null && StringUtils.isNotBlank(icon.getSrc())) {
                fileIds.add(FilenameUtils.getBaseName(icon.getSrc()));
            }
            CardZipDto zip = card.getZip();
            if (zip != null && StringUtils.isNotBlank(zip.getPath())) {
                fileIds.add(FilenameUtils.getBaseName(zip.getPath()));
            }
        }
        fileIds.add(FilenameUtils.getBaseName(settingService.getSettingCache().getLogoPath()));
        return fileIds;
    }

    private Set<String> getExistAttachment() {
        return attachmentRepository.findAll().stream()
                .map(item -> FilenameUtils.getBaseName(item.getPath()))
                .collect(Collectors.toSet());
    }

}
