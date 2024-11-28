package staffEditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MouseButton extends IconButton {
    
    Toolbar parent;
    private JPanel rightPanel;
    private JPanel rightRest;

    public MouseButton(Toolbar p, JPanel rightPanel, JPanel rightRest) {
        super(p);
        parent = p;
        this.rightPanel = rightPanel; 
        this.rightRest = rightRest;

        // 設置圖標
        imageURL = cldr.getResource("images/direct-selection.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("滑鼠");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parent.inputtype = inputType.Cursor; // 更新輸入模式
                disableRightButtons(); // 停用其他按鈕
                resetCursor(); // 將滑鼠切換為預設鼠標
            }
        });
    }

    private void disableRightButtons() {
        for (Component btn : rightPanel.getComponents()) {
            if (btn instanceof ToggleButton) {
                ((ToggleButton) btn).setEnabled(false);  
            }
        }
        for (Component btn : rightRest.getComponents()) {
            if (btn instanceof ToggleButton) {
                ((ToggleButton) btn).setEnabled(false);  
            }
        }
    }

    // 將滑鼠切換回預設鼠標
    private void resetCursor() {
        Component root = parent.getRootPane(); // 獲取工具列的根元件
        if (root != null) {
            root.setCursor(Cursor.getDefaultCursor()); // 將鼠標切換為系統預設指標
        }
    }
}
