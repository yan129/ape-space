package com.ape.common.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/9/19
 *
 * 生成验证码工具类
 */
public class CaptchaUtil {

    public static String PREFIX = "captcha:";
    public static long EXPIRE_TIME = 90L;
    private static String codes = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    private String[] fontNames = {"宋体","楷体","隶书","微软雅黑"};

    /**
     * 图片背景颜色
     */
    private Color bgColor;

    private Random random;

    /**
     * 记录随机字符串
     */
    private String text;

    private CaptchaUtil(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        // 定义验证码图片的背景颜色为白色
        this.bgColor = new Color(255,255,255);
        this.random = new Random();
    }

    public static CaptchaUtil newInstance(){
        return new CaptchaUtil(100, 40);
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 获取一个随意颜色
     */
    private Color randomColor(){
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 获取一个随机字体
     */
    private Font randomFont(){
        String name = fontNames[random.nextInt(fontNames.length)];
        int style = random.nextInt(4);
        int size = random.nextInt(5) + 24;
        return new Font(name, style, size);
    }

    /**
     * 获取一个随机字符
     */
    private char randomChar(){
        return codes.charAt(random.nextInt(codes.length()));
    }

    /**
     * 绘制干扰线
     */
    private void drawLine(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < 4; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2.setColor(randomColor());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建一个空白的BufferedImage对象
     */
    private BufferedImage createImage(){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(bgColor);
        g2.fillRect(0, 0, width, height);
        return image;
    }

    public BufferedImage getImage(){
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            String code = randomChar() + "";
            sb.append(code);
            g2.setColor(randomColor());
            g2.setFont(randomFont());
            float x = i * width * 1.0f / 4;
            g2.drawString(code, x, height - 8);
        }
        this.text = sb.toString();
        drawLine(image);
        return image;
    }

    public String getText(){
        return text;
    }

    /**
     * 输出验证码图片
     * @param image
     * @param out
     * @throws IOException
     */
    public void output(BufferedImage image, OutputStream out) throws IOException {
        ImageIO.write(image, "JPEG", out);
        IOUtils.closeQuietly(out);
    }

    public String output(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = null;
        try{
            bos = new ByteArrayOutputStream();
            ImageIO.write(image, "JPEG", bos);
            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            return "data:image/JPEG;base64," +  encoder.encodeToString(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(bos);
        }
        return null;
    }

}
