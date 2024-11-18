package staffEditor;

// import org.jfugue.player.Player;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame{
	
	    SECreate parent;
	    Dimension size = new Dimension(935, 700);

	    public Toolbar toolbar;
	    public InsMenu instrumentMenu;
	    public TabbedPane tabbedPane;
		private TopToolbar topToolbar;
   		private StaffPage staffPage;

	    MainWindow(SECreate p) {
	        parent = p;

	        this.setSize(size);
	        toolbar = new Toolbar(this);
	        instrumentMenu = new InsMenu(this);
	        tabbedPane = new TabbedPane(this);
			staffPage = new StaffPage(tabbedPane);
        	topToolbar = new TopToolbar(staffPage);

	        this.setLayout(new BorderLayout());

	        this.add(toolbar, BorderLayout.NORTH);
	        this.add(instrumentMenu, BorderLayout.EAST);
	        this.add(tabbedPane, BorderLayout.CENTER);
			this.add(topToolbar, BorderLayout.NORTH);
        	this.add(staffPage, BorderLayout.CENTER);
	        

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
