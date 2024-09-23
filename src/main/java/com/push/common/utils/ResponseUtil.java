package com.push.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Title: ResponseUtil.class
 * Description:
 * Create DateTime: 2018/7/5 11:30
 *
 * 

 */
public class ResponseUtil {

    public static void responseAsText(String text, HttpServletResponse resp) {
        if (!resp.isCommitted()) {
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setContentType("text/plain");
            OutputStream outs = null;
            try {
                outs = resp.getOutputStream();
                outs.write(StringUtils.trimToEmpty(text).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {

            } finally {
                IOUtils.closeQuietly(outs);
            }
        }
    }
}
