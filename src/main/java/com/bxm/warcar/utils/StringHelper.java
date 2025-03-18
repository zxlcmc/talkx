/*
 * Copyright 2015 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.bxm.warcar.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringHelper extends StringUtils {

    public static final char[] CODE_ARRAY = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    }; 
    
    public static String random(char[] scope, int len) {
        if (ArrayUtils.isEmpty(scope))
            scope = CODE_ARRAY;
        StringBuilder str = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            str.append(scope[RandomUtils.nextInt(0, scope.length)]);
        }
        return str.toString();
    }
    
    public static String random(int len) {
        return random(CODE_ARRAY, len);
    }
    
    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    }
    
    public static String randomInt(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RandomUtils.nextInt(0, 10));
        }
        return sb.toString();
    }
    
    public static String convert(byte[] b) {
        if (ArrayUtils.isEmpty(b))
            return null;
        return new String(b, StandardCharsets.UTF_8);
    }

    public static byte[] convert(String string) {
        if (StringUtils.isBlank(string))
            return null;
        return string.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Unicode字符编码转普通字符串。
     * 
     * <pre>
     * StringHelper.unicode2native("\\u8ba2\\u8d2d\\u5931\\u8d25"); = "订购失败"
     * </pre>
     * @param str
     * @return
     */
    public static String unicode2native(String str) {
        char aChar;
        int len = str.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = str.charAt(x++);
            if (aChar == '\\') {
                aChar = str.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = str.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
                else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * 占位符替换<br/>
     *
     * @param prefix 占位符前缀，如："{"
     * @param suffix 占位符后缀，如："}"
     * @param valueMap 占位符中字段名->替换值，如：CODE->bxm
     * @param sources 源字符串列表,如：http://www.xxx.com?code={CODE}
     * @return java.util.List<java.lang.String>
     * @throws
     */
    public static List<String> replaceHolder(String prefix, String suffix, Map valueMap, List<String> sources){
        if(CollectionUtils.isEmpty(sources)){
            return null;
        }
        validation(valueMap);
        // StrSubstitutor非线程安全
        StrSubstitutor substitutor = new StrSubstitutor(valueMap, prefix, suffix);
        List<String> respList = new ArrayList<>();
        for (String source : sources){
            if(StringUtils.isBlank(source)){
                continue;
            }
            respList.add(substitutor.replace(source));
        }
        return respList;
    }

    /**
     * 占位符替换<br/>
     *
     * @param prefix 占位符，如："{"
     * @param suffix 占位符，如："}"
     * @param valueMap 占位字段名->替换值，如：CODE->bxm
     * @param sources 源字符串列表,如：http://www.xxx.com?code={CODE}
     * @return java.util.List<java.lang.String>
     * @throws
     */
    public static List<String> replaceHolder(String prefix, String suffix, Map valueMap, String... sources){
        if(null == sources || sources.length < 1){
            return null;
        }
        validation(valueMap);
        // StrSubstitutor非线程安全
        StrSubstitutor substitutor = new StrSubstitutor(valueMap, prefix, suffix);
        List<String> respList = new ArrayList<>();
        for (String source : sources){
            if(StringUtils.isBlank(source)){
                continue;
            }
            respList.add(substitutor.replace(source));
        }
        return respList;
    }

    /**
     * 占位符替换<br/>
     *
     * @param prefix 占位符，如："{"
     * @param suffix 占位符，如："}"
     * @param valueMap 占位字段名->替换值，如：CODE->bxm
     * @param source 源字符串,如：http://www.xxx.com?code={CODE}
     * @return java.lang.String
     * @throws
     */
    public static String replaceHolder(String prefix, String suffix, Map valueMap, String source){
        if(StringUtils.isBlank(source)){
            return null;
        }
        validation(valueMap);
        // StrSubstitutor非线程安全
        StrSubstitutor substitutor = new StrSubstitutor(valueMap, prefix, suffix);
        return substitutor.replace(source);
    }

    /**
     * 校验并填充valueMap的value值
     */
    private static void validation(Map valueMap) {
        valueMap.forEach((k, v) -> {
            if(null == v){
                valueMap.put(k, StringUtils.EMPTY);
            }
        });
    }

    /**
     * 是否为新版本<br/>
     * 线上版本号是否在某标准版本号（含标准版本号）以上<br/>
     * 纯数字比较<br/>
     *
     * @param standard_version 标准版本号
     * @param online_version 线上版本号
     * @return online_version >= standard_version
     */
    public static boolean isNewVersion(String standard_version, String online_version) {
        if(StringUtils.isEmpty(online_version)){
            return Boolean.FALSE;
        }
        if (StringUtils.isEmpty(standard_version) || standard_version.equals(online_version)) {
            return Boolean.TRUE;
        }
        //过滤非数字和非字符点，并分割版本号数字段
        String[] standardArray = standard_version.replaceAll("[^0-9.]", "").split("[.]");
        String[] onlineArray = online_version.replaceAll("[^0-9.]", "").split("[.]");
        //获取数字段最小长度
        int length = standardArray.length < onlineArray.length ? standardArray.length : onlineArray.length;
        for (int i = 0; i < length; i++) {
            if(StringUtils.isEmpty(onlineArray[i])){
                return Boolean.FALSE;
            }
            if (StringUtils.isEmpty(standardArray[i])){
                return Boolean.TRUE;
            }

            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(standardArray[i])) {
                return Boolean.TRUE;
            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(standardArray[i])) {
                return Boolean.FALSE;
            }
        }
        return onlineArray.length > standardArray.length;
    }
}
