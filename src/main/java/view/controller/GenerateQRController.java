package view.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GenerateQRController {
    public static WritableImage generateQRCode(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        WritableImage writableImage = new WritableImage(width, height);

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    writableImage.getPixelWriter().setColor(x, y,
                            bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return writableImage;
    }
}
