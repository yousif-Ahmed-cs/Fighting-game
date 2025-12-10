import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MasterChatGame extends JFrame {
    SoundManager soundManager = new SoundManager();
    // Game states
    private enum GameState {
        MAIN_MENU,
        PLAYER_SELECT,
        LEVEL_SELECT,
        INFO
    }

    private GameState currentState = GameState.MAIN_MENU;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Buttons
    private JButton exitBtn;
    private JButton newGameBtn;
    private JButton infoBtn;
    private JButton backBtn;
    private JButton singlePlayerBtn;
    private JButton multiPlayerBtn;
    private JButton level1Btn;
    private JButton level2Btn;
    private JButton level3Btn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MasterChatGame());
    }

    public MasterChatGame() {
        initializeFrame();
        createMenuPanels();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("MasterChat");
        setSize(700, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout allows switching between different screens
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

        newGameBtn = createStyledButton("New Game");
        newGameBtn.addActionListener(e -> showScreen("PLAYER_SELECT"));

        infoBtn = createStyledButton("Info");
        infoBtn.addActionListener(e -> showScreen("INFO"));

        exitBtn = createStyledButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        panel.add(newGameBtn, gbc);
        gbc.gridy = 1;
        panel.add(infoBtn, gbc);
        gbc.gridy = 2;
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

        singlePlayerBtn = createStyledButton("Single Player");
        singlePlayerBtn.addActionListener(e -> showScreen("LEVEL_SELECT"));

        multiPlayerBtn = createStyledButton("Multi Player");
        multiPlayerBtn.addActionListener(e -> startMultiPlayerGame());

        backBtn = createStyledButton("Back");
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

        level1Btn = createStyledButton("Level 1 - Easy");
        level1Btn.addActionListener(e -> startGame(1));

        level2Btn = createStyledButton("Level 2 - Medium");
        level2Btn.addActionListener(e -> startGame(2));

        level3Btn = createStyledButton("Level 3 - Hard");
        level3Btn.addActionListener(e -> startGame(3));

        JButton backToPlayerBtn = createStyledButton("Back");
        backToPlayerBtn.addActionListener(e -> showScreen("PLAYER_SELECT"));

        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        gbc.gridy = 1;
        panel.add(level1Btn, gbc);
        gbc.gridy = 2;
        panel.add(level2Btn, gbc);
        gbc.gridy = 3;
        panel.add(level3Btn, gbc);
        gbc.gridy = 4;
        panel.add(backToPlayerBtn, gbc);

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
                        "3. Enjoy the game!\n\n" +
                        "Game by: Your Name"
        );
        infoText.setEditable(false);
        infoText.setFont(new Font("Arial", Font.PLAIN, 16));
        infoText.setBackground(new Color(0, 0, 0, 150));
        infoText.setForeground(Color.WHITE);
        infoText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backToMainBtn = createStyledButton("Back to Menu");
        backToMainBtn.addActionListener(e -> showScreen("MAIN_MENU"));

        panel.add(infoText, BorderLayout.CENTER);
        panel.add(backToMainBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBackgroundPanel() {
        // You can set background image here if you have the file
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set a gradient background as placeholder
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

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });

        return button;
    }

    private void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    private void startGame(int level) {
        JOptionPane.showMessageDialog(this,
                "Starting game at Level " + level + "!\n(Game logic goes here)",
                "Game Started",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void startMultiPlayerGame() {
        JOptionPane.showMessageDialog(this,
                "Starting multiplayer game!\n(Multiplayer logic goes here)",
                "Multiplayer Mode",
                JOptionPane.INFORMATION_MESSAGE);
    }
}