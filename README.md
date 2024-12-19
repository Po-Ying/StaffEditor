這是軟體工程第七組設計的軟體
StaffEditor

使用該軟體時需要設定一些環境變數

## **設定環境變數以使用 Tesseract-OCR**

若要讓本應用程式正常執行，請依照以下步驟設置 Tesseract 的環境變數：

### **步驟 1: 安裝 Tesseract-OCR**
1. 下載 Tesseract-OCR：
   - 前往 [Tesseract 官方下載頁面](https://github.com/UB-Mannheim/tesseract/wiki)。
   - 選擇適合您作業系統的版本並安裝。

2. 默認安裝目錄為：
   ```
   C:\Program Files\Tesseract-OCR\
   ```

---

### **步驟 2: 確認語言數據文件**
1. 確保目錄 `C:\Program Files\Tesseract-OCR\tessdata\` 下存在需要的語言數據文件，例如：
   - `chi_tra.traineddata`（繁體中文）
   - `eng.traineddata`（英文）
   
2. 若文件不存在，請從 [Tesseract 語言數據庫](https://github.com/tesseract-ocr/tessdata) 下載所需語言數據，並將文件放置於上述目錄。

---

### **步驟 3: 配置 `TESSDATA_PREFIX` 環境變數**
1. 開啟 **系統環境變數設定**：
   - Windows: 按下 `Win + S`，搜尋「環境變數」並選擇 **編輯系統環境變數**。
   - macOS 或 Linux: 在終端機中編輯 `~/.bashrc` 或 `~/.zshrc`，具體操作見下文。

2. **新增環境變數**：
   - 變數名稱：`TESSDATA_PREFIX`
   - 變數值：`C:\Program Files\Tesseract-OCR\`

3. Windows 詳細操作：
   - 點擊 **環境變數**。
   - 在 **系統變數** 或 **使用者變數** 中點擊 **新增**。
   - 輸入上述名稱和路徑，然後點擊 **確定**。

4. macOS 或 Linux：
   - 打開終端機，編輯 `~/.bashrc` 或 `~/.zshrc` 文件：
     ```bash
     export TESSDATA_PREFIX="/usr/local/share/tessdata"
     ```
   - 保存文件並執行以下命令，使變更生效：
     ```bash
     source ~/.bashrc
     ```

---

### **步驟 4: 驗證環境配置**
1. 開啟命令提示字元或終端機，輸入以下命令：
   ```bash
   tesseract --list-langs
   ```
2. 若配置成功，應顯示安裝的語言列表，例如：
   ```
   List of available languages (2):
   chi_tra
   eng
   ```

3. 若語言列表未顯示或報錯，請確認：
   - `TESSDATA_PREFIX` 環境變數已正確設置。
   - 語言數據文件已正確安裝到 `tessdata` 目錄。

---

完成上述步驟後，您即可成功運行應用程式！