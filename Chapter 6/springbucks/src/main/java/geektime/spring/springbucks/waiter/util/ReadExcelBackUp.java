package geektime.spring.springbucks.waiter.util;

import geektime.spring.springbucks.waiter.structure.Node;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class ReadExcelBackUp {

    String excelPath = "C:\\Users\\qi.b.yang\\OneDrive - Accenture\\Desktop\\sample.xlsx";

    // 维护一个 对象节点 hashmap
    HashMap<String, Node> complexHashMap = new HashMap<>();

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
                //第一行是列名，所以不读 // 测试 从 5 开始读是 pcXXXWSRequest
                int firstRowIndex = 5;
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: "+firstRowIndex);
                System.out.println("lastRowIndex: "+lastRowIndex);

                // 记录前一条记录的 nodeName 单元格列号
                int preColumnIndex = 0;
                Node preNode = Node.builder().build();
                // 记录当前所在结构体的 父节点 nodeName
//                String parentNodeName = "rootNoParent";

                //遍历行
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        // 遍历前部分列 确认 colIndex 经测试 从 1 B 开始 11 K 结束
                        int firstCellIndex = 1;
                        int endCellIndex = 11;
                        for(int cIndex = firstCellIndex;cIndex<endCellIndex;cIndex++){
                            Cell tempCell = row.getCell(cIndex);
                            // 过滤出只有 nodeName 的 Cell
                            if (tempCell!=null && !tempCell.getStringCellValue().isEmpty()) {
                                System.out.println("firstRowIndex+\"_\"+cIndex+\"_\"+tempCell.toString() = "
                                        + firstRowIndex + "_" + cIndex + "_" + tempCell.getStringCellValue());

                                String nodeName = tempCell.getStringCellValue();
                                String type = row.getCell(13).getStringCellValue();
                                String min = row.getCell(14).toString();
                                String max = row.getCell(15).toString();

                                Node tempNode = Node.builder().name(nodeName)
//                                        .parentName(parentNodeName)
                                        .type(type).minOccurs(min)
                                        .maxOccurs(max).build();;

                                // 当前节点是一个结构体，并且已经存在，则维护原有结构体
                                if ("complex".equals(type) && complexHashMap.containsKey(nodeName)) {
                                    // todo 这里可以对比两个结构体 做校验
                                    tempNode = complexHashMap.get(nodeName);
//                                    parentNodeName = nodeName;
                                }

                                // 设置父节点
                                // 首次执行为空
                                if("complex".equals(type) && complexHashMap.isEmpty()){
                                    tempNode.setParent(Node.builder().name("Root").build());
                                    complexHashMap.put(nodeName,tempNode);
                                    preNode = tempNode;
                                    continue;
                                }

                                System.out.println("前一条记录列号：" + preColumnIndex +" tempNode = " + tempNode);

                                // 当前节点是前序节点的子节点
                                if(cIndex > preColumnIndex){

                                    // 设置父节点
                                    // 首次执行为空
                                    if(tempNode.getParent()==null) {
                                        tempNode.setParent(complexHashMap.get(preNode.getName()));
                                    }

                                    if("complex".equals(tempNode.getType())){
                                        complexHashMap.put(nodeName,tempNode);
                                    } else {
                                        String newName = tempNode.getParent().getName();
                                        Node newNode = complexHashMap.get(tempNode.getParent().getName());
                                        newNode.addKids(tempNode);
                                        complexHashMap.put(newName,newNode);
                                    }

                                } else if (preColumnIndex > cIndex){
                                    // 当前节点是一个新的 complex 结构体

                                    // 设置父节点，前序节点的父节点 的父节点
                                    if(tempNode.getParent()== null){
                                        tempNode.setParent(preNode.getParent().getParent());
                                    }

                                    if("complex".equals(tempNode.getType())){
                                        tempNode.getParent().addKids(tempNode);
                                    } else {
                                        tempNode.getParent().addFields(tempNode);
                                    }
                                } else {
                                    // 当前节点和前序节点平级
                                    // 设置父节点，前序节点的父节点
                                    if(tempNode.getParent()== null){
//                                        tempNode.setParent(complexHashMap.get(preNode.getName()).getParent());
                                        tempNode.setParent(preNode.getParent());
                                    }

                                    if("complex".equals(tempNode.getType())){
                                        tempNode.getParent().addKids(tempNode);
                                    } else {
                                        tempNode.getParent().addFields(tempNode);
                                    }
                                }

                                preColumnIndex = cIndex;
                                preNode = tempNode;
                            }
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
        ReadExcelBackUp readExcel = new ReadExcelBackUp();
        readExcel.readExcel();

        System.out.println("readExcel.complexHashMap.size() = " + readExcel.complexHashMap.size());
    }

}
