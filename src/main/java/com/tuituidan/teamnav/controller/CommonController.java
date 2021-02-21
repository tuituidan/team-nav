package com.tuituidan.teamnav.controller;

import com.sun.xml.internal.ws.streaming.XMLReaderException;
import com.tuituidan.teamnav.consts.Consts;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CommonController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/21
 */
@RestController
@RequestMapping(Consts.API_V1)
public class CommonController {

    private static final String ICON_SVG_PATH = "static/assets/lib/iview/fonts/ionicons.svg";

    @GetMapping("/icons")
    public ResponseEntity<List<String>> icons() {
        Document document;
        try (InputStream inputStream = new ClassPathResource(ICON_SVG_PATH).getInputStream()) {
            document = SAXReader.createDefault().read(inputStream);
        } catch (Exception ex) {
            throw new XMLReaderException("icon-xml读取失败", ex);
        }
        List<String> result = document.getRootElement().element("defs").element("font").elements("glyph")
                .stream()
                .map(element -> element.attribute("glyph-name"))
                .filter(Objects::nonNull)
                .map(Attribute::getValue)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
