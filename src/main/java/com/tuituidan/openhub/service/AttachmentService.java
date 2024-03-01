package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.Attachment;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.bean.vo.AttachmentVo;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.AttachmentRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ResponseUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * AttachmentService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/2/27
 */
@Service
public class AttachmentService {

    @Resource
    private AttachmentRepository attachmentRepository;

    /**
     * 定时删除创建时间距离现在超过一天，且没有businessId的数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearUselessData() {
        // 删除创建时间距离现在超过一天，且没有businessId的数据
        attachmentRepository.deleteByBusinessIdIsNullAndCreateTimeBefore(LocalDateTime.now().plusDays(-2));
    }

    /**
     * saveAttachment
     *
     * @param path path
     * @param file file
     * @return String
     */
    public String saveAttachment(String path, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return attachmentRepository.save(new Attachment()
                .setId(StringExtUtils.getUuid())
                .setName(originalFilename)
                .setExt(FilenameUtils.getExtension(originalFilename))
                .setPath(path)
                .setSize(file.getSize())).getId();
    }

    /**
     * saveBusinessId
     *
     * @param ids ids
     * @param businessId businessId
     */
    public void saveAttachment(String businessId, String[] ids) {
        List<Attachment> exists = attachmentRepository.findByBusinessIdIn(Collections.singleton(businessId));
        if (CollectionUtils.isEmpty(exists)) {
            if (ArrayUtils.isEmpty(ids)) {
                return;
            }
            this.saveBusinessId(businessId, Arrays.stream(ids).collect(Collectors.toSet()));
            return;
        }
        Set<String> existIds = exists.stream().map(Attachment::getId).collect(Collectors.toSet());
        Set<String> saveIds = ArrayUtils.isEmpty(ids)
                ? new HashSet<>() : Arrays.stream(ids).collect(Collectors.toSet());

        Set<String> deleteIds = new HashSet<>(existIds);
        // 现有的去掉要保存的，剩下要删除的
        deleteIds.removeAll(saveIds);
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            attachmentRepository.deleteAllById(deleteIds);
        }
        // 要保存的去掉现有的，就是要新增的
        saveIds.removeAll(existIds);
        if (CollectionUtils.isNotEmpty(saveIds)) {
            saveBusinessId(businessId, saveIds);
        }
    }

    private void saveBusinessId(String businessId, Set<String> ids) {
        List<Attachment> attachments = attachmentRepository.findAllById(ids);
        for (Attachment attachment : attachments) {
            attachment.setBusinessId(businessId);
        }
        attachmentRepository.saveAll(attachments);
    }

    /**
     * getCardAttachmentMap
     *
     * @param cards cards
     * @return Map
     */
    public Map<String, List<AttachmentVo>> getCardAttachmentMap(List<Card> cards) {
        Set<String> businessIds = cards.stream()
                .filter(item -> BooleanUtils.isTrue(item.getHasAttachment()))
                .map(Card::getId).collect(Collectors.toSet());
        List<Attachment> attachments = attachmentRepository.findByBusinessIdIn(businessIds);
        if (CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyMap();
        }
        return attachments.stream()
                .map(item -> BeanExtUtils.convert(item, AttachmentVo::new)
                        .setSize(FileUtils.byteCountToDisplaySize(item.getSize())))
                .collect(Collectors.groupingBy(AttachmentVo::getBusinessId));
    }

    /**
     * deleteByBusinessIds
     *
     * @param businessIds businessIds
     */
    public void deleteByBusinessIds(List<String> businessIds) {
        attachmentRepository.deleteByBusinessIdIn(businessIds);
    }

    /**
     * downloadFile
     *
     * @param id id
     */
    public void downloadFile(String id) {
        Attachment attachment = attachmentRepository.getReferenceById(id);
        ResponseUtils.download(attachment.getName(), Consts.ROOT_DIR + attachment.getPath());
    }

}
