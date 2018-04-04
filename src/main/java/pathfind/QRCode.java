package pathfind;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QRCode {

    private String str2enc;

    /**
     * Creates a new QRCode object form the string that needs to be encoded
     * @param str2enc The string to be encoded as a QR-Code
     */
    public QRCode(String str2enc) {
        this.str2enc = str2enc;
    }

    /**
     * Creates a BufferedImage of the QR-Code that should be displayed
     * @return the QR-Code image
     */
    public BufferedImage getQRCode(){
        QRCodeWriter qr_writer = new QRCodeWriter();
        int width = 300;
        int height = 300;

        BufferedImage buffered_image = null;
        try {
            BitMatrix byte_matrix = qr_writer.encode(str2enc,
                    BarcodeFormat.QR_CODE, width, height);
            buffered_image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            buffered_image.createGraphics();

            Graphics2D graphics = (Graphics2D) buffered_image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byte_matrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
        } catch (WriterException ex) {
            Logger.getLogger("WriterException encountered: ").log(
                    Level.WARNING, null, ex);
        }

        return buffered_image;
    }
}
