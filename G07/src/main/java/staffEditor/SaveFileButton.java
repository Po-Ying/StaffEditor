package staffEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
public class SaveFileButton extends IconButton {
	StaffPage page;
	JFileChooser fileChooser;

    SaveFileButton(Toolbar p,StaffPage page) {
        super(p);
        this.page=page;
        imageURL = cldr.getResource("images/save.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("儲存檔案");

        // 初始化檔案選擇器
        fileChooser = new JFileChooser();

        // 添加 PNG, JPG, PDF 分開的選擇
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Files", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setAcceptAllFileFilterUsed(false); // 關閉「所有檔案」過濾器

        this.addActionListener(e -> saveFile());
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileExtension = getSelectedFileExtension();

            if (fileExtension == null) {
                JOptionPane.showMessageDialog(this, "請選擇有效的檔案格式。");
                return;
            }

            // 自動補全副檔名
            if (!selectedFile.getName().toLowerCase().endsWith("." + fileExtension)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + fileExtension);
            }

            try {
                if (fileExtension.equals("png") || fileExtension.equals("jpg")) {
                    saveImage(selectedFile, fileExtension);
                } /*else if (fileExtension.equals("pdf")) {
                    savePDF(selectedFile);
                }*/

                JOptionPane.showMessageDialog(this, "檔案已成功儲存至: " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "儲存檔案時發生錯誤: " + ex.getMessage());
            }
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

    private void saveImage(File file, String format) throws IOException {
        // 從 MusicSheetPanel 渲染內容
        BufferedImage image = page.renderToImage();
        ImageIO.write(image, format, file);
    }

    /*private void savePDF(File file) throws IOException {
        com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

        BufferedImage Image = page.renderToImage();
        com.itextpdf.io.image.ImageData imageData = com.itextpdf.io.image.ImageDataFactory.create(
                ImageIO.writeToByteArray(Image, "png")
        );
        com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imageData);

        document.add(pdfImage);
        document.close();
    }
    private byte[] writeToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }*/
}