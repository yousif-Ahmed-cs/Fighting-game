import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MasterChatGame extends JFrame {
    SoundManager soundManager = new SoundManager();

    // Game states
    private enum GameState {
        MAIN_MENU,
        PLAYER_SELECT,
        LEVEL_SELECT,
        INFO,
        PLAYING
    }

    private GameState currentState = GameState.MAIN_MENU;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // OpenGL Components
    private GLCanvas glCanvas;
    private Animator animator;
    private AnimGLEventListener4 gameRenderer;

    // Game Settings
    private int selectedLevel = 1;
    private boolean isMultiplayer = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MasterChatGame());
    }

    public MasterChatGame() {
        initializeFrame();
        createMenuPanels();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("MasterChat Game");
        setSize(800, 800);  // غيرنا الحجم لـ 800x800 علشان يتناسب مع OpenGL
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);  // منع تغيير الحجم

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setContentPane(mainPanel);
    }

    private void createMenuPanels() {
        // Create all menu screens
        mainPanel.add(createMainMenuPanel(), "MAIN_MENU");
        mainPanel.add(createPlayerSelectPanel(), "PLAYER_SELECT");
        mainPanel.add(createLevelSelectPanel(), "LEVEL_SELECT");
        mainPanel.add(createInfoPanel(), "INFO");

        // Show main menu first
        showScreen("MAIN_MENU");
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = createBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("MasterChat", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        JButton newGameBtn = createStyledButton("New Game");
        newGameBtn.addActionListener(e -> showScreen("PLAYER_SELECT"));

        JButton infoBtn = createStyledButton("Info");
        infoBtn.addActionListener(e -> showScreen("INFO"));

        JButton exitBtn = createStyledButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(newGameBtn, gbc);
        gbc.gridy = 2;
        panel.add(infoBtn, gbc);
        gbc.gridy = 3;
        panel.add(exitBtn, gbc);

        return panel;
    }

    private JPanel createPlayerSelectPanel() {
        JPanel panel = createBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton singlePlayerBtn = createStyledButton("Single Player");
        singlePlayerBtn.addActionListener(e -> {
            isMultiplayer = false;
            showScreen("LEVEL_SELECT");
        });

        JButton multiPlayerBtn = createStyledButton("Multi Player");
        multiPlayerBtn.addActionListener(e -> {
            isMultiplayer = true;
            showScreen("LEVEL_SELECT");
        });

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> showScreen("MAIN_MENU"));

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(singlePlayerBtn, gbc);
        gbc.gridy = 2;
        panel.add(multiPlayerBtn, gbc);
        gbc.gridy = 3;
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createLevelSelectPanel() {
        JPanel panel = createBackgroundPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Select Level", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JButton level1Btn = createStyledButton("Level 1 - Easy");
        level1Btn.addActionListener(e -> startGame(1));

        JButton level2Btn = createStyledButton("Level 2 - Medium");
        level2Btn.addActionListener(e -> startGame(2));

        JButton level3Btn = createStyledButton("Level 3 - Hard");
        level3Btn.addActionListener(e -> startGame(3));

        JButton backBtn = createStyledButton("Back");
        backBtn.addActionListener(e -> showScreen("PLAYER_SELECT"));

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(level1Btn, gbc);
        gbc.gridy = 2;
        panel.add(level2Btn, gbc);
        gbc.gridy = 3;
        panel.add(level3Btn, gbc);
        gbc.gridy = 4;
        panel.add(backBtn, gbc);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = createBackgroundPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JTextArea infoText = new JTextArea(
                "MasterChat Game\n\n" +
                        "How to Play:\n" +
                        "1. Select single or multiplayer mode\n" +
                        "2. Choose your difficulty level\n" +
                        "3. Use keyboard and mouse to play\n" +
                        "4. Press ESC to return to menu\n\n" +
                        "Controls:\n" +
                        "- Arrow Keys: Move\n" +
                        "- Mouse: Interact\n" +
                        "- ESC: Pause/Menu\n\n" +
                        "Good Luck!"
        );
        infoText.setEditable(false);
        infoText.setFont(new Font("Arial", Font.PLAIN, 16));
        infoText.setBackground(new Color(0, 0, 0, 150));
        infoText.setForeground(Color.WHITE);
        infoText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backBtn = createStyledButton("Back to Menu");
        backBtn.addActionListener(e -> showScreen("MAIN_MENU"));

        panel.add(infoText, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(41, 128, 185),
                        0, getHeight(), new Color(109, 213, 250)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        return panel;
    }

    private JButton createStyledButton(String text) {

        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });

        return button;
    }

    private void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    private void startGame(int level) {
        selectedLevel = level;
        currentState = GameState.PLAYING;

        System.out.println("Starting game at level: " + level);  // للتأكد إن الدالة اتنفذت

        // أوقف موسيقى المنيو
        try {
            soundManager.stopBackgroundMusic();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
        if (level == 1) {
            initializeOpenGLGame(10);
        }
        if (level == 2) {
            initializeOpenGLGame(6);
        }
        if (level == 3) {
            initializeOpenGLGame(2);
        }
        // شغل اللعبة بـ OpenGL
    }

    private void initializeOpenGLGame(int level) {
        try {
            System.out.println("Initializing OpenGL Game...");

            // امسح كل حاجة من الـ mainPanel
            mainPanel.removeAll();

            // اعمل GLCanvas جديد
            glCanvas = new GLCanvas();
            glCanvas.setPreferredSize(new Dimension(800, 800));

            // اعمل instance من AnimGLEventListener4
            gameRenderer = new AnimGLEventListener4( level,!isMultiplayer);

            System.out.println("Game renderer created");

            // ضيف الـ Event Listeners
            glCanvas.addGLEventListener(gameRenderer);
            glCanvas.addKeyListener(gameRenderer);
            glCanvas.addMouseListener(gameRenderer);

            // ضيف ESC listener للرجوع للمنيو
            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.out.println("ESC pressed - returning to menu");

                        returnToMenu();
                    }
                }
            });

            System.out.println("Event listeners added");

            // شغل الـ Animator
            animator = new FPSAnimator(glCanvas, 30);
            animator.start();

            System.out.println("Animator started");

            // ضيف الـ Canvas للـ Frame
            getContentPane().removeAll();
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(glCanvas, BorderLayout.CENTER);

            // تأكد إن كل حاجة اتحدثت
            revalidate();
            repaint();

            System.out.println("Canvas added to frame");

            // اعمل focus للـ canvas علشان الكيبورد يشتغل
            SwingUtilities.invokeLater(() -> {
                glCanvas.requestFocusInWindow();
                System.out.println("Focus requested");
            });

        } catch (Exception e) {
            System.err.println("Error initializing OpenGL: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error starting game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnToMenu() {
        try {
            System.out.println("Returning to menu...");

            // أوقف الـ Animator
            if (animator != null && animator.isAnimating()) {
                animator.stop();
                animator = null;
            }

            // امسح الـ GLCanvas
            if (glCanvas != null) {
                getContentPane().remove(glCanvas);
                glCanvas = null;
            }

            // أوقف موسيقى اللعبة وشغل موسيقى المنيو
            try {
                soundManager.stopBackgroundMusic();
                soundManager.playBackgroundMusic("assets//menuMusic.wav");
            } catch (Exception e) {
                System.out.println("Sound error on return: " + e.getMessage());
            }

            // ارجع للمنيو
            getContentPane().removeAll();
            mainPanel = new JPanel(cardLayout);
            setContentPane(mainPanel);
            createMenuPanels();

            revalidate();
            repaint();

            currentState = GameState.MAIN_MENU;
            System.out.println("Menu restored");

        } catch (Exception e) {
            System.err.println("Error returning to menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}