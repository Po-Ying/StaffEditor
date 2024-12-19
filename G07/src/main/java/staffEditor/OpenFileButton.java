package staffEditor;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.image.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


public class OpenFileButton extends IconButton {
    StaffPage page;
    private Map<Integer, StaffPage> pages; 
    OpenFileButton(Toolbar p, StaffPage page) {
        super(p);
        if (page == null) {
            throw new IllegalArgumentException("StaffPage cannot be null.");
        }
        this.page = page;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV loaded successfully!");

        // 加载图标资源
        imageURL = cldr.getResource("images/open-folder.png");
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
            this.setIcon(icon);
        } else {
            System.out.println("Image resource not found.");
        }

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
            
            // 只接受圖片（jpg, png）和PDF文件
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image and PDF Files", "jpg", "png", "pdf"));
            fileChooser.setAcceptAllFileFilterUsed(false);

            int returnValue = fileChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath().toLowerCase();

                // 如果選擇的是圖片或PDF文件
                if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".pdf")) {
                    // 查找同名的 XML 文件
                    String xmlFilePath = getXmlFilePath(selectedFile);
                    File xmlFile = new File(xmlFilePath);

                    // 如果找到了對應的 XML 文件，則打開它
                    if (xmlFile.exists()) {
                        openXmlFile(xmlFile);
                    } else {
                        JOptionPane.showMessageDialog(this, "No matching XML file found for: " + selectedFile.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // 處理圖片或PDF文件
                    if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
                        processImageFile(selectedFile);
                    } else if (filePath.endsWith(".pdf")) {
                        processPDFFile(selectedFile);
                    }
                }
            }
        }
    private String getXmlFilePath(File imageOrPdfFile) {
            // 去掉圖片或PDF文件的擴展名，然後添加 ".xml" 擴展名
            String fileNameWithoutExtension = imageOrPdfFile.getName().replaceFirst("[.][^.]+$", "");
            File parentDir = imageOrPdfFile.getParentFile();
            
            return new File(parentDir, fileNameWithoutExtension + ".xml").getAbsolutePath();
        }
    private void openXmlFile(File xmlFile) { 
            try {
                // 解析 XML 文件
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);

                // 獲取所有頁面節點
                NodeList pageList = document.getElementsByTagName("page");

                for (int i = 0; i < pageList.getLength(); i++) {
                    Node pageNode = pageList.item(i);
                    if (pageNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element pageElement = (Element) pageNode;
                        int pageNumber = Integer.parseInt(pageElement.getAttribute("number"));
                        System.out.println("Page Number: " + pageNumber);

                        // 獲取頁面中的音符
                        NodeList notesList = pageElement.getElementsByTagName("note");
                        List<NoteData> tupletNotes = new ArrayList<>(); // 局部变量

                        for (int j = 0; j < notesList.getLength(); j++) {
                            Node noteNode = notesList.item(j);
                            if (noteNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element noteElement = (Element) noteNode;

                                // 獲取音符的屬性
                                int x = Integer.parseInt(noteElement.getAttribute("x"));
                                int y = Integer.parseInt(noteElement.getAttribute("y"));
                                String typeString = noteElement.getAttribute("type");
                                boolean isTuplet = Boolean.parseBoolean(noteElement.getAttribute("tuplet")); // 判斷是否有符槓

                                try {
                                    // 將 String 轉換為 longType
                                    longType type = longType.valueOf(typeString);
                                    page.addNoteinPanel(x, y, type);
                                    System.out.println("Added note at (" + x + ", " + y + ") with type " + type);

                                    NoteData noteData = new NoteData(x, y, type);

                                    if (isTuplet) {
                                        tupletNotes.add(noteData);
                                    }

                                    // 每两个音符调用一次 drawTuplet
                                    if (tupletNotes.size() == 2) {
                                        page.drawTuplet(tupletNotes);
                                        tupletNotes.clear(); // 清空以便下一组符槓音符
                                    }
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid note type: " + typeString);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to open XML file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
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

            // 假设单页宽度
            int pageWidth = 1100;
            int totalPages = (int) Math.ceil((double) img.getWidth() / pageWidth);
            
            for (int i = 0; i < totalPages; i++) {
                BufferedImage pageImage = img.getSubimage(
                        i * pageWidth, 0, 
                        Math.min(pageWidth, img.getWidth() - i * pageWidth), 
                        img.getHeight()
                );

                // 提取标题、作者和乐器信息
                String title = extractTextFromImage(pageImage.getSubimage(340, 33, 500, 75));
                String author = extractTextFromImage(pageImage.getSubimage(750, 120, 300, 30));
                String instrument = extractTextFromImage(pageImage.getSubimage(100, 100, 150, 30));
                System.out.println(title + author + instrument);

                if (i == 0) {
                    // 更新第一页面内容
                    updateLabel(page.staffTitle, title, "未知標題");
                    updateLabel(page.authorTitle, author, "未知作者");
                    updateLabel(page.instrumentTitle, instrument, "未知樂器");
                } else {
                    // 创建新页面并填充内容
                    NewPageButton newPageButton = new NewPageButton(this.parent);
                    newPageButton.doSomething(); // 执行新页面的创建操作

                    updateLabel(page.authorTitle, author, "未知作者");
                    updateLabel(page.instrumentTitle, instrument, "未知樂器");
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to process image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void processPDFFile(File file) {
        try {
            // 使用 PDFBox 解析 PDF
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            int totalPages = document.getNumberOfPages();
            for (int i = 0; i < totalPages; i++) {
                BufferedImage pageImage = pdfRenderer.renderImageWithDPI(i, 300);

                // 提取标题、作者和乐器信息
                String title = extractTextFromImage(pageImage.getSubimage(340, 33, 500, 75));
                String author = extractTextFromImage(pageImage.getSubimage(750, 120, 300, 30));
                String instrument = extractTextFromImage(pageImage.getSubimage(100, 100, 150, 30));

                if (i == 0) {
                    // 更新第一页面内容
                    updateLabel(page.staffTitle, title, "未知標題");
                    updateLabel(page.authorTitle, author, "未知作者");
                    updateLabel(page.instrumentTitle, instrument, "未知樂器");
                } else {
                    // 创建新页面并填充内容
                    NewPageButton newPageButton = new NewPageButton(this.parent);
                    newPageButton.doSomething(); // 执行新页面的创建操作

                    updateLabel(page.authorTitle, author, "未知作者");
                    updateLabel(page.instrumentTitle, instrument, "未知樂器");
                }

                
            }
            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to process PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String extractTextFromImage(BufferedImage image) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("chi_tra+eng");
        try {
            // 使用 OCR 提取文本
            String text = tesseract.doOCR(image);
            return text != null ? text.trim() : ""; // 提取失败时返回空字符串
        } catch (TesseractException e) {
            e.printStackTrace();
            return ""; // 异常情况下返回空字符串
        }
    }

    



    private void updateLabel(StaffLabel label, String newText, String defaultText) {
        label.setText(newText.isEmpty() ? defaultText : newText);
    }



}

