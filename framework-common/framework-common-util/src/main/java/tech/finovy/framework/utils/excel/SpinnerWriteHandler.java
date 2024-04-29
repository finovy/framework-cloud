package tech.finovy.framework.utils.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.List;
import java.util.Map;

public class SpinnerWriteHandler implements SheetWriteHandler {
    //列，下拉数据数组
    private Map<Integer, List<String>> mapDropDown;
    private int firstRow = 1;
    private int lastRow = 2000;

    public SpinnerWriteHandler(Map<Integer, List<String>> mapDropDown) {
        this.mapDropDown = mapDropDown;
    }

    public SpinnerWriteHandler(Map<Integer, List<String>> mapDropDown, int firstRow, int lastRow) {
        this.mapDropDown = mapDropDown;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        ///开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        for (Map.Entry<Integer, List<String>> entry : mapDropDown.entrySet()) {
            /***起始行、终止行、起始列、终止列**/
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, entry.getKey(), entry.getKey());
            /***设置下拉框数据**/
            String[] strArray = new String[entry.getValue().size()];
            DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue().toArray(strArray));
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(dataValidation);
        }

    }
}
