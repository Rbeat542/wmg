package alma.apwa;

public class LogoWatermarkParams {

    private double x;
    private double y;
    private double opacity;
    private double scale;
    private double angle;
    private double gridAngle;
    private double spacing;
    private boolean tile;

    public LogoWatermarkParams(double x, double y,
                               double opacity, double scale, double angle,
                               double gridAngle, double spacing, boolean tile) {
        this.x = x;
        this.y = y;
        this.opacity = clamp(opacity, 0.0, 1.0);
        this.scale = clamp(scale, 0.01, 10.0);
        this.angle = angle % 360;
        this.gridAngle = gridAngle % 360;
        this.spacing = Math.max(0.0, spacing);
        this.tile = tile;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getOpacity() { return opacity; }
    public double getScale() { return scale; }
    public double getAngle() { return angle; }
    public double getGridAngle() { return gridAngle; }
    public double getSpacing() { return spacing; }
    public boolean isTile() { return tile; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setOpacity(double opacity) { this.opacity = clamp(opacity, 0.0, 1.0); }
    public void setScale(double scale) { this.scale = clamp(scale, 0.01, 10.0); }
    public void setAngle(double angle) { this.angle = angle % 360; }
    public void setGridAngle(double gridAngle) { this.gridAngle = gridAngle % 360; }
    public void setSpacing(double spacing) { this.spacing = Math.max(0.0, spacing); }
    public void setTile(boolean tile) { this.tile = tile; }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    @Override
    public String toString() {
        return String.format("LogoWatermarkParams[x=%.1f,y=%.1f,opacity=%.2f,scale=%.2f,angle=%.1f,tile=%s]",
                x, y, opacity, scale, angle, tile);
    }
}
