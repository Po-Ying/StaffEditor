package staffEditor;

import javax.swing.ImageIcon;

public class WholeButton extends IconButton{

	public WholeButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/whole.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
	}

}
