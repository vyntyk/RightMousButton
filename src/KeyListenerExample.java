import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class KeyListenerExample extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    private int x = 0; // initial X position of the image
    private int y = 25; // initial Y position of the image
    private BufferedImage image;
    private BufferedImage cursorImage;
    private int moveSpeed = 10; // normal movement speed
    private List<int[]> imagePositions; // List to hold positions of images
    private int[] draggingImage = null; // Reference to the currently dragged image
    private int dragOffsetX = 0; // X offset of the mouse relative to the top-left corner of the image
    private int dragOffsetY = 0; // Y offset of the mouse relative to the top-left corner of the image

    public KeyListenerExample() {
        setTitle("Moving Image and Mouse Interaction");
        setSize(600, 605);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            // Load the image from a PNG file
            image = ImageIO.read(new File("src\\heart.png"));
            // Load the custom cursor image
            cursorImage = ImageIO.read(new File("src\\cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom cursor
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "Custom Cursor");
        setCursor(customCursor); // Set the custom cursor

        imagePositions = new ArrayList<>();
        imagePositions.add(new int[]{x, y}); // Add initial image position

        addKeyListener(this); // Register KeyListener on the frame
        addMouseListener(this); // Register MouseListener on the frame
        addMouseMotionListener(this); // Register MouseMotionListener on the frame
        setFocusable(true); // Set focus on the frame
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int[] pos : imagePositions) {
            g.drawImage(image, pos[0], pos[1], null); // draw the image at the specified positions
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KeyListenerExample imageMovement = new KeyListenerExample();
            imageMovement.setVisible(true);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SHIFT) {
            moveSpeed = 20; // double the speed when Shift is pressed
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            moveImage(-moveSpeed, 0); // move the image to the left
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            moveImage(moveSpeed, 0); // move the image to the right
        } else if (keyCode == KeyEvent.VK_UP) {
            moveImage(0, -moveSpeed); // move the image up
        } else if (keyCode == KeyEvent.VK_DOWN) {
            moveImage(0, moveSpeed); // move the image down
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SHIFT) {
            moveSpeed = 10; // revert to normal speed when Shift is released
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { // Check if left mouse button is clicked
            int mouseX = e.getX();
            int mouseY = e.getY();
            imagePositions.add(new int[]{mouseX - image.getWidth() / 2, mouseY - image.getHeight() / 2});
            repaint();
        } else if (e.getButton() == MouseEvent.BUTTON2) { // Check if mouse wheel is clicked
            int mouseX = e.getX();
            int mouseY = e.getY();
            removeImage(mouseX, mouseY);
        }
    }

    private void removeImage(int mouseX, int mouseY) {
        for (int i = 0; i < imagePositions.size(); i++) {
            int[] pos = imagePositions.get(i);
            int imgX = pos[0];
            int imgY = pos[1];
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();

            if (mouseX >= imgX && mouseX <= imgX + imgWidth && mouseY >= imgY && mouseY <= imgY + imgHeight) {
                imagePositions.remove(i);
                repaint();
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // Check if right mouse button is pressed
            int mouseX = e.getX();
            int mouseY = e.getY();
            for (int[] pos : imagePositions) {
                int imgX = pos[0];
                int imgY = pos[1];
                int imgWidth = image.getWidth();
                int imgHeight = image.getHeight();
                if (mouseX >= imgX && mouseX <= imgX + imgWidth && mouseY >= imgY && mouseY <= imgY + imgHeight) {
                    draggingImage = pos;
                    dragOffsetX = mouseX - imgX;
                    dragOffsetY = mouseY - imgY;
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // Check if right mouse button is released
            draggingImage = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggingImage != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            draggingImage[0] = mouseX - dragOffsetX;
            draggingImage[1] = mouseY - dragOffsetY;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not used in this example
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not used in this example
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used in this example
    }

    public void moveImage(int dx, int dy) {
        x += dx;
        y += dy;
        if (x < 0) {
            x = getWidth() - image.getWidth();
        } else if (x + image.getWidth() > getWidth()) {
            x = 0;
        }
        if (y < 0) {
            y = getHeight() - image.getHeight();
        } else if (y + image.getHeight() > getHeight()) {
            y = 25;
        }
        repaint(); // redraw the window
    }
}
