package staffEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class WholerestButton extends IconButton {
	
	public WholerestButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/whole_rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("全休止符");
        
	}

}
