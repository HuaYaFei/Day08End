import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class xlsxText {
    @Test
    public void text() throws IOException {
//        G:\新建文件夹\01.xlsx
        //设置加载文件
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("G:\\新建文件夹\\01.xlsx"));
//        读取excel文件中第一个sheet标签页
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历sheet标签页
        for (Row cells : sheet) {
            //篇历行
            for (Cell cell : cells) {
                System.out.println(cell.getStringCellValue());
            }
        }
        workbook.close();

//
//
//        //创建工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook("G:\\\\新建文件夹\\\\01.xlsx");
////获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
//        XSSFSheet sheet = workbook.getSheetAt(0);
////遍历工作表获得行对象
//        for (Row row : sheet) {
////遍历行对象获取单元格对象
//            for (Cell cell : row) {
////获得单元格中的值
//                String value = cell.getStringCellValue();
//                System.out.println(value);
//            }
//        }
//        workbook.close();


    }
}
