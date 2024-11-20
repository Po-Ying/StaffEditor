package staffEditor;

import javax.swing.ImageIcon;

public class ModuleButton extends IconButton{
	
	
	ModuleButton(Toolbar p)
	{
		super(p);
        imageURL   = cldr.getResource("images/module.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("模塊化編曲");
        
        
        
	}
}
