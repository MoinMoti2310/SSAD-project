package ourfood.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCode {
    public static byte[] encode(String code) {
        return encode(code, 150);
    }

    public static byte[] encode(String code, int size) {
        String fileType = "png";

        // Set border as 10% of the size
        // Set minimum border as 10
        int borderWidth = (size < 100) ? 10 : size / 10;

        try {

            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.MARGIN, 0);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, size, size, hintMap);

            int byteWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(byteWidth + 2 * borderWidth, byteWidth + 2 * borderWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, byteWidth + 2 * borderWidth, byteWidth + 2 * borderWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < byteWidth; i++) {
                for (int j = 0; j < byteWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i + borderWidth, j + borderWidth, 1, 1);
                    }
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, fileType, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (WriterException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}