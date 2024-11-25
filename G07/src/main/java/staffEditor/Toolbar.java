package staffEditor;

import java.awt.*;
import javax.swing.*;

public class Toolbar extends JPanel{

	MainWindow parent;
	StaffPage staffPage;
	TopToolbar topToolbar;
	
	longType longtype;
	inputType inputtype;
	Toolbar(MainWindow p) {
	    this.parent = p;
		this.longtype = longType.non;
		this.inputtype = staffEditor.inputType.Cursor;

	    topToolbar = new TopToolbar(this);
	    this.setLayout(new BorderLayout());
	    //this.setBackground(Color.darkGray);
	    this.add(topToolbar,BorderLayout.CENTER);
	    this.setPreferredSize(new Dimension(0,95));

	}
	

}
