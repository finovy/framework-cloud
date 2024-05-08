package tech.finovy.framework.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import tech.finovy.framework.common.SecurityEncryption;
import tech.finovy.framework.result.PageData;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class BzUtils {

    /**
     * 随机主键
     * 1711-f31549ef0
     *
     * @return
     */
    public static String generateUuid() {
        String uuid = RandomStringUtils.randomAlphanumeric(32);
        return uuid.substring(0, 4) + "-" + uuid.substring(4, 13);
    }

    /**
     * 随机id
     * B40415923
     *
     * @return
     */
    public static String generatePid() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        Random random = new Random();
        String upper = new StringBuffer().append(uppercase.charAt(random.nextInt(26))).toString();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 7; i++) {
            int number = random.nextInt(10);
            sb.append(digits.charAt(number));
        }
        String num = sb.append(digits.charAt(random.nextInt(7))).toString();
        return upper + num;
    }

    /**
     * 随机表Flow 流水主键
     * //SN202005071519421244
     *
     * @return
     */
    public static String getFlowId() {
        return "SN" + createSerial();
    }

    /**
     * 生成注册审核序列号
     *
     * @return
     */
    public static String getRegSerial() {
        return "RG" + createSerial();
    }

    /**
     * 生成档案审核序列号
     *
     * @return
     */
    public static String getArchivesSerial() {
        return "AC" + createSerial();
    }

    /**
     * 生成工单序列号
     *
     * @return
     */
    public static String getWorkOrderSerial() {
        return "TK" + createSerial();
    }

    /**
     * 生成时间序列号+4位随机数
     *
     * @return string
     */
    public static String createSerial() {
        //202005071519421244
        //随机数
        Integer anInt = ThreadLocalRandom.current().nextInt(1000, 9999);
        return createDateSerial() + anInt.toString();
    }

    /**
     * 生成时间序列号
     *
     * @return string
     */
    public static String createDateSerial() {
        //20200507151942
        Date date = new Date();
        LocalDateTime ldt = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dtf.format(ldt);
    }

    /**
     * ElasticSearche数据转json数组
     *
     * @param jsonObject
     * @return
     */
    public static JSONArray getEsSource(JSONObject jsonObject) {
        if(!jsonObject.containsKey("hits")){
            return new JSONArray();
        }
        JSONObject hits = jsonObject.getJSONObject("hits");
        if(!hits.containsKey("hits")){
            return new JSONArray();
        }
        JSONArray jsonArray = hits.getJSONArray("hits");
        return jsonArray.stream().map(obj -> {
            JSONObject returnObj = new JSONObject();
            JSONObject jsonObj = (JSONObject) obj;
            returnObj.put("value", jsonObj.get("_source"));
            return returnObj;
        }).collect(Collectors.toCollection(JSONArray::new));
    }

    /**
     * ElasticSearche数据转java对象集合
     *
     * @param jsonObject
     * @param <T>        clazz
     * @return
     */
    public static <T> PageData<T> getEsSourceTojava(JSONObject jsonObject, Class<T> clazz) {
        PageData<T> pageData = new PageData<>();
        if(!jsonObject.containsKey("hits")){
            pageData.setTotalCounts(0);
            pageData.setData(new ArrayList<>());
            return pageData;
        }
        JSONObject hits = jsonObject.getJSONObject("hits");
        Long total = hits.getLong("total");
        JSONArray jsonArray = hits.getJSONArray("hits");
        if(!hits.containsKey("hits")){
            pageData.setTotalCounts(0);
            pageData.setData(new ArrayList<>());
            return pageData;
        }
        List<T> list = jsonArray.stream().map(obj -> {
            JSONObject jsonObj = (JSONObject) obj;
            JSONObject source = jsonObj.getJSONObject("_source");
            //转义符、大括号前后引号、json数组前后引号 过滤
            String sourceString = source.toJSONString().replace("\\", "").replace("\"{", "{").replace("}\"", "}").replace("\"[{\"", "[{\"").replace("\"}]\"", "\"}]");
            return JSON.to(clazz, JSON.parse(sourceString));
        }).collect(Collectors.toList());
        pageData.setTotalCounts(total);
        pageData.setData(list);
        return pageData;
    }

    /**
     * getEsIdSourceToJava数据转java对象集合
     *
     * @param jsonObject
     * @param <T>        clazz
     * @return
     */
    public static <T> T getEsIdSourceToJava(JSONObject jsonObject, Class<T> clazz) {
        JSONObject source = (JSONObject) jsonObject.get("_source");
        //转义符、大括号前后引号、json数组前后引号 过滤
        String sourceString = source.toJSONString().replace("\\", "").replace("\"{", "{").replace("}\"", "}").replace("\"[{\"", "[{\"").replace("\"}]\"", "\"}]");
        T ordersFlow = JSON.to(clazz, JSON.parse(sourceString));
        return (T) ordersFlow;
    }

    /**
     * 对象  下划线key 转驼峰
     *
     * @param json
     */
    public static void convert(Object json) {
        if (json instanceof JSONArray) {
            JSONArray arr = (JSONArray) json;
            for (Object obj : arr) {
                convert(obj);
            }
        } else if (json instanceof JSONObject) {
            JSONObject jo = (JSONObject) json;
            Set<String> keys = jo.keySet();
            String[] array = keys.toArray(new String[keys.size()]);
            for (String key : array) {
                Object value = jo.get(key);
                String[] key_strs = key.split("_");
                if (key_strs.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < key_strs.length; i++) {
                        String ks = key_strs[i];
                        if (!"".equals(ks)) {
                            if (i == 0) {
                                sb.append(ks);
                            } else {
                                int c = ks.charAt(0);
                                if (c >= 97 && c <= 122) {
                                    int v = c - 32;
                                    sb.append((char) v);
                                    if (ks.length() > 1) {
                                        sb.append(ks.substring(1));
                                    }
                                } else {
                                    sb.append(ks);
                                }
                            }
                        }
                    }
                    jo.remove(key);
                    jo.put(sb.toString(), value);
                }
                convert(value);
            }
        }
    }

    /**
     * json字符串   下划线key 转驼峰
     *
     * @param jsonStr
     * @return
     */
    public static Object convert(String jsonStr) {
        Object obj = JSON.parse(jsonStr);
        convert(obj);
        return obj;
    }


    /**
     * java对象驼峰转 下划线json
     * 对象转json date类型转字符串yyyy-MM-dd HH:mm:ss
     *
     * @param clazz
     * @return
     */
    public static String javaObjectToJson(Object clazz) {
        return JSON.toJSONString(clazz, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static Date getUtcTime() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);//获取时间偏移量
        int dstOffset = cal.get(Calendar.DST_OFFSET);//取得夏令时差
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));//从本地时间扣除时差
        return new Date(cal.getTimeInMillis());
    }

    public static String getUtcNowStr() {
        return getUtcNowStr("yyyy-MM-dd HH:mm:ss");
    }

    public static String getUtcNowStr(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatUTCDateTime(getUtcTime());
    }

    public static String formatUTCNow(Date utcTime, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return df.format(utcTime);
    }

    public static String formatUTCDate(Date utcTime) {
        return formatUTCNow(utcTime, "yyyy-MM-dd 00:00:00");
    }

    public static String formatUTCDateTime(Date utcTime) {
        return formatUTCNow(utcTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatGMT0Str(Date utcTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return df.format(utcTime);
    }

    public static Timestamp parseUTCDateTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        try {
            return new Timestamp(df.parse(dateStr).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static String saveBase64encode(String v) {
        String replace = v.replace("+", "-").replace("\\", "_");
        if (StringUtils.equals(replace.substring(v.length() - 1), "=")) {
            return replace.substring(0, replace.length() - 1);
        }
        return replace;
    }

    public static String packRequestSignParams(Map<String, ?> map, String connector, String separator) {
        List<String> collect = map.entrySet().stream()
                .map(entry -> entry.getKey() + connector + entry.getValue())
                .collect(Collectors.toList());
        return StringUtils.join(collect, separator);
    }

    public static String formatUrlParam(Map<String, Object> map) {
        return packRequestSignParams(map, "=", "&");
    }


    /**
     * 用户相关的解密
     *
     * @param uuid       uuid
     * @param ciphertext ciphertext
     * @param secret     secret
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String userSecurityEncryption(String uuid, String ciphertext, String secret) {
        String id = uuid.toUpperCase();
        int length = id.length();
        for (int i = 0; i < 16 - length; i++) {
            id = id.concat("0");
        }
        return new String(RemoveExcess(SecurityEncryption.javaOpensslDecryptNoPadding(SecurityEncryption.hexToByte(ciphertext), secret, id)), StandardCharsets.UTF_8);

    }

    public static byte[] RemoveExcess(byte[] bytes) {
        while (bytes[bytes.length - 1] == 0x0) {
            byte[] cByte = new byte[bytes.length - 1];
            System.arraycopy(bytes, 0, cByte, 0, bytes.length - 1);
            bytes = cByte;
        }
        return bytes;
    }

    public static List<List<String>> groupList(List<String> list, int toIndex) {
        List<List<String>> listGroup = new ArrayList<>();
        int listSize = list.size();
        for (int i = 0; i < list.size(); i += toIndex) {
            if (i + toIndex > listSize) {
                toIndex = listSize - i;
            }
            List<String> newList = list.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }


    public static String decryptCommEmailSMS(String date, String key, String iv) {
        if (ObjectUtils.isEmpty(date)) {
            return date;
        }
        byte[] encryText = SecurityEncryption.javaOpensslDecryptNoPadding(
                SecurityEncryption.hexToByte(date), key, iv);
        return new String(RemoveExcess(encryText));
    }

    public static String encryptCommEmailSMS(String date, String key, String iv) {
        if (ObjectUtils.isEmpty(date)) {
            return date;
        }
        return SecurityEncryption.byteToHex(SecurityEncryption.javaOpensslEncryptNoPadding(date.getBytes(), key, iv));
    }

    /**
     * 用户'uemail', 'uphone', 'address', 'document_num 加密
     * @param uuid   拼装的iv
     * @param info   需要加密的字符
     * @param secret nacos配置的密钥
     * @return
     */
    public static String encryptUserBaseInfo(String uuid,String info,String secret) {
        String id = uuid.toUpperCase();
        int length = id.length();
        for (int i = 0; i < 16 - length; i++) {
            id = id.concat("0");
        }
        return SecurityEncryption.byteToHex(SecurityEncryption.javaOpensslEncryptNoPadding(info.getBytes(), secret, id));
    }

}
