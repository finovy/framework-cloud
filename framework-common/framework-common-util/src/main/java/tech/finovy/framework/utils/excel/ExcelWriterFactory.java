package tech.finovy.framework.utils.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.exception.GlobalAppExceptionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelWriterFactory extends ExcelWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalAppExceptionHandler.class);

    private final OutputStream outputStream;
    private int sheetNo = 1;

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactory write(List<? extends BaseRowModel> list, String sheetName, BaseRowModel object) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, object.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception ex) {
            LOGGER.warn("Exception {}", ex.getMessage(), ex);
            try {
                outputStream.flush();
            } catch (IOException e) {
                LOGGER.warn("Exception {}", e.getMessage(), e);
            }
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
        }
    }
}
