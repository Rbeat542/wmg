module alma.apwa {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.twelvemonkeys.imageio.core;
    requires com.twelvemonkeys.imageio.jpeg;
    requires com.twelvemonkeys.imageio.tiff;
    requires com.twelvemonkeys.imageio.webp;
    requires javafx.swing;

    opens alma.apwa to javafx.fxml;
    exports alma.apwa;
}