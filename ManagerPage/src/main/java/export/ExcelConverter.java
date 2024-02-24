package export;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import menu.InventoryItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelConverter {

    ArrayList<InventoryItem> list;

    public ExcelConverter(ArrayList<InventoryItem> list){
        this.list = list;
    }

    /**
     * Displays a save location chooser where the user can add the name of the save file. Then it creates an Excel
     * file at the save location
     * @param window The window of the active app
     */
    public void exportToExcel(Window window){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Export Location");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel File", ".xlsx"),
                    new FileChooser.ExtensionFilter("Old Excel File (2003 and prior versions)", ".xls")
            );
            File location = fileChooser.showSaveDialog(window);

            if(location != null){
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Inventory Sheet");
                XSSFRow header = sheet.createRow(0);
                createHeaderCell(workbook, header, 0, "ID");
                createHeaderCell(workbook, header, 1, "Name");
                createHeaderCell(workbook, header, 2, "Category");
                createHeaderCell(workbook, header, 3, "Unit");
                createHeaderCell(workbook, header, 4, "Cost Per Unit($)");
                createHeaderCell(workbook, header, 5, "Available Quantity");

                for(int i = 0; i < list.size(); i++){
                    XSSFRow row = sheet.createRow(i + 1);
                    createContentCell(workbook, row, 0, String.valueOf(list.get(i).getId()));
                    createContentCell(workbook, row, 1, list.get(i).getName());
                    createContentCell(workbook, row, 2, list.get(i).getCategory());
                    createContentCell(workbook, row, 3, list.get(i).getUnit());
                    createContentCell(workbook, row, 4, String.valueOf(list.get(i).getPrice()));
                    createContentCell(workbook, row, 5, String.valueOf(list.get(i).getQuantity()));
                }

                for(int i = 0; i < 6; i++){
                    sheet.autoSizeColumn(i);
                }

                header.setHeight((short) 600);

                FileOutputStream fileOut = new FileOutputStream(location.getPath());
                workbook.write(fileOut);
                fileOut.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createHeaderCell(XSSFWorkbook workbook, XSSFRow header, int index, String value){
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.index);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFont(workbook.createFont());
        headerStyle.getFont().setBold(true);
        headerStyle.getFont().setColor(IndexedColors.WHITE.index);

        XSSFCell cell = header.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(headerStyle);
    }

    private void createContentCell(XSSFWorkbook workbook, XSSFRow row, int index, String value){
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFCell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

}
