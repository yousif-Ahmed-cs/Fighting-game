import java.awt.event.*;
import javax.media.opengl.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
// تأكد من وجود هذه الاستيرادات للعمل مع الهيكلية الجديدة
// import Player;
// import TextureManager;
import Texture.Texture;

public class AnimGLEventListener4 extends AnimListener implements MouseListener {

    // Add these AI variables with other class variables
    private boolean aiEnabled = true;  // Set to false for 2-player mode
    private int aiThinkTimer = 0;
    private int aiThinkDelay = 30;  // AI makes decisions every 30 frames
    private String aiCurrentAction = "idle";  // idle, chase, attack, retreat
    private float aiAttackRange = 100;
    private float aiChaseRange = 300;



    int animationIndex = 0;
    int framecounter = 0;
    int maxWidth = 800;
    int maxHeight = 800;
    // Player 1 starts on the LEFT side
    float x = 100;  // Left side position
    float y = maxHeight / 2f;  // Middle height

    // Player 2 starts on the RIGHT side
    float x2 = maxWidth - 100;  // Right side position
    float y2 = maxHeight / 2f;  // Middle height
    int maxHealth = 100;
    int player1Health = 100;
    int player2Health = 100;
    // Add these variables with other class variables
    private boolean gameOver = false;
    private String winner = "";

    private Texture gameOverBoxTexture;
    private Texture player1WinsTexture;
    private Texture player2WinsTexture;
    Player player;
    Player player2;
    TextureManager textureManager = new TextureManager();

    boolean isPlaying = false;

    // تأكد من أن مسار الأصول صحيح في TextureManager
    String[] playerNames = {"0_Golem_Walking_000.png", "0_Golem_Walking_001.png", "0_Golem_Walking_002.png", "0_Golem_Walking_003.png", "0_Golem_Walking_004.png",
            "0_Golem_Walking_005.png", "0_Golem_Walking_006.png", "0_Golem_Walking_007.png", "0_Golem_Walking_008.png", "0_Golem_Walking_009.png", "0_Golem_Walking_010.png"
            , "0_Golem_Walking_011.png", "0_Golem_Walking_012.png", "0_Golem_Walking_013.png", "0_Golem_Walking_014.png", "0_Golem_Walking_015.png", "0_Golem_Walking_016.png", "0_Golem_Walking_017.png"
            , "0_Golem_Walking_018.png", "0_Golem_Walking_019.png", "0_Golem_Walking_020.png", "0_Golem_Walking_021.png", "0_Golem_Walking_022.png"};

    String[] playerFightNames = {"0_Golem_Run Slashing_000.png", "0_Golem_Run Slashing_001.png", "0_Golem_Run Slashing_002.png", "0_Golem_Run Slashing_003.png", "0_Golem_Run Slashing_004.png"
            , "0_Golem_Run Slashing_005.png", "0_Golem_Run Slashing_006.png", "0_Golem_Run Slashing_007.png", "0_Golem_Run Slashing_008.png", "0_Golem_Run Slashing_009.png", "0_Golem_Run Slashing_010.png"};

    // Add jumping state variables
    int jumpFrameCounter = 0;
    int jumpFrameCounter2 = 0;
    boolean isJumping = false;      // Player 1
    boolean isJumping2 = false;     // Player 2
    float jumpVelocity = 0;         // Player 1 jump velocity
    float jumpVelocity2 = 0;        // Player 2 jump velocity
    float gravity = -1.0f;          // Gravity force
    float jumpStrength = 15.0f;     // Initial jump force
    float groundLevel = maxHeight / 2f; // Ground position
    float groundLevel2 = maxHeight / 2f; // Ground position for player 2

    String[] playerJumpingNames = {"0_Golem_Jump Start_000.png", "0_Golem_Jump Start_001.png", "0_Golem_Jump Start_002.png", "0_Golem_Jump Start_003.png", "0_Golem_Jump Start_004.png", "0_Golem_Jump Start_005.png"};


    // Add these at the top of the class (replace the single isFighting variable)
    int fightFrameCounter = 0;
    int fightFrameCounter2 = 0;
    boolean isFighting = false;      // Player 1
    boolean isFighting2 = false;     // Player 2
    private String[] backgroundNames = {
            "battleback1.png" // المستوى الثالث (تغيير الاسم حسب ملفك)
            , "battleback2.png"
            , "battleback3.png"
            , "battleback4.png"
            , "battleback5.png"
            , "battleback6.png"
            , "battleback7.png"
            , "battleback8.png"
            , "battleback9.png"
            , "battleback10.png"
    };
    private List<Texture> backgroundsList = new ArrayList<>();

    // 3. مؤشر (Index) لتحديد الخلفية/المستوى الحالي
    private int currentLevelIndex = 0;

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
        gameOverBoxTexture=textureManager.getTexture(gl, "gameover.png");
        player1WinsTexture = textureManager.getTexture(gl, "player1wins.png");
        player2WinsTexture = textureManager.getTexture(gl, "player2wins.png");

        for (String bgName : backgroundNames) {
            Texture tex = textureManager.getTexture(gl, bgName);
            if (tex != null) {
                backgroundsList.add(tex);
            }
        }

        // CREATE PLAYERS FIRST (this was missing or in wrong order)
        player = new Player(x, y, 16, 40, 5.0f);
        player2 = new Player(x2, y2, 20, 40, 6.0f);

        // Store ground levels AFTER creating players
        groundLevel = y;
        groundLevel2 = y2;

        // NOW you can call setFacingRight (AFTER players are created)
        player.setFacingRight(true);   // Player 1 faces RIGHT
        player2.setFacingRight(false); // Player 2 faces LEFT

        // Load textures
        player.loadSkins(gl, textureManager, playerNames);
        player2.loadSkins(gl, textureManager, playerNames);

        player.loadFightingSkins(gl, textureManager, playerFightNames);
        player2.loadFightingSkins(gl, textureManager, playerFightNames);

        // Load jumping animations
        player.loadJumpingSkins(gl, textureManager, playerJumpingNames);
        player2.loadJumpingSkins(gl, textureManager, playerJumpingNames);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 800, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        drawBackground(gl);

        player.draw(gl);
        player2.draw(gl);
        checkCollisionAndDamage(); // Check for damage
        drawHealthBars(gl); // Draw health bars on top

        drawWinnerMessage(gl); // Draw winner overlay if game is over

        if (!gameOver) { // Only handle input if game is not over
            handleKeyPress();
        }
        // ... (بقية منطق القائمة/الخلفية هنا إذا كنت تستخدمه)
    }

    private void drawBackground(GL gl) {
        // التأكد من أن الفهرس الحالي صالح وأن القائمة ليست فارغة
        if (backgroundsList.isEmpty() || currentLevelIndex < 0 || currentLevelIndex >= backgroundsList.size()) {
            return;
        }

        // الحصول على كائن Texture الحالي من القائمة
        Texture currentBackground = backgroundsList.get(currentLevelIndex);

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, currentBackground.getId());

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(maxWidth, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(maxWidth, maxHeight, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0.0f, maxHeight, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    private void drawHealthBars(GL gl) {
        gl.glDisable(GL.GL_TEXTURE_2D); // Disable textures for drawing shapes

        // Player 1 Health Bar (Bottom Left)
        float healthBarWidth = 200;
        float healthBarHeight = 30;
        float healthBarX = 20;
        float healthBarY = 20;

        // Background (Red - represents lost health)
        gl.glColor3f(0.8f, 0.2f, 0.2f); // Dark red
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(healthBarX, healthBarY);
        gl.glVertex2f(healthBarX + healthBarWidth, healthBarY);
        gl.glVertex2f(healthBarX + healthBarWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX, healthBarY + healthBarHeight);
        gl.glEnd();

        // Foreground (Green - current health)
        float player1HealthWidth = (player1Health / (float) maxHealth) * healthBarWidth;
        gl.glColor3f(0.2f, 0.8f, 0.2f); // Green
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(healthBarX, healthBarY);
        gl.glVertex2f(healthBarX + player1HealthWidth, healthBarY);
        gl.glVertex2f(healthBarX + player1HealthWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX, healthBarY + healthBarHeight);
        gl.glEnd();

        // Border
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Black border
        gl.glLineWidth(3);
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2f(healthBarX, healthBarY);
        gl.glVertex2f(healthBarX + healthBarWidth, healthBarY);
        gl.glVertex2f(healthBarX + healthBarWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX, healthBarY + healthBarHeight);
        gl.glEnd();

        // Player 2 Health Bar (Bottom Right)
        float healthBarX2 = maxWidth - healthBarWidth - 20;

        // Background (Red)
        gl.glColor3f(0.8f, 0.2f, 0.2f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(healthBarX2, healthBarY);
        gl.glVertex2f(healthBarX2 + healthBarWidth, healthBarY);
        gl.glVertex2f(healthBarX2 + healthBarWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX2, healthBarY + healthBarHeight);
        gl.glEnd();

        // Foreground (Green - current health)
        float player2HealthWidth = (player2Health / (float) maxHealth) * healthBarWidth;
        gl.glColor3f(0.2f, 0.8f, 0.2f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(healthBarX2, healthBarY);
        gl.glVertex2f(healthBarX2 + player2HealthWidth, healthBarY);
        gl.glVertex2f(healthBarX2 + player2HealthWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX2, healthBarY + healthBarHeight);
        gl.glEnd();

        // Border
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glLineWidth(3);
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2f(healthBarX2, healthBarY);
        gl.glVertex2f(healthBarX2 + healthBarWidth, healthBarY);
        gl.glVertex2f(healthBarX2 + healthBarWidth, healthBarY + healthBarHeight);
        gl.glVertex2f(healthBarX2, healthBarY + healthBarHeight);
        gl.glEnd();

        gl.glEnable(GL.GL_TEXTURE_2D); // Re-enable textures
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Reset color to white
    }

    // Add these variables at the top of the class
    private boolean player1HasHit = false;  // Track if player 1 already dealt damage this attack
    private boolean player2HasHit = false;  // Track if player 2 already dealt damage this attack

    private void checkCollisionAndDamage() {
        // Don't process damage if game is over
        if (gameOver) {
            return;
        }

        // Calculate distance between players
        float dx = player.getX() - player2.getX();
        float dy = player.getY() - player2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // If players are close enough (within attack range)
        float attackRange = 100;

        // Player 1 attacks Player 2
        if (isFighting && distance < attackRange) {
            if (!player1HasHit) {
                // Random damage between 10 and 15
                int damage = 10 + (int) (Math.random() * 6);
                player2Health -= damage;
                if (player2Health < 0) player2Health = 0;
                player1HasHit = true;
                System.out.println("Player 1 hit for " + damage + " damage! Player 2 Health: " + player2Health);

                // Check if Player 2 is defeated
                if (player2Health <= 0) {
                    gameOver = true;
                    winner = "Player 1 Wins!";
                    System.out.println(winner);
                }
            }
        }

        // Reset hit flag when player 1 stops fighting
        if (!isFighting) {
            player1HasHit = false;
        }

        // Player 2 attacks Player 1
        if (isFighting2 && distance < attackRange) {
            if (!player2HasHit) {
                // Random damage between 10 and 15
                int damage = 10 + (int) (Math.random() * 6);
                player1Health -= damage;
                if (player1Health < 0) player1Health = 0;
                player2HasHit = true;
                System.out.println("Player 2 hit for " + damage + " damage! Player 1 Health: " + player1Health);

                // Check if Player 1 is defeated
                if (player1Health <= 0) {
                    gameOver = true;
                    winner = "Player 2 Wins!";
                    System.out.println(winner);
                }
            }
        }

        // Reset hit flag when player 2 stops fighting
        if (!isFighting2) {
            player2HasHit = false;
        }
    }


    private void drawWinnerMessage(GL gl) {
        if (!gameOver) {
            return;
        }

        // Draw semi-transparent overlay
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.7f); // Black overlay
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(maxWidth, 0);
        gl.glVertex2f(maxWidth, maxHeight);
        gl.glVertex2f(0, maxHeight);
        gl.glEnd();

        // Enable textures for drawing the image
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f); // White (so image shows true colors)

        // Draw the game over box image
        float boxWidth = 500;
        float boxHeight = 250;
        float boxX = (maxWidth - boxWidth) / 2;
        float boxY = (maxHeight - boxHeight) / 2;

        // Determine which winner image to show
        Texture winnerTexture = winner.equals("Player 1 Wins!") ? player1WinsTexture : player2WinsTexture;

        if (winnerTexture != null) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, winnerTexture.getId());

            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(boxX, boxY);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(boxX + boxWidth, boxY);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(boxX + boxWidth, boxY + boxHeight);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(boxX, boxY + boxHeight);
            gl.glEnd();
        }
    }


    private void updateAI() {
        if (!aiEnabled || gameOver) {
            return;
        }

        // AI thinks periodically, not every frame
        aiThinkTimer++;
        if (aiThinkTimer < aiThinkDelay) {
            return;
        }
        aiThinkTimer = 0;

        // Calculate distance to player 1
        float dx = player.getX() - player2.getX();
        float dy = player.getY() - player2.getY();
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        // Decision making based on distance and health
        if (distance < aiAttackRange && !isJumping2 && !isFighting2) {
            // Close enough to attack
            aiCurrentAction = "attack";
        } else if (distance < aiChaseRange) {
            // Chase the player
            aiCurrentAction = "chase";
        } else if (player2Health < 30 && player1Health > player2Health) {
            // Low health, try to retreat
            aiCurrentAction = "retreat";
        } else {
            // Move randomly or chase
            aiCurrentAction = Math.random() > 0.3 ? "chase" : "idle";
        }

        // Execute AI action
        executeAIAction(dx, dy, distance);
    }

    private void executeAIAction(float dx, float dy, float distance) {
        float moveSpeed = 8.0f;  // AI moves slightly slower

        switch (aiCurrentAction) {
            case "attack":
                // Attack if not already attacking
                if (!isFighting2 && !isJumping2) {
                    isFighting2 = true;
                    fightFrameCounter2 = 0;
                    player2.startFighting();
                }
                break;

            case "chase":
                // Move toward player 1
                if (!isFighting2) {
                    // Move horizontally toward player
                    if (Math.abs(dx) > 50) {  // Don't get too close
                        if (dx > 0 && player2.getX() < maxWidth - player2.getWidth() / 2) {
                            player2.move(moveSpeed, 0);  // Move right
                        } else if (dx < 0 && player2.getX() > player2.getWidth() / 2) {
                            player2.move(-moveSpeed, 0);  // Move left
                        }
                    }

                    // Move vertically toward player
                    if (Math.abs(dy) > 50) {
                        if (dy > 0 && player2.getY() < maxHeight - player2.getHeight() / 2) {
                            player2.move(0, moveSpeed);  // Move up
                        } else if (dy < 0 && player2.getY() > player2.getHeight() / 2) {
                            player2.move(0, -moveSpeed);  // Move down
                        }
                    }

                    // Random jump while chasing
                    if (Math.random() > 0.95 && !isJumping2) {
                        isJumping2 = true;
                        jumpFrameCounter2 = 0;
                        jumpVelocity2 = jumpStrength;
                        player2.startJumping();
                    }
                }
                break;

            case "retreat":
                // Move away from player 1
                if (!isFighting2) {
                    if (dx > 0 && player2.getX() > player2.getWidth() / 2) {
                        player2.move(-moveSpeed, 0);  // Move left (away)
                    } else if (dx < 0 && player2.getX() < maxWidth - player2.getWidth() / 2) {
                        player2.move(moveSpeed, 0);  // Move right (away)
                    }

                    // Jump to escape
                    if (Math.random() > 0.9 && !isJumping2) {
                        isJumping2 = true;
                        jumpFrameCounter2 = 0;
                        jumpVelocity2 = jumpStrength;
                        player2.startJumping();
                    }
                }
                break;

            case "idle":
                // Stand still or move randomly
                if (Math.random() > 0.8) {
                    int randomMove = (int)(Math.random() * 4);
                    switch (randomMove) {
                        case 0: // Move left
                            if (player2.getX() > player2.getWidth() / 2) {
                                player2.move(-moveSpeed, 0);
                            }
                            break;
                        case 1: // Move right
                            if (player2.getX() < maxWidth - player2.getWidth() / 2) {
                                player2.move(moveSpeed, 0);
                            }
                            break;
                        case 2: // Jump
                            if (!isJumping2) {
                                isJumping2 = true;
                                jumpFrameCounter2 = 0;
                                jumpVelocity2 = jumpStrength;
                                player2.startJumping();
                            }
                            break;
                    }
                }
                break;
        }
    }

    // ... reshape, displayChanged ...

    public void handleKeyPress() {
        boolean moving = false;
        boolean moving2 = false;
        float moveSpeed = 10.0f;

        // Update AI (this controls Player 2 automatically)
        updateAI();

        // Check if B is pressed for Player 1 fighting
        if (isKeyPressed(KeyEvent.VK_B)) {
            if (!isFighting && !isJumping) {
                isFighting = true;
                fightFrameCounter = 0;
                player.startFighting();
            }
        }

        // Player 2 controls ONLY work if AI is disabled
        if (!aiEnabled) {
            // Check if P is pressed for Player 2 fighting
            if (isKeyPressed(KeyEvent.VK_P)) {
                if (!isFighting2 && !isJumping2) {
                    isFighting2 = true;
                    fightFrameCounter2 = 0;
                    player2.startFighting();
                }
            }

            // Check if W is pressed for Player 2 jumping
            if (isKeyPressed(KeyEvent.VK_W)) {
                if (!isJumping2 && !isFighting2) {
                    isJumping2 = true;
                    jumpFrameCounter2 = 0;
                    jumpVelocity2 = jumpStrength;
                    player2.startJumping();
                }
            }

            // Movement controls for player 2 (WASD)
            if (isKeyPressed(KeyEvent.VK_A)) {
                if (player2.getX() > player2.getWidth() / 2) {
                    player2.move(-moveSpeed, 0);
                    moving2 = true;
                }
            }

            if (isKeyPressed(KeyEvent.VK_D)) {
                if (player2.getX() < maxWidth - player2.getWidth() / 2) {
                    player2.move(moveSpeed, 0);
                    moving2 = true;
                }
            }

            if (isKeyPressed(KeyEvent.VK_S)) {
                if (player2.getY() > player2.getHeight() / 2) {
                    player2.move(0, -moveSpeed);
                    moving2 = true;
                }
            }
        }

        // Check if UP arrow is pressed for Player 1 jumping
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (!isJumping && !isFighting) {
                isJumping = true;
                jumpFrameCounter = 0;
                jumpVelocity = jumpStrength;
                player.startJumping();
            }
        }

        // Update Player 1 fighting animation
        if (isFighting) {
            fightFrameCounter++;
            if (fightFrameCounter >= 3) {
                fightFrameCounter = 0;
                boolean finished1 = player.updateFighting();

                if (finished1) {
                    isFighting = false;
                    player.stopFighting();
                }
            }
        }

        // Update Player 2 fighting animation
        if (isFighting2) {
            fightFrameCounter2++;
            if (fightFrameCounter2 >= 3) {
                fightFrameCounter2 = 0;
                boolean finished2 = player2.updateFighting();

                if (finished2) {
                    isFighting2 = false;
                    player2.stopFighting();
                }
            }
        }

        // Update Player 1 jumping physics and animation
        if (isJumping) {
            jumpVelocity += gravity;
            player.move(0, jumpVelocity);

            jumpFrameCounter++;
            if (jumpFrameCounter >= 3) {
                jumpFrameCounter = 0;
                player.updateJumping();
            }

            if (player.getY() <= groundLevel) {
                player.move(0, groundLevel - player.getY());
                isJumping = false;
                jumpVelocity = 0;
                player.stopJumping();
            }
        }

        // Update Player 2 jumping physics and animation
        if (isJumping2) {
            jumpVelocity2 += gravity;
            player2.move(0, jumpVelocity2);

            jumpFrameCounter2++;
            if (jumpFrameCounter2 >= 3) {
                jumpFrameCounter2 = 0;
                player2.updateJumping();
            }

            if (player2.getY() <= groundLevel2) {
                player2.move(0, groundLevel2 - player2.getY());
                isJumping2 = false;
                jumpVelocity2 = 0;
                player2.stopJumping();
            }
        }

        // Movement controls for player 1 (Arrow keys)
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (player.getX() > player.getWidth() / 2) {
                player.move(-moveSpeed, 0);
                moving = true;
            }
        }

        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (player.getX() < maxWidth - player.getWidth() / 2) {
                player.move(moveSpeed, 0);
                moving = true;
            }
        }

        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (player.getY() > player.getHeight() / 2) {
                player.move(0, -moveSpeed);
                moving = true;
            }
        }

        // Update walking animation for Player 1 (only if not fighting or jumping)
        if (!isFighting && !isJumping && moving) {
            framecounter++;
            if (framecounter >= 3) {
                framecounter = 0;
                player.switchImage();
            }
        }

        // Update walking animation for Player 2 (only if not fighting or jumping)
        // In AI mode, check if AI is moving the player
        if (!isFighting2 && !isJumping2) {
            // Check if player2 position changed (AI moved it)
            if (aiEnabled && aiCurrentAction.equals("chase") || aiCurrentAction.equals("retreat")) {
                framecounter++;
                if (framecounter >= 3) {
                    framecounter = 0;
                    player2.switchImage();
                }
            } else if (!aiEnabled && moving2) {
                // Manual control animation
                framecounter++;
                if (framecounter >= 3) {
                    framecounter = 0;
                    player2.switchImage();
                }
            }
        }

        // Background change with SPACE
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (!backgroundsList.isEmpty()) {
                if (!spacePressedLastFrame) {
                    currentLevelIndex = (currentLevelIndex + 1) % backgroundsList.size();
                    System.out.println("Changed background to level: " + (currentLevelIndex + 1));
                    spacePressedLastFrame = true;
                }
            }
        } else {
            spacePressedLastFrame = false;
        }
    }

    private boolean spacePressedLastFrame = false;

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    /**
     * الدالة المطلوبة تم تعديلها لتكون جزءاً من الكلاس.
     *
     * @param keyCode كود الزر المراد فحصه.
     * @return true إذا كان الزر مضغوطاً حالياً، false عدا ذلك.
     */
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    // --- تنفيذ دوال الماوس ---
    @Override
    public void mouseClicked(MouseEvent e) {
        // ... (بقية كود الماوس هنا) ...
    }

    // الدوال الأخرى الفارغة المطلوبة من MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
