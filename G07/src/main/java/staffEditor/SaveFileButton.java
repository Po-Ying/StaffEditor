package staffEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class SaveFileButton extends IconButton {
    StaffPage page;
    JFileChooser fileChooser;
    Toolbar toolbar;

    SaveFileButton(Toolbar p, StaffPage page) {
        super(p);
        this.page = page;
        this.toolbar = toolbar;
        imageURL = cldr.getResource("images/save.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");

        // 初始化檔案選擇器
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Files", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        this.addActionListener(e -> {
            try {
                saveFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "發生未預期的錯誤: " + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void saveFile() { 
    	
        SwingUtilities.invokeLater(() -> {
        	
        	page.revalidate();
            page.repaint();
            
            TabbedPane tabbedPane = parent.getTabbedPane(); // 獲取 TabbedPane
            StaffPage selectedPage = tabbedPane.getSelectedStaffPage();
            List<StaffPage> allPages = tabbedPane.getAllStaffPages();
        	
            int returnValue = fileChooser.showSaveDialog(this);

            if (returnValue != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "保存操作已取消。");
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();
            String fileExtension = getSelectedFileExtension();

            if (fileExtension == null) {
                JOptionPane.showMessageDialog(this, "請選擇有效的檔案格式。", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!selectedFile.getName().toLowerCase().endsWith("." + fileExtension)) {
                selectedFile = new File(selectedFile.getAbsolutePath().replaceAll("\\.+$", "") + "." + fileExtension);
            }

            try {
                if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                	saveImage(selectedFile, fileExtension, selectedPage);                 
                    JOptionPane.showMessageDialog(this, "成功儲存圖片: " + selectedFile.getAbsolutePath());
                } else if (fileExtension.equals("pdf")) {
                    savePDF(selectedFile, allPages);
                    JOptionPane.showMessageDialog(this, "成功儲存 PDF: " + selectedFile.getAbsolutePath());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "儲存檔案時發生錯誤: " + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
        
    }

    private void saveImage(File file, String format, StaffPage selectedPage) throws IOException {
    	BufferedImage combinedImage = selectedPage.AllPagesToImage();
    	if (combinedImage == null) {
            JOptionPane.showMessageDialog(this, "圖像渲染失敗，無法保存文件。", "錯誤", JOptionPane.ERROR_MESSAGE);
            throw new IOException("圖像渲染失敗，圖像為 null。");
        }
    	
    	// 檢查是否是 RGB 格式，如果不是，轉換為 RGB
        if (combinedImage.getType() != BufferedImage.TYPE_INT_RGB) {
            BufferedImage rgbImage = new BufferedImage(combinedImage.getWidth(), combinedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = rgbImage.createGraphics();
            g.drawImage(combinedImage, 0, 0, null);
            g.dispose();
            combinedImage = rgbImage;
        }
    	
        try {
        	ImageIO.write(combinedImage, format, file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void savePDF(File file, List<StaffPage> allPages) throws IOException {

        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

        	for (StaffPage page : allPages) {
                BufferedImage image = page.renderToImage();
                if (image == null) {
                    throw new IOException("圖像渲染失敗，圖像為 null。");
                }

                byte[] imageDataBytes = writeToByteArray(image, "png");
                com.itextpdf.io.image.ImageData imageData = ImageDataFactory.create(imageDataBytes);
                Image pdfImage = new Image(imageData);
                document.add(pdfImage);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "儲存 PDF 檔案時發生錯誤: " + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            throw new IOException("PDF 保存失敗", e);
        }
    }

    private String getSelectedFileExtension() {
        String description = fileChooser.getFileFilter().getDescription();
        if (description.contains("PNG")) {
            return "png";
        } else if (description.contains("JPG")) {
            return "jpg";
        } else if (description.contains("PDF")) {
            return "pdf";
        }
        return null;
    }
    
    private byte[] writeToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        baos.flush();
        return baos.toByteArray();
    }
}
