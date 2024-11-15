package staffEditor;

import javax.swing.ImageIcon;

public class EightButton extends IconButton{

	public EightButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/eighth_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("八分音符");
	}

}
