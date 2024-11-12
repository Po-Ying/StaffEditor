package staffEditor;

import javax.swing.ImageIcon;

public class HalfButton extends IconButton{

	public HalfButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/half_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
	}

}
