package staffEditor;
import javax.swing.*;
import java.awt.*;

public class InsMenu extends JPanel{
	MainWindow parent;
    InsImages instrumentImages;
    InsList instrumentList;
    
    InsMenu(MainWindow p)
    {
    	parent = p;
    	instrumentImages = new InsImages(this);
    	instrumentList = new InsList(this);
    	
    	this.setBackground(Color.white);
    	this.setLayout(new BorderLayout());
        this.add(instrumentList,BorderLayout.CENTER);
        this.add(instrumentImages,BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(200,0));
        
    }
}
