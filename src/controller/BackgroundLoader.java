package controller;

import model.Background;
import model.CharacterAnimationState;
import model.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BackgroundLoader {
    public static Background loadAnimations(String name){
        Background background = new Background(name);
        Path backgroundDirectory = Path.of("./src/resources/backgrounds/" + name);
        try {
            Files.walk(backgroundDirectory).forEach((path -> {
                if (Files.isRegularFile(path)){
                    String[] fileData = path.getFileName().toString().split("_");
                    String bgName = fileData[0];
                    String animationNumber = fileData[1].split("\\.")[0];

                    if (!bgName.equalsIgnoreCase(name))
                        return; // Wrong file

                    Map<Integer, BufferedImage> sprites = background.getFrames();
                    if (sprites == null)
                        return; // Inexistent animation

                    BufferedImage sprite;
                    try {
                        sprite =  ImageIO.read(path.toFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (sprite == null)
                        return; // Error loading image from disk

                    BufferedImage scaled = new BufferedImage(2560, 1440, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = scaled.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(sprite, 0, 0, GameState.getInstance().getWindowWidth(), GameState.getInstance().getWindowHeight(), null);
                    g.dispose();
                    sprites.put(Integer.parseInt(animationNumber), scaled);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Loaded background: %s\n", name);
        return background;
    }
}
