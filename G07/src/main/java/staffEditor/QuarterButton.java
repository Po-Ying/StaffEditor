package staffEditor;

import javax.swing.ImageIcon;

public class QuarterButton extends IconButton{

	public QuarterButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/quarter_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
	}

}
