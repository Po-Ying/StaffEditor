package staffEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class EightrestButton extends IconButton {

	public EightrestButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/eight-note-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("八分休止符");
	}
}
