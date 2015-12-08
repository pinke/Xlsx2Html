package ru.ntmedia;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App {
    private final String srcFolder;
    private final String destFolder;

    public App(String srcFolder, String destFolder) {
        this.srcFolder = srcFolder;
        this.destFolder = destFolder;
    }

    public static void main(String[] args) {

        System.out.println(getDataFromXlsx("f:\\tmp\\Excel\\Книга10.xlsx"));
        return;
        /*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainDialog dlg = new MainDialog();
                dlg.pack();
                dlg.setTitle("Конвертация файлов из Excel в HTML");
                dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
            }
        });
        */
    }
    public boolean getTemplateCfg(String templatePath) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        try {
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return true;
    }
    public void convertAllFiles() {
        File dir = new File(this.srcFolder);
        if(dir == null) {
            return;
        }
        for( File f : dir.listFiles() ) {
            try {
                convertXlsxFile(f.getCanonicalPath(), getHtmlFileName(f.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void convertXlsxFile(String srcFileName, String dstFileName) {
        String s;
        if( (s = getDataFromXlsx(srcFileName)).equals("") ) {
            System.out.println( "EMPTY DATA" );
            return;
        }
        try {
            writeHtmlFile( dstFileName, s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public String getHtmlFileName(String fileName) {
        // cut path
        String baseFileName = Paths.get(fileName).getFileName().toString();
        // cut extension
        String rootFileName = baseFileName.substring(0, baseFileName.lastIndexOf("."));
        return this.destFolder + File.separator + rootFileName + ".html";
    }
    public static void writeHtmlFile(String fileName, String data) throws IOException {
        if(fileName == null || fileName.equals("")) {
            throw new IllegalArgumentException( "Имя файла не указано." );
        }
        FileWriter fw = new FileWriter(fileName);
        fw.write(data);
        fw.close();
    }
    public static String getDataFromXlsx(String fileName) {
        ArrayList<String[]> tableData = new ArrayList<>();
        String result = "";
        int rowIndex = -1;
        try {
            FileInputStream file = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()) {
                rowIndex++;
                String[] tableRow = new String[2];
                boolean addRow = true;
                Row row = rowIterator.next();
                rowloop:
                for(int colIndex = 0; colIndex < 2; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    if( cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        //addRow = false;
                        //break rowloop;
                        continue;
                    }
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            tableRow[colIndex] = cell.getStringCellValue().replace("\u00A0", " ").trim();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            tableRow[colIndex] += String.valueOf(cell.getNumericCellValue());
                            break;
                    }
                }
                if(addRow) {
                    //tableRow += "</tr>\n";
                    tableData.add(tableRow);
                }
            }
            //result += "</table>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String updateLastCell(String result, String data) {
        return result;
    }
    public static void createSpreadShit() {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[]{1, "Amit", "Shukla"});
        data.put("3", new Object[]{2, "Lokesh", "Gupta"});
        data.put("4", new Object[]{3, "John", "Adwards"});
        data.put("5", new Object[]{4, "Brian", "Schultz"});

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }
        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("howtodoinjava_demo.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
