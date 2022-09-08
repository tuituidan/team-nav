package com.tuituidan.openhub.service;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceReadException;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.util.FileTypeUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import com.tuituidan.openhub.util.thread.CompletableUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
public class CommonService {

    private static final int HTTP_SLASH_COUNT = 3;

    private static final String ICON_SVG_PATH = "static/assets/lib/iview/fonts/ionicons.svg";

    private static final List<String> ICONS = new ArrayList<>();

    @Resource
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        org.dom4j.Document document;
        try (InputStream inputStream = new ClassPathResource(ICON_SVG_PATH).getInputStream()) {
            document = SAXReader.createDefault().read(inputStream);
        } catch (Exception ex) {
            throw new ResourceReadException("icon-xml读取失败", ex);
        }
        ICONS.addAll(document.getRootElement().element("defs").element("font").elements("glyph")
                .stream()
                .map(element -> element.attribute("glyph-name"))
                .filter(Objects::nonNull)
                .map(Attribute::getValue)
                .collect(Collectors.toList()));
    }

    /**
     * 上传图标
     *
     * @param file 图标文件
     * @return 保存路径
     */
    public String upload(MultipartFile file) {
        String url = StringExtUtils.format("/ext-resources/images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), FilenameUtils.getExtension(file.getOriginalFilename()));
        try {
            FileUtils.writeByteArrayToFile(new File(Consts.ROOT_DIR + url), file.getBytes());
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
        return url;
    }

    /**
     * 上传原型
     *
     * @param file 原型文件
     * @return 保存路径
     */
    public String uploadZip(MultipartFile file) {
        try {
            byte[] datas = file.getBytes();
            String saveFilePath = StringExtUtils.format("/ext-resources/modules/{}.zip", DigestUtils.md5Hex(datas));
            File saveFile = new File(Consts.ROOT_DIR + saveFilePath);
            if (!saveFile.exists()) {
                FileUtils.writeByteArrayToFile(saveFile, datas);
            }
            return saveFilePath;
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
    }

    /**
     * 获取分类的图标
     *
     * @return List
     */
    public List<String> categoryIcons() {
        return ICONS;
    }

    /**
     * 获取卡片对应链接的 favicon.ico 用于icon
     *
     * @param url url
     * @return List
     */
    public List<String> cardIcons(String url) {
        String indexUrl = getIndexUrl(url);
        List<String> result = new ArrayList<>();
        String docUrl = getFromDocument(indexUrl);
        if (StringUtils.isNotBlank(docUrl)) {
            result.add(docUrl);
        }
        String favUrl = checkFavicon(indexUrl + "/favicon.ico");
        if (StringUtils.isNotBlank(favUrl)) {
            result.add(favUrl);
        }
        return result;
    }

    private String getIndexUrl(String orgUrl) {
        int count = StringUtils.countMatches(orgUrl, "/");
        if (count >= HTTP_SLASH_COUNT) {
            return orgUrl.substring(0, StringUtils.ordinalIndexOf(orgUrl, "/", HTTP_SLASH_COUNT));
        }
        return orgUrl;
    }

    private String getFromDocument(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("UA")
                    // TODO 最新的Jsoup没有validateTLSCertificates
                    .validateTLSCertificates(false)
                    .timeout(60000).get();
            Elements links = doc.head().children().select("link[rel~=icon]");
            if (links.isEmpty()) {
                return "";
            }
            String href = links.get(0).attr("href");
            if (StringUtils.startsWith(href, "http")) {
                return checkFavicon(href);
            }
            href = StringUtils.startsWith(href, "//") ? href : url + href;
            return checkFavicon(href);
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        }
        return "";
    }

    private String checkFavicon(String url) {
        try {
            ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(url, byte[].class);
            byte[] body = forEntity.getBody();
            if (body != null && !FileTypeUtils.isHtml(body)) {
                return url;
            }
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        }
        return "";
    }

    /**
     * deleteFiles
     *
     * @param sync sync
     * @param paths paths
     */
    public void deleteFiles(boolean sync, List<String> paths) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (String path : paths) {
            futures.add(CompletableUtils.runAsync(() -> {
                try {
                    FileUtils.forceDelete(new File(Consts.ROOT_DIR + path));
                } catch (IOException ex) {
                    log.error("删除原文件失败：【{}】", path, ex);
                }
            }));
        }
        if (sync) {
            CompletableUtils.waitAll(futures);
        }
    }

}
