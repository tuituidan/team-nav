package com.tuituidan.openhub.service.cardtype;

import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.util.FileExtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;

/**
 * 静态网页生成器
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StaticPageGenerator {
    @Resource
    private final SpringTemplateEngine engine;

    public String createHTML(Map<String, Object> displayContent, String savePath, String templateName) {
        Context context = new Context();
        if (displayContent.size() > 0) {
            context.setVariables(displayContent);
        }
        //  创建输出文件
        File file = new File(Consts.ROOT_DIR+ savePath);
        try {
            if (!file.exists()) {
                FileExtUtils.createFile(file.getAbsolutePath());
            }
        } catch (IOException ex) {
            log.error("文件创建失败", ex);
            return Strings.EMPTY;
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter( new FileWriter(file)))) {
            engine.process(templateName, context, writer);
            log.info("http path: {}", file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException ex) {
            log.error("模板写入失败", ex);
            return Strings.EMPTY;
        }

    }



}

