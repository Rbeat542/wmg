package alma.apwa;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class WatermarkProcessor {

    public static void applyTextWatermark(WritableImage image, TextWatermarkParams params) {
        GraphicsContext gc = new Canvas(image.getWidth(), image.getHeight()).getGraphicsContext2D();
        gc.drawImage(image, 0, 0);

        gc.setGlobalAlpha(params.getOpacity());
        gc.setFill(params.getColor());
        gc.setFont(Font.font(params.getFontName(), params.getFontSize()));
        gc.setTextAlign(TextAlignment.valueOf(params.getAlignment().toUpperCase()));
        gc.fillText(params.getText(), params.getX(), params.getY());
        gc.setGlobalAlpha(1.0);

        snapshotBackToImage(gc, image);
    }

    public static void applyImageWatermark(WritableImage image, LogoWatermarkParams params, Image logoImage) {
        GraphicsContext gc = new Canvas(image.getWidth(), image.getHeight()).getGraphicsContext2D();
        gc.drawImage(image, 0, 0);

        double x = params.getX();
        double y = params.getY();
        double opacity = params.getOpacity();
        double scale = params.getScale();

        gc.setGlobalAlpha(opacity);
        gc.drawImage(logoImage, x, y, logoImage.getWidth() * scale, logoImage.getHeight() * scale);
        gc.setGlobalAlpha(1.0);

        snapshotBackToImage(gc, image);
    }

    private static void snapshotBackToImage(GraphicsContext gc, WritableImage image) {
        SnapshotParameters sp = new SnapshotParameters();
        WritableImage snapshot = gc.getCanvas().snapshot(sp, null);

        PixelReader reader = snapshot.getPixelReader();
        PixelWriter writer = image.getPixelWriter();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                writer.setArgb(i, j, reader.getArgb(i, j));
            }
        }
    }
}
