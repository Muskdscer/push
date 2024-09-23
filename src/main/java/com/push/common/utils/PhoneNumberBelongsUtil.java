package com.push.common.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description:
 * Create DateTime: 2020/4/11 13:08
 *
 * 

 */
public class PhoneNumberBelongsUtil {

    private static final String formatSpecifier = "^1[3|4|5|7|8][0-9]{9}$";
    //正则模板：编译
    private static Pattern fsPattern = Pattern.compile(formatSpecifier);

    /**
     * 查询手机号码查询归属地
     *
     * @param phoneNumber 手机号码
     * @return map （省<prov>、市<city>归属）
     * @since 1.0.0
     */
    public static Map<String, Object> checkPhoneNumberBelongs(String phoneNumber) {
        Map<String, Object> map = new HashMap<>();
        //校验是否为手机号码
        if (phoneNumber.length() != 11 || !fsPattern.matcher(phoneNumber).matches()) {
            return map;
        }

        StringBuffer url = new StringBuffer();
        url.append("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=");
        url.append(phoneNumber);
        url.append("&co=&resource_id=6004&t=1498214330640&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206964374771341681_1498213853507&_=1498213853531");

        URL openUrl = null;
        URLConnection conn = null;
        BufferedReader br = null;
        String inputLine = null;

        try {
            openUrl = new URL(url.toString());
            conn = openUrl.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            StringBuffer sb = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            List<String> asList = Arrays.asList(String.valueOf(sb).split(","));
            for (String string : asList) {
                if (string.contains("city") || string.contains("prov")) {
                    String[] split = string.replaceAll(" ", "").replaceAll("\"", "").split(":");
                    map.put(split[0], split[1]);
                    continue;
                }
            }

        } catch (Exception e) {
            return map;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return map;
    }
}
