// src/MasterChatGame2.java

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// --- الإضافات الضرورية هنا ---
import java.util.List;
import java.util.Collections;

public class MasterChatGame2 extends JFrame {
    // Game states
    private enum GameState {
        MAIN_MENU,
        PLAYER_SELECT,
        LEVEL_SELECT,
        INFO,
        HIGH_SCORES,
        PLAYING,
        PAUSED
    }

    private GameState currentState = GameState.MAIN_MENU;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Sound components
    private SoundManager soundManager = new SoundManager();
    private JButton menuSoundButton;
    private JButton gameSoundButton;
    private boolean isSoundMuted = false;

    // Game overlay components
    private JPanel overlayPanel;
    private JLabel timerLabel;
    private int gameTimeSeconds = 180;
    private Timer gameTimer;

    // OpenGL Components
    private GLCanvas glCanvas;
    private Animator animator;
    private AnimGLEventListener4 gameRenderer;

    // Game Settings
    private int selectedLevel = 1;
    private boolean isMultiplayer = false;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";
    private List<HighScoreEntry> highScores; // الآن سيعمل هذا السطر بشكل صحيح
    private static final int MAX_HIGH_SCORES = 10;

    // Menu panels
    private JTextArea highScoreTextArea;
    private JPanel mainMenuWithSound;
    private JPanel playerSelectWithSound;
    private JPanel levelSelectWithSound;
    private JPanel infoWithSound;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MasterChatGame2());
    }

    public MasterChatGame2() {
        highScores = HighScoreManager.loadScores();
        initializeFrame();
        createMenuSoundButton();
        createMenuPanels();
        playMenuSound();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("MasterChat Game");
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // ... (بقية الدوال مثل createMenuSoundButton, createStyledButton, etc. تبقى كما هي)
    // ... (تأكد من وجودها في ملفك)

    // --- تأكد من أن كل الدوال موجودة ---

    private void createMenuSoundButton() {
        menuSoundButton = new JButton("🔊");
        menuSoundButton.setFocusable(false);
        menuSoundButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuSoundButton.setPreferredSize(new Dimension(50, 30));
        menuSoundButton.setBackground(new Color(52, 80, 219));
        menuSoundButton.setForeground(Color.WHITE);
        menuSoundButton.setBorder(BorderFactory.createRaisedBevelBorder());
        menuSoundButton.addActionListener(e -> toggleSound());
    }

    private JPanel createPanelWithSoundButton(JPanel contentPanel) {
        JPanel container = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(menuSoundButton);
        JPanel backgroundPanel = createBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        container.add(backgroundPanel, BorderLayout.CENTER);
        return container;
    }

    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel() {
            private Image backgroundImage = new ImageIcon("Assets/MoM.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 80, 219));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(41, 80, 185));
                playHoverSound();
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(52, 80, 219));
            }
        });
        return button;
    }

    private void playClickSound() {
        if (!isSoundMuted) {
            soundManager.playSound("Sound/punch-04-383965.wav");
        }
    }

    private void playHoverSound() {
        if (!isSoundMuted) {
            soundManager.playSound("Sound/hover.wav");
        }
    }

    private void toggleSound() {
        isSoundMuted = !isSoundMuted;
        if (menuSoundButton != null) {
            menuSoundButton.setText(isSoundMuted ? "🔇" : "🔊");
        }
        if (gameSoundButton != null) {
            gameSoundButton.setText(isSoundMuted ? "UNMUTE" : "MUTE");
        }
        if (isSoundMuted) {
            soundManager.stopBackgroundMusic();
        } else {
            if (currentState == GameState.MAIN_MENU || currentState == GameState.PLAYER_SELECT || currentState == GameState.LEVEL_SELECT || currentState == GameState.INFO || currentState == GameState.PAUSED) {
                playMenuSound();
            } else if (currentState == GameState.PLAYING) {
                playGameSound();
            }
        }
    }

    private void playMenuSound() {
        if (!isSoundMuted) {
            soundManager.playBackgroundMusic("Sound/menu_music.wav");
        }
    }

    private void playGameSound() {
        if (!isSoundMuted) {
            soundManager.playBackgroundMusic("Sound/fight 01.wav");
        }
    }

    private void createGameOverlay() {
        overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        overlayPanel.setBounds(650, 10, 140, 50);
        timerLabel = new JLabel("00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        gameSoundButton = new JButton(isSoundMuted ? "UNMUTE" : "MUTE");
        gameSoundButton.setFocusable(false);
        gameSoundButton.setFont(new Font("Arial", Font.BOLD, 12));
        gameSoundButton.setPreferredSize(new Dimension(60, 30));
        gameSoundButton.setBackground(new Color(52, 80, 219));
        gameSoundButton.setForeground(Color.WHITE);
        gameSoundButton.setBorder(BorderFactory.createRaisedBevelBorder());
        gameSoundButton.addActionListener(e -> toggleSound());
        overlayPanel.add(timerLabel);
        overlayPanel.add(gameSoundButton);
        overlayPanel.setVisible(false);
    }

    private void handleTimeUp() {
        SwingUtilities.invokeLater(() -> {
            currentState = GameState.PAUSED;
            if (animator != null && animator.isAnimating()) {
                animator.stop();
            }
            if (gameRenderer != null) {
                gameRenderer.endGameByTimeUp();
            }
        });
    }

    private void startGameTimer() {
        stopGameTimer();
        gameTimeSeconds = 180;
        updateTimerDisplay();
        gameTimer = new Timer(1000, e -> {
            if (gameTimeSeconds > 0) {
                gameTimeSeconds--;
                updateTimerDisplay();
            } else {
                stopGameTimer();
                handleTimeUp();
            }
        });
        gameTimer.start();
    }

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private void updateTimerDisplay() {
        if (timerLabel != null) {
            int minutes = gameTimeSeconds / 60;
            int seconds = gameTimeSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }

    private JPanel createMainMenuContent() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("MasterChat Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        JButton newGameBtn = createStyledButton("New Game");
        newGameBtn.addActionListener(e -> {
            playClickSound();
            showScreen("PLAYER_SELECT");
        });

        JButton highScoreBtn = createStyledButton("High Scores");
        highScoreBtn.addActionListener(e -> {
            playClickSound();
            updateHighScoreScreen();
            showScreen("HIGH_SCORES");
        });

        JButton infoBtn = createStyledButton("Info");
        infoBtn.addActionListener(e -> {
            playClickSound();
            showScreen("INFO");
        });

        JButton exitBtn = createStyledButton("Exit");
        exitBtn.addActionListener(e -> {
            playClickSound();
            System.exit(0);
        });

        gbc.gridy = 0; contentPanel.add(titleLabel, gbc);
        gbc.gridy = 1; contentPanel.add(newGameBtn, gbc);
        gbc.gridy = 2; contentPanel.add(highScoreBtn, gbc);
        gbc.gridy = 3; contentPanel.add(infoBtn, gbc);
        gbc.gridy = 4; contentPanel.add(exitBtn, gbc);

        return contentPanel;
    }

    private JPanel createPlayerSelectContent() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton singlePlayerBtn = createStyledButton("Single Player");
        singlePlayerBtn.addActionListener(e -> {
            playClickSound();
            String name = JOptionPane.showInputDialog(this, "Enter Your Name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                player1Name = name.trim();
            } else {
                player1Name = "Player 1";
            }
            player2Name = "AI";
            isMultiplayer = false;
            showScreen("LEVEL_SELECT");
        });

        JButton multiPlayerBtn = createStyledButton("Multi Player");
        multiPlayerBtn.addActionListener(e -> {
            playClickSound();
            String name1 = JOptionPane.showInputDialog(this, "Enter Player 1 Name:", "Player 1", JOptionPane.PLAIN_MESSAGE);
            if (name1 == null) return;
            player1Name = (name1.trim().isEmpty()) ? "Player 1" : name1.trim();

            String name2 = JOptionPane.showInputDialog(this, "Enter Player 2 Name:", "Player 2", JOptionPane.PLAIN_MESSAGE);
            if (name2 == null) return;
            player2Name = (name2.trim().isEmpty()) ? "Player 2" : name2.trim();

            isMultiplayer = true;
            showScreen("LEVEL_SELECT");
        });

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> {
            playClickSound();
            showScreen("MAIN_MENU");
        });

        gbc.gridy = 0; contentPanel.add(titleLabel, gbc);
        gbc.gridy = 1; contentPanel.add(singlePlayerBtn, gbc);
        gbc.gridy = 2; contentPanel.add(multiPlayerBtn, gbc);
        gbc.gridy = 3; contentPanel.add(backBtn, gbc);

        return contentPanel;
    }

    private JPanel createLevelSelectContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Select Level", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton level1Btn = createStyledButton("Level 1 - Easy");
        level1Btn.addActionListener(e -> {
            playClickSound();
            startGame(1);
        });

        JButton level2Btn = createStyledButton("Level 2 - Medium");
        level2Btn.addActionListener(e -> {
            playClickSound();
            startGame(2);
        });

        JButton level3Btn = createStyledButton("Level 3 - Hard");
        level3Btn.addActionListener(e -> {
            playClickSound();
            startGame(3);
        });

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> {
            playClickSound();
            showScreen("PLAYER_SELECT");
        });

        gbc.gridy = 0; contentPanel.add(titleLabel, gbc);
        gbc.gridy = 1; contentPanel.add(level1Btn, gbc);
        gbc.gridy = 2; contentPanel.add(level2Btn, gbc);
        gbc.gridy = 3; contentPanel.add(level3Btn, gbc);
        gbc.gridy = 4; contentPanel.add(backBtn, gbc);

        return contentPanel;
    }

    private JPanel createInfoContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        JTextArea infoText = new JTextArea(
                "MasterChat Game\n\n" +
                        "How to Play:\n" +
                        "1. Select single or multiplayer mode\n" +
                        "2. Choose your difficulty level\n" +
                        "3. Use keyboard key ( up , left , right) for moving & press L to punch for player 1\n" +
                        "4. Use keyboard key ( W , A , D) for moving & press C to punch for player 2\n" +
                        "5. Press ESC to pause\n" +
                        "By 1. Mostafa Abdel Mowla\n"+
                        " 2. Yousef Ahmed Hassan\n"+
                        " 3. Genidy Abd Elazeem\n"+
                        " 4. Mostafa Ezzat Abd elaaty \n"+



                        "Good Luck!"
        );
        infoText.setEditable(false);
        infoText.setFont(new Font("Arial", Font.PLAIN, 16));
        infoText.setBackground(new Color(0, 0, 0, 150));
        infoText.setForeground(Color.WHITE);
        infoText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backBtn = createStyledButton("Back to Menu");
        backBtn.addActionListener(e -> {
            playClickSound();
            showScreen("MAIN_MENU");
        });

        contentPanel.add(infoText, BorderLayout.CENTER);
        contentPanel.add(backBtn, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createHighScoreContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        JLabel title = new JLabel("Top Scores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        contentPanel.add(title, BorderLayout.NORTH);

        highScoreTextArea = new JTextArea();
        highScoreTextArea.setEditable(false);
        highScoreTextArea.setFont(new Font("Monospaced", Font.BOLD, 18));
        highScoreTextArea.setBackground(new Color(0, 0, 0, 150));
        highScoreTextArea.setForeground(Color.CYAN);
        highScoreTextArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(highScoreTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = createStyledButton("Back to Menu");
        backBtn.addActionListener(e -> {
            playClickSound();
            showScreen("MAIN_MENU");
        });

        contentPanel.add(backBtn, BorderLayout.SOUTH);
        return contentPanel;
    }

    private void updateHighScoreScreen() {
        if (highScoreTextArea == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %s\n", "Player Name", "Time"));
        sb.append("------------------------------\n");
        if (highScores.isEmpty()) {
            sb.append("\nNo scores recorded yet.");
        } else {
            int rank = 1;
            for (HighScoreEntry entry : highScores) {
                sb.append(String.format("%d. %s\n", rank++, entry.toString()));
            }
        }
        highScoreTextArea.setText(sb.toString());
    }

    private void createMenuPanels() {
        JPanel mainMenuContent = createMainMenuContent();
        JPanel playerSelectContent = createPlayerSelectContent();
        JPanel levelSelectContent = createLevelSelectContent();
        JPanel infoContent = createInfoContent();
        JPanel highScoreContent = createHighScoreContent();

        mainMenuWithSound = createPanelWithSoundButton(mainMenuContent);
        playerSelectWithSound = createPanelWithSoundButton(playerSelectContent);
        levelSelectWithSound = createPanelWithSoundButton(levelSelectContent);
        infoWithSound = createPanelWithSoundButton(infoContent);
        JPanel highScoreWithSound = createPanelWithSoundButton(highScoreContent);

        mainPanel.add(mainMenuWithSound, "MAIN_MENU");
        mainPanel.add(playerSelectWithSound, "PLAYER_SELECT");
        mainPanel.add(levelSelectWithSound, "LEVEL_SELECT");
        mainPanel.add(infoWithSound, "INFO");
        mainPanel.add(highScoreWithSound, "HIGH_SCORES");

        showScreen("MAIN_MENU");
    }

    private void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
        if (menuSoundButton != null) {
            menuSoundButton.setVisible(true);
        }
    }

    private void startGame(int level) {
        selectedLevel = level;
        currentState = GameState.PLAYING;
        if (menuSoundButton != null) {
            menuSoundButton.setVisible(false);
        }
        soundManager.stopBackgroundMusic();
        if (!isSoundMuted) {
            soundManager.playSound("Sound/dark-engine-logo-141942_1.wav");
        }
        if (level == 1) initializeOpenGLGame(10);
        if (level == 2) initializeOpenGLGame(6);
        if (level == 3) initializeOpenGLGame(2);
    }

    private void initializeOpenGLGame(int level) {
        try {
            mainPanel.setVisible(false);
            if (overlayPanel == null) {
                createGameOverlay();
            }
            glCanvas = new GLCanvas();
            glCanvas.setPreferredSize(new Dimension(800, 800));

            gameRenderer = new AnimGLEventListener4(this, !isMultiplayer, level);

            glCanvas.addGLEventListener(gameRenderer);
            glCanvas.addKeyListener(gameRenderer);
            glCanvas.addMouseListener(gameRenderer);
            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        togglePause();
                    } else if (e.getKeyCode() == KeyEvent.VK_M) {
                        toggleSound();
                    }
                }
            });
            animator = new FPSAnimator(glCanvas, 60);
            JLayeredPane gameLayeredPane = new JLayeredPane();
            gameLayeredPane.setPreferredSize(new Dimension(800, 800));
            glCanvas.setBounds(0, 0, 800, 800);
            gameLayeredPane.add(glCanvas, Integer.valueOf(0));
            if (overlayPanel != null) {
                overlayPanel.setVisible(true);
                overlayPanel.setBounds(650, 10, 140, 50);
                gameLayeredPane.add(overlayPanel, Integer.valueOf(1));
            }
            getContentPane().removeAll();
            getContentPane().add(gameLayeredPane, BorderLayout.CENTER);
            revalidate();
            repaint();
            SwingUtilities.invokeLater(() -> {
                animator.start();
                glCanvas.requestFocusInWindow();
                startGameTimer();
                Timer musicTimer = new Timer(500, e -> playGameSound());
                musicTimer.setRepeats(false);
                musicTimer.start();
            });
        } catch (Exception e) {
            System.err.println("Error initializing OpenGL: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error starting game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            returnToMenu();
        }
    }

    public void recordWin(String winnerIdentifier) {
        stopGameTimer();
        int timeTaken = 180 - gameTimeSeconds;
        String winnerName = winnerIdentifier.equals("Player 1") ? player1Name : player2Name;

        if (winnerName.equals("AI")) {
            System.out.println("AI won, score not recorded.");
            return;
        }

        highScores.add(new HighScoreEntry(winnerName, timeTaken));
        Collections.sort(highScores);
        while (highScores.size() > MAX_HIGH_SCORES) {
            highScores.remove(highScores.size() - 1);
        }
        HighScoreManager.saveScores(highScores);
        System.out.println("Score saved for " + winnerName + " with time " + timeTaken + "s");
    }

    private void togglePause() {
        if (currentState == GameState.PLAYING) {
            pauseGame();
        } else if (currentState == GameState.PAUSED) {
            resumeGame();
        }
    }

    private void pauseGame() {
        currentState = GameState.PAUSED;
        if (animator != null && animator.isAnimating()) {
            animator.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
        showPauseMenu();
    }

    private void showPauseMenu() {
        String[] options = {"Resume", "Restart", "Main Menu"};
        int choice = JOptionPane.showOptionDialog(this, "Game Paused\nTime: " + (timerLabel != null ? timerLabel.getText() : "00:00"), "Pause Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            resumeGame();
        } else if (choice == 1) {
            restartGame();
        } else if (choice == 2) {
            returnToMenu();
        } else if (choice == -1) {
            resumeGame();
        }
    }

    private void resumeGame() {
        currentState = GameState.PLAYING;
        if (animator != null && !animator.isAnimating()) {
            animator.start();
        }
        if (gameTimer != null) {
            gameTimer.start();
        }
        if (glCanvas != null) {
            glCanvas.requestFocusInWindow();
        }
    }

    private void restartGame() {
        try {
            cleanupGame();
            TextureManager.clearCache();
            SwingUtilities.invokeLater(() -> startGame(selectedLevel));
        } catch (Exception e) {
            System.err.println("Error restarting game: " + e.getMessage());
            e.printStackTrace();
            returnToMenu();
        }
    }

    private void cleanupGame() {
        stopGameTimer();
        if (animator != null) {
            if (animator.isAnimating()) {
                animator.stop();
            }
            animator = null;
        }
        glCanvas = null;
        gameRenderer = null;
        soundManager.stopBackgroundMusic();
    }

    private void returnToMenu() {
        try {
            System.out.println("Returning to menu...");
            cleanupGame();
            TextureManager.clearCache();
            getContentPane().removeAll();
            getContentPane().add(mainPanel, BorderLayout.CENTER);
            mainPanel.setVisible(true);
            cardLayout.show(mainPanel, "MAIN_MENU");
            currentState = GameState.MAIN_MENU;
            if (menuSoundButton != null) {
                menuSoundButton.setVisible(true);
            }
            revalidate();
            repaint();
            playMenuSound();
            System.out.println("Menu restored");
        } catch (Exception e) {
            System.err.println("Error returning to menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
