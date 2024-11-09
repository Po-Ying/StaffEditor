package staffEditor;

import javax.swing.ImageIcon;

public class TupletButton extends IconButton{	
	
	TupletButton(Toolbar p)
	{
		super(p);
        imageURL   = cldr.getResource("images/save.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
	}
}
