package geektime.spring.springbucks.waiter.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class ReadExcel {

    String excelPath = "C:\\Users\\Administrator\\Desktop\\sample.xlsx";

    private void readExcel(){
        try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
            //判断文件是否存在
            if (excel.isFile() && excel.exists()) {
                //.是特殊字符，需要转义！！！！！
                String[] split = excel.getName().split("\\.");
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    //文件流对象
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("文件类型错误!");
                    return;
                }

                //开始解析  //读取sheet 0
               //  Sheet sheet = wb.getSheetAt(0);
                Sheet sheet = wb.getSheet("(HEC) IN OUT");
                //第一行是列名，所以不读
                int firstRowIndex = 1;
                int lastRowIndex = 11;
                System.out.println("firstRowIndex: "+firstRowIndex);
                System.out.println("lastRowIndex: "+lastRowIndex);
                //遍历行
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
//                        int firstCellIndex = row.getFirstCellNum();
//                        int lastCellIndex = row.getLastCellNum();
//                        //遍历列
//                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
//                            Cell cell = row.getCell(cIndex);
//                            if (cell != null) {
//                                System.out.println(cell.toString() + "_" + cIndex);
//                            }
//                        }

                        Cell nodeNameCell = row.getCell(3);
                        if(nodeNameCell !=null ){
                            String nodeName = nodeNameCell.toString();
                            String type = row.getCell(13).toString();
                            String min = row.getCell(14).toString();
                            String max = row.getCell(15).toString();

                            System.out.println("nodeName+\"_\"+type+\"_\"+min+\"_\"+max = " + nodeName+"_"+type+"_"+min+"_"+max);
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel();
        readExcel.readExcel();
    }

}
