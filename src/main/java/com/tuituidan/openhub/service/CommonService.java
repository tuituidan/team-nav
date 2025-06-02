package com.tuituidan.openhub.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.entity.Card;
import com.tuituidan.openhub.config.BackupSqlProperties;
import com.tuituidan.openhub.consts.CardTypeEnum;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.consts.UploadTypeEnum;
import com.tuituidan.openhub.exception.ResourceReadException;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.repository.CardRepository;
import com.tuituidan.openhub.util.BookmarkUtils;
import com.tuituidan.openhub.util.FileExtUtils;
import com.tuituidan.openhub.util.HttpUtils;
import com.tuituidan.openhub.util.QrCodeUtils;
import com.tuituidan.openhub.util.ResponseUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.TransactionUtils;
import com.tuituidan.openhub.util.ZipUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * CommonService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/5
 */
@Service
@Slf4j
public class CommonService implements ApplicationRunner {

    private static final List<String> CARD_ICONS = new ArrayList<>();

    private static final String CARD_ICON_PATH = "/ext-resources/images/default/";

    private static final String PATH_EXT_SOURCE = Consts.ROOT_DIR
            + File.separator + "ext-resources";

    private static final String DATA_BACKUP_FILE = PATH_EXT_SOURCE + File.separator + "data-backup.json";

    @Resource
    private CardRepository cardRepository;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private BackupSqlProperties backupSqlProperties;

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private CacheService cacheService;

    @Resource
    private EmailSettingService emailSettingService;

    /**
     * 初始化
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadCardIcons();
    }

    /**
     * 加载放置在/ext-resources/images/default路径下的图片图标用于卡片图标选择
     */
    private void loadCardIcons() {
        CARD_ICONS.clear();
        File root = new File(Consts.ROOT_DIR + CARD_ICON_PATH);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            CARD_ICONS.add(file.getName());
        }
    }

    /**
     * 文件上传，包括静态网站和图标的
     *
     * @param file 文件
     * @param type 文件类型 images 或者 modules
     * @return 保存路径
     */
    public Object upload(MultipartFile file, String type) {
        if (UploadTypeEnum.REVERT.getType().equals(type)) {
            revertData(file);
            return type;
        }
        if (UploadTypeEnum.BOOKMARK.getType().equals(type)) {
            try (InputStream in = file.getInputStream()) {
                return BookmarkUtils.extractTreeData(IOUtils.toString(in, StandardCharsets.UTF_8));
            } catch (IOException ex) {
                throw new ResourceReadException("浏览器书签解析失败", ex);
            }
        }
        String savePath = formatSavePath(type, file);
        File saveFile = new File(Consts.ROOT_DIR + savePath);
        try {
            FileUtils.forceMkdirParent(saveFile);
        } catch (IOException ex) {
            throw new ResourceWriteException("父目录生成失败", ex);
        }
        try (InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(saveFile)) {
            IOUtils.copy(in, out);
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
        if (UploadTypeEnum.DEFAULT.getType().equals(type)) {
            CARD_ICONS.add(file.getOriginalFilename());
        }
        if (UploadTypeEnum.ATTACHMENTS.getType().equals(type)) {
            return attachmentService.saveAttachment(savePath, file);
        }
        return savePath;
    }

    private String formatSavePath(String type, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (UploadTypeEnum.DEFAULT.getType().equals(type)) {
            String path = CARD_ICON_PATH + fileName;
            Assert.isTrue(!new File(Consts.ROOT_DIR + path).exists(), "文件名已经存在");
            return path;
        }
        return StringExtUtils.format("/ext-resources/{}/{}/{}.{}",
                type,
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(),
                FilenameUtils.getExtension(fileName));
    }

    /**
     * 修改icon文件名
     *
     * @param fileName fileName
     * @param newName newName
     */
    public void updateIconName(String fileName, String newName) {
        String root = Consts.ROOT_DIR + CARD_ICON_PATH;
        File oldFile = new File(root + fileName);
        String newFileName = newName + "." + FilenameUtils.getExtension(fileName);
        File newFile = new File(root + newFileName);
        Assert.isTrue(oldFile.exists(), "原图标已不存在");
        Assert.isTrue(!newFile.exists(), "无法修改为图标名【" + newName + "】，该图标名已存在");
        Assert.isTrue(oldFile.renameTo(newFile), "文件名修改失败");
        CARD_ICONS.set(CARD_ICONS.indexOf(fileName), newFileName);
        resetRefIcon(fileName, newFileName);
    }

    private void resetRefIcon(String fileName, String newFileName) {
        List<Card> updateList = cardRepository.findAll().stream()
                .filter(item -> Objects.nonNull(item.getIcon())
                        && StringUtils.isNotBlank(item.getIcon().getSrc())
                        && StringUtils.contains(item.getIcon().getSrc(), CardTypeEnum.DEFAULT.getType())
                        && StringUtils.endsWith(item.getIcon().getSrc(), fileName)
                ).map(card -> {
                    CardIconDto icon = card.getIcon();
                    icon.setSrc(icon.getSrc().replace(fileName, newFileName));
                    return card;
                }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updateList)) {
            cardRepository.saveAll(updateList);
        }
    }

    /**
     * 图标删除
     *
     * @param fileName fileName
     * @throws IOException IOException
     */
    public void deleteDefaultIcon(String fileName) throws IOException {
        File file = new File(Consts.ROOT_DIR + CARD_ICON_PATH + fileName);
        if (file.exists()) {
            checkIconRef(fileName);
            FileUtils.forceDelete(file);
            CARD_ICONS.removeIf(name -> StringUtils.equals(fileName, name));
        }
    }

    private void checkIconRef(String fileName) {
        List<String> list = cardRepository.findAll().stream()
                .filter(item -> Objects.nonNull(item.getIcon())
                        && StringUtils.isNotBlank(item.getIcon().getSrc())
                        && StringUtils.contains(item.getIcon().getSrc(), CardTypeEnum.DEFAULT.getType())
                        && StringUtils.endsWith(item.getIcon().getSrc(), fileName)
                ).map(Card::getTitle).collect(Collectors.toList());
        Assert.isTrue(CollectionUtils.isEmpty(list), "图标已被卡片【"
                + StringUtils.join(list, ",") + "】使用，不能删除");
    }

    /**
     * 获取卡片的图标
     *
     * @return List
     */
    public List<String> cardIcons() {
        return CARD_ICONS;
    }

    /**
     * 获取卡片对应链接的 favicon.ico 用于icon
     *
     * @param url url
     * @return List
     */
    public List<String> cardIcons(String url) {
        String domainUrl = HttpUtils.getDomainUrl(url);
        String docUrl = HttpUtils.getFromDocument(domainUrl);
        // 有时候从dom树的link icon中获取的和favicon图标样式并不一样，都返回给用户去选择
        // 但很多时候两个又是一样的，懒得处理了，所以一个地址获取到两个一样的图标的时候是正常的
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotBlank(docUrl)) {
            result.add(docUrl);
        }
        String favUrl = HttpUtils.requestFavicon(domainUrl + "/favicon.ico");
        if (StringUtils.isNotBlank(favUrl)) {
            result.add(favUrl);
        }
        return result;
    }

    /**
     * generateQrCode
     *
     * @param url url
     */
    public void generateQrCode(String url) {
        HttpServletResponse response = ResponseUtils.getHttpResponse();
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            BufferedImage image = QrCodeUtils.generate(url, 200);
            ImageIO.write(image, "png", outputStream);
        } catch (Exception ex) {
            throw new ResourceWriteException("二维码写入失败");
        }
    }

    /**
     * backupData
     */
    public void backupData() {
        List<String> tables = namedParameterJdbcTemplate.queryForList(backupSqlProperties.getSelectTable(),
                Collections.emptyMap(), String.class);
        Map<String, List<Map<String, Object>>> dataMap = new HashMap<>();
        for (String table : tables) {
            List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(
                    StringExtUtils.format(backupSqlProperties.getSelectData(), table), Collections.emptyMap());
            dataMap.put(table, list);
        }
        FileExtUtils.writeString(DATA_BACKUP_FILE, JSON.toJSONString(dataMap,
                new Filter[] {
                        (NameFilter) (object, name, value) -> name.toLowerCase(),
                        (ValueFilter) (object, name, value) -> value,
                },
                Feature.WriteNulls, Feature.PrettyFormat));
        List<String> paths = new ArrayList<>();
        paths.add(PATH_EXT_SOURCE + File.separator + "attachments");
        paths.add(PATH_EXT_SOURCE + File.separator + "images");
        paths.add(PATH_EXT_SOURCE + File.separator + "modules");
        paths.add(DATA_BACKUP_FILE);
        String zipPath = PATH_EXT_SOURCE + ".zip";
        FileExtUtils.deleteOnExists(zipPath);
        ZipUtils.zip(zipPath, paths);
        ResponseUtils.download("backup-" + System.currentTimeMillis() + ".zip", zipPath);
    }

    /**
     * revertData
     *
     * @param file file
     */
    private void revertData(MultipartFile file) {
        String zipPath = PATH_EXT_SOURCE + ".zip";
        FileExtUtils.transferTo(file, zipPath);
        ZipUtils.unzip(zipPath);
        String json = FileExtUtils.readString(DATA_BACKUP_FILE);
        JSONObject dataMap = JSON.parseObject(json);
        List<Pair<String, List<JSONObject>>> pairList = new ArrayList<>();
        for (Entry<String, Object> entry : dataMap.entrySet()) {
            String tableName = entry.getKey();
            JSONArray values = (JSONArray) entry.getValue();
            if (CollectionUtils.isEmpty(values)) {
                continue;
            }
            String insertSql = buildInsertSql(tableName, values.getJSONObject(0));
            List<JSONObject> list = extractRealDataList(tableName, values);
            if (CollectionUtils.isNotEmpty(list)) {
                pairList.add(Pair.of(insertSql, list));
            }
        }
        TransactionUtils.execute(() -> {
            for (Pair<String, List<JSONObject>> pair : pairList) {
                for (JSONObject item : pair.getValue()) {
                    namedParameterJdbcTemplate.update(pair.getKey(), item);
                }
            }
        });

        //初始化
        cacheService.run(null);
        emailSettingService.run(null);
        this.loadCardIcons();
    }

    private List<JSONObject> extractRealDataList(String tableName, JSONArray values) {
        List<JSONObject> list = new ArrayList<>();
        for (Object item : values) {
            JSONObject value = (JSONObject) item;
            List<String> ids = namedParameterJdbcTemplate.queryForList(
                    StringExtUtils.format(backupSqlProperties.getSelectId(), tableName), value, String.class);
            if (CollectionUtils.isEmpty(ids)) {
                list.add(value);
            }
        }
        return list;
    }

    private String buildInsertSql(String tableName, JSONObject data) {
        List<String> cols = new ArrayList<>();
        List<String> vals = new ArrayList<>();
        for (String key : data.keySet()) {
            cols.add(key);
            vals.add(":" + key);
        }
        return StringExtUtils.format(backupSqlProperties.getInsertSql(), tableName,
                StringUtils.join(cols, ","), StringUtils.join(vals, ","));
    }

}
