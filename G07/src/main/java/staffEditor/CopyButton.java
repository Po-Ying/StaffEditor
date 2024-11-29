package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CopyButton extends JButton {
    MainWindow parent;

    public CopyButton(MainWindow parent) {
        this.parent = parent;

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

                // 獲取當前的 TabbedPane
                TabbedPane tabbedPane = parent.getTabbedPane();

                // 檢查當前的頁面是否為 StaffPage
                if (tabbedPane.getSelectedComponent() instanceof StaffPage) {
                    StaffPage staffPage = (StaffPage) tabbedPane.getSelectedComponent();

                    // 呼叫 StaffPage 的複製邏輯
                    if (staffPage.copySelectedMeasure()) {
                        System.out.println("Selected measures copied to clipboard.");
                    } else {
                        System.out.println("No measures selected for copying.");
                    }
                } else {
                    System.out.println("Current tab is not a StaffPage.");
                }
            }
        });

    }
}
