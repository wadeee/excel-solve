package com.coffee.excelsolve.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
    public void fileA(@RequestParam("originFileA") MultipartFile originFile,
                      @RequestParam("dataFilesA") List<MultipartFile> dataFiles,
                      HttpServletResponse response) throws IOException {
        Workbook targetWorkbook = fileToWorkbook(originFile);
        Sheet targetSheet = targetWorkbook.getSheetAt(0);

        int index = 1;
        for (MultipartFile dataFile : dataFiles) {
            String dataFileName = dataFile.getOriginalFilename();
            if (dataFileName.isEmpty()) break;
            String shopNo = dataFileName.substring(dataFileName.indexOf("(") + 1, dataFileName.indexOf(")"));
            Workbook dataWorkbook = fileToWorkbook(dataFile);
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

            index++;
            targetSheet.createRow(index);
            targetSheet.getRow(index).createCell(titleMap.get("店号")).setCellValue(shopNo);
            targetSheet.getRow(index).createCell(titleMap.get("基装\n报价金额")).setCellValue(dataSheet.getRow(baseCostCells.get(0).getRowIndex() + 5).getCell(baseCostCells.get(0).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基础装修及安装工程")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 1).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("增容施工费")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 2).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("其他工程")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 3).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("建筑工程一切险")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 4).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基装\n一审金额")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 5).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("基装\n二审金额")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 5).getCell(baseCostCells.get(1).getColumnIndex()).getNumericCellValue());
            targetSheet.getRow(index).createCell(titleMap.get("施工面积")).setCellValue(dataSheet.getRow(baseCostCells.get(1).getRowIndex() + 1).getCell(baseCostCells.get(1).getColumnIndex() + 1).getNumericCellValue());
        }

        workbookToResponse(targetWorkbook, response);
    }

    @PostMapping("/b")
    public void fileB(@RequestParam("originFileB") MultipartFile originFile,
                      @RequestParam("dataFilesB") List<MultipartFile> dataFiles,
                      HttpServletResponse response) throws IOException {
        Workbook targetWorkbook = fileToWorkbook(originFile);
        Sheet targetSheet = targetWorkbook.getSheetAt(0);

        int index = 1;
        for (MultipartFile dataFile : dataFiles) {
            String dataFileName = dataFile.getOriginalFilename();
            if (dataFileName.isEmpty()) break;
            String shopNo = dataFileName.substring(dataFileName.indexOf("(") + 1, dataFileName.indexOf(")"));
            Workbook dataWorkbook = fileToWorkbook(dataFile);
            Sheet dataSheet = dataWorkbook.getSheetAt(0);

            List<Cell> baseCostCells = new ArrayList<>();
            for (Row row : dataSheet) {
                for (Cell cell : row) {
                    if (cell.toString().equals("（五）工程总造价")) {
                        baseCostCells.add(row.getCell(cell.getColumnIndex() + 8));
                    }
                }
            }

            Row titleRow = targetSheet.getRow(0);
            Map<String, Integer> titleMap = new HashMap<>();
            for (Cell cell : titleRow) {
                titleMap.put(cell.toString(), cell.getColumnIndex());
            }

            for (Row row : targetSheet) {
                if (row.getLastCellNum() > titleMap.get("店号")) {
                    Cell cell = row.getCell(titleMap.get("店号"));
                    if (cell.getCellTypeEnum().equals(CellType.NUMERIC) && String.valueOf((int) cell.getNumericCellValue()).equals(shopNo) ||
                            cell.getCellTypeEnum().equals(CellType.STRING) && cell.getStringCellValue().equals(shopNo)) {
                        Cell cell1 = row.getCell(titleMap.get("增项\n报价金额"));
                        Cell cell2 = row.getCell(titleMap.get("增项\n一审金额"));
                        Cell cell3 = row.getCell(titleMap.get("增项\n二审金额"));
                        if (cell2.getNumericCellValue() != cell3.getNumericCellValue()) return;

                        double num1 = cell1.getNumericCellValue();
                        double num2 = cell2.getNumericCellValue();
                        double num3 = cell3.getNumericCellValue();

                        cell1.setCellValue(num1 + baseCostCells.get(0).getNumericCellValue());
                        cell2.setCellValue(num2 + baseCostCells.get(1).getNumericCellValue());
                        cell3.setCellValue(num3 + baseCostCells.get(1).getNumericCellValue());
                        break;
                    }
                }
            }
        }

        workbookToResponse(targetWorkbook, response);
    }

    private Workbook fileToWorkbook(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String originFileName = file.getOriginalFilename();
        Workbook workbook;
        if (originFileName.substring(originFileName.lastIndexOf(".")).equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook(inputStream);
        }
        inputStream.close();
        return workbook;
    }

    private void workbookToResponse(Workbook workbook, HttpServletResponse response) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream("/media/xiaozhi");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        Path file = Paths.get("/media/", "xiaozhi");
        response.addHeader("Content-Disposition", "attachment; filename=xiaozhi");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Files.copy(file, response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
