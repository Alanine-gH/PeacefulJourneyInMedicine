package com.PJDM.untils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具类
 *
 * @author: ruoyi-alanine
 * @date: 2025/12/26 10:09
 */
@Component
public class ExcelUtil {

    /**
     * 导出Excel文件
     *
     * @param response  HTTP响应对象
     * @param dataList  导出数据列表
     * @param sheetName Excel表名
     * @param fileName  导出文件名
     * @param headMap   表头映射（key：字段名，value：表头名）
     */
    public void exportExcel(HttpServletResponse response, List<Map<String, Object>> dataList,
                            String sheetName, String fileName, Map<String, String> headMap) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 文件名需要URLEncoder编码
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 构建表头列表
        List<List<String>> headList = buildHeadList(headMap);

        // 构建数据列表
        List<List<Object>> dataListForExcel = buildDataList(dataList, headMap);

        // 设置表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        // 设置表头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 设置内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 构建样式策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 导出Excel
        ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream()).head(headList).registerWriteHandler(horizontalCellStyleStrategy);
        writerBuilder.sheet(sheetName).doWrite(dataListForExcel);
    }

    /**
     * 构建表头列表
     */
    private List<List<String>> buildHeadList(Map<String, String> headMap) {
        List<List<String>> headList = new ArrayList<>();
        for (Map.Entry<String, String> entry : headMap.entrySet()) {
            List<String> headItem = new ArrayList<>();
            headItem.add(entry.getValue());
            headList.add(headItem);
        }
        return headList;
    }

    /**
     * 构建数据列表
     */
    private List<List<Object>> buildDataList(List<Map<String, Object>> dataList, Map<String, String> headMap) {
        List<List<Object>> dataListForExcel = new ArrayList<>();
        for (Map<String, Object> dataMap : dataList) {
            List<Object> rowData = new ArrayList<>();
            for (String key : headMap.keySet()) {
                rowData.add(dataMap.getOrDefault(key, ""));
            }
            dataListForExcel.add(rowData);
        }
        return dataListForExcel;
    }
}
