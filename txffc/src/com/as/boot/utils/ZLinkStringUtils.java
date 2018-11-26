package com.as.boot.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class ZLinkStringUtils {
	public static int DEFAULT_BUFFER_SIZE = 1024;

	public static String conversionSpecialCharacters(String string) {
		return string.replaceAll("\\\\", "/");
	}

	public static Boolean stringToBoolean(String booleanString) {
		if (ZLinkStringUtils.isNotEmpty(booleanString)
				&& booleanString.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static String stringListToString(List<String> params, String sepator) {
		if (params.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String string : params) {
				buffer.append(string + sepator);
			}
			String result = buffer.toString();
			return result.substring(0, result.length() - 1);
		} else {
			return "";
		}
	}

	public static String getString(Object string, String replaceWord) {
		if (string == null) {
			return replaceWord;
		} else {
			return String.valueOf(string);
		}
	}

	public static String getString(Object object) {
		if (object == null)
			return "";
		if (object instanceof Integer) {
			return String.valueOf(object);
		} else if (object instanceof Float) {
			return String.valueOf(object);
		} else if (object instanceof Double) {
			return String.valueOf(object);
		} else if (object instanceof Float) {
			return String.valueOf(object);
		} else if (object instanceof Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object);
		} else if (object instanceof String) {
			return String.valueOf(object);
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param string
	 * @return 判断一个字符串是否为NULL或者是是否为"" 用法： StringUtils.isEmpty(NULL) 结果:true
	 *         StringUtils.isEmpty("") 结果:true StringUtils.isEmpty(" ") 结果:false
	 *         StringUtils.isEmpty("aa") 结果:false
	 *         当然，与isEmpty的相反函数为StringUtils.isNotEmpty();相当于!isEmpty()
	 */
	public static boolean isNull(String string) {
		return ZLinkStringUtils.isEmpty(string);
	}

	/***
	 * 根据0/1返回true/false
	 * 
	 * @param filter
	 * @return
	 */
	public static boolean returnFilter(int filter) {
		return filter == 0 ? false : true;
	}

	/**
	 * 判断字符串是否都是数字组成
	 * 
	 * @param numString
	 * @return
	 */
	public static boolean isNumber(String numString) {
		return ZLinkStringUtils.isNumeric(numString);
	}

	/**
	 * 邮箱验证 方法名：isEmail 创建人：xuchengfei 时间：2014年6月6日-下午3:14:46
	 * 
	 * @param str
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	public static boolean isEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 网络地址验证 
	 * 方法名：isHomepage 
	 * 创建人：Ason 
	 * 时间：2014年6月6日-下午3:15:10
	 * @param str
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	public static boolean isHomepage(String str) {
		String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(str).matches();
		
	}

	public static void main(String[] args) {
		/*String a = "hhhh0";
		String b = "的撒范德萨";
		System.out.println(containsChinese(a));
		System.out.println(containsChinese(b));*/
		System.out.println(MD5_32("nhmasonxt950203", null));
	}
	
	public static String matchString(String str, String regex) {
		Pattern pt = Pattern.compile(regex);
//		Pattern pt = Pattern.compile("http(s)?://.+(,|，)");
		Matcher matcher = pt.matcher(str);
		if (matcher.find())
			return matcher.group();
		return null;
	}
	
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);// 将给定的正则表达式编译到具有给定标志的模式中
		Matcher matcher = pattern.matcher(str);// 模式进行匹配字符�?
		return matcher.matches();
	}


	/**
	 * 
	 * @param str
	 * @return 去掉字符串两边的空格，如果输入的是""或者是NULL则返回NULL 用法 ： StringUtils.trim(" abc ")
	 *         结果："abc" StringUtils.trimToNull("") 结果：NULL
	 *         StringButils.trimToNull(NULL) 结果：NULL
	 */
	public static String trimToNull(String str) {
		return ZLinkStringUtils.trimToNull(str);
	}

	/**
	 * 统计单个字符在字符串中出现的次数
	 * 
	 * @param string
	 *            字符串
	 * @param c
	 *            单个字符串
	 * @return 返回次数
	 */
	public static int counter(String string, char c) {
		int count = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Ason 统计字符串中的中文个数，
	 * 
	 * @param s
	 *            字符串
	 * @return 返回中文出现的次数
	 * @throws UnsupportedEncodingException
	 */
	public static int getChineseCount(String s)
			throws UnsupportedEncodingException {
		char c;
		int chineseCount = 0;
		if (!"".equals("")) {
			try {
				s = new String(s.getBytes(), "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (isChineseChar(c)) {
				chineseCount++;
			}
		}
		return chineseCount;
	}

	/**
	 * Ason 统计字符串中的空白数，字母，数字
	 * 
	 * @param s
	 * @return
	 */
	public static String StringCountBlankNumCharacter(String s) {
		char ch;
		int character = 0, blank = 0, number = 0, other = 0;
		for (int i = 0; i < s.length(); i++) {
			ch = s.charAt(i);
			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
				character++;
			} else if (ch == ' ') {
				blank++;
			} else if (ch >= '0' && ch <= '9') {
				number++;
			}
		}
		return "字符串共有:" + character + "个字母," + blank + "个空格," + number
				+ "个数字,其他字符占有:" + other;
	}

	public static boolean isChineseChar(char c)
			throws UnsupportedEncodingException {
		return String.valueOf(c).getBytes("GBK").length > 1;
	}

	/**
	 * Ason 从文件中读取文件内容，
	 * 
	 * @param res
	 * @param filePath
	 * @return
	 */
	public static boolean stringToFile(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists()) {
				distFile.getParentFile().mkdirs();
			}
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					filePath)));
			char buf[] = new char[1024];
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * Ason 截取带汉字的字符串 strings : 字符串 copynum:截取的长度
	 */
	public static String subStringContainChinese(String strings, int copyNum) {
		String[] arr = strings.split("");
		strings = "";// 清空，用于存放已截取的字符串
		int cutNum = 0;
		int cc = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].getBytes().length == 1) {
				cutNum = cutNum + 1;
				strings = strings + arr[i];
			} else if (arr[i].getBytes().length == 2) {
				cc = cc + 1;
				cutNum = cutNum + 2;
				strings = strings + arr[i];
			}
			if (cutNum >= copyNum) {
				break;
			}
		}
		if (cutNum > copyNum) {
			return strings.substring(0, copyNum - cc);
		} else {
			return strings;
		}
	}

	/**
	 * 编码的转换
	 * 
	 * @param string
	 *            转换的字符串
	 * @param encoding
	 *            转换后的编码
	 * @return 返回转换后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String transferEncoding(String string, String encoding)
			throws UnsupportedEncodingException {
		return new String(string.getBytes("ISO8859-1"), encoding);
	}

	public String StringtoSql(String str) {
		if (str == null) {
			return "";
		} else {
			try {
				str = str.trim().replace('\'', (char) 32);
			} catch (Exception e) {
				return "";
			}
		}
		return str;
	}

	/**
	 * 整数的转换与0补齐
	 * 
	 * @param str
	 *            转换的数字
	 * @param length
	 *            转换的长度。不够0补齐.
	 * @return
	 */
	public static String formatNO(int str, int length) {
		float ver = Float.parseFloat(System
				.getProperty("java.specification.version"));
		String laststr = "";
		if (ver < 1.5) {
			try {
				NumberFormat formater = NumberFormat.getNumberInstance();
				formater.setMinimumIntegerDigits(length);
				laststr = formater.format(str).toString().replace(",", "");
			} catch (Exception e) {
				System.out.println("格式化字符串时的错误信息：" + e.getMessage());
			}
		} else {
			Integer[] arr = new Integer[1];
			arr[0] = new Integer(str);
			laststr = String.format("%0" + length + "d", arr);
		}
		return laststr;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || str.equals("")
				|| str.matches("\\s*");
	}

	/**
	 * 判断字符串中是否含有空白字符。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 去掉字符串左右空格.
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * Ason 替换字符串
	 * 
	 * @param line
	 *            字符串
	 * @param oldString
	 *            替换字符串中的某字符
	 * @param newString
	 *            替换后的新字符
	 * @return
	 */
	public static final String replace(String line, String oldString,
			String newString) {
		if (null == line && "".equals(line)) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int len = oldString.length();
			StringBuffer buffer = new StringBuffer(line2.length);
			buffer.append(line2, 0, i).append(newString2);
			i += len;
			int j = i;
			while (((i = line.indexOf(oldString, i))) > 0) {
				buffer.append(line2, j, i - j).append(newString2);
				i += len;
				j = i;
			}
			buffer.append(line2, j, line2.length - j);
			return buffer.toString();
		}
		return line;
	}

	/**
	 * 密码加密 不可逆
	 * 
	 * @param originString
	 *            要加密的字符串
	 * @return
	 */
	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(originString.getBytes());
				String resultstring = byteArrayToHexString(results);
				return resultstring.toUpperCase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 替换字符串全前缀面的字符
	 * 
	 * @param string
	 *            startIntercept("12abc123","123") ==> abc123
	 *            startIntercept("21abc323","123") ==> abc123
	 * @param charRemove
	 * @return 如果字符串为空，直接返回
	 */
	public static String startIntercept(String string, String charRemove) {
		int len = string.length();
		if (isEmpty(string)) {
			return string;
		}
		int start = 0;
		if (charRemove == null) {
			while ((start != len)
					&& Character.isWhitespace(string.charAt(start))) {
				start++;
			}
		} else if (charRemove.length() == 0) {
			return string;
		} else {
			while ((start != len)
					&& (charRemove.indexOf(string.charAt(start)) != -1)) {
				start++;
			}
		}
		return string.substring(start);
	}

	/**
	 * 替换字符串后缀的字符
	 * 
	 * @param str
	 *            endIntercept("12abc123","123") ==> 12abc
	 *            endIntercept("12abc323","123") ==> 12abc
	 * @param stripChars
	 * @return
	 */
	public static String endIntercept(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}

		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0)
					&& (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	/**
	 * 替换多个字符串
	 * 
	 * @param strs
	 *            替换前的字符串数组
	 * @param interceptChars
	 *            要替换的字符串
	 * @return
	 */
	public static String[] interceptAll(String[] strs, String interceptChars) {
		int strsLen;
		if (strs == null || (strsLen = strs.length) == 0) {
			return strs;
		}
		String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = intercept(strs[i], interceptChars);
		}
		return newArr;
	}

	/**
	 * 截取前后包含的字符串
	 * 
	 * @param str
	 *            原始字符串
	 * @param stripChars
	 *            要去掉的前后字符串。与顺序无关。 如：intercept("ac123ab","abc") ==>123
	 *            intercept("acb123acb","abc") ==>123
	 *            intercept("ac123abx","abc") ==>123abx
	 * @return
	 */
	public static String intercept(String str, String stripChars) {
		if (isEmpty(str)) {
			return str;
		}
		str = startIntercept(str, stripChars);
		return endIntercept(str, stripChars);
	}

	public static int indexOf(String str, char strChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(strChar);
	}

	/**
	 * <pre>
	 * ExmayStringUtils.indexOf(null, *)          = -1
	 * ExmayStringUtils.indexOf(*, null)          = -1
	 * ExmayStringUtils.indexOf("", "")           = 0
	 * ExmayStringUtils.indexOf("aabaabaa", "a")  = 0
	 * ExmayStringUtils.indexOf("aabaabaa", "b")  = 2
	 * ExmayStringUtils.indexOf("aabaabaa", "ab") = 1
	 * ExmayStringUtils.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 * 
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	public static int indexOf(String str, char searchChar, int startPos) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(searchChar, startPos);
	}

	public static boolean containsAny(String str, char[] searchChars) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < searchChars.length; j++) {
				if (searchChars[j] == ch) {
					return true;
				}
			}
		}
		return false;
	}

	public static int indexOfDifference(String[] strs) {
		if (strs == null || strs.length <= 1) {
			return -1;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		int arrayLen = strs.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;

		// find the min and max string lengths; this avoids checking to make
		// sure we are not exceeding the length of the string each time through
		// the bottom loop.
		for (int i = 0; i < arrayLen; i++) {
			if (strs[i] == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			} else {
				allStringsNull = false;
				shortestStrLen = Math.min(strs[i].length(), shortestStrLen);
				longestStrLen = Math.max(strs[i].length(), longestStrLen);
			}
		}

		// handle lists containing all nulls or all empty strings
		if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
			return -1;
		}

		// handle lists containing some nulls or some empty strings
		if (shortestStrLen == 0) {
			return 0;
		}

		// find the position with the first difference across all strings
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			char comparisonChar = strs[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (strs[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}

		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			// we compared all of the characters up to the length of the
			// shortest string and didn't find a match, but the string lengths
			// vary, so return the length of the shortest string.
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * 返回一个数组中所有字符串中，前缀相同的前缀名
	 * 
	 * @param strs
	 *            System.out.println(getCommonPrefix(new String[]{"a","abc"}))
	 *            ==>a
	 * @return
	 */
	public static String getCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		int smallestIndexOfDiff = indexOfDifference(strs);
		if (smallestIndexOfDiff == -1) {
			// all strings were identical
			if (strs[0] == null) {
				return "";
			}
			return strs[0];
		} else if (smallestIndexOfDiff == 0) {
			// there were no common initial characters
			return "";
		} else {
			// we found a common initial character sequence
			return strs[0].substring(0, smallestIndexOfDiff);
		}
	}

	public static String replace(String text, String searchString,
			String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null
				|| max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = (increase < 0 ? 0 : increase);
		increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
		StringBuffer buf = new StringBuffer(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}


	/**
	 * 判断一个字符串是否存在数组中
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isHave(String s) {
		String[] strs = { "gbk", "gb2312", "utf-8", "big5" };
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public static String doHtm(String str) {
		str = str.replace("&", "&amp;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace(" ", "&nbsp;");
		str = str.replace("\n", "<br>");
		return str;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/***
	 * 字符串数组转换成字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static String arrToString(String[] strings, String separtor) {
		StringBuffer buffer = new StringBuffer();
		if (strings != null) {
			for (String string : strings) {
				buffer.append(string + separtor);
			}
			String result = buffer.toString();
			return result.substring(0, result.length() - 1);
		} else {
			return "";
		}
	}

	/***
	 * 字符串数组转换成字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static String arrToString(String[] strings, String prefix,
			String separtor) {
		StringBuffer buffer = new StringBuffer();
		if (strings != null) {
			for (String string : strings) {
				buffer.append(prefix + string + separtor);
			}
			String result = buffer.toString();
			return result.substring(0, result.length() - 1);
		} else {
			return "";
		}
	}

	public static String listToString(List<String> strings) {
		StringBuffer buffer = new StringBuffer();
		if (strings != null) {
			for (String string : strings) {
				buffer.append("\"" + string + "\",");
			}
			return buffer.toString().substring(0,
					buffer.toString().length() - 1);
		}
		return buffer.append(" ").toString();
	}

	public static final byte[] BOM = { -17, -69, -65 };

	public static final char[] HexDigits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static final Pattern PTitle = Pattern.compile(
			"<title>(.+?)</title>", 34);

	public static Pattern patternHtmlTag = Pattern.compile("<[^<>]+>", 32);

	public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", 34);

	public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", 34);

	public static final Pattern PDigit = Pattern.compile("^\\d*$", 34);

	private static Pattern chinesePattern = Pattern.compile("[^一-龥]+", 34);

	private static Pattern idPattern = Pattern.compile("[\\w\\_\\.\\,]*", 34);
	
	public static final String PATTERN_MI = "\\d{11}|\\d{13}|\\d{15}|[\\d\\w]{20}";

	public static byte[] md5(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return md;
		} catch (Exception e) {
		}
		return null;
	}

	public static byte[] md5(byte[] src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src);
			return md;
		} catch (Exception e) {
		}
		return null;
	}


	public static String byteToBin(byte[] bs) {
		char[] cs = new char[bs.length * 9];
		for (int i = 0; i < bs.length; ++i) {
			byte b = bs[i];
			int j = i * 9;
			cs[j] = (((b >>> 7 & 0x1) == 1) ? 49 : '0');
			cs[(j + 1)] = (((b >>> 6 & 0x1) == 1) ? 49 : '0');
			cs[(j + 2)] = (((b >>> 5 & 0x1) == 1) ? 49 : '0');
			cs[(j + 3)] = (((b >>> 4 & 0x1) == 1) ? 49 : '0');
			cs[(j + 4)] = (((b >>> 3 & 0x1) == 1) ? 49 : '0');
			cs[(j + 5)] = (((b >>> 2 & 0x1) == 1) ? 49 : '0');
			cs[(j + 6)] = (((b >>> 1 & 0x1) == 1) ? 49 : '0');
			cs[(j + 7)] = (((b & 0x1) == 1) ? 49 : '0');
			cs[(j + 8)] = ',';
		}
		return new String(cs);
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			resultSb.append(byteToHexString(b[i]));
			resultSb.append(" ");
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return "" + HexDigits[d1] + HexDigits[d2];
	}


	public static String javaEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "\\", "\\\\");
		txt = replaceEx(txt, "\r\n", "\n");
		txt = replaceEx(txt, "\n", "\\n");
		txt = replaceEx(txt, "\"", "\\\"");
		txt = replaceEx(txt, "'", "\\'");
		return txt;
	}

	public static String javaDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "\\\\", "\\");
		txt = replaceEx(txt, "\\n", "\n");
		txt = replaceEx(txt, "\\r", "\r");
		txt = replaceEx(txt, "\\\"", "\"");
		txt = replaceEx(txt, "\\'", "'");
		return txt;
	}

	public static String[] splitEx(String str, String spilter) {
		if (str == null) {
			return null;
		}
		if ((spilter == null) || (spilter.equals(""))
				|| (str.length() < spilter.length())) {
			String[] t = { str };
			return t;
		}
		ArrayList al = new ArrayList();
		char[] cs = str.toCharArray();
		char[] ss = spilter.toCharArray();
		int length = spilter.length();
		int lastIndex = 0;
		for (int i = 0; i <= str.length() - length;) {
			boolean notSuit = false;
			for (int j = 0; j < length; ++j) {
				if (cs[(i + j)] != ss[j]) {
					notSuit = true;
					break;
				}
			}
			if (!(notSuit)) {
				al.add(str.substring(lastIndex, i));
				i += length;
				lastIndex = i;
			} else {
				++i;
			}
		}
		if (lastIndex <= str.length()) {
			al.add(str.substring(lastIndex, str.length()));
		}
		String[] t = new String[al.size()];
		for (int i = 0; i < al.size(); ++i) {
			t[i] = ((String) al.get(i));
		}
		return t;
	}

	public static String replaceEx(String str, String subStr, String reStr) {
		if (str == null) {
			return null;
		}
		if ((subStr == null) || (subStr.equals(""))
				|| (subStr.length() > str.length()) || (reStr == null)) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		String tmp = str;
		int index = -1;
		while (true) {
			index = tmp.indexOf(subStr);
			if (index < 0) {
				break;
			}
			sb.append(tmp.substring(0, index));
			sb.append(reStr);
			tmp = tmp.substring(index + subStr.length());
		}

		sb.append(tmp);
		return sb.toString();
	}

	public static String replaceAllIgnoreCase(String source, String oldstring,
			String newstring) {
		Pattern p = Pattern.compile(oldstring, 34);
		Matcher m = p.matcher(source);
		return m.replaceAll(newstring);
	}

	public static String quotEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "&", "&amp;");
		txt = replaceEx(txt, "\"", "&quot;");
		return txt;
	}

	public static String quotDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "&quot;", "\"");
		txt = replaceEx(txt, "&amp;", "&");
		return txt;
	}

	public static String escape(String src) {
		StringBuffer sb = new StringBuffer();
		sb.ensureCapacity(src.length() * 6);
		for (int i = 0; i < src.length(); ++i) {
			char j = src.charAt(i);
			if ((Character.isDigit(j)) || (Character.isLowerCase(j))
					|| (Character.isUpperCase(j))) {
				sb.append(j);
			} else if (j < 256) {
				sb.append("%");
				if (j < '\16') {
					sb.append("0");
				}
				sb.append(Integer.toString(j, 16));
			} else {
				sb.append("%u");
				sb.append(Integer.toString(j, 16));
			}
		}
		return sb.toString();
	}

	public static String leftPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();

		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iMax; ++i) {
			sb.append(c);
		}
		sb.append(srcString);
		return sb.toString();
	}

	public static String subString(String src, int length) {
		if (src == null) {
			return null;
		}
		int i = src.length();
		if (i > length) {
			return src.substring(0, length);
		}
		return src;
	}

	public static String subStringEx(String src, int length) {
		length *= 2;
		if (src == null) {
			return null;
		}
		int k = lengthEx(src);
		if (k > length) {
			int m = 0;
			boolean unixFlag = false;
			String osname = System.getProperty("os.name").toLowerCase();
			if ((osname.indexOf("sunos") > 0)
					|| (osname.indexOf("solaris") > 0)
					|| (osname.indexOf("aix") > 0))
				unixFlag = true;
			try {
				byte[] b = src.getBytes("Unicode");
				for (int i = 2; i < b.length; i += 2) {
					byte flag = b[(i + 1)];
					if (unixFlag) {
						flag = b[i];
					}
					if (flag == 0)
						++m;
					else {
						m += 2;
					}
					if (m > length)
						return src.substring(0, (i - 2) / 2);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
			}
		}
		return src;
	}

	public static int lengthEx(String src) {
		int length = 0;
		boolean unixFlag = false;
		String osname = System.getProperty("os.name").toLowerCase();
		if ((osname.indexOf("sunos") > 0) || (osname.indexOf("solaris") > 0)
				|| (osname.indexOf("aix") > 0))
			unixFlag = true;
		try {
			byte[] b = src.getBytes("Unicode");
			for (int i = 2; i < b.length; i += 2) {
				byte flag = b[(i + 1)];
				if (unixFlag) {
					flag = b[i];
				}
				if (flag == 0)
					++length;
				else
					length += 2;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
		}
		return length;
	}

	public static String rightPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();

		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		sb.append(srcString);
		for (int i = 0; i < iMax; ++i) {
			sb.append(c);
		}
		return sb.toString();
	}

	public static void printStringWithAnyCharset(String str) {
		Map map = Charset.availableCharsets();
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < keys.length; ++i) {
			for (int j = 0; j < keys.length; ++j) {
				System.out.print("\t");
				try {
					System.out.println("From "
							+ keys[i]
							+ " To "
							+ keys[j]
							+ ":"
							+ new String(str.getBytes(keys[i].toString()),
									keys[j].toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 转半角的函数(DBC case)
	//
	// 任意字符串
	// 半角字符串
	//
	// 全角空格为12288，半角空格为32
	// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	//
	public static String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// public static String getChineseFullSpell(String cnStr)
	// {
	// if ((cnStr == null) || ("".equals(cnStr.trim()))) {
	// return cnStr;
	// }
	// return ChineseSpelling.convert(cnStr);
	// }
	//
	// public static String getChineseFamilyNameSpell(String cnStr)
	// {
	// if ((cnStr == null) || ("".equals(cnStr.trim()))) {
	// return cnStr;
	// }
	// return ChineseSpelling.convertName(cnStr);
	// }
	//
	// public static String getChineseFirstAlpha(String cnStr) {
	// if ((cnStr == null) || ("".equals(cnStr.trim()))) {
	// return cnStr;
	// }
	// return ChineseSpelling.getFirstAlpha(cnStr);
	// }

	public static String capitalize(String str) {
		int strLen = 0;
		if (str != null)
			if ((strLen = str.length()) != 0)
				return str;

		return strLen + Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	public static final String noNull(String string, String defaultString) {
		return ((isEmpty(string)) ? defaultString : string);
	}

	public static final String noNull(String string) {
		return noNull(string, "");
	}

	public static String join(Object[] arr) {
		return join(arr, ",");
	}

	public static String join(Object[][] arr) {
		return join(arr, "\n", ",");
	}

	public static String join(Object[] arr, String spliter) {
		if (arr == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String join(Object[][] arr, String spliter1, String spliter2) {
		if (arr == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			if (i != 0) {
				sb.append(spliter2);
			}
			sb.append(join(arr[i], spliter2));
		}
		return sb.toString();
	}

	public static String join(List list) {
		return join(list, ",");
	}

	public static String join(List list, String spliter) {
		if (list == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	public static int count(String str, String findStr) {
		int lastIndex = 0;
		int length = findStr.length();
		int count = 0;
		int start = 0;
		while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
			lastIndex = start + length;
			++count;
		}
		return count;
	}

	public static boolean isLetterOrDigit(String str) {
		return PLetterOrDigit.matcher(str).find();
	}

	public static boolean isLetter(String str) {
		return PLetter.matcher(str).find();
	}

	public static boolean isDigit(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return PDigit.matcher(str).find();
	}

	public static boolean containsChinese(String str) {
		return (!(chinesePattern.matcher(str).matches()));
	}

	public static boolean checkID(String str) {
		if (isEmpty(str)) {
			return true;
		}

		return (idPattern.matcher(str).matches());
	}

	public static String getURLExtName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf(63);
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf(46, index1);
		if (index2 == -1) {
			return null;
		}
		int index3 = url.indexOf(47, 8);
		if (index3 == -1) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		if (ext.matches("[^\\/\\\\]*")) {
			return ext;
		}
		return null;
	}

	public static String getURLFileName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf(63);
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf(47, index1);
		if ((index2 == -1) || (index2 < 8)) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		return ext;
	}

	/** 首字母转换为大写 **/
	public static String toUpperCaseFirst(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	/** 首字母转换为小写 **/
	public static String toLowerCaseFirst(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}

	/** 字符串替换 **/
	public static String replaceAll(String content, String targetWord,
			String replaceWord) {
		int lastIndex;
		if (content == null)
			return null;
		StringBuffer text = new StringBuffer();
		int contentLength = content.length();
		int targetWordLenght = targetWord.length();
		int startIndex = 0;

		while ((lastIndex = content.indexOf(targetWord, startIndex)) >= 0) {
			text.append(content.substring(startIndex, lastIndex));
			text.append(replaceWord);
			startIndex = lastIndex + targetWordLenght;
		}
		if (startIndex < contentLength) {
			text.append(content.substring(startIndex));
		}
		return text.toString();
	}

	/** 替换所有空格 **/
	public static String replaceAllSpace(String content) {
		return replaceAll(content, " ", "");
	}

	/** 定义HTML escape 替换字符 */
	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
			0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	/**
	 * 随即生产随即数，可以用来生产tocken字符串等.
	 * 
	 * @param length
	 *            生成长度
	 * @return 随即数字符串.
	 */
	public static String getRandomString(int length) {
		StringBuffer bu = new StringBuffer();
		String[] arr = { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
				"d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		Random random = new Random();
		while (bu.length() < length) {
			String temp = arr[random.nextInt(57)];
			if (bu.indexOf(temp) == -1) {
				bu.append(temp);
			}
		}
		return bu.toString();
	}

	public static String getRandomNumber(int length) {
		StringBuffer bu = new StringBuffer();
		String[] arr = { "2", "3", "4", "5", "6", "7", "8", "9", "1", "0" };
		Random random = new Random();
		while (bu.length() < length) {
			String temp = arr[random.nextInt(10)];
			if (bu.indexOf(temp) == -1) {
				bu.append(temp);
			}
		}
		return bu.toString();
	}

	/**
	 * 获取某个区间的随机整数.
	 * 
	 * @param sek
	 *            随机种子
	 * @param min
	 *            最小整数
	 * @param max
	 *            最大整数
	 * @return 随机整数
	 */
	public static int getRandomInt(int sek, int min, int max) {
		Random random = new Random();
		int temp = 0;
		do {
			temp = random.nextInt(sek);
		} while (temp < min || temp > max);
		return temp;
	}

	/**
	 * 实现了js 版本的unescape方法.
	 * <p>
	 * 用于前台JS对传入的参数做了escape，JAVA后台取到参数后需要unescape。
	 * 
	 * @param s
	 * @return
	 */
	public static String unescape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) {
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else {
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			} else {
				sbuf.append((char) ch);
			}
			i++;
		}
		return sbuf.toString();
	}

	public static enum ENCODING {
		GBK, UTF_8, ISO_8859_1;
	}

	/**
	 * @作用:null 或者 'null' 转换成 ""
	 */
	public static final String nullToEmpty(Object str) {
		return (((null == str) || ("null".equals(str))) ? "" : str.toString());
	}

	/**
	 * @作用:判断是否为数字
	 */
	public static boolean isNumeric(String str) {
		 // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
	}

	/**
	 * @Title: isDate  
	 * @Description: 根据parttern匹配日期格式是否正确
	 * parttern：yyyyMMddHHmmss、yyyy-MM-dd HH:mm:ss等
	 * @author: Ason
	 * @param dateStr
	 * @param parttern
	 * @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean isDate(String dateStr,String parttern){
		DateFormat dateFormat = new SimpleDateFormat(parttern);
		try {
			// 采用严格的解析方式，防止类似 “2017-05-35” 类型的字符串通过
			dateFormat.setLenient(false);
			dateFormat.parse(dateStr);
			Date date = (Date) dateFormat.parse(dateStr);
			// 重复比对一下，防止类似 “2017-5-15” 类型的字符串通过
			String newDateStr = dateFormat.format(date);
			if (dateStr.equals(newDateStr)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
	}
	
	public static List<Integer> splitStringToList(String str, String reg) {
		List<Integer> list = new ArrayList<Integer>();
		String[] arr = str.split(reg);
		for (String item : arr) { 
			list.add(parseInt(item));
		}
		return list;
	}
	
	public static Integer parseInt(Object str) {
		return str == null ? 0 : Integer
				.valueOf((isNumeric(str.toString())) ? Integer.parseInt(str
						.toString()) : 0);
	}
	
	public static Float parseFloat(Object str) {
		try {
			return Float.valueOf(Float.parseFloat(str.toString()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Float.valueOf(0.0F);
	}

	public static Double parseDouble(Object str) {
		try {
			return Double.valueOf(Double.parseDouble(str.toString()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Double.valueOf(0.0D);
	}

	public static Boolean parseBoolean(Object str) {
		String s = str.toString().toLowerCase();
		if (("y".equalsIgnoreCase(s)) || ("yes".equalsIgnoreCase(s))
				|| ("true".equalsIgnoreCase(s)) || ("t".equalsIgnoreCase(s))
				|| ("1".equalsIgnoreCase(s))) {
			return true;
		}
		return false;
	}

	public static double formatDoubleValue(int medianAfterTheDecimalPoint,
			String doubleStringValue) {
		Double doubleValue = Double.valueOf(doubleStringValue);
		return new BigDecimal(doubleValue.doubleValue()).setScale(
				medianAfterTheDecimalPoint, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public static String formatAnyseValue(String formatExpression, Object value) {
		DecimalFormat df = new DecimalFormat(formatExpression);
		return df.format(value);
	}

	/**
	 * @作用: 字符编码转换
	 */
	public static String fromEncodingToAnotherEncoding(String content,
			ENCODING fromEncoding, ENCODING toEncoding) {
		String from = fromEncoding.toString().replace("_", "-");
		String to = toEncoding.toString().replace("_", "-");
		if (null != content)
			try {
				byte[] tmpbyte = content.getBytes(from);
				content = new String(tmpbyte, to);
			} catch (Exception e) {
				System.out.println("Error: from: " + from + " To: " + to + " :"
						+ e.getMessage());
			}

		return content;
	}

	/**
	 * @作用:转换成html编码
	 */
	public static String htmlEncode(String txt) {
		if (null != txt) {
			txt = txt.replace("&", "&amp;").replace("&amp;amp;", "&amp;")
					.replace("&amp;quot;", "&quot;").replace("\"", "&quot;")
					.replace("&amp;lt;", "&lt;").replace("<", "&lt;")
					.replace("&amp;gt;", "&gt;").replace(">", "&gt;")
					.replace("&amp;nbsp;", "&nbsp;");
		}
		return txt;
	}

	/**
	 * @作用:去除html编码
	 */
	public static String unHtmlEncode(String txt) {
		if (null != txt) {
			txt = txt.replace("&amp;", "&").replace("&quot;", "\"")
					.replace("&lt;", "<").replace("&gt;", ">")
					.replace("&nbsp;", " ");
		}
		return txt;
	}

	/**
	 * @作用:转换字符串首字母,默认是转换成小写字母,如boolean... bs 设置成true 转大写
	 */
	public static String firstChar2xCase(String str, boolean... bs) {
		if ((null == str) || ("".equalsIgnoreCase(str))) {
			return "";
		}
		return ((bs.length == 0) ? str.substring(0, 1).toLowerCase() : str
				.substring(0, 1).toUpperCase()).concat(str.substring(1));
	}

	public static String getUserDir() {
		String dir = System.getProperty("user.dir");
		return conversionSpecialCharacters(dir);
	}

	public static String getUserDir(String path) {
		String dir = System.getProperty("user.dir");
		return conversionSpecialCharacters(dir) + path;
	}

	/**
	 * 字符串排序
	 * 
	 * @param str
	 * @return
	 */
	public static String Sort(String str) {
		char[] chr = str.toCharArray();
		Arrays.sort(chr);
		return new String(chr);
	}
	
	/**
	 * byte[]数组转换为16进制的字符串。
	 * 
	 * @param data
	 *            要转换的字节数组。
	 * @return 转换后的结果。
	 */
	public static final String bytesToHexString(byte[] data) {
		StringBuilder valueHex = new StringBuilder();
		for (int i = 0, tmp; i < data.length; i++) {
			tmp = data[i] & 0xff;
			if (tmp < 16) {
				valueHex.append(0);
			}
			valueHex.append(Integer.toHexString(tmp));
		}
		return valueHex.toString();
	}

	/**
	 * 16进制表示的字符串转换为字节数组。
	 * 
	 * @param hexString
	 *            16进制表示的字符串
	 * @return byte[] 字节数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		char[] hexChars = hexString.toCharArray();
		int length = hexString.length();
		byte[] d = new byte[length >>> 1];
		for (int n = 0; n < length; n += 2) {
			String item = new String(hexChars, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			d[n >>> 1] = (byte) Integer.parseInt(item, 16);
		}
		return d;
	}
	
	/**
	 * 百分比转换
	 * 方法名：getPercent<BR>
	 * 创建人：xuchengfei <BR>
	 * 时间：2014年8月12日-下午9:50:46 <BR>
	 * @param num
	 * @param totalCount
	 * @param format
	 * @return String<BR>
	 * @exception <BR>
	 * @since  1.0.0
	 */
	public static String getPercent(int num,double totalCount,String...objects){
		String format = "#.##";
		if(objects!=null && objects.length>0){
			format = objects[0];
		}
		return ZLinkStringUtils.formatDoubleToString((num/totalCount)*100,format)+"%";
	}
	
	/**
	 * 
	 * 将小数格式化成字符串，会进行四舍五入 如：3656.4554===结果:3656.46<BR>
	 * 方法名：formatDoubleToString<BR>
	 * 创建人：xuchengfei <BR>
	 * 时间：2014年8月12日-下午9:12:01 <BR>
	 * 
	 * @param dou
	 * @return String<BR>
	 * @exception <BR>
	 * @since 1.0.0
	 */
	public static String formatDoubleToString(double dou,String format) {
		if(isEmpty(format))format = "#.##";
		DecimalFormat decimalFormat = new DecimalFormat(format);
		String string = decimalFormat.format(dou);// 四舍五入，逢五进一
		return string;
	}

	public static String getPercent(double percent) {
		return new DecimalFormat("#.##").format(percent);
	}

	public static String getPercent(float percent) {
		return new DecimalFormat("#.##").format(percent);
	}

	public static boolean isImage(String ext) {
		return ext.toLowerCase().matches("jpg|gif|bmp|png|jpeg");
	}

	public static boolean isDoc(String ext) {
		return ext.toLowerCase().matches("doc|docx|xls|xlsx|pdf|txt|ppt|pptx");
	}

	public static boolean isVideo(String ext) {
		return ext.toLowerCase().matches("mp4|flv|mp3");
	}

	
	
	public static String encryption(String str,int k){
		String string = "";
		for (int i = 0; i < str.length(); i++) {
			char c= str.charAt(i);
			if(c>='a' && c<='z'){
				c += k%26;
				if(c<'a'){
					c+=26;
				}
				if(c>'z'){
					c-=26;
				}
			}else if(c>='A' && c<='Z'){
				c+=k%26;
				if(c<'A'){
					c+=26;
				}
				if(c>'Z'){
					c-=26;
				}
			}
			string+=c;
		}
		return string;
	}
	
	
	public static String dencryption(String str,int n){
		String string = "";
		int k = Integer.parseInt("-"+n);
		for (int i = 0; i < str.length(); i++) {
			char c= str.charAt(i);
			if(c>='a' && c<='z'){
				c += k%26;
				if(c<'a'){
					c+=26;
				}
				if(c>'z'){
					c-=26;
				}
			}else if(c>='A' && c<='Z'){
				c+=k%26;
				if(c<'A'){
					c+=26;
				}
				if(c>'Z'){
					c-=26;
				}
			}
			string+=c;
		}
		return string;
	}
	
	/**
	 * 讲aaabbccdd--a3b2c2d2
	 * 方法名：countString<BR>
	 * 创建人：xuchengfei <BR>
	 * 时间：2014年7月30日-下午11:34:37 <BR>
	 * @param a
	 * @return String<BR>
	 * @exception <BR>
	 * @since  1.0.0
	 */
	public static String countString(String a){
		 if(a==null)return null;
		 char []c=a.toCharArray();  
	       TreeMap<String,Integer> m=new TreeMap<String, Integer>();
	       StringBuffer buffer = new StringBuffer();
	       for(int i=0;i<c.length;i++){  
	            String cstr=String.valueOf(c[i]);  
	            if(null!=m.get(cstr)){  
	                int count=m.get(cstr);  
	                m.put(cstr, count+1);  
	            }else{  
	                m.put(cstr,1);  
	            }  
	       }
	       for (Map.Entry<String, Integer> entry : m.entrySet()) {
	    	   buffer.append(entry.getKey()+entry.getValue());
	       }
	       return buffer.toString();
	 }

	/**
	 * 获取当前时间的字符串
	 * @return
	 */
	public static String getNowTimeString(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm:dd HH:mm:ss");
		return format.format(date.getTime());
	}
	
	/**
	 * 求最大公约数
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static Integer maxGCD(Integer num1,Integer num2){
		if(num1==num2)return num1;
		else{
			if(num1>num2){
				while (num1%num2!=0) {
					num2 = num1%num2;
				}
				return num2;
			}else{
				while (num2%num1!=0) {
					num1 = num2%num1;
				}
				return num1;
			}
		}
	}
	
	/**
	 * 将Json类转为String
	 * @author Ason
	 * @Title: parseJsonToString 
	 * @param json
	 * @return String
	 * @throws
	 */
	public static String parseJsonToString(Object json){
		SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;  
		
		//byte[] bytes = JSON.toJSONBytes(json,feature);  
		//String  j = new String(bytes); 
		String j = JSON.toJSONString(json,feature);  
		return j;
	}
	
	/**
	 * 对字符串序列进行排序并拼接
	 * @param strs
	 * @return
	 */
	public static String concatSortStrings(String[] strs){
		
		String result ="";
		if ((strs!=null) && (strs.length >0)){
			Arrays.sort(strs);
			for (String str:strs){
				 //按URL需求拼装
				result = result + str;				
			}
			
		}
		return result;

	}
	
	/**
	 * 对一个字符串数组进行
	 * @param strs
	 * @return
	 */
	public static String[] sortStrings(String[] strs){
		if ((strs!=null) && (strs.length>0)){
			Arrays.sort(strs);
			return strs;
		}
		return null;
	}
	
	/**
	 * sha1算法加密报文
	 * @param str
	 * @return
	 */
    public static String signBySha1(String str){
    	    	
    	try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(str.getBytes("utf-8"));
			return byte2Hex(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
	 * sha1算法加密报文，返回大写字符串
	 * @param str
	 * @return
	 */
    public static String signBySha1WithUpperCase(String str){
    	return signBySha1(str).toUpperCase();
    	
    }
    
    /**
     * MD5加密
     * @param str
     * @return
     */
    public static String signByMd5(String str){
    	
    	try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			String newstr=byte2Hex(messageDigest.digest(str.getBytes("utf-8")));			
			return newstr;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
    }
	
    
    /**
     * UCS2解码
     * 
     * @param src
     *            UCS2 源串
     * @return 解码后的UTF-16BE字符串
     */
    public static String DecodeUCS2(String src) {
        byte[] bytes = new byte[src.length() / 2];

        for (int i = 0; i < src.length(); i += 2) {
            bytes[i / 2] = (byte) (Integer
                    .parseInt(src.substring(i, i + 2), 16));
        }
        String reValue="";
        try {
            reValue = new String(bytes, "UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            //
        }
        return reValue;

    }

    /**
     * UCS2编码
     * 
     * @param src
     *            UTF-16BE编码的源串
     * @return 编码后的UCS2串
     */
    public static String EncodeUCS2(String src) {

        byte[] bytes=null;
        try {
            bytes = src.getBytes("UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            //
        }

        StringBuffer reValue = new StringBuffer();
        StringBuffer tem = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            tem.delete(0, tem.length());
            tem.append(Integer.toHexString(bytes[i] & 0xFF));
            if(tem.length()==1){
                tem.insert(0, '0');
            }
            reValue.append(tem);
        }
        return reValue.toString().toUpperCase();
    }
	
	
	/**
	 * SHA1加密
	 * @param data
	 * @return
	 * @throws IOException
	 */
	
	private static byte[] getSHA1Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}
	
	/**
	* 二进制转十六进制字符串
	*
	* @param bytes
	* @return
	*/
	public static String byte2Hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}
	
	/**
	 * UTF8转换
	 * @param value
	 * @param sourceCharsetName
	 * @return
	 */
	public static String utf8Encoding(String value, String sourceCharsetName) {
		try {
			return new String(value.getBytes(sourceCharsetName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String listToString(List list,String seperator){
		if(list==null || seperator==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();  
		for (int i = 0; i < list.size(); i++) {  
			sb.append(list.get(i));
		    if (i < list.size()-1) {  
		    	sb.append(seperator);  
		    }  
		}  
		return sb.toString();  
	}
	
	public static List<String> stringToList(String str,String seperator){
		
		if (str == null || str.equals("")) {  
            return null;  
        }           
        
        String[] strs = str.split(seperator);  
        List<String> list = new ArrayList<String>();  
        for (int i=0;i<strs.length;i++) {  
            list.add(strs[i]);                
         }  
        return list;  
	}
	
	public static String[] stringToArray(String str,String seperator){
		
		if (str == null || str.equals("")) {  
            return null;  
        }           
        
        String[] strs = str.split(seperator);
        return strs;  
	}
	
	/**
	 * 生成帐号（ec_+ecId）
	 * @author Ason
	 * @Title: createAccount 
	 * @param prefix
	 * @param ecId
	 * @return String
	 * @throws
	 */
	public static String createAccount(String prefix,Integer ecId){
		StringBuffer sb = new StringBuffer(prefix);
		String ecIdStr = ecId.toString();
		//少于八位则补充0
		Integer l = 8-ecIdStr.length();
		if(l>0){
			for (int i = 0; i < l; i++) {
				sb.append(0);
			}
			sb.append(ecIdStr);
		}else
			sb.append(ecId);
		return sb.toString();
	}

	/**
	 * 根据日期生成订单或账单编号
	 * @author Ason
	 * @Title: createRandomNumberByDate 
	 * @param preStr 订单前置字符
	 * @param ramdonLength 后边随机数长度
	 * @return String
	 * @throws
	 */
	public static String createRandomNumberByDate(String preStr,Integer ramdonLength) {
		if(isEmpty(preStr))preStr = "";
		String randomStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		randomStr = f.format(new Date());
		randomStr = randomStr + (ramdonLength==0?"":getCode(ramdonLength, 0));
		return preStr+randomStr;
	}
	
	/**
	 * 生成随机字符串
	 * @author Ason
	 * @Title: getCode 
	 * @param passLength 长度
	 * @param type 类型  
	 * 	0：纯数字(0-9)
	 * 1：全小写字母(a-z)
	 * 2：全大写字母(A-Z)
	 * 3: 数字+小写字母
	 * 4: 数字+大写字母
	 * 5：大写字母+小写字母
	 * 6：数字+大写字母+小写字母
	 * 7：固定长度33位：根据UUID拿到的随机字符串，去掉了四个"-"(相当于长度33位的小写字母加数字)
	 * @return String
	 * @throws
	 */
	public static String getCode(int passLength, int type)  
    {  
		 StringBuffer buffer = null;
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        //r.setSeed(new Date().getTime());
        switch (type) {
        case 0:
            buffer = new StringBuffer("0123456789");
            break;
        case 1:
            buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
            break;
        case 2:
            buffer = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            break;
        case 3:
            buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
            break;
        case 4:
            buffer = new StringBuffer("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            break;
        case 5:
            buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
            break;
        case 6:
            buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
            passLength -= 1;
            break;
        case 7:
            String s = UUID.randomUUID().toString();
            sb.append(s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24));
        }

        if (type != 7) {
            int range = buffer.length();
            for (int i = 0; i < passLength; ++i) {
                sb.append(buffer.charAt(r.nextInt(range)));
            }
        }
        return sb.toString();
    }
	
	/*** 
     * 把中文替换为指定字符<br> 
     * 注意:一次只匹配一个中文字符 
     * @param source 
     * @param replacement 
     * @return 
     */  
    public static String replaceChinese(String source, String replacement){  
        if(isEmpty(source)){  
            return null;  
        }  
        if(replacement==null){  
            replacement = "";
        }  
        String reg = "[\u4e00-\u9fa5]";  
        Pattern pat = Pattern.compile(reg);    
        Matcher mat=pat.matcher(source);   
        String repickStr = mat.replaceAll(replacement);  
        return repickStr;  
    } 
    

	/**
	 * 是否至少有一个对象为null
	 * @author Kenny
	 * @date 2018年1月17日
	 */
	public static boolean hasNull(Object... obj) {
		for (Object o : obj) {
			if (o == null)
				return true;
		}
		return false;
	}

	/**
	 * @Title: serilazeIntsStrToList  
	 * @Description:将数字字符串转换为List 
	 * @author: Ason
	 * @param spIds
	 * @return      
	 * @return: List<Integer>      
	 * @throws
	 */
	public static List<Integer> serilazeIntsStrToList(String spIds, String splidStr) {
		String[] strArray = spIds.split(splidStr);
		Integer[] strIntArray = new Integer[strArray.length];
		for (int i = 0, il = strArray.length; i < il; i++) {
			strIntArray[i] = Integer.parseInt(strArray[i]);
		}
		return Arrays.asList(strIntArray);
	}
	
	/**
	 * @Title: serilazeIntsStrToIntegerArray  
	 * @Description:将数字字符串转换为IntegerArray 
	 * @author: Ason
	 * @param spIds
	 * @return      
	 * @return: List<Integer>      
	 * @throws
	 */
	public static Integer[] serilazeIntsStrToIntegerArray(String spIds, String splidStr) {
		String[] strArray = spIds.split(splidStr);
		Integer[] strIntArray = new Integer[strArray.length];
		for (int i = 0, il = strArray.length; i < il; i++) {
			strIntArray[i] = Integer.parseInt(strArray[i]);
		}
		return strIntArray;
	}
	
	/**
	 * @Title: serilazeIdsStrToList  
	 * @Description:将数字字符串转换为List 
	 * @author: Ason
	 * @param spIds
	 * @return      
	 * @return: List<Integer>      
	 * @throws
	 */
	public static List<String> serilazeStrsToList(String spIds, String splidStr) {
		String[] strArray = spIds.split(splidStr);
		return Arrays.asList(strArray);
	}

	/**
	 * @Title: removeLastStr  
	 * @Description:删除最后一个字符 
	 * @author: Ason
	 * @param str
	 * @return      
	 * @return: String      
	 * @throws
	 */
	public static String removeLastStr(String str) {
		return str.substring(0,str.length()-1);
	}
	
	/**
	 * @Title: MD5  
	 * @Description:md5加密 
	 * @author: Ason
	 * @param sourceStr
	 * @return      
	 * @return: String      
	 * @throws
	 */
	public static String MD5_32(String sourceStr, Integer w) {
		String result = "";// 通过result返回结果值
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");// 1.初始化MessageDigest信息摘要对象,并指定为MD5不分大小写都可以
			md.update(sourceStr.getBytes());// 2.传入需要计算的字符串更新摘要信息，传入的为字节数组byte[],将字符串转换为字节数组使用getBytes()方法完成
			byte b[] = md.digest();// 3.计算信息摘要digest()方法,返回值为字节数组

			int i;// 定义整型
			// 声明StringBuffer对象
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];// 将首个元素赋值给i
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");// 前面补0
				buf.append(Integer.toHexString(i));// 转换成16进制编码
			}
			result = buf.toString().toLowerCase();// 转换成字符串，默认为32位小写
			//16位结果
			if(w!=null&&w.equals(16))
				result = buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;// 返回结果
	}
}
