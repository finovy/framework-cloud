package tech.finovy.framework.logappender.push.internals;

import tech.finovy.framework.logappender.entry.LogContent;
import tech.finovy.framework.logappender.entry.LogItem;

import java.util.List;

/**
 * @author dtype.huang
 */
public abstract class AbstractLogSizeCalculator {
    private AbstractLogSizeCalculator() {
    }

    public static int calculate(LogItem logItem) {
        int sizeInBytes = 4;
        for (LogContent content : logItem.getLogContents()) {
            if (content.mKey != null) {
                sizeInBytes += content.mKey.length();
            }
            if (content.mValue != null) {
                sizeInBytes += content.mValue.length();
            }
        }
        return sizeInBytes;
    }

    public static int calculate(List<LogItem> logItems) {
        int sizeInBytes = 0;
        for (LogItem logItem : logItems) {
            sizeInBytes += calculate(logItem);
        }
        return sizeInBytes;
    }
}
