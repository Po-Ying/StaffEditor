package staffEditor;

import javax.swing.ImageIcon;

public class RestButton extends IconButton{

	RestButton(Toolbar p) 
	{
        super(p);
  
        imageURL   = cldr.getResource("images/quarter-note-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("選擇休止符");
	}

}
