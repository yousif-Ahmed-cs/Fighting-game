

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class PageTab extends JFrame implements ActionListener, MouseMotionListener, MouseListener,
        KeyListener {


    // add line
    private final JPanel jPanel;

    private final JButton jButton1;
    private final JButton jButton2;
    private final JButton jButton3;
    private final JButton jButton4;
    private final JButton jButton5;
    private final JButton jButton6;
    private final JLabel jLabel1;
    private final JRadioButton jRadioButton;
    private final JCheckBox jCheckBox1;
    private final JTextField jTextField1;
    private final JTextArea jTextArea;

    public PageTab() {

        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(4, 3, 50, 50));

        jButton1 = new JButton("jButton1");
        jButton2 = new JButton("jButton2");
        jButton3 = new JButton("jButton3");
        jButton4 = new JButton("jButton4");
        jButton5 = new JButton("jButton5");
        jButton6 = new JButton("jButton6-submit");
        jLabel1 = new JLabel("jLabel1- string - some words");
        jRadioButton = new JRadioButton("jRadioButton");
        jCheckBox1 = new JCheckBox("jCheckBox1");
        jTextField1 = new JTextField("");
        jTextArea = new JTextArea("");

//    setLayout(new FlowLayout(FlowLayout.CENTER, 100, 100));
//    setLayout(new GridLayout(4, 3, 50, 50));
        setLayout(new BorderLayout(50, 50));

//    this.add(jButton1);
//    add(jButton2);
//    add(jButton3);
//    add(jButton4);
//    add(jButton5);
//    add(jLabel1);
//    add(jRadioButton);
//    add(jCheckBox1);
//    add(jTextField1);
//    add(jTextArea);

        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);
        jButton5.addActionListener(this);
        jButton6.addActionListener(this);

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        jPanel.setFocusable(true);
        jPanel.addKeyListener(this);

        jPanel.add(jButton1);
        jPanel.add(jButton2);
        jPanel.add(jButton3);
        jPanel.add(jButton4);
        jPanel.add(jButton5);
        jPanel.add(jButton6);
        jPanel.add(jLabel1);
        jPanel.add(jRadioButton);
        jPanel.add(jCheckBox1);
        jPanel.add(jTextField1);

        this.add(jPanel, BorderLayout.NORTH);
        this.add(jTextArea, BorderLayout.CENTER);

        setTitle("CS304-PageTab");
        setSize(700, 700);
        setLocationRelativeTo(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new PageTab();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(jButton1)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton1 pressed");
            System.out.println("jButton1 pressed");
        } else if (e.getSource().equals(jButton2)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton2 pressed");
            System.out.println("jButton2 pressed");
        } else if (e.getSource().equals(jButton3)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton3 pressed");
            System.out.println("jButton3 pressed");
        } else if (e.getSource().equals(jButton4)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton4 pressed");
            System.out.println("jButton4 pressed");
        } else if (e.getSource().equals(jButton5)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton5 pressed");
            System.out.println("jButton5 pressed");
        } else if (e.getSource().equals(jButton6)) {
            jTextArea.setText(jTextArea.getText() + "\n jButton5 pressed" + jTextField1.getText());
            System.out.println("jButton6 pressed");
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
//    System.out.println("The Mouse now in (x,y)=(" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed now in (x,y)=(" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released now in (x,y)=(" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("KeyEvent");
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("keyPressed=LEFT");
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("keyPressed=UP");
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("keyPressed=RIGHT");
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("keyPressed=DOWN");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
