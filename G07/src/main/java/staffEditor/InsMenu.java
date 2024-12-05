package staffEditor;

import javax.swing.*;
import java.awt.*;

public class InsMenu extends JPanel {
    MainWindow parent;
    InsList instrumentList;
    CopyButton copyBtn;
    PasteButton pasteBtn;
    PlayButton playBtn;
    MeasureManager measureManager;

    public InsMenu(MainWindow p) {
        parent = p;
        
        JComponent panel = parent.getStaffPage().getPanel();
        measureManager = new MeasureManager(panel);  // 初始化 MeasureManager
        // 設置主佈局為 BoxLayout 垂直方向
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(255, 220, 150)); // 背景色與設計一致
        this.setPreferredSize(new Dimension(200, 0));

        // 創建 Copy 和 Paste 按鈕
        copyBtn = new CopyButton(parent,measureManager); // 假設 CopyButton 類需要傳遞父類
        pasteBtn = new PasteButton(parent); // 假設 PasteButton 類需要傳遞父類
        
        playBtn = new PlayButton(parent);
        
        // 設置按鈕大小
        copyBtn.setPreferredSize(new Dimension(50, 50));
        pasteBtn.setPreferredSize(new Dimension(50, 50));
        playBtn.setPreferredSize(new Dimension(50, 50));
        copyBtn.setVisible(false);
        pasteBtn.setVisible(false);
        this.add(playBtn);
        // 創建按鈕面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // 水平排列
        buttonPanel.setBackground(new Color(255, 220, 150));
        buttonPanel.add(copyBtn);
        buttonPanel.add(Box.createHorizontalStrut(10)); // 添加按鈕之間的間距
        buttonPanel.add(pasteBtn);
        
        
        // 樂器列表
        instrumentList = new InsList(this);

        // 添加到主面板
        this.add(Box.createVerticalStrut(10)); // 頂部間距
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(20)); // 按鈕與樂器列表間距
        this.add(instrumentList);
    }
}

