//
//
//
//import com.sun.opengl.util.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import javax.media.opengl.*;
//import javax.swing.*;
//public class mygame extends JFrame {
//
//
//    public static void main(String[] args) {
//        new mygame();
//    }
//    public mygame() {
//        GLCanvas glcanvas;
//        Animator animator;
//
//        AnimListener listener = new AnimListener() {
//            @Override
//            public void init(GLAutoDrawable glAutoDrawable) {
//
//            }
//
//            @Override
//            public void display(GLAutoDrawable glAutoDrawable) {
//
//            }
//
//            @Override
//            public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
//
//            }
//
//            @Override
//            public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
//
//            }
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//
//            }
//        };
//        glcanvas = new GLCanvas();
//        glcanvas.addGLEventListener(listener);
//        glcanvas.addKeyListener(listener);
//        getContentPane().add(glcanvas, BorderLayout.CENTER);
//        animator = new FPSAnimator(15);
//        animator.add(glcanvas);
//        animator.start();
//
//        setTitle("Anim Test");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(700, 700);
//        setLocationRelativeTo(null);
//        setVisible(true);
//        setFocusable(true);
//        glcanvas.requestFocus();
//    }
//}
