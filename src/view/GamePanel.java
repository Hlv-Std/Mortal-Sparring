package view;

import controller.BackgroundLoader;
import controller.InputHandler;
import model.*;
import model.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Deque;


/*
 * TODO: Pause Screen (Independent from who): Freeze dt
 */
public class GamePanel extends JPanel {
    private int WIDTH;
    private int HEIGHT;
    private double GROUND;

    private Background bg;
    private final Player player1;
    private final Player player2;
    private final Character character1;
    private final Character character2;

    private final InputHandler input;
    private final GameState gameState;
    private final BufferedImage pauseScreen;

    public GamePanel(){
        gameState = GameState.getInstance();
        this.player1    = gameState.getPlayer1();
        this.player2    = gameState.getPlayer2();
        this.character1 = player1.getCharacter();
        this.character2 = player2.getCharacter();
        this.WIDTH      = gameState.getWindowWidth();
        this.HEIGHT     = gameState.getWindowHeight();
        this.bg         = gameState.getBg();
        GROUND          = (double) HEIGHT - (double) HEIGHT / 5;
        input           = new InputHandler(this, player1, player2);

        pauseScreen = new BufferedImage(
                gameState.getWindowWidth(),
                gameState.getWindowHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = pauseScreen.createGraphics();
        graphics.setColor(new Color(20, 20, 20, 170));
        graphics.fillRect(0, 0, gameState.getWindowWidth(), gameState.getWindowHeight());
        graphics.dispose();

        setFocusable(true);
        requestFocus();

        Timer t = getTimer();
        t.start();
        input.start();
    }

    private Timer getTimer() {
        final int FPS = 60;
        final double dt = (double) 1 / FPS;
        final double GRAVITY = 1500;
        final double FRICTION = 10;
        final double MAX_VELOCITY = 900;
        return new Timer(1000 / FPS, (_) -> {
            if (!gameState.isPaused()) {
                // NOTE: Input -> Forces -> Friction -> Integrate -> Collide
                // Forces
                character1.vely += GRAVITY * dt;
                character2.vely += GRAVITY * dt;

                // Friction
                if (character1.isInAir()){
                    character1.velx *= (1 - FRICTION/2.3 * dt);
                } else {
                    character1.velx *= (1 - FRICTION * dt);
                }
                if (Math.abs(character1.velx) < 10) character1.velx = 0;

                if (character2.isInAir()){
                    character2.velx *= (1 - FRICTION/2.3 * dt);
                } else {
                    character2.velx *= (1 - FRICTION * dt);
                }
                if (Math.abs(character2.velx) < 10) character2.velx = 0;

                if (character1.x > character2.x - 30){
                    character1.velx = 0;
                    character2.x++;
                    character1.x--;
                }

                if (character2.x < character1.x + 30){
                    character2.velx = 0;
                    character1.x--;
                    character2.x++;
                }

                // Integrating
                character1.x += character1.velx * dt;
                character1.y += character1.vely * dt;
                character2.x += character2.velx * dt;
                character2.y += character2.vely * dt;

                // Collisions
                if (character1.x < 0){
                    character1.x = 0;
                    character1.velx = 0;
                } else if (character1.x + character1.hitbox.w >= WIDTH){
                    character1.x = WIDTH - character1.hitbox.w;
                    character1.velx = 0;
                }
                if (character1.y + character1.hitbox.h > GROUND){
                    character1.vely = 0;
                    character1.y = GROUND - character1.hitbox.h;
                }

                if (character2.x < 0){
                    character2.x = 0;
                    character2.velx = 0;
                } else if (character2.x + character2.hitbox.w >= WIDTH){
                    character2.x = WIDTH - character2.hitbox.w;
                    character2.velx = 0;
                }
                if (character2.y + character2.hitbox.h > GROUND){
                    character2.vely = 0;
                    character2.y = GROUND - character2.hitbox.h;
                }

                update();
            }
            repaint();
        });
    }

    private void update(){
        if (gameState.windowHasChanged()){
            bg = BackgroundLoader.loadAnimations(bg.getName());
            this.WIDTH      = gameState.getWindowWidth();
            this.HEIGHT     = gameState.getWindowHeight();
            GROUND          = (double) HEIGHT - (double) HEIGHT / 5;
            gameState.ok();
        }

        if (gameState.needsRestart()){
            character1.reset();
            character2.reset();
            gameState.ok();
        }

        if (!character1.isAlive()){
            character1.changeAnimation(CharacterAnimationState.Dead);
            gameState.setGameover(true);
        }else if (!character2.isAlive()){
            character2.changeAnimation(CharacterAnimationState.Dead);
            gameState.setGameover(true);
        }

        if (gameState.isGameover()){
            if(character1.isAlive())
                character1.changeAnimation(CharacterAnimationState.Idle);
            else if (character2.isAlive())
                character2.changeAnimation(CharacterAnimationState.Idle);
            return;
        }

        // Position
        if (character1.y + character1.hitbox.h < GROUND) {
            character1.setFalling(character1.vely > 0);
            character1.setInAir(true);
        } else {
            character1.setInAir(false);
            character1.setFalling(false);
            character1.resetJumps();
            character1.setDucking(false);
        }

        if (character2.y + character2.hitbox.h < GROUND) {
            character2.setFalling(character2.vely > 0);
            character2.setInAir(true);
        } else {
            character2.setInAir(false);
            character2.setFalling(false);
            character2.resetJumps();
            character2.setDucking(false);
        }

        // Input handling
        if (input.isHeld(KeyEvent.VK_W)) character1.executeAction("Jump");
        if (input.isHeld(KeyEvent.VK_A)) character1.executeAction("Left");
        if (input.isHeld(KeyEvent.VK_S)) character1.executeAction("Duck");
        if (input.isHeld(KeyEvent.VK_D)) character1.executeAction("Right");
        if (input.isHeld(KeyEvent.VK_X)) character1.executeAction("Punch");
        if (input.isHeld(KeyEvent.VK_C)) character1.executeAction("Kick");
        if (input.isHeld(KeyEvent.VK_V)) character1.executeAction("Special1");
        // DEBUG: Remove this later
        if (player1.getInputProcesser().combo(KeyEvent.VK_S, KeyEvent.VK_D)) character1.executeAction("SDCombo");

        if (input.isHeld(KeyEvent.VK_U))      character2.executeAction("Jump");
        if (input.isHeld(KeyEvent.VK_H))      character2.executeAction("Left");
        if (input.isHeld(KeyEvent.VK_J))      character2.executeAction("Duck");
        if (input.isHeld(KeyEvent.VK_K))      character2.executeAction("Right");
        if (input.isHeld(KeyEvent.VK_M))      character2.executeAction("Punch");
        if (input.isHeld(KeyEvent.VK_COMMA))  character2.executeAction("Kick");
        if (input.isHeld(KeyEvent.VK_PERIOD)) character2.executeAction("Special1");
        // DEBUG: Remove this later
        if (player2.getInputProcesser().combo(KeyEvent.VK_J, KeyEvent.VK_H)) character2.executeAction("SDCombo");

        // Attacks
        Deque<Attack> runningAttacks = character1.getRunningAttacks();
        if (!runningAttacks.isEmpty()){
            Attack hb = runningAttacks.getFirst();
            if (hb.isAlive()){
                hb.advance();

                Rectangle attackHB = new Rectangle(
                        (int) hb.x,
                        (int) hb.y,
                        (int) hb.hitbox.w,
                        (int) hb.hitbox.h);

                Rectangle playerHB = new Rectangle(
                        (int) character2.x,
                        (int) character2.y,
                        (int) character2.hitbox.w,
                        (int) character2.hitbox.h);

                // NOTE: For some strange reason, if we swap these two parameters, we get a wrong detection
                if (intersects(playerHB, attackHB)){
                    character2.damage(hb.damage);
                }
            }else {
                character1.setInAction(false);
                runningAttacks.removeFirst();
            }

        }

        runningAttacks = character2.getRunningAttacks();
        if (!runningAttacks.isEmpty()){
            Attack hb = runningAttacks.getFirst();
            if (hb.isAlive()){
                hb.advance();
            }else {
                character2.setInAction(false);
                runningAttacks.removeFirst();
            }

            Rectangle attackHB = new Rectangle(
                    (int) hb.x,
                    (int) hb.y,
                    (int) hb.hitbox.w,
                    (int) hb.hitbox.h);

            Rectangle playerHB = new Rectangle(
                    (int) character1.x,
                    (int) character1.y,
                    (int) character1.hitbox.w,
                    (int) character1.hitbox.h);

            if (intersects(playerHB, attackHB)){
                character1.damage(hb.damage);
            }
        }

        // Reset Idle animation
        if (!character1.isInAir()    &&
                !character1.isDucking()  &&
                !character1.isInAction() &&
                character1.velx == 0     &&
                character1.vely == 0     &&
                !character1.getAnimationState().equals(CharacterAnimationState.Idle))
            character1.changeAnimation(CharacterAnimationState.Idle);

        if (!character2.isInAir()    &&
                !character2.isDucking()  &&
                !character2.isInAction() &&
                character2.velx == 0     &&
                character2.vely == 0     &&
                !character2.getAnimationState().equals(CharacterAnimationState.Idle))
            character2.changeAnimation(CharacterAnimationState.Idle);
    }

    private boolean intersects(Rectangle a, Rectangle b){
        // (rectOneRight > rectTwoLeft && rectOneLeft < rectTwoRight && rectOneBottom > rectTwoTop && rectOneTop < rectTwoBottom)
        int pointA = a.x + a.y;
        int pointB = b.x + b.y;
        return (pointA + a.width + a.height / 2) > (pointB + b.height / 2)           &&
               (pointA + a.height / 2)           < (pointB + b.width + b.height / 2) &&
               (pointA + a.height + a.width / 2) > (pointB + b.width / 2)            &&
               (pointA + a.width / 2)            < (pointB + b.height + b.width / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        {
            bg.advanceFrame();
            BufferedImage sprite = bg.getCurrentAnimationFrame();
            if (sprite != null){
                g2.drawImage(
                        sprite,
                        0,
                        0,
                        null
                );
            }
        }

        if (!gameState.isPaused()){
            // Player 1
            {
                character1.advanceFrame();
                // Sprite
                BufferedImage sprite = character1.getCurrentFrame();
                if (sprite != null){
                    g2.drawImage(
                            sprite,
                            (int) character1.x,
                            (int) character1.y,
                            null);
                }
                g2.setColor(Color.BLUE);
                // Position
                g2.drawRect((int) character1.x, (int) character1.y, 1, 1);
                // Hitbox
                if (!character1.isAlive())
                    g2.setColor(Color.RED);
                g2.drawRect(
                        (int) character1.x,
                        (int) character1.y,
                        (int) character1.hitbox.w,
                        (int) character1.hitbox.h
                );

                g2.setColor(Color.ORANGE);
                if (character1.isInAction()){
                    Attack hb = character1.getCurrentAttackHitbox();
                    if (hb != null){
                        g2.drawRect(
                                (int) hb.x,
                                (int) hb.y,
                                (int) hb.hitbox.w,
                                (int) hb.hitbox.h
                        );
                    }
                }
            }

            // Player 2
            {
                character2.advanceFrame();
                // Sprite
                BufferedImage sprite = character2.getCurrentFrame();
                if (sprite != null){
                    g2.drawImage(
                            sprite,
                            (int) character2.x,
                            (int) character2.y,
                            null);
                }
                g2.setColor(Color.BLUE);
                // Position
                g2.drawRect((int) character2.x, (int) character2.y, 1, 1);
                // Hitbox
                if (!character2.isAlive())
                    g2.setColor(Color.RED);
                g2.drawRect(
                        (int) character2.x,
                        (int) character2.y,
                        (int) character2.hitbox.w,
                        (int) character2.hitbox.h
                );

                g2.setColor(Color.ORANGE);
                if (character2.isInAction()){
                    Attack hb = character2.getCurrentAttackHitbox();
                    if (hb != null){
                        g2.drawRect(
                                (int) hb.x,
                                (int) hb.y,
                                (int) hb.hitbox.w,
                                (int) hb.hitbox.h
                        );
                    }
                }
            }
        } else {
            g2.drawImage(pauseScreen, null, 0, 0);
            Font font = g2.getFont();
            font = font.deriveFont(AffineTransform.getScaleInstance(6,6));
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString(
                    "Pause",
                    (int) (gameState.getWindowWidth() / 1.3),
                    gameState.getWindowHeight() / 10);
        }

        // Ground
        // g2.drawLine(0, (int) GROUND, WIDTH, (int) GROUND);
    }
}
