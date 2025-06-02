package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.vo.BookmarkVo;
import com.tuituidan.openhub.consts.Consts;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;

/**
 * BookmarkUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/2
 */
@UtilityClass
public class BookmarkUtils {

    /**
     * extractTreeData
     *
     * @param html html
     * @return List
     */
    public static List<BookmarkVo> extractTreeData(String html) {
        Document doc = Jsoup.parse(html);
        List<BookmarkVo> bookmarks = processFolder(doc.select("body > dl > dt"));
        Assert.notEmpty(bookmarks, "书签解析失败");
        List<BookmarkVo> rootBookmarks = bookmarks.get(0).getChildren();
        Assert.notEmpty(rootBookmarks, "书签解析失败");
        return rootBookmarks;
    }

    private static List<BookmarkVo> processFolder(Elements items) {
        List<BookmarkVo> result = new ArrayList<>();
        for (Element item : items) {
            Element folder = item.selectFirst("h3");
            if (folder != null) {
                BookmarkVo node = new BookmarkVo();
                node.setId(StringExtUtils.getUuid());
                node.setType("folder");
                node.setName(folder.text());
                node.setCreateTime(parseTimestamp(folder.attr("add_date")));
                // 递归处理子节点
                Elements children = item.select("> dl > dt");
                if (!children.isEmpty()) {
                    node.setChildren(processFolder(children));
                }
                result.add(node);
            } else {
                // 处理书签节点
                Element link = item.selectFirst("a");
                if (link != null) {
                    BookmarkVo node = new BookmarkVo();
                    node.setId(StringExtUtils.getUuid());
                    node.setType("bookmark");
                    node.setName(link.text());
                    node.setCreateTime(parseTimestamp(link.attr("add_date")));
                    node.setUrl(link.attr("href"));
                    node.setIcon(link.attr("icon"));
                    result.add(node);
                }
            }
        }
        return result;
    }

    private static String parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)),
                    ZoneId.systemDefault()).format(Consts.TIME_FORMATTER);
        } catch (NumberFormatException ex) {
            throw new DateTimeException("时间转换失败", ex);
        }
    }

}
