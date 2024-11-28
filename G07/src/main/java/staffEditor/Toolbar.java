package staffEditor;

import java.awt.*;
import javax.swing.*;

public class Toolbar extends JPanel {

    MainWindow parent;
    TopToolbar topToolbar;
	
//Buttest
	public MainWindow getMainWindow() {
        return parent;}
//main
    public Toolbar(MainWindow p) {
        parent = p;
        
        topToolbar = new TopToolbar(this);
        this.setLayout(new BorderLayout());
        this.add(topToolbar, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(0, 95));
    }


    // 返回 ModuleButton 的 getter 方法
    public ModuleButton getModuleButton() {
        return topToolbar.moduleBtn; // 返回 TopToolbar 中的 moduleBtn 實例
    }
}


