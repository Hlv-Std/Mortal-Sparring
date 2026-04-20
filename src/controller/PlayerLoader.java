package controller;

import model.Character;
import model.CharacterAnimationState;
import model.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PlayerLoader {
    public static Player loadAnimations(String playerName, String characterName, boolean mirror){
        Player player = new Player(playerName);
        player.setCharacter(new Character(characterName));
        Character character = player.getCharacter();
        Path playerDirectory = Path.of("./src/resources/Characters/" + characterName);
        try {
            Files.walk(playerDirectory).forEach((path -> {
                if (Files.isRegularFile(path)){
                    String[] fileData = path.getFileName().toString().split("_");
                    String loaded_characterName = fileData[0];
                    String animationName = fileData[1];
                    String animationNumber = fileData[2].split("\\.")[0];

                    if (!loaded_characterName.equalsIgnoreCase(characterName))
                        return; // Wrong file

                    Map<Integer, BufferedImage> frames = character.getFrames(CharacterAnimationState.valueOf(animationName));
                    if (frames == null)
                        return; // Inexistent animation

                    BufferedImage sprite;
                    try {
                        sprite = ImageIO.read(path.toFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (sprite == null)
                        return; // Error loading image from disk

                    BufferedImage scaled = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = scaled.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    if (mirror){
                        g.drawImage(sprite, 100, 0, -100, 100, null);
                    } else {
                        g.drawImage(sprite, 0, 0, 100, 100, null);
                    }
                    g.dispose();
                    frames.put(Integer.parseInt(animationNumber), scaled);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Loaded %s: %d frames\n", characterName, character.getFrames(CharacterAnimationState.Idle).size());
        return player;
    }
}
