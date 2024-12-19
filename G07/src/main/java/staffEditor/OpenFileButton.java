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

public class OpenFileButton extends IconButton {
    StaffPage page;

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

                // 调用 Audiveris 生成 MusicXML，并在程序内使用
                exportMusicXmlFromImage(file);
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

                // 调用 Audiveris 生成 MusicXML，并在程序内使用
                exportMusicXmlFromImage(file);
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

    private void exportMusicXmlFromImage(File imageFile) {
            try {
                // 设置 Audiveris JAR 文件路径和主类
                String audiverisPath = "C:\\Program Files\\Audiveris\\lib\\audiveris.jar"; // 路径
                String audiverisMainClass = "org/audiveris/omr/Main.class";  // 主类名

                // 构建命令行，确保路径包含空格时加上引号
                String command = String.format("java -cp \"%s\" %s -batch -exportMusicXml -output - \"%s\"",
                        audiverisPath, audiverisMainClass, imageFile.getAbsolutePath());

                // 执行命令并读取标准输出
                Process process = Runtime.getRuntime().exec(command);

                // 读取 Audiveris 的输出（即 MusicXML 内容）
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder musicXmlBuilder = new StringBuilder();
                String line;

                // 读取输出的 MusicXML 内容
                while ((line = reader.readLine()) != null) {
                    musicXmlBuilder.append(line).append("\n");
                }

                // 等待命令执行完毕
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    String musicXmlContent = musicXmlBuilder.toString();
                    System.out.println("MusicXML 内容：");
                    System.out.println(musicXmlContent);

                    // 在这里，您可以进一步处理或使用 musicXmlContent
                    // 比如解析它，加载到某个界面，或直接将其传递给其他方法进行处理
                } else {
                    System.out.println("处理过程中出错。");
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        System.out.println(errorLine);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to export MusicXML: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }



    private void updateLabel(StaffLabel label, String newText, String defaultText) {
        label.setText(newText.isEmpty() ? defaultText : newText);
    }



    private void recognizeNotesWithPositions(BufferedImage image) {
            // 将 BufferedImage 转换为 Mat
            Mat targetMat = bufferedImageToMat(image);

            // 转换为灰度图（如果不是灰度图）
            if (targetMat.channels() == 3) {
                Imgproc.cvtColor(targetMat, targetMat, Imgproc.COLOR_BGR2GRAY);
            }

            // 存储识别到的音符类型和位置
            Map<String, List<Rect>> notePositions = new LinkedHashMap<>();

            // 模板匹配识别音符类型
            notePositions.put("全音符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("whole.png"), "全音符", notePositions.get("全音符"));

            notePositions.put("半音符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("half_note.png"), "半音符", notePositions.get("半音符"));

            notePositions.put("四分音符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("quarter_note.png"), "四分音符", notePositions.get("四分音符"));

            notePositions.put("八分音符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("eighth_note.png"), "八分音符", notePositions.get("八分音符"));

            notePositions.put("十六分音符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("sixteenth_note.png"), "十六分音符", notePositions.get("十六分音符"));

            notePositions.put("全休止符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("whole_rest.png"), "全休止符", notePositions.get("全休止符"));

            notePositions.put("二分休止符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("half_rest.png"), "二分休止符", notePositions.get("二分休止符"));

            notePositions.put("四分休止符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("quarter_note_rest.png"), "四分休止符", notePositions.get("四分休止符"));

            notePositions.put("八分休止符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("eighth_note_rest.png"), "八分休止符", notePositions.get("八分休止符"));

            notePositions.put("十六分休止符", new ArrayList<>());
            matchTemplateAndRecognize(targetMat, loadTemplate("sixteenth_rest.png"), "十六分休止符", notePositions.get("十六分休止符"));

            // 输出音符位置和类型，并绘制到图像上
            for (Map.Entry<String, List<Rect>> entry : notePositions.entrySet()) {
                String noteType = entry.getKey();
                for (Rect rect : entry.getValue()) {
                    int x = rect.x + rect.width / 2;
                    int y = rect.y + rect.height / 2;
                    System.out.println("音符类型: " + noteType + " | 位置: x = " + x + ", y = " + y);

                    // 在图像上绘制边界框和音符类型
                    Imgproc.rectangle(targetMat, rect, new Scalar(0, 255, 0), 2);
                    Imgproc.putText(targetMat, noteType, new Point(rect.x, rect.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 0, 0), 1);
                }
            }

            // 保存标记后的图像
            Imgcodecs.imwrite("output_with_notes.png", targetMat);
            System.out.println("标记后的图像已保存: output_with_notes.png");
        }

        private Mat loadTemplate(String fileName) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/" + fileName);

            if (inputStream == null) {
                System.err.println("模板加载失败: " + fileName);
                return new Mat();
            }

            try {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                Mat matImage = bufferedImageToMat(bufferedImage);
                if (matImage.empty()) {
                    throw new IOException("转换为 Mat 时失败: " + fileName);
                }
                return matImage;
            } catch (IOException e) {
                e.printStackTrace();
                return new Mat();
            }
        }

        private String matchTemplateAndRecognize(Mat targetMat, Mat template, String noteType, List<Rect> matchedPositions) {
            if (template.empty()) {
                System.err.println("模板为空，跳过匹配: " + noteType);
                return "未識別";
            }

            Mat result = new Mat();
            Imgproc.matchTemplate(targetMat, template, result, Imgproc.TM_CCOEFF_NORMED);
            double threshold = 0.8; // 匹配阈值

            while (true) {
                Core.MinMaxLocResult localMax = Core.minMaxLoc(result);
                if (localMax.maxVal >= threshold) {
                    Point matchLoc = localMax.maxLoc;

                    // 获取匹配区域的边界框
                    Rect rect = new Rect((int) matchLoc.x, (int) matchLoc.y, template.width(), template.height());
                    matchedPositions.add(rect);

                    // 抑制当前最大值区域
                    Imgproc.rectangle(result, rect, new Scalar(0), -1);
                } else {
                    break;
                }
            }

            return matchedPositions.isEmpty() ? "未識別" : noteType;
        }

        private Mat bufferedImageToMat(BufferedImage bufferedImage) {
            byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
            Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC1);
            mat.put(0, 0, data);
            return mat;
        }
        private String recognizeNote(BufferedImage image) {
                // 使用 OCR 或其他方法识别音符类型
                return "音符类型"; // 假设返回音符类型的字符串
            }

        private void processGrayImage(Mat grayMat) {
            // 对图像进行高斯模糊，减少噪声
            Mat blurredMat = new Mat();
            Imgproc.GaussianBlur(grayMat, blurredMat, new Size(5, 5), 1.5);

            // 使用Canny算法进行边缘检测
            Mat edges = new Mat();
            Imgproc.Canny(blurredMat, edges, 50, 150);

            // 找到图像中的轮廓
            java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
            Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // 在图像上绘制所有找到的轮廓，并打印出音符的位置
            for (int i = 0; i < contours.size(); i++) {
                // 根据轮廓的面积来筛选出较大的轮廓，假设音符大小相对较大
                double area = Imgproc.contourArea(contours.get(i));
                if (area > 100) {  // 可以根据实际情况调整这个阈值
                    // 获取轮廓的边界框
                    Rect boundingBox = Imgproc.boundingRect(contours.get(i));

                    // 打印出音符的中心位置 (x, y)
                    int x = boundingBox.x + boundingBox.width / 2;  // 计算音符的X坐标（中心点）
                    int y = boundingBox.y + boundingBox.height / 2; // 计算音符的Y坐标（中心点）
                    System.out.println("音符位置: x = " + x + ", y = " + y);

                    // 在图像中标记该位置（如果需要）
                    // Imgproc.rectangle(targetMat, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0), 2);
                }
            }

            // 将图像保存下来，输出标记过音符位置的图像
            // Imgcodecs.imwrite("output_image_with_notes.png", targetMat);
        }
}


    /*private void BufferedImageToMat(BufferedImage bufferedImage, Mat mat) {
        // 将 BufferedImage 转换为 OpenCV Mat
        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, pixels);
    }*/





   /* private List<NoteData> extractNotesFromImage(BufferedImage image) {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // 加载 OpenCV 库

            // 将 BufferedImage 转为 Mat
            Mat mat = bufferedImageToMat(image);

            // 转为灰度图像
            Mat gray = new Mat();
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

            // 应用二值化
            Mat binary = new Mat();
            Imgproc.threshold(gray, binary, 127, 255, Imgproc.THRESH_BINARY_INV);

            // 检测五线谱的线条
            List<Line> staffLines = detectStaffLines(binary);

            // 检测音符位置
            List<NoteData> notes = detectNotes(binary, staffLines);

            return notes;
        }

        // 将 BufferedImage 转为 Mat
        private Mat bufferedImageToMat(BufferedImage bi) {
            Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
            byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, data);
            return mat;
        }

        // 检测五线谱线条
        private List<Line> detectStaffLines(Mat binary) {
                List<Line> staffLines = new ArrayList<>();
                Mat lines = new Mat();
                Imgproc.HoughLinesP(binary, lines, 1, Math.PI / 180, 100, 50, 10);

                for (int i = 0; i < lines.rows(); i++) {
                    double[] l = lines.get(i, 0);
                    // 将线条的起点和终点作为 Point 对象存储
                    Point start = new Point(l[0], l[1]);
                    Point end = new Point(l[2], l[3]);
                    staffLines.add(new Line(start, end));  // 假设你有一个 Line 类，或者直接用两个 Point 来代替
                }
                return staffLines;
            }

        // 检测音符位置
        private List<NoteData> detectNotes(Mat binary, List<Line> staffLines) {
            List<NoteData> notes = new ArrayList<>();
            Mat contours = new Mat();
            List<MatOfPoint> contourList = new ArrayList<>();
            Imgproc.findContours(binary, contourList, contours, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            for (MatOfPoint contour : contourList) {
                Rect boundingRect = Imgproc.boundingRect(contour);

                // 基于矩形大小过滤非音符元素
                if (boundingRect.width > 5 && boundingRect.height > 10) {
                    Point center = new Point(boundingRect.x + boundingRect.width / 2, boundingRect.y + boundingRect.height / 2);
                    String pitch = determinePitch(center, staffLines);
                    String duration = determineDuration(boundingRect);

                    notes.add(new NoteData(pitch, duration, (int) center.x, (int) center.y));
                }
            }
            return notes;
        }

        
     // 根据音符相对五线谱线条的位置确定音高
        private String determinePitch(Point center, List<Line> staffLines) {
            // 找到与音符最近的五线谱线条
            Line closestLine = null;
            double minDistance = Double.MAX_VALUE;

            for (Line line : staffLines) {
                // 使用 line.getStart() 和 line.getEnd() 来获取起点和终点
                double distance = Math.abs(center.y - line.getStart().y);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestLine = line;
                }
            }

            // 假设五线谱是标准 G 谱号，从下到上依次为 E4, G4, B4, D5, F5
            String[] pitches = {"E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5"};
            int index = (int) Math.round(minDistance / (staffLines.get(1).getStart().y - staffLines.get(0).getStart().y)) * 2;
            return pitches[index];
        }


        // 根据音符的矩形形状确定时值
        private String determineDuration(Rect boundingRect) {
            if (boundingRect.height > boundingRect.width * 2) {
                return "quarter";
            } else if (boundingRect.height < boundingRect.width) {
                return "half";
            } else {
                return "whole";
            }
        }

    private void addNotesToPanel(List<NoteData> noteDataList) {
        for (NoteData noteData : noteDataList) {
            // 创建音符并添加到面板
            JLabel noteLabel = createNoteLabel(noteData);
            page.panel.add(noteLabel);
        }
        page.panel.repaint();
    }

    private JLabel createNoteLabel(NoteData noteData) {
        String pitch = noteData.getPitch();
        String duration = noteData.getDuration();
        URL imageURL = getNoteImage(pitch, duration);

        ImageIcon icon = new ImageIcon(imageURL);
        ImageIcon imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT));

        // 创建音符标签
        JLabel noteLabel = new JLabel(imageIcon);
        noteLabel.setLocation(noteData.getX(), noteData.getY());
        noteLabel.putClientProperty("notePitch", pitch);
        noteLabel.putClientProperty("noteDuration", duration);
        noteLabel.setSize(30, 40);
        noteLabel.setVisible(true);
        return noteLabel;
    }

    private URL getNoteImage(String pitch, String duration) {
        // 根据音符的音高和时值返回图像路径
        String imagePath = "images/" + duration + "_note.png";
        return getClass().getClassLoader().getResource(imagePath); // 获取音符图像
    }*/

