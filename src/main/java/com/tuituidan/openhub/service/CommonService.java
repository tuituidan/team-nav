package com.tuituidan.openhub.service;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.exception.ResourceReadException;
import com.tuituidan.openhub.exception.ResourceWriteException;
import com.tuituidan.openhub.util.FileExtUtils;
import com.tuituidan.openhub.util.QrCodeUtils;
import com.tuituidan.openhub.util.RequestUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    private static final String ICON_SVG_PATH = "static/assets/lib/iview/fonts/ionicons.svg";

    private static final List<String> CATEGORY_ICONS = new ArrayList<>();

    private static final List<String> CARD_ICONS = new ArrayList<>();

    private static final String CARD_ICON_PATH = Consts.ROOT_DIR + "/ext-resources/images/default";

    /**
     * 初始化
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadCategoryIcons();
        loadCardIcons();
    }

    /**
     * 解析出iview的所有字体图标名称，用于前端选择图标的控件
     */
    private void loadCategoryIcons() {
        org.dom4j.Document document;
        try (InputStream inputStream = new ClassPathResource(ICON_SVG_PATH).getInputStream()) {
            document = SAXReader.createDefault().read(inputStream);
        } catch (Exception ex) {
            throw new ResourceReadException("icon-xml读取失败", ex);
        }
        CATEGORY_ICONS.addAll(document.getRootElement().element("defs").element("font").elements("glyph")
                .stream()
                .map(element -> element.attribute("glyph-name"))
                .filter(Objects::nonNull)
                .map(Attribute::getValue)
                .collect(Collectors.toList()));
    }

    /**
     * 解析出iview的所有字体图标名称，用于前端选择图标的控件
     */
    public void loadCardIcons() {
        CARD_ICONS.clear();
        File root = new File(CARD_ICON_PATH);
        if (!root.exists()) {
            return;
        }
        File[] files = root.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            CARD_ICONS.add(FilenameUtils.getBaseName(file.getName()));
        }
    }

    /**
     * 文件上传，包括原型和图标的
     *
     * @param file 文件
     * @param type 文件类型 images 或者 modules
     * @return 保存路径
     */
    public String upload(MultipartFile file, String type) {
        String savePath = StringExtUtils.format("/ext-resources/{}/{}/{}.{}",
                type,
                DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()),
                StringExtUtils.getUuid(),
                FilenameUtils.getExtension(file.getOriginalFilename()));
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
        return savePath;
    }

    /**
     * 获取分类的图标
     *
     * @return List
     */
    public List<String> categoryIcons() {
        return CATEGORY_ICONS;
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
        String domainUrl = getDomainUrl(url);
        String docUrl = getFromDocument(domainUrl);
        // 有时候从dom树的link icon中获取的和favicon图标样式并不一样，都返回给用户去选择
        // 但很多时候两个又是一样的，懒得处理了，所以一个地址获取到两个一样的图标的时候是正常的
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotBlank(docUrl)) {
            result.add(docUrl);
        }
        String favUrl = requestFavicon(domainUrl + "/favicon.ico");
        if (StringUtils.isNotBlank(favUrl)) {
            result.add(favUrl);
        }
        return result;
    }

    /**
     * 获取url的根路径，如http://www.test.com/aa/xx.html -> http://www.test.com
     *
     * @param orgUrl 原始url
     * @return url
     */
    private String getDomainUrl(String orgUrl) {
        try {
            URL url = new URL(orgUrl);
            StringBuilder sb = new StringBuilder(url.getProtocol())
                    .append("://").append(url.getHost());
            if (url.getPort() != -1) {
                sb.append(":").append(url.getPort());
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new IllegalArgumentException("url解析错误", ex);
        }
    }

    private String getFromDocument(String domainUrl) {
        try {
            Document doc = Jsoup.connect(domainUrl)
                    .timeout(Consts.FAVICON_TIMEOUT).get();
            Elements links = doc.head().children().select("link[rel~=icon]");
            if (links.isEmpty()) {
                return "";
            }
            String href = links.get(0).attr("href");
            return requestFavicon(formatLinkIcon(domainUrl, href));
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        }
        return "";
    }

    /**
     * 从link拿到的格式很多种，这里统一格式化一下
     *
     * @param domainUrl domainUrl
     * @param href href
     * @return String
     */
    private String formatLinkIcon(String domainUrl, String href) {
        if (StringUtils.startsWith(href, "http")) {
            return href;
        }
        if (StringUtils.startsWith(href, "//")) {
            return StringUtils.substringBefore(domainUrl, "//") + href;
        }
        if (StringUtils.startsWith(href, "/")) {
            return domainUrl + href;
        }
        return domainUrl + "/" + href;
    }

    private String requestFavicon(String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            conn.setConnectTimeout(Consts.FAVICON_TIMEOUT);
            conn.setReadTimeout(Consts.FAVICON_TIMEOUT);
            byte[] body = IOUtils.toByteArray(conn);
            // 要能实际获取到favicon的数据，如果返回是一个html文件，往往是鉴权导致重定向了
            if (body != null && !FileExtUtils.isHtml(body)) {
                return url;
            }
        } catch (Exception ex) {
            // 拿不到就算了，不写日志
        } finally {
            if (conn != null) {
                IOUtils.close(conn);
            }
        }
        return "";
    }

    /**
     * generateQrCode
     *
     * @param url url
     */
    public void generateQrCode(String url) {
        HttpServletResponse response = RequestUtils.getResponse();
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            BufferedImage image = QrCodeUtils.generate(url, 200, 200);
            ImageIO.write(image, "png", outputStream);
        } catch (Exception ex) {
            throw new ResourceWriteException("二维码写入失败");
        }
    }

}
