package staffEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class HalfrestButton extends IconButton {

	public HalfrestButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/half-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("二分休止符");
	}
}
