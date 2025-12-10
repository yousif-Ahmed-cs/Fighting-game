//
//
//
//
//
//
//import com.sun.opengl.util.*;
//import java.awt.*;
//import javax.media.opengl.*;
//import javax.swing.*;
//
//public class Anim extends JFrame {
//
//    public static void main(String[] args) {
//        new Anim();
//    }
//
//
//    public Anim() {
//        GLCanvas glcanvas;
//        Animator animator;
//
//        AnimListener listener = new AnimGLEventListener4();
//        glcanvas = new GLCanvas();
//        glcanvas.addGLEventListener(listener);
//        glcanvas.addKeyListener(listener);
////        glcanvas.addMouseListener(this);
//        glcanvas.addMouseListener(listener);
////        glcanvas.addMouseMotionListener(listener);
////        glcanvas.addMouseMotionListener(this);
//        getContentPane().add(glcanvas, BorderLayout.CENTER);
//        animator = new FPSAnimator(30);
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
