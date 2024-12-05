package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CopyButton extends JButton {
    MainWindow parent;
    public MeasureManager measureManager;  // 用來管理小節的 MeasureManager
    
    public CopyButton(MainWindow parent, MeasureManager measureManager) {
        this.parent = parent;
        this.measureManager = measureManager;  // 在這裡初始化 measureManager

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
                    if (staffPage.copySelectedMeasures()) {
                        System.out.println("Selected measures copied to clipboard.");
                        
                        // 切換到貼上模式
                        staffPage.clearSelectedPasteMeasures(); // 清空貼上區塊
                        staffPage.setSelectionMode(true);       // 啟用選取貼上區塊
                        System.out.println("Switched to paste selection mode.");
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
