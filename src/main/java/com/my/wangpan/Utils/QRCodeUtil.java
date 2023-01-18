package com.my.wangpan.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成帮助类
 */
public class QRCodeUtil {
    //编码
    private static final String CHARSET = "utf-8";
    //文件格式
    private static final String FORMAT = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int LOGO_WIDTH = 60;
    // LOGO高度
    private static final int LOGO_HEIGHT = 60;

    /**
     * @Description 生成二维码,获得到输出流 ,logo内嵌
     * @Param [content, logoPath, output, needCompress] 内容,logo路径,输出流,是否压缩
     * @return void
     **/
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImag(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT, output);
    }

    /**
     * 生成二维码
     * @param content 内容
     * @param logoPath logo
     * @param needCompress 是否压缩，true表示压缩logo
     * @return
     * @throws WriterException
     */
    public static BufferedImage createImag(String content,String logoPath,boolean needCompress) throws WriterException, IOException {
        Hashtable<EncodeHintType,Object> hints = new Hashtable<>();
        //错误校正级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置字符集
        hints.put(EncodeHintType.CHARACTER_SET,CHARSET);
        //边距信息
        hints.put(EncodeHintType.MARGIN,1);

        //生成矩阵
        BitMatrix bit = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bit.getWidth();
        int height = bit.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int x = 0;x < width; x++){
            for(int y = 0;y < height;y++){
                image.setRGB(x,y,bit.get(x,y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if(logoPath == null || "".equals(logoPath)){
            return image;
        }
        //插入logo
        insetLogo(image,logoPath,needCompress);
        return image;
    }

    /**
     * 添加logo到二维码
     * 这个方法接受一个名为"source"的BufferedImage，
     * 一个名为"logoPath"的字符串，表示要插入的图像的文件路径，
     * 以及一个名为"needCompress"的布尔值作为输入。
     * 它打开一个InputStream来读取logoPath路径的图像文件
     * 然后使用ImageIO.read(inputStream)读取图像文件。
     * 如果needCompress标志为true，方法会将图像缩放到LOGO_WIDTH和LOGO_HEIGHT(如果原始图像大于这些尺寸)，
     * 然后在新的BufferedImage上绘制图像。方法然后从源图像创建一个Graphics2D对象，
     * 计算图像在QR码上居中的x和y坐标，并在QR码上绘制图像。最后，它在logo周围添加了一个带有3像素笔触的圆角矩形。
     * 如果读取文件或关闭输入流存在问题，该方法会抛出IOException。
     * @param source
     * @param logoPath
     * @param needCompress
     * @throws IOException
     */
    public static void insetLogo(BufferedImage source,String logoPath,boolean needCompress) throws IOException {
        InputStream input = null;
        try{
            input = QRCodeUtil.getResourceAsStream(logoPath);
            Image src = ImageIO.read(input);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if(needCompress){
                //压缩logo
                width = width>LOGO_WIDTH ? LOGO_WIDTH : width;
                height = height>LOGO_HEIGHT ? LOGO_HEIGHT : height;
                Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = tag.getGraphics();
                graphics.drawImage(image,0,0,null);
                //绘制缩小图
                graphics.dispose();
                src = image;
            }
            //插入logo
            Graphics2D graphics2D = source.createGraphics();
            int x = (QRCODE_SIZE - width) / 2;
            int y = (QRCODE_SIZE - height) / 2;
            graphics2D.drawImage(src,x,y,width,height,null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graphics2D.setStroke(new BasicStroke(3f));
            graphics2D.draw(shape);
            graphics2D.dispose();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * @Description 获取指定文件的输入流，获取logo
     * @Param [logoPath] logo路径
     * @return java.io.InputStream
     **/
    public static InputStream getResourceAsStream(String logoPath) {
        return QRCodeUtil.class.getResourceAsStream(logoPath);
    }
}
