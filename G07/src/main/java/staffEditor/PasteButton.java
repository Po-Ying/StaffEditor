package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasteButton extends JButton {
    MainWindow parent;

    public PasteButton(MainWindow parent) {
        this.parent = parent;

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

                // 獲取當前的 TabbedPane
                TabbedPane tabbedPane = parent.getTabbedPane();

                // 檢查當前的頁面是否為 StaffPage
                if (tabbedPane.getSelectedComponent() instanceof StaffPage) {
                    StaffPage staffPage = (StaffPage) tabbedPane.getSelectedComponent();

                    // 呼叫 StaffPage 的貼上邏輯
                    if (staffPage.pasteToSelectedMeasures()) {
                        System.out.println("Clipboard content pasted into selected measures.");
                        
                        // 清空貼上選取區域
                        staffPage.clearSelectedPasteMeasures();
                    } else {
                        System.out.println("Failed to paste content. No measures selected or clipboard is empty.");
                    }
                } else {
                    System.out.println("Current tab is not a StaffPage.");
                }
            }
        });
    }
}
