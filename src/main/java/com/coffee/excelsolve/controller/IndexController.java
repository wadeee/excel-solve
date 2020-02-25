package com.coffee.excelsolve.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index() {
        return "/index";
    }

    @PostMapping("/a")
    public void indexPost(@RequestParam("originFileA") MultipartFile originFile,
                          @RequestParam("dataFilesA") List<MultipartFile> dataFiles,
                          HttpServletResponse response) throws IOException {
        InputStream inputStream = originFile.getInputStream();
        String originFileName = originFile.getOriginalFilename();
        Workbook targetWorkbook;
        if (originFileName.substring(originFileName.indexOf(".")).equals(".xls")) {
            targetWorkbook = new HSSFWorkbook(inputStream);
        } else {
            targetWorkbook = new XSSFWorkbook(inputStream);
        }
        inputStream.close();
        Sheet targetSheet = targetWorkbook.getSheetAt(0);

        int index = 1;
        for (MultipartFile dataFile : dataFiles) {
            String dataFileName = dataFile.getOriginalFilename();
            if (dataFileName.isEmpty()) break;
            String shopNo = dataFileName.substring(dataFileName.indexOf("(") + 1, dataFileName.indexOf(")"));
            InputStream dataInputStream = dataFile.getInputStream();
            Workbook dataWorkbook;
            if (dataFileName.substring(dataFileName.indexOf(".")).equals(".xls")) {
                dataWorkbook = new HSSFWorkbook(dataInputStream);
            } else {
                dataWorkbook = new XSSFWorkbook(dataInputStream);
            }
            dataInputStream.close();
            Sheet dataSheet = dataWorkbook.getSheetAt(0);

            List<Cell> baseCostCells = new ArrayList<>();
            for (Row row : dataSheet) {
                for (Cell cell : row) {
                    if (cell.toString().equals("金额(元)")) {
                        baseCostCells.add(cell);
                    }
                }
            }

            Row titleRow = targetSheet.getRow(0);
            Map<String, Integer> titleMap = new HashMap<>();
            for (Cell cell : titleRow) {
                titleMap.put(cell.toString(), cell.getColumnIndex());
            }

            index ++;
            targetSheet.createRow(index);
            targetSheet.getRow(index).createCell(titleMap.get("店号")).setCellValue(shopNo);
            targetSheet.getRow(index).createCell(titleMap.get("基装\n报价金额")).setCellValue(dataSheet.getRow(baseCostCells.get(0).getRowIndex()+5).getCell(baseCostCells.get(0).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基础装修及安装工程")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+1).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("增容施工费")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+2).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("其他工程")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+3).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("建筑工程一切险")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+4).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基装\n一审金额")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+5).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基装\n二审金额")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+5).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("施工面积")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex()+1).getCell(baseCostCells.get(1).getColumnIndex()+1).getNumericCellValue());
        }

        FileOutputStream fileOutputStream = new FileOutputStream("/media/" + originFileName);
        targetWorkbook.write(fileOutputStream);
        fileOutputStream.close();

        Path file = Paths.get("/media/", originFileName);
        response.addHeader("Content-Disposition", "attachment; filename="+ new String(originFileName.getBytes("utf-8"),"iso-8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Files.copy(file, response.getOutputStream());
        response.getOutputStream().flush();
    }
}
