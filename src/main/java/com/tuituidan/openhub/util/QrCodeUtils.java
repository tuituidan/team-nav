package com.tuituidan.openhub.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tuituidan.openhub.exception.ResourceWriteException;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * QrCodeUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/9/11
 */
@UtilityClass
@Slf4j
public class QrCodeUtils {

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param width 宽度
     * @param height 高度
     * @return BufferedImage
     */
    public static BufferedImage generate(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            //设置编码格式
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
            //设置外边距
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException ex) {
            throw new ResourceWriteException("二维码生成失败", ex);
        }

    }

}
