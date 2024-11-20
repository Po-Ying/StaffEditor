package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CopyButton extends JButton {
	InsMenu parent;

    public CopyButton(InsMenu p) {
        parent = p;

        // 設定按鈕屬性
        this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/copy icon.png")));
        this.setToolTipText("複製");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // 添加按鈕功能
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Copy button clicked");
                // 複製邏輯：選取範圍，將內容保存至臨時變量
                // e.g., parent.copySelectedNotes();
            }
        });
    }
}
