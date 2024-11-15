package staffEditor;

import javax.swing.ImageIcon;

public class SixteenthButton extends IconButton{

	public SixteenthButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/sixteenth-note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("十六分音符");
	}

}
