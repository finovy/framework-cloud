package tech.finovy.framework.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;
import tech.finovy.framework.exception.BusinessException;
import tech.finovy.framework.exception.CommonExEnum;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel 文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo) {
        return readExcel(excel, rowModel, sheetNo, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel 文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo, int headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);

        if (reader == null) {
            return null;
        }

        reader.read(new Sheet(sheetNo, headLineNum, rowModel.getClass()));
        return excelListener.getDatas();
    }
    private static HorizontalCellStyleStrategy myHorizontalCellStyleStrategy(){
        //1 表头样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //表头前景设置淡蓝色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
//        headWriteFont.setBold(true);
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short)11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        List<WriteCellStyle>   listCntWritCellSty =  new ArrayList<>();

        //2 内容样式策略  样式一
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        //内容字体大小
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short)30);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //设置自动换行
        contentWriteCellStyle.setWrapped(true);
        //设置垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return  new HorizontalCellStyleStrategy(headWriteCellStyle, listCntWritCellSty);
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response HttpServletResponse
     * @param list 数据 list，每个元素为一个 BaseRowModel
     * @param fileName 导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public static <T> void writeExcel(HttpServletResponse response, List<?> list, String fileName,
                                      String sheetName, Class<T> headClazz) {

        EasyExcel.write(getOutputStream(fileName, response)).sheet(1).sheetName(sheetName).head(headClazz).registerWriteHandler(myHorizontalCellStyleStrategy()).doWrite(list);
    }

    public static <T> void writeExcelFile(String filePath, List<?> list,
                                      String sheetName, Class<T> headClazz) {

        EasyExcel.write(filePath, headClazz).sheet(sheetName).registerWriteHandler(myHorizontalCellStyleStrategy()).doWrite(list);
    }


    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        //创建本地文件
        fileName = fileName + ExcelTypeEnum.XLSX.getValue();
        try {
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName );

            return response.getOutputStream();
        } catch (Exception e) {
            throw new BusinessException(CommonExEnum.EXCEL_ERROR);
        }
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel 需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();

        if (filename == null || (!filename.toLowerCase().endsWith(ExcelTypeEnum.XLS.getValue()) && !filename.toLowerCase().endsWith(ExcelTypeEnum.XLSX.getValue()))) {
            throw new BusinessException(CommonExEnum.EXCEL_ERROR,"Unit file format error");
        }
        InputStream inputStream;

        try {
            inputStream = new BufferedInputStream(excel.getInputStream());

            return new ExcelReader(inputStream, null, excelListener, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 读取Excel表格数据，封装成实体
     *
     * @param inputStream
     * @param clazz
     * @param sheetNo
     * @param headLineMun
     * @return
     */
    public static Object readExcel(InputStream inputStream, Class<? extends BaseRowModel> clazz, Integer sheetNo,
                                   Integer headLineMun) {
        if (null == inputStream) {

            throw new NullPointerException("the inputStream is null!");
        }

        ExcelListener listener = new ExcelListener();
        ExcelReader reader = new ExcelReader(inputStream, valueOf(inputStream), null, listener);
        reader.read(new Sheet(sheetNo, headLineMun, clazz));

        return listener.getDatas();
    }

    /**
     * 根据输入流，判断为xls还是xlsx，该方法原本存在于easyexcel 1.1.0 的ExcelTypeEnum中。
     */
    public static ExcelTypeEnum valueOf(InputStream inputStream) {
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);

            if (FileMagic.OLE2.equals(fileMagic)) {
                return ExcelTypeEnum.XLS;
            }

            if (FileMagic.OOXML.equals(fileMagic)) {
                return ExcelTypeEnum.XLSX;
            }
            throw new BusinessException(CommonExEnum.EXCEL_ERROR,"excelTypeEnum can not null");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
