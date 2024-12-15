package staffEditor;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.image.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;

public class OpenFileButton extends IconButton {
    StaffPage page;

    OpenFileButton(Toolbar p, StaffPage page) {
        super(p);
        if (page == null) {
            throw new IllegalArgumentException("StaffPage cannot be null.");
        }
        this.page = page;
        imageURL = cldr.getResource("images/open-folder.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("開啟檔案");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Files", "jpg"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath().toLowerCase();

            if (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
                processImageFile(selectedFile);
            } else if (filePath.endsWith(".pdf")) {
                processPDFFile(selectedFile);
            } else {
                JOptionPane.showMessageDialog(this, "Unsupported file type.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void processImageFile(File file) {
        try {
            // 加载图像
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                JOptionPane.showMessageDialog(this, "Invalid image file: " + file.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 计算页数，假设每页宽度为 800 像素
            int pageWidth = 1100;
            int totalPages = (img.getWidth()-100) / pageWidth;
            System.out.println(totalPages);
            // 减去 1 页，因为默认已经显示第一页
            totalPages -= 1;

            // 创建页面
            for (int i = 0; i < totalPages; i++) {
                NewPageButton newPageButton = new NewPageButton(this.parent);
                newPageButton.doSomething(); // 执行 NewPageButton 操作

                // 提取标题、作者和乐器信息
                String title = extractTextFromImage(img.getSubimage(340, 33, 500, 75));
                String author = extractTextFromImage(img.getSubimage(750, 120, 300, 30));
                String instrument = extractTextFromImage(img.getSubimage(100, 100, 150, 30));

                // 更新页面上的标签
                updateLabel(page.staffTitle, title, "未知標題");
                updateLabel(page.authorTitle, author, "未知作者");
                updateLabel(page.instrumentTitle, instrument, "未知樂器");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to process image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPDFFile(File file) {
        try {
            // 使用 PDFBox 解析 PDF
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 获取 PDF 页数
            int totalPages = document.getNumberOfPages();
            totalPages -= 1; // 减去 1 页

            // 创建页面
            for (int i = 0; i < totalPages; i++) {
                NewPageButton newPageButton = new NewPageButton(this.parent);
                newPageButton.doSomething(); // 执行 NewPageButton 操作

                // 渲染页面图像
                BufferedImage pageImage = pdfRenderer.renderImageWithDPI(i, 300);

                // 提取标题、作者和乐器信息
                String title = extractTextFromImage(pageImage);
                String author = extractTextFromImage(pageImage);
                String instrument = extractTextFromImage(pageImage);

                // 更新页面上的标签
                updateLabel(page.staffTitle, title, "未知標題");
                updateLabel(page.authorTitle, author, "未知作者");
                updateLabel(page.instrumentTitle, instrument, "未知樂器");
            }

            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to process PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String extractTextFromImage(BufferedImage image) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("eng"); // 指定语言
        try {
            String text = tesseract.doOCR(image); // 使用 OCR 提取文本
            return text.trim(); // 去除首尾多余空格和换行符
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return null; // 提取失败时返回默认值
    }

    private void updateLabel(StaffLabel label, String newText, String defaultText) {
        if (newText != null && !newText.isEmpty()) {
            label.setText(newText);
        } else {
            label.setText(defaultText);
        }
    }
}
