package staffEditor;

import java.awt.*;
import javax.swing.*;

public class Toolbar extends JPanel{

	MainWindow parent;
	
	TopToolbar topToolbar;
	
	
	Toolbar(MainWindow p) {
	    parent = p;
	    
	    topToolbar = new TopToolbar(this);
	    this.setLayout(new BorderLayout());
	    this.add(topToolbar,BorderLayout.CENTER);
	    this.setPreferredSize(new Dimension(0,95));

	}
	

}

