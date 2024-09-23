package com.push.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * Description:
 * Create DateTime: 2020-04-22 09:37
 *
 * 

 */
public class ExcelUtils {

    /**
     * 获取ExcelWriter
     *
     * @param type 文件类型 {@link ExcelType}
     * @return ExcelWriter
     */
    private static ExcelWriter getExcelWriter(ExcelType type) {
        ExcelWriter writer = null;
        switch (type) {
            case XLS:
                writer = ExcelUtil.getWriter();
                break;
            case XLSX:
                writer = ExcelUtil.getWriter(true);
        }
        return writer;
    }

    /**
     * 导出并下载普通xls类型的excel文件
     *
     * @param rows     记录集合
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, String fileName, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(rows, fileName, ExcelType.XLS, request, response);
    }

    /**
     * 导出并下载指定类型的普通excel文件
     *
     * @param rows     记录集合
     * @param fileName 文件名称
     * @param type     文件类型 {@link ExcelType}
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(rows, fileName, type, null, request, response);
    }

    /**
     * 导出并下载带有样式的excel文件
     *
     * @param rows     记录集合
     * @param fileName 文件名称
     * @param type     文件类型 {@link ExcelType}
     * @param styleSet 样式
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, String fileName, ExcelType type, StyleSet styleSet, HttpServletRequest request, HttpServletResponse response) {
        if (type == null) {
            return;
        }
        ExcelWriter writer = getExcelWriter(type);

        if (styleSet != null) {
            writer.setStyleSet(styleSet);
        }

        writer.write(rows, true);

        download(writer, fileName, type, request, response);
    }

    /**
     * 导出并下载普通xls类型的excel文件，需指定header别名
     *
     * @param rows        记录集合
     * @param headerAlias 别名
     * @param fileName    文件名
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, Map<String, String> headerAlias, String fileName, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(rows, headerAlias, fileName, ExcelType.XLS, request, response);
    }

    /**
     * 导出并下载指定类型的普通excel文件，需指定header别名
     *
     * @param rows        记录集合
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, Map<String, String> headerAlias, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(rows, headerAlias, fileName, type, null, request, response);
    }

    /**
     * 导出并下载带有样式的excel文件
     *
     * @param rows        记录集合
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param styleSet    样式
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Collection rows, Map<String, String> headerAlias, String fileName, ExcelType type, StyleSet styleSet, HttpServletRequest request, HttpServletResponse response) {
        if (type == null) {
            return;
        }
        ExcelWriter writer = getExcelWriter(type);

        if (styleSet != null) {
            writer.setStyleSet(styleSet);
        }

        if (headerAlias != null) {
            writer.setHeaderAlias(headerAlias);
        }

        writer.write(rows, true);

        download(writer, fileName, type, request, response);
    }

    /**
     * 导出并下载多个sheet的xls文件
     *
     * @param sheetMap 多个sheet 存储格式  key：sheet名称   value：rows
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, String fileName, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(sheetMap, fileName, ExcelType.XLS, request, response);
    }

    /**
     * 导出并下载指定类型的多个sheet的excel文件
     *
     * @param sheetMap 多个sheet 存储格式  key：sheet名称   value：rows
     * @param fileName 文件名称
     * @param type     文件类型 {@link ExcelType}
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(sheetMap, fileName, type, null, request, response);
    }

    /**
     * 导出并下载带有样式的多个sheet的excel文件
     *
     * @param sheetMap 多个sheet 存储格式  key：sheet名称   value：rows
     * @param fileName 文件名称
     * @param type     文件类型 {@link ExcelType}
     * @param styleSet 样式
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, String fileName, ExcelType type, StyleSet styleSet, HttpServletRequest request, HttpServletResponse response) {
        if (type == null) {
            return;
        }
        ExcelWriter writer = getExcelWriter(type);

        if (styleSet != null) {
            writer.setStyleSet(styleSet);
        }

        for (Map.Entry<String, Collection> next : sheetMap.entrySet()) {
            String key = next.getKey();
            Collection value = next.getValue();

            writer.setSheet(key);
            writer.write(value, true);
        }

        download(writer, fileName, type, request, response);
    }

    /**
     * 导出并下载多个sheet的xls文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, Map<String, String> headerAlias, String fileName, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(sheetMap, headerAlias, fileName, ExcelType.XLS, request, response);
    }

    /**
     * 导出并下载指定类型的多个sheet的excel文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, Map<String, String> headerAlias, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        exportExcelAndDownload(sheetMap, headerAlias, fileName, type, null, request, response);
    }

    /**
     * 导出并下载带有样式的多个sheet的excel文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param styleSet    样式
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelAndDownload(Map<String, Collection> sheetMap, Map<String, String> headerAlias, String fileName, ExcelType type, StyleSet styleSet, HttpServletRequest request, HttpServletResponse response) {
        if (type == null) {
            return;
        }
        ExcelWriter writer = getExcelWriter(type);

        if (styleSet != null) {
            writer.setStyleSet(styleSet);
        }

        for (Map.Entry<String, Collection> next : sheetMap.entrySet()) {
            String key = next.getKey();
            Collection value = next.getValue();

            writer.setSheet(key);
            writer.setHeaderAlias(headerAlias);
            writer.write(value, true);
        }

        download(writer, fileName, type, request, response);
    }

    /**
     * 导出并下载多个sheet的excel文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelWithMultilAliasAndDownload(Map<String, Collection> sheetMap, Map<String, Map<String, String>> headerAlias, String fileName, HttpServletRequest request, HttpServletResponse response) {
        exportExcelWithMultilAliasAndDownload(sheetMap, headerAlias, fileName, ExcelType.XLS, request, response);
    }

    /**
     * 导出并下载指定类型的多个sheet的excel文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelWithMultilAliasAndDownload(Map<String, Collection> sheetMap, Map<String, Map<String, String>> headerAlias, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        exportExcelWithMultilAliasAndDownload(sheetMap, headerAlias, fileName, type, null, request, response);
    }

    /**
     * 导出并下载带有样式的多个sheet的excel文件
     *
     * @param sheetMap    多个sheet 存储格式  key：sheet名称   value：rows
     * @param headerAlias 别名
     * @param fileName    文件名称
     * @param type        文件类型 {@link ExcelType}
     * @param styleSet    样式
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportExcelWithMultilAliasAndDownload(Map<String, Collection> sheetMap, Map<String, Map<String, String>> headerAlias, String fileName, ExcelType type, StyleSet styleSet, HttpServletRequest request, HttpServletResponse response) {
        if (type == null) {
            return;
        }
        ExcelWriter writer = getExcelWriter(type);

        if (styleSet != null) {
            writer.setStyleSet(styleSet);
        }

        for (Map.Entry<String, Collection> next : sheetMap.entrySet()) {
            String key = next.getKey();
            Collection value = next.getValue();

            writer.setSheet(key);
            Map<String, String> alias = headerAlias.get(key);
            writer.setHeaderAlias(alias);
            writer.write(value, true);
        }

        download(writer, fileName, type, request, response);
    }

    /**
     * 下载文件
     *
     * @param writer   ExcelWriter
     * @param fileName 文件名称
     * @param type     文件类型
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private static void download(ExcelWriter writer, String fileName, ExcelType type, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }
        ServletOutputStream out = null;

        try {
            String userAgent = request.getHeader("User-Agent");
            boolean isIE = (userAgent != null) && (userAgent.toLowerCase().contains("msie"));
            response.reset();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "must-revalidate, no-transform");
            response.setDateHeader("Expires", 0L);
            switch (type) {
                case XLS:
                    response.setContentType("application/vnd.ms-excel;charset=utf-8");
                    break;
                case XLSX:
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            }
            fileName = fileName + "." + type.code;
            if (isIE) {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            } else {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
            if (out != null) {
                IoUtil.close(out);
            }
        }
    }


    @AllArgsConstructor
    public enum ExcelType {
        XLS("xls"),

        XLSX("xlsx");

        String code;
    }
}
