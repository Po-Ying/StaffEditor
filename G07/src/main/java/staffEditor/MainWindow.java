package staffEditor;

import org.jfugue.player.Player;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    SECreate parent;
    Dimension size = new Dimension(935, 700);

    public Toolbar toolbar;
    public InsMenu instrumentMenu;
    public TabbedPane tabbedPane;

    public MainWindow(SECreate p) {
        parent = p;

        this.setSize(size);
        toolbar = new Toolbar(this);
        instrumentMenu = new InsMenu(this);
        tabbedPane = new TabbedPane(this);

        this.setLayout(new BorderLayout());

        this.add(toolbar, BorderLayout.NORTH);
        this.add(instrumentMenu, BorderLayout.EAST);
        this.add(tabbedPane, BorderLayout.CENTER);

        this.setLocation(25, 50);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 控制 CopyButton 和 PasteButton 的顯示與隱藏
    public void toggleCopyPasteButtons(boolean isVisible) {
        instrumentMenu.copyBtn.setVisible(isVisible);
        instrumentMenu.pasteBtn.setVisible(isVisible);
    }

    // 隱藏 CopyButton 和 PasteButton
    public void hideCopyPasteButtons() {
        instrumentMenu.copyBtn.setVisible(false);
        instrumentMenu.pasteBtn.setVisible(false);
    }

    // 新增方法：獲取當前所有的 StaffPage
    public List<StaffPage> getAllStaffPages() {
        return tabbedPane.getAllStaffPages();
    }
    
    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }
    


}
