package staffEditor;

import org.jfugue.player.Player;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame{
	
	   SECreate parent;
	    Dimension size = new Dimension(935, 700);

	    //public Toolbar toolbar;
	    public InsMenu InstrumentMenu;
	    //public TabbedPane tabbedPane;

	    MainWindow(SECreate p) {
	        parent = p;

	        this.setSize(size);
	        //toolbar = new Toolbar(this);
	        InstrumentMenu = new InsMenu(this);
	        //tabbedPane = new TabbedPane(this);

	        this.setLayout(new BorderLayout());

	        //this.add(toolbar, BorderLayout.NORTH);
	        this.add(InstrumentMenu, BorderLayout.EAST);
	        //this.add(tabbedPane, BorderLayout.CENTER);
	        

	        this.setLocation(25, 50);
	        this.setVisible(true);

	        this.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                System.exit(0);
	            }
	        });
	    }
}
