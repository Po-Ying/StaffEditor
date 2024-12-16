package staffEditor;

import java.awt.*;
import javax.swing.*;

public class Toolbar extends JPanel{

	MainWindow parent;
	TopToolbar topToolbar;
	private TabbedPane tabbedPane;
	
	longType longtype;
	inputType inputtype;
	Toolbar(MainWindow p) {
	    parent = p;
		longtype = longType.non;
		inputtype = inputType.Cursor;

	    topToolbar = new TopToolbar(this);
	    this.setLayout(new BorderLayout());
	    //this.setBackground(Color.darkGray);
	    this.add(topToolbar,BorderLayout.CENTER);
	    this.setPreferredSize(new Dimension(0,95));

	    this.tabbedPane = p.getTabbedPane();
	    
	}
	
    // 返回 ModuleButton 的 getter 方法
    public ModuleButton getModuleButton() 
    {
        return topToolbar.moduleBtn; // 返回 TopToolbar 中的 moduleBtn 實例
    }
    
    public TupletButton getTupletButton() {
        return topToolbar.tupletBtn;  // 返回已創建的 TupletButton 實例
    }
    
    public MainWindow getMainWindow() 
    {
        return parent;
    }

    //讓其他類別能夠訪問 TabbedPane
    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }

}
