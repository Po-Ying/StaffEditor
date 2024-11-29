package staffEditor;

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.event.ActionListener;
import java.net.URL; 

public class HalfrestButton extends ToggleButton {
    Toolbar parent;
    ImageIcon imageIcon;

    public HalfrestButton(Toolbar p) {
        super(p);
        parent = p;

        // 使用與父類別相同的 ClassLoader 方法
        imageURL = cldr.getResource("images/half-rest.png");
        
        icon = new ImageIcon(imageURL);


        this.setIcon(icon);

        this.setToolTipText("二分休止符");
    } 

    public void doSomething() {
        if (parent.inputtype == inputType.Cursor) {
            return;
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        icon = new ImageIcon(imageURL);
        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(20, 30, Image.SCALE_DEFAULT));
        Cursor cu = tk.createCustomCursor(imageIcon.getImage(), new Point(16, 16), "");
        for (int i = 0; i < parent.parent.tabbedPane.getTabCount(); i++) {
                parent.parent.tabbedPane.getComponentAt(i).setCursor(cu);
            }
        parent.longtype = longType.halfR;
    }
}