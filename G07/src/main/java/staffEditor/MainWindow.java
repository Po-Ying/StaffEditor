package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {

    SECreate parent;
    Dimension size = new Dimension(935, 700);

    public Toolbar toolbar;
    public InsMenu instrumentMenu;
    public TabbedPane tabbedPane;
    public StaffPage staffPage;
	private TopToolbar toptoolbar;

    public MainWindow(SECreate p) {
        this.parent = p;
        
		tabbedPane = new TabbedPane(this);
        staffPage = new StaffPage(tabbedPane);

        toolbar = new Toolbar(this);
        instrumentMenu = new InsMenu(this);	

        this.setSize(size);
        this.setLayout(new BorderLayout());
        
        // 添加組件到界面
        this.add(toolbar, BorderLayout.NORTH);
        this.add(instrumentMenu, BorderLayout.EAST);
        this.add(tabbedPane, BorderLayout.CENTER);


        // 設置窗口位置和顯示
        this.setLocation(25, 50);
        this.setVisible(true);

        // 設置關閉窗口時的行為
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

		// System.out.println("tabbedPane: " + (tabbedPane != null));
		// System.out.println("staffPage: " + (staffPage != null));
		// System.out.println("toolbar: " + (toolbar != null));
		// System.out.println("instrumentMenu: " + (instrumentMenu != null));

    }
}
