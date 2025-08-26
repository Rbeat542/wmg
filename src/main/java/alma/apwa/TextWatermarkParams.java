package alma.apwa;

import javafx.scene.paint.Color;

public class TextWatermarkParams {
    private String text;
    private String fontName;
    private double fontSize;
    private Color color;
    private double opacity;
    private String alignment;
    private double x;
    private double y;
    private boolean shadowEnabled;
    private Color shadowColor;
    private double shadowRadius;
    private double shadowOffsetX;
    private double shadowOffsetY;

    public TextWatermarkParams(String text, String fontName, double fontSize, Color color,
                               double opacity, String alignment, double x, double y, boolean shadowEnabled, Color shadowColor, double shadowRadius, double shadowOffsetX, double shadowOffsetY) {
        this.text = text;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.color = color;
        this.opacity = opacity;
        this.alignment = alignment;
        this.x = x;
        this.y = y;
        this.shadowEnabled = shadowEnabled;
        this.shadowColor = shadowColor;
        this.shadowRadius = shadowRadius;
        this.shadowOffsetX = shadowOffsetX;
        this.shadowOffsetY = shadowOffsetY;
    }

    public String getText() { return text; }
    public String getFontName() { return fontName; }
    public double getFontSize() { return fontSize; }
    public Color getColor() { return color; }
    public double getOpacity() { return opacity; }
    public String getAlignment() { return alignment; }
    public double getX() { return x; }
    public double getY() { return y; }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }
    public Color getShadowColor() {
        return shadowColor;
    }

    public double getShadowRadius() {
        return shadowRadius;
    }

    public double getShadowOffsetX() {
        return shadowOffsetX;
    }

    public double getShadowOffsetY() {
        return shadowOffsetY;
    }

    public void setText(String text) { this.text = text; }
    public void setFontName(String fontName) { this.fontName = fontName; }
    public void setFontSize(double fontSize) { this.fontSize = fontSize; }
    public void setColor(Color color) { this.color = color; }
    public void setOpacity(double opacity) { this.opacity = opacity; }
    public void setAlignment(String alignment) { this.alignment = alignment; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setShadowRadius(double shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public void setShadowOffsetX(double shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
    }

    public void setShadowOffsetY(double shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
    }
}
