package staffEditor;

import javax.swing.ImageIcon;

public class MouseButton extends IconButton{

	public MouseButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/direct-selection.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("滑鼠");
	}

}
