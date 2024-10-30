package ezpaint;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Toolbar extends Panel{

	MainWindow parent;
	
	JButton addfileBtn, openfileBtn, saveBtn;
	JButton mouseBtn, musicBtn, restBtn;
	JButton quarterBtn, eighthBtn, sixteenthBtn, halfBtn, wholeBtn;
	
	ImageIcon addIcon, openIcon, saveIcon;
	ImageIcon mouseIcon, musicIcon, restIcon;
	ImageIcon quarterIcon, eighthIcon, sixteenthIcon, halfIcon, wholeIcon;
	
	
	Toolbar(MainWindow parent)
	{
		super();
		Toolbar.this.parent = parent;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(Color.darkGray);
		
		addfileBtn = new JButton();
	    ImageIcon addIcon = new ImageIcon("add-file.png");
	    addfileBtn.setIcon(addIcon);
	    addfileBtn.setPreferredSize(new Dimension(40, 40));
	    
	    openfileBtn = new JButton();
	    ImageIcon openIcon = new ImageIcon("open-folder.png");
	    openfileBtn.setIcon(openIcon);
	    openfileBtn.setPreferredSize(new Dimension(40, 40));
	    
	    saveBtn = new JButton();
	    ImageIcon saveIcon = new ImageIcon("save.png");
	    saveBtn.setIcon(saveIcon);
	    saveBtn.setPreferredSize(new Dimension(40, 40));
	    
	    mouseBtn = new JButton();
	    ImageIcon mouseIcon = new ImageIcon("direct-selection.png");
	    mouseBtn.setIcon(mouseIcon);
	    mouseBtn.setPreferredSize(new Dimension(40, 40));
	    
	    musicBtn = new JButton();
	    ImageIcon musicIcon = new ImageIcon("music-note.png");
	    musicBtn.setIcon(musicIcon);
	    musicBtn.setPreferredSize(new Dimension(40, 40));
	    
	    restBtn = new JButton();
	    ImageIcon restIcon = new ImageIcon("quarter-note-rest.png");
	    restBtn.setIcon(restIcon);
	    restBtn.setPreferredSize(new Dimension(40, 40));
	    
	    quarterBtn = new JButton();
	    ImageIcon quarterIcon = new ImageIcon("quarter note.png");
	    quarterBtn.setIcon(quarterIcon);
	    quarterBtn.setPreferredSize(new Dimension(40, 40));
	    
	    eighthBtn = new JButton();
	    ImageIcon eighthIcon = new ImageIcon("eighth note.png");
	    eighthBtn.setIcon(eighthIcon);
	    eighthBtn.setPreferredSize(new Dimension(40, 40));
	    
	    sixteenthBtn = new JButton();
	    ImageIcon sixteenthIcon = new ImageIcon("sixteenth-note.png");
	    sixteenthBtn.setIcon(sixteenthIcon);
	    sixteenthBtn.setPreferredSize(new Dimension(40, 40));
	    
	    halfBtn = new JButton();
	    ImageIcon halfIcon = new ImageIcon("half note.png");
	    halfBtn.setIcon(halfIcon);
	    halfBtn.setPreferredSize(new Dimension(40, 40));
	    
	    wholeBtn = new JButton();
	    ImageIcon wholeIcon = new ImageIcon("whole.png");
	    wholeBtn.setIcon(wholeIcon);
	    wholeBtn.setPreferredSize(new Dimension(40, 40));
	    
	    this.add(addfileBtn);
	    this.add(openfileBtn);
	    this.add(saveBtn);
	    this.add(mouseBtn);
	    this.add(musicBtn);
	    this.add(restBtn);
	    this.add(quarterBtn);
	    this.add(eighthBtn);
	    this.add(sixteenthBtn);
	    this.add(halfBtn);
	    this.add(wholeBtn);
	    
	    addfileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addFile();
            }
        });
	    
	    openfileBtn.addMouseListener(new MouseAdapter() 
	    {
	    	 @Override
	    	    public void mouseClicked(MouseEvent e) {
	    	        openFileChooser(); 
	    	    }
	    });    
	    
	    saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveFileChooser();
            }
        });
	    
	}
	
	private void addFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File newFile = fileChooser.getSelectedFile();
            // 確保檔案有擴展名
            if (!newFile.getName().endsWith(".txt")) {
                newFile = new File(newFile.getAbsolutePath() + ".txt");
            }
            try (FileWriter writer = new FileWriter(newFile)) {
                // 寫入空內容或初始內容
                writer.write(""); // 可以改為想寫入的內容
                JOptionPane.showMessageDialog(this, "已新增檔案: " + newFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "新增檔案時發生錯誤: " + ex.getMessage());
            }
        }
    }
	
	private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "選擇的檔案: " + selectedFile.getAbsolutePath());
        }
    }

	private void saveFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // 確保檔案有擴展名
            if (!selectedFile.getName().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            try (FileWriter writer = new FileWriter(selectedFile)) {
                // 寫入檔案內容
                writer.write("這是儲存的內容。"); // 這裡可以替換為你要儲存的實際內容
                JOptionPane.showMessageDialog(this, "檔案已儲存: " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "儲存檔案時發生錯誤: " + ex.getMessage());
            }
        }
    }
}