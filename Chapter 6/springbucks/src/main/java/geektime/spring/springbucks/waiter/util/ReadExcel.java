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
import java.util.*;

public class ReadExcel {

    String excelPath = "C:\\Users\\Administrator\\Desktop\\sample.xlsx";

    // 维护一个 对象节点 hashmap
    HashMap<String, Node> complexHashMap = new HashMap<>();
    // 维护一个 level, nodeName
    HashMap<String,String> levelNameHashMap = new HashMap<>();

    Node rootNode = Node.builder().name("Root").build();
    // 记录当前所在结构体的 父节点 nodeName
    String parentNodeName = "Root";

    List<List<Node>> levelResult = new ArrayList();

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
//                    int lastRowIndex = sheet.getLastRowNum();
                    int lastRowIndex = 351;
                    System.out.println("firstRowIndex: "+firstRowIndex);
                    System.out.println("lastRowIndex: "+lastRowIndex);

                    // 记录前一条记录的 nodeName 单元格列号
                    int preColumnIndex = 0;

                    // 首次执行为空
                    if(complexHashMap.isEmpty()){
                        complexHashMap.put("Root",rootNode);
                    }

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
                                    String tempKey = firstRowIndex + "_" + cIndex + "_" + tempCell.getStringCellValue();
                                    System.out.println("firstRowIndex+\"_\"+cIndex+\"_\"+tempCell.toString() = " + tempKey);

                                    String nodeName = tempCell.getStringCellValue();
                                    String type = row.getCell(13).getStringCellValue();
                                    String min = row.getCell(14).toString();
                                    String max = row.getCell(15).toString();

                                    Node tempNode = Node.builder().name(nodeName)
                                            .type(type)
                                            .minOccurs(min)
                                            .maxOccurs(max).build();

                                    // 维护level Name HashMap
                                    levelNameHashMap.put(String.valueOf(cIndex),nodeName);
                                    if("complex".equals(type)){
                                        complexHashMap.put(nodeName,tempNode);
                                    }
                                    if (cIndex >1) {
                                        parentNodeName = levelNameHashMap.get(String.valueOf(cIndex - 1));
                                    }

//                                    rootNode.mergeNode(parentNodeName,tempNode);
                                    this.mergeSubNode(parentNodeName,rootNode,tempNode);

                                    System.out.println("前一条记录列号：" + preColumnIndex
                                            + " parentName = "+ parentNodeName
                                            + " currentName = " + tempNode.getName());

                                    preColumnIndex = cIndex;
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

    public Node findNode(Node root, String searchKey){
        Node result = null;
        Queue<Node> queue = new ArrayDeque<>();

        queue.add(root);

        while(queue.size()!=0){
            Node tempNode = queue.poll();
            if(searchKey.equals(tempNode.getName())){
                result = tempNode;
                break;
            }

            if(tempNode.getKids() != null && !tempNode.getKids().isEmpty()){
                tempNode.getKids().forEach((k,v)->queue.add(v));
            }
        }

        return result;
    }

    public Node mergeSubNode(String parentName,Node root, Node subNode){
        Node parent = this.findNode(root,parentName);
        if("complex".equals(subNode.getType())){
            parent.addKids(subNode);
        }else {
            parent.addFields(subNode);
        }

        return parent;
    }

    public List<Node> dfsOrder(Node root){
        List<Node> resList = new ArrayList<>();
        Queue<Node> queue = new ArrayDeque<>();

        if(root != null){
            queue.add(root);
        }

        while(!queue.isEmpty()){
            // 弹出父节点，并且直接插入结果
            Node node = queue.poll();
            resList.add(node);

            // 该层级下的fields
            if(node.getFields() != null && !node.getFields().isEmpty()){
                node.getFields().forEach((k,v)->queue.add(v));
            }

            if(node.getKids() != null && !node.getKids().isEmpty()){
                node.getKids().forEach((k,v)->queue.add(v));
            }
        }

        return resList;
    }

    /**
     * 递归实现 levelOrder 层遍历
     * levelResult
     */
    public List<List<Node>> levelOrder(Node root) {
        if (root != null) traverseNode(root, 0);
        return levelResult;
    }

    private void traverseNode(Node node, int level) {
        if (levelResult.size() <= level) {
            levelResult.add(new ArrayList<>());
        }
        levelResult.get(level).add(node);

        if(node.getKids() != null && !node.getKids().isEmpty()){
            node.getKids().forEach((k,v)->traverseNode(v,level+1));
        }
    }

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel();
        readExcel.readExcel();

        System.out.println("readExcel.complexHashMap.size() = " + readExcel.complexHashMap.size());

        System.out.println("root = " + readExcel.rootNode);

        List<Node> resList = new ArrayList<>();
        resList = readExcel.dfsOrder(readExcel.rootNode);
        resList.forEach((node -> System.out.println("node.getName() = " + node.getName())));

        // 层遍历
        readExcel.traverseNode(readExcel.rootNode,0);
        System.out.println("readExcel.levelResult = " + readExcel.levelResult);
    }

}
