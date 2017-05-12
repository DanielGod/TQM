package tqm.bianfeng.com.tqm.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符处理工具
 */
public class StringUtil {
	
	private static StringBuilder sb = new StringBuilder();

	public static boolean checkEmail(String email) {
		String regex = "^[a-zA-Z][a-zA-Z0-9._-]*\\@\\w+(\\.)*\\w+\\.\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(email);
		return matcher.matches();
	}

	public static String formatHTMLIn(String html) {
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		html = html.replaceAll("\"", "&quot;");
		return html;
	}

	public static String formatHTMLOut(String html) {
		html = html.replaceAll("&amp;", "&");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		html = html.replaceAll("&quot;", "\"");
		return html;
	}

	public static String subString(String str, int length) {
		if (isBlank(str))
			return "";
		if (str.getBytes().length <= length)
			return str;
		char[] ch = null;
		if (str.length() >= length)
			ch = str.substring(0, length).toCharArray();
		else
			ch = str.toCharArray();
		int readLen = 0;
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < ch.length; i++) {
			String c = String.valueOf(ch[i]);
			readLen += c.getBytes().length;
			if (readLen > length)
				return sb.toString();
			sb.append(c);
		}
		return sb.toString();
	}

	public static boolean checkLength(String str, int minLength, int maxLength) {
		if (str != null) {
			int len = str.length();
			if (minLength == 0)
				return len <= maxLength;
			if (maxLength == 0) {
				return len >= minLength;
			}
			return (len >= minLength) && (len <= maxLength);
		}
		return false;
	}

	public static String decodeStringByUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return "";
	}

	public static String encodeStringByUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return "";
	}

	public static String isoToUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return "";
	}

	public static String utf8ToISO(String str) {
		if (isBlank(str))
			return "";
		try {
			return new String(str.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		return "";
	}

	public static String utf8Togb2312(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
				} catch (NumberFormatException localNumberFormatException) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:
				sb.append(c);
			}

		}

		String result = sb.toString();
		String res = null;
		try {
			byte[] inputBytes = result.getBytes("8859_1");
			res = new String(inputBytes, "UTF-8");
		} catch (Exception localException) {
		}
		return res;
	}

	public static String getFormatDateStr(java.util.Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static boolean isBlank(String str) {
		return (str == null) || (str.trim().equals("")) || (str.length() < 0);
	}

	public static boolean isBlank(Object str) {
		return (str == null) || (str.toString().trim().equals("")) || (str.toString().length() < 0);
	}

	public static boolean isBlank(String[] args) {
		return (args == null) || (args.length == 0);
	}

	public static boolean isInteger(String str) {
		if (isBlank(str))
			return false;
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static boolean isInteger(Object str) {
		String temp = (String) str;
		if (isBlank(str))
			return false;
		try {
			Integer.parseInt(temp);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static String null2String(String str) {
		if ((str == null) || (str.equals("")) || (str.trim().length() == 0)) {
			return str = "";
		}
		return str;
	}

	public static int string2Int(String str) {
		int valueInt = 0;
		if (!isBlank(str)) {
			valueInt = Integer.parseInt(str);
		}
		return valueInt;
	}

	public static String int2String(int comment) {
		String srt = "";
		srt = Integer.toString(comment);
		return srt;
	}

	public static boolean isMaxZeroInteger(Object str) {
		if (isBlank(str))
			return false;
		try {
			int temp = Integer.parseInt(str.toString());
			return temp > 0;
		} catch (Exception localException) {
		}
		return false;
	}

	public static boolean isLong(String str) {
		if (isBlank(str))
			return false;
		try {
			Long.parseLong(str);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static boolean isLongs(String[] str) {
		try {
			for (int i = 0; i < str.length; i++)
				Long.parseLong(str[i]);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static boolean isIntegers(String[] str) {
		try {
			for (int i = 0; i < str.length; i++)
				Integer.parseInt(str[i]);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static boolean isDoubles(String[] str) {
		try {
			for (int i = 0; i < str.length; i++)
				Double.parseDouble(str[i]);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	@Deprecated
	public static String Md5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte[] b = md.digest();
			int i = 0;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public static String Md5(String plainText, String coding) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(coding));
			byte[] b = md.digest();
			int i = 0;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	public static long[] stringsToLongs(String[] str) {
		long[] lon = new long[str.length];
		for (int i = 0; i < lon.length; i++)
			lon[i] = Long.parseLong(str[i]);
		return lon;
	}

	public static Integer[] stringsToIntegers(String[] str) {
		Integer[] array = new Integer[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Integer.valueOf(Integer.parseInt(str[i]));
		return array;
	}

	public static int[] stringsToInts(String[] str) {
		int[] array = new int[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Integer.parseInt(str[i]);
		return array;
	}

	public static double[] stringsToDoubles(String[] str) {
		double[] array = new double[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Double.parseDouble(str[i]);
		return array;
	}

	public static String[] delLopStrings(String[] str) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < str.length; i++) {
			if (!list.contains(str[i]))
				list.add(str[i]);
		}
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = ((String) list.get(i));
		}
		return array;
	}

	public static boolean[] stringsToBooleans(String[] str) {
		boolean[] array = new boolean[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Boolean.parseBoolean(str[i]);
		return array;
	}

	public static boolean isTimestamp(String str) {
		try {
			java.sql.Date.valueOf(str);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static int getPageStart(String pageNo) {
		int istart = 1;
		if (isBlank(pageNo))
			return istart;
		try {
			istart = Integer.parseInt(pageNo) < 0 ? istart : Integer.parseInt(pageNo);
		} catch (NumberFormatException localNumberFormatException) {
		}
		return istart;
	}

	public static String getDateSimpleStr() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static Long[] StrToLong(String[] args) {
		if (args == null)
			return null;
		Long[] _ref = new Long[args.length];
		for (int i = 0; i < args.length; i++) {
			_ref[i] = new Long(args[i]);
		}
		return _ref;
	}

	public static Integer[] StrToInteger(String[] args) {
		if (args == null)
			return null;
		Integer[] _ref = new Integer[args.length];
		for (int i = 0; i < args.length; i++) {
			_ref[i] = new Integer(args[i]);
		}
		return _ref;
	}

	public static String getSimpleDateStr(java.util.Date day, String fomStr) {
		SimpleDateFormat format = new SimpleDateFormat(fomStr);
		return format.format(day);
	}

	public static java.util.Date getDateForStr(String str) {
		java.sql.Date sqlDate = java.sql.Date.valueOf(str);
		return sqlDate;
	}

	public static String getFileFix(String filePath) {
		String temp = "";
		if (filePath != null) {
			temp = filePath.substring(filePath.indexOf("."), filePath.length());
		}
		return temp;
	}

	public static String convertStreamToString(InputStream dataFlow) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(dataFlow));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				sb.append(line);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				dataFlow.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				dataFlow.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String checkStr(String str) {
		String s = null;
		char[] cc = str.toCharArray();
		for (int i = 0; i < cc.length; i++) {
			boolean b = isValidChar(cc[i]);
			if (!b)
				cc[i] = ' ';
		}
		s = String.valueOf(cc);
		return s;
	}

	private static boolean isValidChar(char ch) {
		if (((ch >= '0') && (ch <= '9')) || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')))
			return true;
		if (((ch >= '一') && (ch <= '翿')) || ((ch >= 32768) && (ch <= 38191)))
			return true;
		return false;
	}

	public static String removeRepeatStr(String content, String target) {
		StringBuffer sb = new StringBuffer(content);
		for (int i = 0; i < sb.length() - 1; i++) {
			if ((sb.substring(i, i + target.length()).equals(target))
					&& (sb.substring(i, i + target.length()).equals(sb.substring(i + 1, i + target.length() + 1)))) {
				sb.delete(i, i + target.length());
				if (i + target.length() + 1 > sb.length()) {
					break;
				}
				i--;
			}

		}

		return sb.toString();
	}

	public static Boolean isEmail(String email) {
		boolean tag = true;

		Pattern pattern = Pattern
				.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return Boolean.valueOf(tag);
	}

	public static String buildUrl(String url, String parm) {
		if (url.indexOf("?") > 0) {
			return url = url + "&" + parm;
		}
		return url = url + "?" + parm;
	}

	public static String buildPath(Object[] params) {
		String temp = "";
		Object[] arrayOfObject = params;
		int j = params.length;
		for (int i = 0; i < j; i++) {
			Object o = arrayOfObject[i];
			temp = temp + File.separator + o;
		}
		return temp;
	}

	public static String buildUrl(String url, Map parms) {
		Iterator key = parms.keySet().iterator();
		String paramsStr = "";
		while (key.hasNext()) {
			Object temp = key.next();
			if (!isBlank(parms.get(temp))) {
				if (paramsStr != "") {
					paramsStr = paramsStr + "&";
				}
				paramsStr = paramsStr + temp + "=" + parms.get(temp);
			}
		}
		if (paramsStr != "") {
			if (url.indexOf("?") > 0) {
				return url = url + "&" + paramsStr;
			}
			return url = url + "?" + paramsStr;
		}

		return url;
	}

	public static String javaProperty2DatabaseCloumn(String property) {
		String[] ss = property.split("(?<!^)(?=[A-Z])");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ss.length; i++) {
			sb.append(ss[i]);
			if (i < ss.length - 1) {
				sb.append("_");
			}
		}
		if (!isBlank(sb)) {
			return sb.toString().toUpperCase();
		}
		return null;
	}

//	public static Map<String, String> sortMapByKey(Map<String, String> map) {
//		if ((map == null) || (map.isEmpty())) {
//			return null;
//		}
//		Map sortMap = new TreeMap(new MapKeyComparator());
//		sortMap.putAll(map);
//		return sortMap;
//	}
//
//	public static Map<String, String> sortMapByValue(Map<String, String> map) {
//		if ((map == null) || (map.isEmpty())) {
//			return null;
//		}
//		Map sortedMap = new LinkedHashMap<>();
//		List entryList = new ArrayList(map.entrySet());
//		Collections.sort(entryList, new MapValueComparator());
//		Iterator iter = entryList.iterator();
//		Map.Entry tmpEntry = null;
//		while (iter.hasNext()) {
//			tmpEntry = (Map.Entry) iter.next();
//			sortedMap.put((String) tmpEntry.getKey(), (String) tmpEntry.getValue());
//		}
//		return sortedMap;
//	}

	public static boolean isExpressNo(String str) {
		if (isBlank(str)) {
			return false;
		}

		if (str.length() == 13)
			return true;
		if (str.length() == 12) {
			return true;
		}
		return true;
	}
}
