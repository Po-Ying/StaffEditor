package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModuleButton extends IconButton {
    Toolbar parentToolbar;
    
    private boolean selectionModeActive = false; // 用於追蹤選取模式的狀態
    public ModuleButton(Toolbar p) {
        super(p);
        parentToolbar = p;

        imageURL = cldr.getResource("images/module.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("模塊化編曲");

        // 添加點擊事件
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切換選取模式
                selectionModeActive = !selectionModeActive;

                // 通過 MainWindow 控制 CopyButton 和 PasteButton 的可見性
                MainWindow mainWindow = parentToolbar.parent;
                mainWindow.toggleCopyPasteButtons(selectionModeActive); // 將按鈕設為可見或隱藏

                // 設定所有 StaffPage 的選取模式
                for (StaffPage page : mainWindow.getAllStaffPages()) {
                    page.setSelectionMode(selectionModeActive); // 啟用或停用選取模式
                }

                // 更新按鈕提示文字
                if (selectionModeActive) {
                    setToolTipText("點擊以停用模塊化編曲");
                } else {
                    setToolTipText("點擊以啟用模塊化編曲");
                }
            }
        });
    }
}
