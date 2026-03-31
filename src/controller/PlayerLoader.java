package controller;

import model.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PlayerLoader {
    private Player player;
    private Path playerDirectory;

    public PlayerLoader(Player player, Path playerDirectory){
        this.player = player;
        this.playerDirectory = playerDirectory;
    }

    public void loadAnimations(){
        try {
            Files.walk(playerDirectory).forEach((path -> {
                if (Files.isRegularFile(path)){
                    String[] fileData = path.getFileName().toString().split("_");
                    String playerName = fileData[0];
                    String animationName = fileData[1];
                    // String animationNumber = fileData[2];

                    if (!playerName.equalsIgnoreCase(player.getName()))
                        return; // Wrong file

                    List<BufferedImage> frames = player.getFrames(animationName);
                    if (frames == null)
                        return; // Inexistent animation

                    BufferedImage sprite = loadImageFromDisk(path);
                    if (sprite == null)
                        return; // Error loading image from disk

                    frames.add(sprite);
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
