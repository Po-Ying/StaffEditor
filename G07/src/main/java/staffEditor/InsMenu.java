package staffEditor;

import javax.swing.*;
import java.awt.*;

public class InsMenu extends JPanel{
	MainWindow parent;
    InsList instrumentList;
    
    InsMenu(MainWindow p)
    {
    	parent = p;

    	instrumentList = new InsList(this);
    	
    	this.setBackground(Color.blue);
    	this.setLayout(new BorderLayout());
        this.add(instrumentList,BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(200,0));
        
    }
}
