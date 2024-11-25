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
        Toolkit tk = Toolkit.getDefaultToolkit();
        icon = new ImageIcon(imageURL);
        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 45, Image.SCALE_DEFAULT));
        Cursor cu = tk.createCustomCursor(imageIcon.getImage(), new Point(16, 16), "");
        for (int i = 0; i < parent.parent.tabbedPane.getTabCount(); i++) {
                parent.parent.tabbedPane.getComponentAt(i).setCursor(cu);
            }
        parent.longtype = longType.halfR;
    }
        // 先確認 imageURL 不為 null
        // if (imageURL != null) {
        //     if (parent.inputtype == inputType.Note) {
        //         icon = new ImageIcon(imageURL);
        //         imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 45, Image.SCALE_DEFAULT));
        //     } else if (parent.inputtype == inputType.rest) {
        //         icon = new ImageIcon(imageURL);
        //         imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 28, Image.SCALE_DEFAULT));
        //     }
            
            // 確保 imageIcon 不為 null 再創建游標
            // if (imageIcon != null) {
            //     Cursor cu = tk.createCustomCursor(imageIcon.getImage(), new Point(16, 16), "");
            //     for (int i = 0; i < parent.parent.tabbedPane.getTabCount(); i++) {
            //         parent.parent.tabbedPane.getComponentAt(i).setCursor(cu);
            //     }
            //     parent.longtype = longType.quarter;
            // }
        //}
}