package controller;

import model.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BackgroundLoader {
    private Background background;
    private Path backgroundDirectory;

    public BackgroundLoader(Background background, Path backgroundDirectory){
        this.background = background;
        this.backgroundDirectory = backgroundDirectory;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public void loadAnimations(){
        try {
            Files.walk(backgroundDirectory).forEach((path -> {
                if (Files.isRegularFile(path)){
                    String[] fileData = path.getFileName().toString().split("_");
                    String bgName = fileData[0];
                    String bgType = fileData[1];
                    // String animationNumber = fileData[2];

                    if (!bgName.equalsIgnoreCase(background.getName()))
                        return; // Wrong file

                    if (!bgType.equalsIgnoreCase(background.getType())){
                        return; // Wrong type
                    }

                    List<BufferedImage> sprites = background.getSprites();
                    if (sprites == null)
                        return; // Inexistent animation

                    BufferedImage sprite = loadImageFromDisk(path);
                    if (sprite == null)
                        return; // Error loading image from disk

                    BufferedImage scaled = new BufferedImage(701, 401, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = scaled.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(sprite, 0, 0, 701, 401, null);
                    g.dispose();
                    sprites.add(scaled);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage loadImageFromDisk(Path filename){
        try {
            return ImageIO.read(filename.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
