package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasteButton extends JButton {
	MainWindow parent;

    public PasteButton(MainWindow parent2) {
        parent = parent2;

        // 設定按鈕屬性
        this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/paste.png")));
        this.setToolTipText("貼上");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // 添加按鈕功能
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paste button clicked");
                // 貼上邏輯：將複製的內容粘貼到新位置
                // e.g., parent.pasteNotesAtCurrentPosition();
            }
        });
    }
}
