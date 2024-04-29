package tech.finovy.framework.utils.excel;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class RowWriteHandlerImpl implements CellWriteHandler {
    //那些列要单元格设置text格式
    private List<Integer> textColumn = new ArrayList<>();

    public RowWriteHandlerImpl() {
    }

    public RowWriteHandlerImpl(List<Integer> textColumn) {
        this.textColumn = textColumn;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        //设置单元格格式为文本
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(cellStyle);
        textColumn.forEach(c->{
            sheet.setDefaultColumnStyle(c, cellStyle);
        });
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }
}
