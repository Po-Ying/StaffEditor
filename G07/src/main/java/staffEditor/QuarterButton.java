package staffEditor;

import javax.swing.*;
import java.awt.event.*;

public class QuarterButton extends IconButton {
    // private StaffPage staffPage; // 移除內部初始化 staffPage
    
    public QuarterButton(Toolbar p, StaffPage staffPage) {
        super(p);

        // 設置音符按鈕圖標
        imageURL = cldr.getResource("images/quarter_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("四分音符");

        // 設置按鈕的點擊事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ( staffPage != null) {
                    System.out.println("Mouse clicked at: " + e.getX() + ", " + e.getY());
                    staffPage.addNote("Quarter", e.getX(), e.getY());
                } else {
                    System.out.println("staffPage is null");
                }
            }
        });
    }
}
