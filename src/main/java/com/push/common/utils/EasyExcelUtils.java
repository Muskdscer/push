package com.push.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Description:
 * Create DateTime: 2020-04-23 13:34
 *
 * 

 */
public class EasyExcelUtils {


    /**
     * excel 导出
     *
     * @param list           数据
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       文件名称
     * @param isCreateHeader 是否创建表头
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, exportParams, request, response);
    }

    /**
     * excel 导出
     *
     * @param list      数据
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名称
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, new ExportParams(title, sheetName, ExcelType.XSSF), request, response);
    }

    /**
     * excel导出
     *
     * @param list      数据
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名称
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static void exportExcel(List<?> list, String sheetName, Class<?> pojoClass, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName(sheetName);
        exportParams.setType(ExcelType.XSSF);
        defaultExport(list, pojoClass, fileName, exportParams, request, response);
    }

    /**
     * excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param exportParams 导出参数
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, exportParams, request, response);
    }

    /**
     * excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, request, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param exportParams 导出参数
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, request, response, workbook);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.XSSF);
        downLoadExcel(fileName, request, response, workbook);
    }


    //==================================================================================================================================================================================================================


    /**
     * 导出大数据excel
     *
     * @param sheetName   sheet名称
     * @param pojoClass   pojo类型
     * @param server      实现IExcelExportServer的实现类
     * @param queryParams 查询参数
     * @param fileName    文件名称
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportBigExcel(String sheetName, Class<?> pojoClass, IExcelExportServer server, Object queryParams, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName(sheetName);
        exportParams.setType(ExcelType.XSSF);
        defaultExportBigExcel(fileName, exportParams, pojoClass, server, queryParams, request, response);
    }

    /**
     * 导出大数据excel
     *
     * @param title       标题
     * @param sheetName   sheet名称
     * @param pojoClass   pojo类型
     * @param server      实现IExcelExportServer的实现类
     * @param queryParams 查询参数
     * @param fileName    文件名称
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    public static void exportBigExcel(String title, String sheetName, Class<?> pojoClass, IExcelExportServer server, Object queryParams, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        if (StringUtils.isNotBlank(title)) {
            exportParams.setCreateHeadRows(true);
        }
        defaultExportBigExcel(fileName, exportParams, pojoClass, server, queryParams, request, response);
    }

    /**
     * 默认的 大数据 excel导出
     *
     * @param fileName     文件名称
     * @param exportParams 导出参数
     * @param pojoClass    pojo类型
     * @param server       实现IExcelExportServer的实现类
     * @param queryParams  查询参数
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     */
    private static void defaultExportBigExcel(String fileName, ExportParams exportParams, Class<?> pojoClass, IExcelExportServer server, Object queryParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, pojoClass, server, queryParams);
        downLoadExcel(fileName, request, response, workbook);
    }

    /**
     * 下载
     *
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param workbook excel数据
     */
    private static void downLoadExcel(String fileName, HttpServletRequest request, HttpServletResponse response, Workbook workbook) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        }

        String userAgent = request.getHeader("User-Agent");
        boolean isIE = (userAgent != null) && (userAgent.toLowerCase().contains("msie"));
        response.reset();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "must-revalidate, no-transform");
        response.setDateHeader("Expires", 0L);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (fileName.endsWith("xls")) {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        }
        if (isIE) {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        } else {
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }

        try {
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       excel文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        return importExcel(file, titleRows, headerRows, false, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param needVerify 是否检验excel内容
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, boolean needVerify, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, needVerify, pojoClass);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   标题行
     * @param headerRows  表头行
     * @param needVerify  是否检验excel内容
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, boolean needVerify, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(needVerify);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("excel文件不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

}
