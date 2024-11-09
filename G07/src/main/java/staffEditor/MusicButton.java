package staffEditor;

import javax.swing.ImageIcon;

public class MusicButton extends IconButton{

	MusicButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/music-note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("選擇音符");
        
	}

}
