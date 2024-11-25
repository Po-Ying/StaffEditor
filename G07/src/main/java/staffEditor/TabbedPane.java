package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class TabbedPane extends JTabbedPane{
	
	MainWindow parent;
	int c=0;//計數用
	int id;
	
	TabbedPane(MainWindow p)
	{
        this.parent = p;

        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(40,0));

        this.parent.setVisible(true);

        this.addTab("page",new StaffPage(this));
	}
	
    @Override
    public void addTab(String title, final Component content) {
        c++;
        id = c;
        JPanel tab = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title+id);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        CloseTabBtn closeTabBtn = new CloseTabBtn(this,content);
        closeTabBtn.setBorder(BorderFactory.createEmptyBorder());


        tab.add(label, BorderLayout.WEST);
        tab.add(closeTabBtn, BorderLayout.EAST);
        tab.setOpaque(false);
        tab.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
        super.addTab(title, content);
        setTabComponentAt(getTabCount() - 1, tab);


    }

}

class CloseTabBtn extends JButton {

    TabbedPane parent;

    public CloseTabBtn(TabbedPane p, final Component c) {
        parent = p;

        this.setText("x");
        this.setPreferredSize(new Dimension(17, 17));
        this.setToolTipText("關閉TabbedPage");
        this.setUI(new BasicButtonUI());
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBorderPainted(false);

        this.setForeground(new Color(255, 255, 255));
        this.setBackground(new Color(255, 0, 0));
        this.setOpaque(true);
        this.setRolloverEnabled(true);
        

        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
            	int a=JOptionPane.showConfirmDialog(null, "確定關閉頁面？", "警告",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(a==0)
                    doSomething(c);
            }
        });
        
    }
    public void doSomething(final Component c)
    {
        if(this.parent.getTabCount()==1) {

            /*parent.parent.toolbar.editBar.setTypeEnable(false);
            parent.parent.toolbar.editBar.setLengthEnable(false);*/

        }

        this.parent.removeTabAt(this.parent.indexOfComponent(c));


    }
}

