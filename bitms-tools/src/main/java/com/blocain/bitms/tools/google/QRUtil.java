package com.blocain.bitms.tools.google;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRUtil 介绍
 * <p>File：QRUtil.java </p>
 * <p>Title: QRUtil </p>
 * <p>Description:QRUtil </p>
 * <p>Copyright: Copyright (c) 2017/7/14 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QRUtil
{
    /**
     * 编码（将文本生成二维码）
     *
     * @param content  二维码中的内容
     * @param width    二维码图片宽度
     * @param height   二维码图片高度
     * @param imagePath 二维码图片存放位置
     * @return 图片地址
     */
    @SuppressWarnings("deprecation")
    private static String encode(String content, int width, int height, String imagePath)
    {
        HashMap<EncodeHintType, Object> hints = Maps.newHashMap();
        // 设置编码类型为utf-8
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码纠错能力级别为H（最高）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix byteMatrix;
        try
        {
            // 生成二维码
            byteMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            File file = new File(imagePath);
            if (!file.exists()) file.mkdirs();
            MatrixToImageWriter.writeToFile(byteMatrix, "png", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        return imagePath;
    }
    
    /**
     * 解码（读取二维码图片中的文本信息）
     *
     * @param imagePath
     *            二维码图片路径
     * @return 文本信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    static String decode(String imagePath)
    {
        // 返回的文本信息
        String content = "";
        try
        {
            // 创建图片文件
            File file = new File(imagePath);
            if (!file.exists()) { return content; }
            BufferedImage image = null;
            image = ImageIO.read(file);
            if (null == image) { return content; }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            
            Hashtable hints = new Hashtable();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result rs = new MultiFormatReader().decode(bitmap, hints);
            content = rs.getText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ReaderException e)
        {
            e.printStackTrace();
        }
        return content;
    }
    
    /**
     * 图片打水印
     *
     * @param bgImage
     *            背景图
     * @param waterImg
     *            水印图
     * @param uniqueFlag
     *            生成的新图片名称中的唯一标识，用来保证生成的图片名称不重复，如果为空或为null,将使用当前时间作为标识
     * @return 新图片路径
     */
    private static String addImageWater(String bgImage, String waterImg, String uniqueFlag)
    {
        int x = 0;
        int y = 0;
        String newImgPath = "";
        if (null == uniqueFlag)
        {
            uniqueFlag = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        }
        else if (uniqueFlag.trim().length() < 1)
        {
            uniqueFlag = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        }
        try
        {
            File file = new File(bgImage);
            String fileName = file.getName();
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            Image waterImage = ImageIO.read(new File(waterImg)); // 水印文件
            int width_water = waterImage.getWidth(null);
            int height_water = waterImage.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
            int widthDiff = width - width_water;
            int heightDiff = height - height_water;
            x = widthDiff / 2;
            y = heightDiff / 2;
            g.drawImage(waterImage, x, y, width_water, height_water, null); // 水印文件结束
            g.dispose();
            if (bgImage.contains(fileName))
            {
                newImgPath = bgImage.replace(fileName, uniqueFlag + fileName);
            }
            File newImg = new File(newImgPath);
            ImageIO.write(bufferedImage, "png", newImg);
            File waterFile = new File(waterImg);
            if (file.exists())
            {
                file.delete();
            }
            if (waterFile.exists())
            {
                waterFile.delete();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return newImgPath;
    }
    
    /**
     * 图片缩放
     *
     * @param filePath  图片路径
     * @param height    缩放到高度
     * @param width     缩放宽度
     * @param fill      比例足时是否填白 true为填白，二维码是黑白色，这里调用时建议设为true
     * @return 新图片路径
     */
    private static String resizeImg(String filePath, int width, int height, boolean fill)
    {
        String newImgPath = "";
        try
        {
            double ratio = 0; // 缩放比例
            File f = new File(filePath);
            String fileName = f.getName();
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            if (height != 0 && width != 0)
            {
                if ((bi.getHeight() > height) || (bi.getWidth() > width))
                {
                    if (bi.getHeight() > bi.getWidth())
                    {
                        ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                    }
                    else
                    {
                        ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                    }
                    AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                    itemp = op.filter(bi, null);
                }
            }
            if (fill)
            {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                else
                {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            if (filePath.contains(fileName))
            {
                newImgPath = filePath.replace(fileName, now + fileName);
            }
            File newImg = new File(newImgPath);
            ImageIO.write((BufferedImage) itemp, "png", newImg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return newImgPath;
    }
    
    /**
     * 图片添加边框
     *
     * @param mainImgPath  要加边框的图片
     * @param bgImgPath    背景图（实际上是将图片放在背景图上，只利用背景图的边框效果）
     * @return 制作完成的图片路径
     */
    private static String addWaterBorder(String mainImgPath, String bgImgPath)
    {
        String borderImgPath = "";
        try
        {
            File f = new File(mainImgPath);
            BufferedImage bi;
            bi = ImageIO.read(f);
            int width = bi.getWidth();
            int height = bi.getHeight();
            int bgWidth = width + 4;
            int bgHeight = height + 4;
            String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            borderImgPath = QRUtil.addImageWater(QRUtil.resizeImg(bgImgPath, bgHeight, bgWidth, true), mainImgPath, now);
            if (f.exists())
            {
                f.delete();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return borderImgPath;
    }
    
    /**
     * @Definition: 生成常规二维码
     * @author: TangWenWu
     * @Created date: 2014-11-25
     * @param content  二维码中的内容
     * @param width    二维码宽度  单位：像素  建议300
     * @param height   二维码高度  单位：像素  建议300
     * @param imageName  二维码图片名称     生成路径为：appName/WebRoot/指定的目录/imageName 
     * @return
     */
    public static String generateCommonQR(String content, int width, int height, String imageName)
    {
        String appPath = System.getProperty("java.io.tmpdir");
        /** 部分一开始***********生成常规二维码 *************/
        // 二维码存放地址
        imageName = appPath + "templates/qr/" + imageName;
        // 生成二维码,返回的是生成好的二维码图片的所在路径
        String qrImgPath = QRUtil.encode(content, width, height, imageName);
        /** 部分一结束***********如果生成不带图片的二维码，到这步已经完成了 *************/
        return qrImgPath;
    }
    
    /**
     * @Definition: 生成带图片但图片不带边框的二维码
     * @author: TangWenWu
     * @Created date: 2014-11-25
     * @param content  二维码中的内容
     * @param width    二维码宽度  单位：像素  建议300
     * @param height   二维码高度  单位：像素  建议300
     * @param imageName  二维码图片名称     生成路径为：appName/WebRoot/指定的目录/imageName 
     * @return
     */
    public static String generateOnlyImageQR(String content, int width, int height, String imageName)
    {
        String appPath = System.getProperty("java.io.tmpdir");
        String qrImgPath = generateCommonQR(content, width, height, imageName);
        // 缩放水印图片,为保证二维码的读取正确，图片不超过二维码图片的五分之一，这里设为六分之一
        String waterImgPath = QRUtil.resizeImg(appPath + "images/boco_big.png", width / 6, height / 6, true);
        // 生成带有图片的二维码，返回的是生成好的二维码图片的所在路径
        String qrImage = QRUtil.addImageWater(qrImgPath, waterImgPath, "BOCO");
        return qrImage;
    }
    
    /**
     * @Definition: 生成带图片且图片带边框的二维码
     * @author: TangWenWu
     * @Created date: 2014-11-25
     * @param content  二维码中的内容
     * @param width    二维码宽度  单位：像素  建议300
     * @param height   二维码高度  单位：像素  建议300
     * @param imageName  二维码图片名称     生成路径为：appName/WebRoot/指定的目录/imageName 
     * @return
     */
    public static String generateImageInBorderQR(String content, int width, int height, String imageName)
    {
        String appPath = System.getProperty("java.io.tmpdir");
        String qrImgPath = generateCommonQR(content, width, height, imageName);
        // 缩放水印图片,为保证二维码的读取正确，图片不超过二维码图片的五分之一，这里设为六分之一
        // d:/qr/heihei.png 这图片是要加在二维码中间的那张图
        String waterImgPath = QRUtil.resizeImg(appPath + "images/boco_big.png", width / 6, height / 6, true);
        // d:/qr/qr_bg.png这种图片是自己画好边框光晕效果的边框底图
        String tempImg = QRUtil.addWaterBorder(waterImgPath, appPath + "images/qr_bg.png");
        // 生成带有边框图片的二维码,返回的是生成好的二维码图片的所在路径
        String qrImage = QRUtil.addImageWater(qrImgPath, tempImg, "BOCO");
        return qrImage;
    }
}
