package staffEditor;

import javax.swing.*;
import java.awt.event.*;

public class HalfButton extends IconButton {
    // private StaffPage staffPage; // 移除內部初始化 staffPage
    
    public HalfButton(Toolbar p, StaffPage staffPage) {
        super(p);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("新增音符StaffPage的雜湊值: " + System.identityHashCode(staffPage));
                staffPage.addNote("Half", e.getX(), e.getY());
                System.out.println("音符切換為: Half");
            }

            // @Override
            // public void mouseEntered(MouseEvent e) {
            //     backButton.setVisible(!notes.isEmpty());
            //     forwardButton.setVisible(false); // 如有必要, trash_notes 逻辑可恢复
            // }

            // @Override
            // public void mouseExited(MouseEvent e) {
            //     backButton.setVisible(false);
            //     forwardButton.setVisible(false);
            // }
        });
        // 設置音符按鈕圖標
        imageURL = cldr.getResource("images/half_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("二分音符");
    }
    
    // public void setStaffPage(StaffPage staffPage) {
    //     this.staffPage = staffPage;
    // }
}
