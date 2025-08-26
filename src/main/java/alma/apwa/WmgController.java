package alma.apwa;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WmgController {

    @FXML private RadioButton textRadioButton, logoRadioButton;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider opacitySlider, opacitySliderLogo;
    @FXML private TextField watermarkText, fontSizeField;
    @FXML private ComboBox<String> fontFamilyCombo;
    @FXML private CheckBox shadowCheckBox, tileCheckBox, tileCheckBoxLogo, combinedGenerationCheckedBox;
    @FXML private ImageView previewImage;
    @FXML private Button openButton, saveButton;
    @FXML private Label statusLabel, imageSizeLabel;
    @FXML private Slider angleSlider;
    @FXML private Slider angleSliderLogo;
    @FXML private TextField angleField;
    @FXML private TextField angleFieldLogo;
    @FXML private Slider gridAngleSlider;
    @FXML private Slider gridAngleSliderLogo;
    @FXML private TextField gridAngleField;
    @FXML private TextField gridAngleFieldLogo;
    @FXML private TextField spacingField;
    @FXML private TextField spacingFieldLogo;
    @FXML private Slider scaleSlider;
    @FXML private Slider scaleSliderLogo;
    @FXML private Label scalePercentLabel;
    @FXML private Label scalePercentLabelLogo;
    @FXML private Button loadLogoButton;
    @FXML private Button clearLogoButton;
    @FXML private Label logoStatusLabel;
    @FXML private VBox textSettingsBox;
    @FXML private VBox imageSettingsBox;
    @FXML private ImageView zoomImageView;
    @FXML private Label filesCountLabel;
    @FXML private Spinner<Integer> offsetXSpinnerLogo;
    @FXML private Spinner<Integer> offsetYSpinnerLogo;

    private static final int PREVIEW_MAX_WIDTH = 800;
    private final List<File> loadedFiles = new ArrayList<>();
    private File currentPreviewFile;
    private Image originalImage;
    private Image previewImage_scaled;
    private Image logoImage;
    private double previewScale = 1.0;
    private File logoFile;
    private final ToggleGroup watermarkTypeGroup = new ToggleGroup();
    private final Map<String, Object> fxmlElements = new HashMap<>();
    private Image currentWatermarkedPreview;
    private final ChangeListener<Object> livePreviewListener = (obs, oldVal, newVal) -> updateLivePreview();
    private final boolean previewEnabled = true;

    @FXML
    private void initialize() {
        registerAllElements();
        validateAllElements();
        safeInitialize();
        initControls();
        setupLivePreviewListeners();

        if (filesCountLabel != null) {
            filesCountLabel.setText("Files loaded: 0");
        }

        setupZoomFeature();
    }

    private void registerAllElements() {
        fxmlElements.put("textRadioButton", textRadioButton);
        fxmlElements.put("logoRadioButton", logoRadioButton);
        fxmlElements.put("colorPicker", colorPicker);
        fxmlElements.put("opacitySlider", opacitySlider);
        fxmlElements.put("watermarkText", watermarkText);
        fxmlElements.put("fontSizeField", fontSizeField);
        fxmlElements.put("fontFamilyCombo", fontFamilyCombo);
        fxmlElements.put("shadowCheckBox", shadowCheckBox);
        fxmlElements.put("tileCheckBox", tileCheckBox);
        fxmlElements.put("tileCheckBoxLogo", tileCheckBoxLogo);
        fxmlElements.put("combinedGenerationCheckedBox", combinedGenerationCheckedBox);
        fxmlElements.put("previewImage", previewImage);
        fxmlElements.put("openButton", openButton);
        fxmlElements.put("saveButton", saveButton);
        fxmlElements.put("statusLabel", statusLabel);
        fxmlElements.put("imageSizeLabel", imageSizeLabel);
        fxmlElements.put("angleSlider", angleSlider);
        fxmlElements.put("angleSliderLogo", angleSliderLogo);
        fxmlElements.put("angleField", angleField);
        fxmlElements.put("angleFieldLogo", angleFieldLogo);
        fxmlElements.put("gridAngleSlider", gridAngleSlider);
        fxmlElements.put("gridAngleSliderLogo", gridAngleSliderLogo);
        fxmlElements.put("gridAngleField", gridAngleField);
        fxmlElements.put("gridAngleFieldLogo", gridAngleFieldLogo);
        fxmlElements.put("spacingField", spacingField);
        fxmlElements.put("spacingFieldLogo", spacingFieldLogo);
        fxmlElements.put("scaleSlider", scaleSlider);
        fxmlElements.put("scaleSliderLogo", scaleSliderLogo);
        fxmlElements.put("scalePercentLabel", scalePercentLabel);
        fxmlElements.put("scalePercentLabelLogo", scalePercentLabelLogo);
        fxmlElements.put("loadLogoButton", loadLogoButton);
        fxmlElements.put("clearLogoButton", clearLogoButton);
        fxmlElements.put("logoStatusLabel", logoStatusLabel);
        fxmlElements.put("textSettingsBox", textSettingsBox);
        fxmlElements.put("imageSettingsBox", imageSettingsBox);
        fxmlElements.put("filesCountLabel", filesCountLabel);
        fxmlElements.put("offsetXSpinnerLogo", offsetXSpinnerLogo);
        fxmlElements.put("offsetYSpinnerLogo", offsetYSpinnerLogo);
    }

    private void validateAllElements() {
        fxmlElements.forEach((name, element) -> {
            if (element == null) {
                throw new IllegalStateException("Element " + name + " has not been initialized in FXML. " +
                        "Check that fx:id in FXML matches field names in controller.");
            }
        });
    }

    private void safeInitialize() {
        try {
            if (textRadioButton != null && logoRadioButton != null) {
                watermarkTypeGroup.getToggles().addAll(textRadioButton, logoRadioButton);
                textRadioButton.setSelected(true);
            }

            tileCheckBox.setSelected(true);
            tileCheckBoxLogo.setSelected(true);
            if (colorPicker != null) colorPicker.setValue(Color.rgb(0, 0, 0, 0.5));
            if (opacitySlider != null) opacitySlider.setValue(40);
            if (opacitySliderLogo != null) opacitySliderLogo.setValue(40);
            if (watermarkText != null) watermarkText.setText("Watermark");
            if (fontSizeField != null) fontSizeField.setText("24");
            if (spacingField != null) spacingField.setText("200");
            if (spacingFieldLogo != null) spacingFieldLogo.setText("200");
            if (angleField != null) angleField.setText("0");
            if (angleFieldLogo != null) angleFieldLogo.setText("0");
            if (gridAngleField != null) gridAngleField.setText("0");
            if (gridAngleFieldLogo != null) gridAngleFieldLogo.setText("0");
            if (offsetXSpinnerLogo != null) {
                SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(-1000, 1000, 0);
                offsetXSpinnerLogo.setValueFactory(valueFactory);
            }
            if (offsetYSpinnerLogo != null) {
                SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(-1000, 1000, 0);
                offsetYSpinnerLogo.setValueFactory(valueFactory);
            }

            if (fontFamilyCombo != null) {
                List<String> fontFamilies = Font.getFamilies();
                fontFamilyCombo.getItems().addAll(fontFamilies);
                String defaultFont = fontFamilies.contains("Arial") ? "Arial" :
                        fontFamilies.contains("Sans Serif") ? "Sans Serif" :
                                !fontFamilies.isEmpty() ? fontFamilies.get(0) : "System";
                fontFamilyCombo.setValue(defaultFont);
            }

            if (statusLabel != null) statusLabel.setText("Ready");
            if (imageSizeLabel != null) imageSizeLabel.setText("Dimensions: n/a");
            if (logoStatusLabel != null) logoStatusLabel.setText("No logo loaded");
            if (filesCountLabel != null) filesCountLabel.setText("No files loaded");

            if (openButton != null) openButton.setOnAction(e -> openImage());
            if (saveButton != null) saveButton.setOnAction(e -> saveAllImages());
            if (loadLogoButton != null) loadLogoButton.setOnAction(e -> loadLogo());
            if (clearLogoButton != null) clearLogoButton.setOnAction(e -> clearLogo());

            watermarkTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                boolean textMode = textRadioButton != null && textRadioButton.isSelected();
                boolean logoMode = logoRadioButton != null && logoRadioButton.isSelected();

                if (textSettingsBox != null) {
                    textSettingsBox.setVisible(textMode);
                    textSettingsBox.setManaged(textMode);
                }

                if (imageSettingsBox != null) {
                    imageSettingsBox.setVisible(logoMode);
                    imageSettingsBox.setManaged(logoMode);
                }
            });

        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initControls() {
        initAngleControls();
        initAngleControlsLogo();
        initGridAngleControls();
        initGridAngleControlsLogo();
        initScaleControls();
        initScaleControlsLogo();
    }

    private void initAngleControls() {
        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!angleField.isFocused()) {
                angleField.setText(String.format("%.0f", newVal));
            }
        });
        angleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    if (!angleSlider.isValueChanging()) {
                        angleSlider.setValue(value % 360);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    private void initAngleControlsLogo() {
        angleSliderLogo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!angleFieldLogo.isFocused()) {
                angleFieldLogo.setText(String.format("%.0f", newVal));
            }
        });
        angleFieldLogo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    if (!angleSliderLogo.isValueChanging()) {
                        angleSliderLogo.setValue(value % 360);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    private void initGridAngleControls() {
        gridAngleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!gridAngleField.isFocused()) {
                gridAngleField.setText(String.format("%.0f", newVal));
            }
        });
        gridAngleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    if (!gridAngleSlider.isValueChanging()) {
                        gridAngleSlider.setValue(value % 360);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    private void initGridAngleControlsLogo() {
        gridAngleSliderLogo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!gridAngleFieldLogo.isFocused()) {
                gridAngleFieldLogo.setText(String.format("%.0f", newVal));
            }
        });
        gridAngleFieldLogo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    if (!gridAngleSliderLogo.isValueChanging()) {
                        gridAngleSliderLogo.setValue(value % 360);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    private void initScaleControls() {
        scaleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            scalePercentLabel.setText(value + "%");
        });
    }

    private void initScaleControlsLogo() {
        scaleSliderLogo.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            scalePercentLabelLogo.setText(value + "%");
        });
    }

    private void setupLivePreviewListeners() {
        opacitySlider.valueProperty().addListener(livePreviewListener);
        opacitySliderLogo.valueProperty().addListener(livePreviewListener);
        angleSlider.valueProperty().addListener(livePreviewListener);
        angleSliderLogo.valueProperty().addListener(livePreviewListener);
        gridAngleSlider.valueProperty().addListener(livePreviewListener);
        gridAngleSliderLogo.valueProperty().addListener(livePreviewListener);
        scaleSlider.valueProperty().addListener(livePreviewListener);
        scaleSliderLogo.valueProperty().addListener(livePreviewListener);
        watermarkText.textProperty().addListener(livePreviewListener);
        fontSizeField.textProperty().addListener(livePreviewListener);
        spacingField.textProperty().addListener(livePreviewListener);
        spacingFieldLogo.textProperty().addListener(livePreviewListener);
        tileCheckBox.selectedProperty().addListener(livePreviewListener);
        tileCheckBoxLogo.selectedProperty().addListener(livePreviewListener);
        shadowCheckBox.selectedProperty().addListener(livePreviewListener);
        colorPicker.valueProperty().addListener(livePreviewListener);
        fontFamilyCombo.valueProperty().addListener(livePreviewListener);
        offsetXSpinnerLogo.valueProperty().addListener(livePreviewListener);
        offsetYSpinnerLogo.valueProperty().addListener(livePreviewListener);
        combinedGenerationCheckedBox.selectedProperty().addListener(livePreviewListener);
        watermarkTypeGroup.selectedToggleProperty().addListener(livePreviewListener);

    }

    private void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null && !files.isEmpty()) {
            loadedFiles.clear();
            loadedFiles.addAll(files);
            currentPreviewFile = loadedFiles.get(0);

            try {
                loadImageForPreview(currentPreviewFile);

                if (filesCountLabel != null) {
                    filesCountLabel.setText("Files loaded: " + loadedFiles.size());
                }
                if (statusLabel != null) {
                    statusLabel.setText("Preview: " + currentPreviewFile.getName());
                }
            } catch (Exception e) {
                if (statusLabel != null) statusLabel.setText("Image load error");
                e.printStackTrace();
            }
        }
    }

    private void loadImageForPreview(File file) {
        originalImage = new Image(file.toURI().toString());
        if (originalImage.getWidth() > PREVIEW_MAX_WIDTH) {
            previewScale = PREVIEW_MAX_WIDTH / originalImage.getWidth();
            int newWidth = PREVIEW_MAX_WIDTH;
            previewImage_scaled = getScaledInstance(originalImage, newWidth);
        } else {
            previewScale = 1.0;
            previewImage_scaled = originalImage;
        }
        updateImageInfo();
        updateLivePreview();
    }

    private static Image getScaledInstance(Image image, int newWidth) {
        double ratio = newWidth / image.getWidth();
        int newHeight = (int) (image.getHeight() * ratio);

        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(newWidth, newHeight);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, newWidth, newHeight);

        javafx.scene.SnapshotParameters sp = new javafx.scene.SnapshotParameters();
        sp.setFill(javafx.scene.paint.Color.TRANSPARENT);
        return canvas.snapshot(sp, null);
    }

    private void updateLivePreview() {
        if (!previewEnabled || previewImage_scaled == null) return;

        try {
            Image watermarkedImage = previewImage_scaled;

            if (combinedGenerationCheckedBox.isSelected()) {
                if (textRadioButton.isSelected() || logoRadioButton.isSelected()) {
                    TextWatermarkParams textParams = createTextWatermarkParams(true);
                    watermarkedImage = applyTextWatermarkDirect(watermarkedImage, textParams);

                    if (logoImage != null) {
                        LogoWatermarkParams logoParams = createLogoWatermarkParams(true);
                        watermarkedImage = applyImageWatermarkDirect(watermarkedImage, logoParams);
                    }
                }
            } else {
                if (textRadioButton.isSelected()) {
                    TextWatermarkParams textParams = createTextWatermarkParams(true);
                    watermarkedImage = applyTextWatermarkDirect(watermarkedImage, textParams);
                } else if (logoRadioButton.isSelected() && logoImage != null) {
                    LogoWatermarkParams logoParams = createLogoWatermarkParams(true);
                    watermarkedImage = applyImageWatermarkDirect(watermarkedImage, logoParams);
                }
            }

            currentWatermarkedPreview = watermarkedImage;
            previewImage.setImage(currentWatermarkedPreview);

        } catch (Exception e) {
            System.err.println("Live preview update error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private TextWatermarkParams createTextWatermarkParams(boolean isPreview) {
        double scaleFactor = isPreview ? previewScale : 1.0;
        double fontSize = Double.parseDouble(fontSizeField.getText()) * scaleFactor;
        fontSize *= (scaleSlider.getValue() / 100.0);
        double x = (isPreview ? previewImage_scaled.getWidth() : originalImage.getWidth()) / 2;
        double y = (isPreview ? previewImage_scaled.getHeight() : originalImage.getHeight()) / 2;

        return new TextWatermarkParams(
                watermarkText.getText(),
                fontFamilyCombo.getValue(),
                fontSize,
                colorPicker.getValue(),
                opacitySlider.getValue() / 100.0,
                "CENTER",
                x,
                y,
                shadowCheckBox.isSelected(),
                Color.BLACK,
                10.0,
                2.0,
                2.0
        );
    }

    private LogoWatermarkParams createLogoWatermarkParams(boolean isPreview) {
        double scaleFactor = isPreview ? previewScale : 1.0;
        double spacing = Double.parseDouble(spacingFieldLogo.getText()) * scaleFactor;
        double x = (isPreview ? previewImage_scaled.getWidth() : originalImage.getWidth()) / 2;
        double y = (isPreview ? previewImage_scaled.getHeight() : originalImage.getHeight()) / 2;

        return new LogoWatermarkParams(
                x, y, opacitySliderLogo.getValue() / 100.0,
                (scaleSliderLogo.getValue() / 100.0) * scaleFactor,
                angleSliderLogo.getValue(), gridAngleSliderLogo.getValue(),
                spacing, tileCheckBoxLogo.isSelected()
        );
    }

    private Image applyTextWatermarkDirect(Image sourceImage, TextWatermarkParams params) {
        Canvas canvas = new Canvas(sourceImage.getWidth(), sourceImage.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(sourceImage, 0, 0);
        gc.setGlobalAlpha(params.getOpacity());
        gc.setFill(params.getColor());
        gc.setFont(Font.font(params.getFontName(), params.getFontSize()));
        gc.setTextAlign(TextAlignment.valueOf(params.getAlignment().toUpperCase()));

        if (shadowCheckBox.isSelected()) {
            gc.setEffect(new javafx.scene.effect.DropShadow(
                    5,
                    Color.BLACK
            ));
        }

        if (tileCheckBox.isSelected()) {
            double spacing;
            if (sourceImage == previewImage_scaled) {
                spacing = Double.parseDouble(spacingField.getText()) * previewScale;
            } else {
                spacing = Double.parseDouble(spacingField.getText()) * (sourceImage.getWidth() / originalImage.getWidth());
            }

            double angle = angleSlider.getValue();
            double gridAngle = gridAngleSlider.getValue();

            gc.save();
            gc.translate(sourceImage.getWidth() / 2, sourceImage.getHeight() / 2);
            gc.rotate(gridAngle);
            gc.translate(-sourceImage.getWidth() / 2, -sourceImage.getHeight() / 2);

            for (double x = -sourceImage.getWidth() * 0.7; x < sourceImage.getWidth() + sourceImage.getWidth() * 0.7; x += spacing) {
                for (double y = -sourceImage.getHeight(); y < sourceImage.getHeight() * 2; y += spacing) {
                    gc.save();
                    gc.translate(x, y);
                    gc.rotate(angle);
                    gc.fillText(params.getText(), 0, 0);
                    gc.restore();
                }
            }
            gc.setEffect(null);
            gc.restore();
        } else {
            gc.save();
            gc.translate(params.getX(), params.getY());
            gc.rotate(angleSlider.getValue());
            gc.fillText(params.getText(), 0, 0);
            gc.restore();
        }
        gc.setGlobalAlpha(1.0);

        javafx.scene.SnapshotParameters sp = new javafx.scene.SnapshotParameters();
        sp.setFill(javafx.scene.paint.Color.TRANSPARENT);
        return canvas.snapshot(sp, null);
    }

    private Image applyImageWatermarkDirect(Image sourceImage, LogoWatermarkParams params) {
        Canvas canvas = new Canvas(sourceImage.getWidth(), sourceImage.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(sourceImage, 0, 0);
        gc.setGlobalAlpha(params.getOpacity());

        double logoWidth = logoImage.getWidth() * params.getScale();
        double logoHeight = logoImage.getHeight() * params.getScale();
        double offsetX = offsetXSpinnerLogo.getValue();
        double offsetY = offsetYSpinnerLogo.getValue();

        if (params.isTile()) {
            gc.save();

            gc.translate(sourceImage.getWidth() / 2, sourceImage.getHeight() / 2);
            gc.rotate(params.getGridAngle());
            gc.translate(-sourceImage.getWidth() / 2, -sourceImage.getHeight() / 2);

            for (double x = -sourceImage.getWidth() * 0.7; x < sourceImage.getWidth() + sourceImage.getWidth()* 0.7; x += params.getSpacing()) {
                for (double y = -sourceImage.getHeight(); y < sourceImage.getHeight() * 2; y += params.getSpacing()) {
                    gc.save();
                    gc.translate(x + logoWidth/2 + offsetX, y + logoHeight/2 + offsetY);
                    gc.rotate(params.getAngle());
                    gc.drawImage(logoImage, -logoWidth/2, -logoHeight/2, logoWidth, logoHeight);
                    gc.restore();
                }
            }
            gc.restore();
        } else {
            gc.save();
            gc.translate(params.getX() + offsetX, params.getY() + offsetY);
            gc.rotate(params.getAngle());
            gc.drawImage(logoImage, -logoWidth/2, -logoHeight/2, logoWidth, logoHeight);
            gc.restore();
        }
        gc.setGlobalAlpha(1.0);

        javafx.scene.SnapshotParameters sp = new javafx.scene.SnapshotParameters();
        sp.setFill(javafx.scene.paint.Color.TRANSPARENT);
        return canvas.snapshot(sp, null);
    }

    private Image applyTextWatermark(Image sourceImage, TextWatermarkParams params) {
        javafx.scene.image.WritableImage writableImage = new javafx.scene.image.WritableImage(
                (int) sourceImage.getWidth(), (int) sourceImage.getHeight());

        javafx.scene.image.PixelReader reader = sourceImage.getPixelReader();
        javafx.scene.image.PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < sourceImage.getWidth(); x++) {
            for (int y = 0; y < sourceImage.getHeight(); y++) {
                writer.setArgb(x, y, reader.getArgb(x, y));
            }
        }
        WatermarkProcessor.applyTextWatermark(writableImage, params);
        return writableImage;
    }

    private void setupZoomFeature() {
        if (zoomImageView != null && previewImage != null) {
            previewImage.setOnMouseMoved(event -> showZoom(event.getX(), event.getY()));
            previewImage.setOnMouseExited(event -> zoomImageView.setImage(null));
        }
    }

    private void showZoom(double mouseX, double mouseY) {
        if (currentWatermarkedPreview == null || zoomImageView == null || previewImage == null) return;

        try {
            double displayScaleX = currentWatermarkedPreview.getWidth() / previewImage.getFitWidth();
            double displayScaleY = currentWatermarkedPreview.getHeight() / previewImage.getFitHeight();
            int zoomSize = 60;
            int zoomFactor = 2;

            int centerX = (int)(mouseX * displayScaleX);
            int centerY = (int)(mouseY * displayScaleY);

            int x0 = Math.max(0, centerX - zoomSize / 2);
            int y0 = Math.max(0, centerY - zoomSize / 2);
            int x1 = Math.min((int)currentWatermarkedPreview.getWidth(), x0 + zoomSize);
            int y1 = Math.min((int)currentWatermarkedPreview.getHeight(), y0 + zoomSize);

            if (x1 > x0 && y1 > y0) {
                javafx.scene.image.WritableImage zoomed = new javafx.scene.image.WritableImage(
                        currentWatermarkedPreview.getPixelReader(), x0, y0, x1 - x0, y1 - y0);

                zoomImageView.setImage(zoomed);
                zoomImageView.setFitWidth(zoomFactor * zoomSize);
                zoomImageView.setFitHeight(zoomFactor * zoomSize);
            }
        } catch (Exception e) {
        }
    }

    private void loadLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        logoFile = fileChooser.showOpenDialog(null);
        if (logoFile != null) {
            try {
                logoImage = new Image(logoFile.toURI().toString());
                if (logoStatusLabel != null) {
                    logoStatusLabel.setText("Logo: " + logoFile.getName());
                    logoStatusLabel.setStyle("-fx-text-fill: green;");
                }
                if (statusLabel != null) statusLabel.setText("Logo loaded: " + logoFile.getName());
                updateLivePreview();
            } catch (Exception e) {
                System.err.println("Error loading logo: " + e.getMessage());
                e.printStackTrace();
                if (statusLabel != null) statusLabel.setText("Error loading logo");
                if (logoStatusLabel != null) {
                    logoStatusLabel.setText("Failed to load logo");
                    logoStatusLabel.setStyle("-fx-text-fill: red;");
                }
            }
        }
    }

    private void clearLogo() {
        logoFile = null;
        logoImage = null;
        if (logoStatusLabel != null) {
            logoStatusLabel.setText("No logo loaded");
            logoStatusLabel.setStyle("");
        }
        updateLivePreview();
    }

    private void updateImageInfo() {
        if (originalImage != null) {
            if (imageSizeLabel != null) {
                imageSizeLabel.setText(String.format("%.0fx%.0f",
                        originalImage.getWidth(),
                        originalImage.getHeight(),
                        previewScale * 100));
            }
            if (previewImage != null) {
                previewImage.setImage(previewImage_scaled);
            }
        }
    }

    private void saveAllImages() {
        if (loadedFiles.isEmpty()) {
            if (statusLabel != null) statusLabel.setText("No file(s) for saving");
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder to save file(s)");
        File outputDir = directoryChooser.showDialog(null);
        if (outputDir == null) return;

        int savedCount = 0;

        for (File file : loadedFiles) {
            try {
                Image sourceImage = new Image(file.toURI().toString());
                Image processedImage = sourceImage;

                if (combinedGenerationCheckedBox.isSelected()) {
                    TextWatermarkParams textParams = createFullResolutionTextParams(processedImage);
                    processedImage = applyTextWatermarkDirect(processedImage, textParams);

                    if (logoImage != null) {
                        LogoWatermarkParams logoParams = createLogoWatermarkParams(false);
                        processedImage = applyImageWatermarkDirect(processedImage, logoParams);
                    }
                } else {
                    if (textRadioButton.isSelected()) {
                        TextWatermarkParams textParams = createFullResolutionTextParams(processedImage);
                        processedImage = applyTextWatermarkDirect(processedImage, textParams);
                    } else if (logoRadioButton.isSelected() && logoImage != null) {
                        LogoWatermarkParams logoParams = createLogoWatermarkParams(false);
                        processedImage = applyImageWatermarkDirect(processedImage, logoParams);
                    }
                }

                String originalName = file.getName();
                String extension = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();

                if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                    extension = "png";
                }

                String nameWithoutExt = originalName.substring(0, originalName.lastIndexOf('.'));
                File outputFile = new File(outputDir, nameWithoutExt + "_w." + extension);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(processedImage, null);

                if (extension.equals("jpg") || extension.equals("jpeg")) {
                    if (bufferedImage.getTransparency() != BufferedImage.OPAQUE) {
                        BufferedImage rgbImage = new BufferedImage(
                                bufferedImage.getWidth(),
                                bufferedImage.getHeight(),
                                BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = rgbImage.createGraphics();
                        g.drawImage(bufferedImage, 0, 0, null);
                        g.dispose();
                        bufferedImage = rgbImage;
                    }

                    javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
                    javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
                    param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.9f);

                    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
                         javax.imageio.stream.ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
                        writer.setOutput(ios);
                        writer.write(null, new javax.imageio.IIOImage(bufferedImage, null, null), param);
                    }
                    writer.dispose();
                } else {
                    ImageIO.write(bufferedImage, "PNG", outputFile);
                }
                savedCount++;

            } catch (Exception e) {
                System.err.println("Error while processing file " + file.getName() + ": " + e.getMessage());
                if (statusLabel != null) {
                    statusLabel.setText("Error while processing " + file.getName());
                }
            }
        }

        if (statusLabel != null) {
            statusLabel.setText("Saved " + savedCount + " of " + loadedFiles.size() + " files");
        }
    }

    private TextWatermarkParams createFullResolutionTextParams(Image targetImage) {
        double imageScale = targetImage.getWidth() / originalImage.getWidth();
        double fullResFontSize = Double.parseDouble(fontSizeField.getText()) * imageScale;
        double x = targetImage.getWidth() / 2;
        double y = targetImage.getHeight() / 2;

        return new TextWatermarkParams(
                watermarkText.getText(),
                fontFamilyCombo.getValue(),
                fullResFontSize,
                colorPicker.getValue(),
                opacitySlider.getValue() / 100.0,
                "CENTER",
                x,
                y,
                shadowCheckBox.isSelected(),
                Color.BLACK,
                10.0,
                2.0,
                2.0
        );
    }

    private LogoWatermarkParams createFullResolutionLogoParams(Image targetImage) {
        double imageScale = targetImage.getWidth() / originalImage.getWidth();
        double fullResSpacing = Double.parseDouble(spacingFieldLogo.getText()) * imageScale;
        double x = targetImage.getWidth() / 2;
        double y = targetImage.getHeight() / 2;

        return new LogoWatermarkParams(
                x, y, opacitySliderLogo.getValue() / 100.0,
                (scaleSliderLogo.getValue() / 100.0) * imageScale,
                angleSliderLogo.getValue(), gridAngleSliderLogo.getValue(),
                fullResSpacing, tileCheckBoxLogo.isSelected()
        );
    }
}