package com.tuituidan.teamnav.service;

import com.sun.xml.internal.ws.streaming.XMLReaderException;
import com.tuituidan.teamnav.consts.Consts;
import com.tuituidan.teamnav.exception.ResourceReadException;
import com.tuituidan.teamnav.exception.ResourceWriteException;
import com.tuituidan.teamnav.util.StringExtUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
    private static final String COLOR_PATH = "config/color.txt";

    private static final List<String> ICONS = new ArrayList<>();

    private static final List<String> COLORS = new ArrayList<>();

    @Resource
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        org.dom4j.Document document;
        try (InputStream inputStream = new ClassPathResource(ICON_SVG_PATH).getInputStream()) {
            document = SAXReader.createDefault().read(inputStream);
        } catch (Exception ex) {
            throw new XMLReaderException("icon-xml读取失败", ex);
        }
        ICONS.addAll(document.getRootElement().element("defs").element("font").elements("glyph")
                .stream()
                .map(element -> element.attribute("glyph-name"))
                .filter(Objects::nonNull)
                .map(Attribute::getValue)
                .collect(Collectors.toList()));

        try (InputStream inputStream = new ClassPathResource(COLOR_PATH).getInputStream()) {
            COLORS.addAll(IOUtils.readLines(inputStream, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new ResourceReadException("color.txt读取失败", ex);
        }
    }

    public String upload(MultipartFile file) {
        String url = StringExtUtils.format("/team-nav-images/{}/{}.{}",
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(), FilenameUtils.getExtension(file.getOriginalFilename()));
        try {
            FileUtils.writeByteArrayToFile(new File(Consts.ROOT_DIR + url), file.getBytes());
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
        return url;
    }

    public String uploadZip(MultipartFile file) {
        try {
            byte[] datas = file.getBytes();
            String saveFilePath = StringExtUtils.format("/team-nav-modules/{}.zip", DigestUtils.md5Hex(datas));
            File saveFile = new File(Consts.ROOT_DIR + saveFilePath);
            if (!saveFile.exists()) {
                FileUtils.writeByteArrayToFile(saveFile, datas);
            }
            return saveFilePath;
        } catch (Exception ex) {
            throw new ResourceWriteException("文件写入失败", ex);
        }
    }

    public String getColor() {
        return COLORS.get(new Random().nextInt(COLORS.size()));
    }

    public List<String> categoryIcons() {
        return ICONS;
    }

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
            if (body != null && notHtml(body)) {
                return url;
            }
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        }
        return "";
    }

    public static boolean notHtml(byte[] data) {
        byte[] src = new byte[4];
        System.arraycopy(data, 0, src, 0, src.length);
        StringBuilder builder = new StringBuilder();
        for (byte bt : src) {
            String hv = Integer.toHexString(bt & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return !"3C21444F".equals(builder.toString());
    }
}
