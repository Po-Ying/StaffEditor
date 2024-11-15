package staffEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveFileButton extends IconButton {
    JFileChooser fileChooser;
    
    SaveFileButton(Toolbar p) {
        
        super(p);
        
        imageURL   = cldr.getResource("images/save.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");
        
        fileChooser = new JFileChooser();
        
        
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG, JPG, PDF Files", "png", "jpg", "pdf"));

        this.addActionListener(e -> saveFile());
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);

        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String fileExtension = "";

            
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".pdf")) {
                fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            } else {
                
                if (fileChooser.getSelectedFile().getPath().endsWith(".png")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
                    fileExtension = "png";
                } else if (fileChooser.getSelectedFile().getPath().endsWith(".jpg")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".jpg");
                    fileExtension = "jpg";
                } else if (fileChooser.getSelectedFile().getPath().endsWith(".pdf")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".pdf");
                    fileExtension = "pdf";
                }
            }

            try {
                
                if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                    saveImage(selectedFile, fileExtension);  
                } else if (fileExtension.equals("pdf")) {
                    savePDF(selectedFile);  
                }

                JOptionPane.showMessageDialog(this, "檔案已儲存: " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "儲存檔案時發生錯誤: " + ex.getMessage());
            }
        }
    }

    
    private void saveImage(File file, String format) throws IOException {
        
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 200, 200);
        g.setColor(Color.WHITE);
        g.drawString("Test Image", 50, 100);
        g.dispose();

        
        ImageIO.write(image, format, file);
    }

    
    private void savePDF(File file) throws IOException {
        
        /*com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

        
        document.add(new com.itextpdf.layout.element.Paragraph("Test PDF Content"));

        
        document.close();*/
    }
}

