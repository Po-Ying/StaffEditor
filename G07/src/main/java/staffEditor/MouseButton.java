package staffEditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MouseButton extends ToggleButton {
    
    Toolbar parent;
    private JPanel rightPanel;
    private JPanel rightRest;

    public MouseButton(Toolbar p, JPanel rightPanel, JPanel rightRest) {
        super(p);
        parent = p;
        this.rightPanel = rightPanel; 
        this.rightRest = rightRest;

        parent.inputtype = inputType.Cursor;
        parent.longtype=longType.non;

        // 設置圖標
        imageURL = cldr.getResource("images/direct-selection.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("滑鼠");

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                disableRightButtons();
                resetCursor();
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
    public void resetCursor() {
        // System.out.println("clicked!");
        Cursor cu = new Cursor(Cursor.DEFAULT_CURSOR);
        for(int i=0;i<parent.parent.tabbedPane.getTabCount();i++) {
            parent.parent.tabbedPane.getComponentAt(i).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        parent.inputtype = inputType.Cursor;
        parent.longtype=longType.non;

        parent.topToolbar.setLengthEnable(false);
    }
}
