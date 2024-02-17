package com.tuituidan.openhub.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tuituidan.openhub.exception.ResourceReadException;
import com.tuituidan.openhub.exception.ResourceWriteException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
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

    private static final Map<EncodeHintType, Object> HINTS = new EnumMap<>(EncodeHintType.class);

    static {
        //设置编码格式
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        HINTS.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        //设置外边距
        HINTS.put(EncodeHintType.MARGIN, 1);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param size 二维码宽高
     * @return BufferedImage
     */
    public static BufferedImage generate(String content, int size) {
        try {
            return MatrixToImageWriter.toBufferedImage(
                    new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, HINTS),
                    // MatrixToImageConfig默认前后景色如果是纯黑白，logo也会变黑白
                    new MatrixToImageConfig(new Color(0, 0, 1).getRGB(), Color.WHITE.getRGB()));
        } catch (WriterException ex) {
            throw new ResourceWriteException("二维码生成失败", ex);
        }

    }

    /**
     * 生成带logo的二维码
     *
     * @param content 二维码内容
     * @param size 二维码宽高
     * @param logoIn logo流
     * @param logoSize logo宽高
     */
    public static void generateWithLogo(String content, int size, InputStream logoIn, int logoSize) {
        BufferedImage source = generate(content, size);
        BufferedImage logo = toBufferedImage(logoIn);
        int width = Math.min(logo.getWidth(), logoSize);
        int height = Math.min(logo.getHeight(), logoSize);
        int x = (source.getWidth() - width) / 2;
        int y = (source.getHeight() - height) / 2;
        Graphics2D graph = source.createGraphics();
        graph.drawImage(createLogo(logo, width, height), x, y, width, height, null);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(new RoundRectangle2D.Float(x, y, width, height, 6, 6));
        graph.dispose();
    }

    private BufferedImage toBufferedImage(InputStream logoIn) {
        try (InputStream in = logoIn) {
            return ImageIO.read(in);
        } catch (IOException ex) {
            throw new ResourceReadException("logo文件流读取失败", ex);
        }
    }

    /**
     * 生成新的logo，缩小宽高，设置白色背景（解决PNG透明背景问题）
     *
     * @param logo logo
     * @param width width
     * @param height height
     * @return BufferedImage
     */
    private static BufferedImage createLogo(BufferedImage logo, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.drawImage(logo, 0, 0, null);
        graphics.dispose();
        return image;
    }

}
