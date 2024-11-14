package staffEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SixteenthrestButton extends IconButton {

	public SixteenthrestButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/sixteenth_rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
	}
}