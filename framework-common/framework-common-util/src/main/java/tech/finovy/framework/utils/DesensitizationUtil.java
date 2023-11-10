package tech.finovy.framework.utils;

import org.apache.commons.lang3.StringUtils;

public class DesensitizationUtil {
	 /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为星号<例子：李**>
     *
     * @param fullName 姓名
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }
 
    /**
     * [中文姓名] 只显示姓氏，其他隐藏为星号<例子：欧阳娜娜  ： 欧阳**>
     *
     * @param familyName  姓氏
     * @param givenName   名字
     * @return
     */
    public static String chineseName(String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        if(familyName.length()>1){
            String name = StringUtils.left(familyName, familyName.length());
            return StringUtils.rightPad(name, StringUtils.length(familyName+givenName), "*");
        }
        return chineseName(familyName + givenName);
    }
 
    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address 详细地址
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }
 
    /**
     * [手机固话] 电话中间隐藏，前面保留3位明文，后面保留4位明文 <例子：176****6506>
     *
     * @param num 电话号码
     * @param index 3
     * @param end 4
     * @return
     */
    public static String mobileEncrypt(String num,int index,int end) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, end), StringUtils.length(num), "*"), "***"));
    }
 
    /**
     * [电子邮箱] 邮箱前缀隐藏，用星号代替，@及后面的地址显示 <例子:******@163.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else{
            //特殊处理： index = 6 表示部分隐藏时默认用6个*表示用户名
            return StringUtils.rightPad(StringUtils.left(email, 3), 7, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * [自定义规则] 卡号开头用星号隐藏几位，每位1个星号 <例子:******789>
     *
     * @param cardNum
     * @param hideDigit 隐藏位数 6
     * @return
     */
    public static String hideLeftNum(String cardNum, int hideDigit) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        int length = StringUtils.length(cardNum);
        return StringUtils.leftPad(StringUtils.right(cardNum, length - hideDigit), length, "*");
    }
 
    /**
     * [自定义规则] 卡号后面用星号隐藏几位，每位1个星号<例子:6222600******>
     *
     * @param cardNum
     * @param hideDigit 隐藏位数 6
     * @return
     */
    public static String hideRightNum(String cardNum, int hideDigit) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        int length = StringUtils.length(cardNum);
        return StringUtils.rightPad(StringUtils.left(cardNum, length - hideDigit), length, "*");
    }
 
    /**
     * [自定义规则] 卡号中间用星号隐藏几位，每位1个星号<例子:622******600>
     *
     * @param cardNum
     * @param hideDigit 隐藏位数 6
     * @return
     */
    public static String hideCenterNum(String cardNum, int hideDigit) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        int length = StringUtils.length(cardNum);
        int index = (length - hideDigit) >> 1;
        if(hideDigit % 2 == 0){
            index += 1;
        }
        return StringUtils.left(cardNum, index).concat(StringUtils.leftPad(StringUtils.right(cardNum, length-index-hideDigit), length-index, "*"));
    }
 
    /**
     * [自定义规则] 卡号用星号隐藏，每位1个星号<例子:*********>
     *
     * @param cardNum
     * @return
     */
    public static String hideAllNum(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        int length = StringUtils.length(cardNum);
        return StringUtils.leftPad(StringUtils.right(cardNum, 0), length, "*");
    }

}
