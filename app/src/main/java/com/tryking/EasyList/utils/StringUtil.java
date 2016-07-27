package com.tryking.EasyList.utils;

import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * 
 *
 */
public class StringUtil {

    /**
     * The empty String <code>""</code>.
     * 
     * @since 2.0
     */
    public static final String EMPTY = "";

    /**
     * Represents a failed index search.
     * 
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * <p>
     * <code>StringUtils</code> instances should NOT be constructed in standard
     * programming. Instead, the class should be used as
     * <code>StringUtils.trim(" foo ");</code>.
     * </p>
     * 
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public StringUtil() {
	super();
    }

    // Empty checks
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * 
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().
     * </p>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
	return str == null || str.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is not empty ("") and not null.
     * </p>
     * 
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
	return !StringUtil.isEmpty(str);
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
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
     * <p>
     * Checks if a String is not empty (""), not null and not whitespace only.
     * </p>
     * 
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null and not
     *         whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
	return !StringUtil.isBlank(str);
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String,
     * handling <code>null</code> by returning <code>null</code>.
     * </p>
     * 
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32. To strip whitespace use {@link #strip(String)}.
     * </p>
     * 
     * <p>
     * To trim your choice of characters, use the {@link #strip(String, String)}
     * methods.
     * </p>
     * 
     * <pre>
     * StringUtils.trim(null)          = null
     * StringUtils.trim("")            = ""
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * </pre>
     * 
     * @param str
     *            the String to be trimmed, may be null
     * @return the trimmed string, <code>null</code> if null String input
     */
    public static String trim(String str) {
	return str == null ? null : str.trim();
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String
     * returning <code>null</code> if the String is empty ("") after the trim or
     * if it is <code>null</code>.
     * 
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32. To strip whitespace use
     * {@link #stripToNull(String)}.
     * </p>
     * 
     * <pre>
     * StringUtils.trimToNull(null)          = null
     * StringUtils.trimToNull("")            = null
     * StringUtils.trimToNull("     ")       = null
     * StringUtils.trimToNull("abc")         = "abc"
     * StringUtils.trimToNull("    abc    ") = "abc"
     * </pre>
     * 
     * @param str
     *            the String to be trimmed, may be null
     * @return the trimmed String, <code>null</code> if only chars &lt;= 32,
     *         empty or null String input
     * @since 2.0
     */
    public static String trimToNull(String str) {
	String ts = trim(str);
	return isEmpty(ts) ? null : ts;
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String
     * returning an empty String ("") if the String is empty ("") after the trim
     * or if it is <code>null</code>.
     * 
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32. To strip whitespace use
     * {@link #stripToEmpty(String)}.
     * </p>
     * 
     * <pre>
     * StringUtils.trimToEmpty(null)          = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     * 
     * @param str
     *            the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String trimToEmpty(String str) {
	return str == null ? EMPTY : str.trim();
    }



    /**
     * <p>
     * Strips any of a set of characters from the end of a String.
     * </p>
     * 
     * <p>
     * A <code>null</code> input String returns <code>null</code>. An empty
     * string ("") input returns the empty string.
     * </p>
     * 
     * <p>
     * If the stripChars String is <code>null</code>, whitespace is stripped as
     * defined by {@link Character#isWhitespace(char)}.
     * </p>
     * 
     * <pre>
     * StringUtils.stripEnd(null, *)          = null
     * StringUtils.stripEnd("", *)            = ""
     * StringUtils.stripEnd("abc", "")        = "abc"
     * StringUtils.stripEnd("abc", null)      = "abc"
     * StringUtils.stripEnd("  abc", null)    = "  abc"
     * StringUtils.stripEnd("abc  ", null)    = "abc"
     * StringUtils.stripEnd(" abc ", null)    = " abc"
     * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     * 
     * @param str
     *            the String to remove characters from, may be null
     * @param stripChars
     *            the characters to remove, null treated as whitespace
     * @return the stripped String, <code>null</code> if null String input
     */
    public static String stripEnd(String str, String stripChars) {
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


    // Equals
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Compares two Strings, returning <code>true</code> if they are equal.
     * </p>
     * 
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.
     * </p>
     * 
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     * 
     * @see java.lang.String#equals(Object)
     * @param str1
     *            the first String, may be null
     * @param str2
     *            the second String, may be null
     * @return <code>true</code> if the Strings are equal, case sensitive, or
     *         both <code>null</code>
     */
    public static boolean equals(String str1, String str2) {
	return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * <p>
     * Compares two Strings, returning <code>true</code> if they are equal
     * ignoring the case.
     * </p>
     *
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered equal. Comparison is case insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @see java.lang.String#equalsIgnoreCase(String)
     * @param str1
     *            the first String, may be null
     * @param str2
     *            the second String, may be null
     * @return <code>true</code> if the Strings are equal, case insensitive, or
     *         both <code>null</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
	return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    // IndexOf
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Finds the first index within a String, handling <code>null</code>. This
     * method uses {@link String#indexOf(int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> or empty ("") String will return <code>-1</code>.
     * </p>
     *
     * <pre>
     * StringUtils.indexOf(null, *)         = -1
     * StringUtils.indexOf("", *)           = -1
     * StringUtils.indexOf("aabaabaa", 'a') = 0
     * StringUtils.indexOf("aabaabaa", 'b') = 2
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchChar
     *            the character to find
     * @return the first index of the search character, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar) {
	if (isEmpty(str)) {
	    return -1;
	}
	return str.indexOf(searchChar);
    }

    /**
     * <p>
     * Finds the first index within a String from a start position, handling
     * <code>null</code>. This method uses {@link String#indexOf(int, int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> or empty ("") String will return <code>-1</code>. A
     * negative start position is treated as zero. A start position greater than
     * the string length returns <code>-1</code>.
     * </p>
     *
     * <pre>
     * StringUtils.indexOf(null, *, *)          = -1
     * StringUtils.indexOf("", *, *)            = -1
     * StringUtils.indexOf("aabaabaa", 'b', 0)  = 2
     * StringUtils.indexOf("aabaabaa", 'b', 3)  = 5
     * StringUtils.indexOf("aabaabaa", 'b', 9)  = -1
     * StringUtils.indexOf("aabaabaa", 'b', -1) = 2
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchChar
     *            the character to find
     * @param startPos
     *            the start position, negative treated as zero
     * @return the first index of the search character, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, char searchChar, int startPos) {
	if (isEmpty(str)) {
	    return -1;
	}
	return str.indexOf(searchChar, startPos);
    }

    /**
     * <p>
     * Finds the first index within a String, handling <code>null</code>. This
     * method uses {@link String#indexOf(String)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>.
     * </p>
     *
     * <pre>
     * StringUtils.indexOf(null, *)          = -1
     * StringUtils.indexOf(*, null)          = -1
     * StringUtils.indexOf("", "")           = 0
     * StringUtils.indexOf("aabaabaa", "a")  = 0
     * StringUtils.indexOf("aabaabaa", "b")  = 2
     * StringUtils.indexOf("aabaabaa", "ab") = 1
     * StringUtils.indexOf("aabaabaa", "")   = 0
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	return str.indexOf(searchStr);
    }


    /**
     * <p>
     * Finds the first index within a String, handling <code>null</code>. This
     * method uses {@link String#indexOf(String, int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position is treated as zero. An empty ("") search String always matches.
     * A start position greater than the string length only matches an empty
     * search String.
     * </p>
     *
     * <pre>
     * StringUtils.indexOf(null, *, *)          = -1
     * StringUtils.indexOf(*, null, *)          = -1
     * StringUtils.indexOf("", "", 0)           = 0
     * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
     * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
     * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
     * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
     * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
     * StringUtils.indexOf("aabaabaa", "b", -1) = 2
     * StringUtils.indexOf("aabaabaa", "", 2)   = 2
     * StringUtils.indexOf("abc", "", 9)        = 3
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @param startPos
     *            the start position, negative treated as zero
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int indexOf(String str, String searchStr, int startPos) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	// JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
	if (searchStr.length() == 0 && startPos >= str.length()) {
	    return str.length();
	}
	return str.indexOf(searchStr, startPos);
    }

    /**
     * <p>
     * Case in-sensitive find of the first index within a String.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position is treated as zero. An empty ("") search String always matches.
     * A start position greater than the string length only matches an empty
     * search String.
     * </p>
     *
     * <pre>
     * StringUtils.indexOfIgnoreCase(null, *)          = -1
     * StringUtils.indexOfIgnoreCase(*, null)          = -1
     * StringUtils.indexOfIgnoreCase("", "")           = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "a")  = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "b")  = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "ab") = 1
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.5
     */
    public static int indexOfIgnoreCase(String str, String searchStr) {
	return indexOfIgnoreCase(str, searchStr, 0);
    }

    /**
     * <p>
     * Case in-sensitive find of the first index within a String from the
     * specified position.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position is treated as zero. An empty ("") search String always matches.
     * A start position greater than the string length only matches an empty
     * search String.
     * </p>
     *
     * <pre>
     * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtils.indexOfIgnoreCase("abc", "", 9)        = 3
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @param startPos
     *            the start position, negative treated as zero
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.5
     */
    public static int indexOfIgnoreCase(String str, String searchStr,
										int startPos) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	if (startPos < 0) {
	    startPos = 0;
	}
	int endLimit = (str.length() - searchStr.length()) + 1;
	if (startPos > endLimit) {
	    return -1;
	}
	if (searchStr.length() == 0) {
	    return startPos;
	}
	for (int i = startPos; i < endLimit; i++) {
	    if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
		return i;
	    }
	}
	return -1;
    }

    // LastIndexOf
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Finds the last index within a String, handling <code>null</code>. This
     * method uses {@link String#lastIndexOf(int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> or empty ("") String will return <code>-1</code>.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOf(null, *)         = -1
     * StringUtils.lastIndexOf("", *)           = -1
     * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
     * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchChar
     *            the character to find
     * @return the last index of the search character, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar) {
	if (isEmpty(str)) {
	    return -1;
	}
	return str.lastIndexOf(searchChar);
    }

    /**
     * <p>
     * Finds the last index within a String from a start position, handling
     * <code>null</code>. This method uses {@link String#lastIndexOf(int, int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> or empty ("") String will return <code>-1</code>. A
     * negative start position returns <code>-1</code>. A start position greater
     * than the string length searches the whole string.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOf(null, *, *)          = -1
     * StringUtils.lastIndexOf("", *,  *)           = -1
     * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
     * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
     * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
     * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
     * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
     * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchChar
     *            the character to find
     * @param startPos
     *            the start position
     * @return the last index of the search character, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, char searchChar, int startPos) {
	if (isEmpty(str)) {
	    return -1;
	}
	return str.lastIndexOf(searchChar, startPos);
    }

    /**
     * <p>
     * Finds the last index within a String, handling <code>null</code>. This
     * method uses {@link String#lastIndexOf(String)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOf(null, *)          = -1
     * StringUtils.lastIndexOf(*, null)          = -1
     * StringUtils.lastIndexOf("", "")           = 0
     * StringUtils.lastIndexOf("aabaabaa", "a")  = 0
     * StringUtils.lastIndexOf("aabaabaa", "b")  = 2
     * StringUtils.lastIndexOf("aabaabaa", "ab") = 1
     * StringUtils.lastIndexOf("aabaabaa", "")   = 8
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return the last index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	return str.lastIndexOf(searchStr);
    }


    /**
     * <p>
     * Finds the first index within a String, handling <code>null</code>. This
     * method uses {@link String#lastIndexOf(String, int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position returns <code>-1</code>. An empty ("") search String always
     * matches unless the start position is negative. A start position greater
     * than the string length searches the whole string.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOf(null, *, *)          = -1
     * StringUtils.lastIndexOf(*, null, *)          = -1
     * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
     * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
     * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
     * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
     * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
     * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
     * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @param startPos
     *            the start position, negative treated as zero
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static int lastIndexOf(String str, String searchStr, int startPos) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	return str.lastIndexOf(searchStr, startPos);
    }

    /**
     * <p>
     * Case in-sensitive find of the last index within a String.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position returns <code>-1</code>. An empty ("") search String always
     * matches unless the start position is negative. A start position greater
     * than the string length searches the whole string.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
     * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.5
     */
    public static int lastIndexOfIgnoreCase(String str, String searchStr) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    /**
     * <p>
     * Case in-sensitive find of the last index within a String from the
     * specified position.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>-1</code>. A negative start
     * position returns <code>-1</code>. An empty ("") search String always
     * matches unless the start position is negative. A start position greater
     * than the string length searches the whole string.
     * </p>
     *
     * <pre>
     * StringUtils.lastIndexOfIgnoreCase(null, *, *)          = -1
     * StringUtils.lastIndexOfIgnoreCase(*, null, *)          = -1
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @param startPos
     *            the start position
     * @return the first index of the search String, -1 if no match or
     *         <code>null</code> string input
     * @since 2.5
     */
    public static int lastIndexOfIgnoreCase(String str, String searchStr,
											int startPos) {
	if (str == null || searchStr == null) {
	    return -1;
	}
	if (startPos > (str.length() - searchStr.length())) {
	    startPos = str.length() - searchStr.length();
	}
	if (startPos < 0) {
	    return -1;
	}
	if (searchStr.length() == 0) {
	    return startPos;
	}

	for (int i = startPos; i >= 0; i--) {
	    if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
		return i;
	    }
	}
	return -1;
    }

    // Contains
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Checks if String contains a search character, handling <code>null</code>.
     * This method uses {@link String#indexOf(int)}.
     * </p>
     *
     * <p>
     * A <code>null</code> or empty ("") String will return <code>false</code>.
     * </p>
     *
     * <pre>
     * StringUtils.contains(null, *)    = false
     * StringUtils.contains("", *)      = false
     * StringUtils.contains("abc", 'a') = true
     * StringUtils.contains("abc", 'z') = false
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchChar
     *            the character to find
     * @return true if the String contains the search character, false if not or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, char searchChar) {
	if (isEmpty(str)) {
	    return false;
	}
	return str.indexOf(searchChar) >= 0;
    }

    /**
     * <p>
     * Checks if String contains a search String, handling <code>null</code>.
     * This method uses {@link String#indexOf(String)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>false</code>.
     * </p>
     *
     * <pre>
     * StringUtils.contains(null, *)     = false
     * StringUtils.contains(*, null)     = false
     * StringUtils.contains("", "")      = true
     * StringUtils.contains("abc", "")   = true
     * StringUtils.contains("abc", "a")  = true
     * StringUtils.contains("abc", "z")  = false
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return true if the String contains the search String, false if not or
     *         <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, String searchStr) {
	if (str == null || searchStr == null) {
	    return false;
	}
	return str.indexOf(searchStr) >= 0;
    }

    /**
     * <p>
     * Checks if String contains a search String irrespective of case, handling
     * <code>null</code>. Case-insensitivity is defined as by
     * {@link String#equalsIgnoreCase(String)}.
     *
     * <p>
     * A <code>null</code> String will return <code>false</code>.
     * </p>
     *
     * <pre>
     * StringUtils.contains(null, *) = false
     * StringUtils.contains(*, null) = false
     * StringUtils.contains("", "") = true
     * StringUtils.contains("abc", "") = true
     * StringUtils.contains("abc", "a") = true
     * StringUtils.contains("abc", "z") = false
     * StringUtils.contains("abc", "A") = true
     * StringUtils.contains("abc", "Z") = false
     * </pre>
     *
     * @param str
     *            the String to check, may be null
     * @param searchStr
     *            the String to find, may be null
     * @return true if the String contains the search String irrespective of
     *         case or false if not or <code>null</code> string input
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
	if (str == null || searchStr == null) {
	    return false;
	}
	int len = searchStr.length();
	int max = str.length() - len;
	for (int i = 0; i <= max; i++) {
	    if (str.regionMatches(true, i, searchStr, 0, len)) {
		return true;
	    }
	}
	return false;
    }


    // Substring
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets a substring from the specified String avoiding exceptions.
     * </p>
     *
     * <p>
     * A negative start position can be used to start <code>n</code> characters
     * from the end of the String.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>null</code>. An empty ("")
     * String will return "".
     * </p>
     *
     * <pre>
     * StringUtils.substring(null, *)   = null
     * StringUtils.substring("", *)     = ""
     * StringUtils.substring("abc", 0)  = "abc"
     * StringUtils.substring("abc", 2)  = "c"
     * StringUtils.substring("abc", 4)  = ""
     * StringUtils.substring("abc", -2) = "bc"
     * StringUtils.substring("abc", -4) = "abc"
     * </pre>
     *
     * @param str
     *            the String to get the substring from, may be null
     * @param start
     *            the position to start from, negative means count back from the
     *            end of the String by this many characters
     * @return substring from start position, <code>null</code> if null String
     *         input
     */
    public static String substring(String str, int start) {
	if (str == null) {
	    return null;
	}

	// handle negatives, which means last n characters
	if (start < 0) {
	    start = str.length() + start; // remember start is negative
	}

	if (start < 0) {
	    start = 0;
	}
	if (start > str.length()) {
	    return EMPTY;
	}

	return str.substring(start);
    }

    /**
     * <p>
     * Gets a substring from the specified String avoiding exceptions.
     * </p>
     *
     * <p>
     * A negative start position can be used to start/end <code>n</code>
     * characters from the end of the String.
     * </p>
     *
     * <p>
     * The returned substring starts with the character in the
     * <code>start</code> position and ends before the <code>end</code>
     * position. All position counting is zero-based -- i.e., to start at the
     * beginning of the string use <code>start = 0</code>. Negative start and
     * end positions can be used to specify offsets relative to the end of the
     * String.
     * </p>
     *
     * <p>
     * If <code>start</code> is not strictly to the left of <code>end</code>, ""
     * is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substring(null, *, *)    = null
     * StringUtils.substring("", * ,  *)    = "";
     * StringUtils.substring("abc", 0, 2)   = "ab"
     * StringUtils.substring("abc", 2, 0)   = ""
     * StringUtils.substring("abc", 2, 4)   = "c"
     * StringUtils.substring("abc", 4, 6)   = ""
     * StringUtils.substring("abc", 2, 2)   = ""
     * StringUtils.substring("abc", -2, -1) = "b"
     * StringUtils.substring("abc", -4, 2)  = "ab"
     * </pre>
     *
     * @param str
     *            the String to get the substring from, may be null
     * @param start
     *            the position to start from, negative means count back from the
     *            end of the String by this many characters
     * @param end
     *            the position to end at (exclusive), negative means count back
     *            from the end of the String by this many characters
     * @return substring from start position to end positon, <code>null</code>
     *         if null String input
     */
    public static String substring(String str, int start, int end) {
	if (str == null) {
	    return null;
	}

	// handle negatives
	if (end < 0) {
	    end = str.length() + end; // remember end is negative
	}
	if (start < 0) {
	    start = str.length() + start; // remember start is negative
	}

	// check length next
	if (end > str.length()) {
	    end = str.length();
	}

	// if start is greater than end, return ""
	if (start > end) {
	    return EMPTY;
	}

	if (start < 0) {
	    start = 0;
	}
	if (end < 0) {
	    end = 0;
	}

	return str.substring(start, end);
    }

    // Left/Right/Mid
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the leftmost <code>len</code> characters of a String.
     * </p>
     *
     * <p>
     * If <code>len</code> characters are not available, or the String is
     * <code>null</code>, the String will be returned without an exception. An
     * exception is thrown if len is negative.
     * </p>
     *
     * <pre>
     * StringUtils.left(null, *)    = null
     * StringUtils.left(*, -ve)     = ""
     * StringUtils.left("", *)      = ""
     * StringUtils.left("abc", 0)   = ""
     * StringUtils.left("abc", 2)   = "ab"
     * StringUtils.left("abc", 4)   = "abc"
     * </pre>
     *
     * @param str
     *            the String to get the leftmost characters from, may be null
     * @param len
     *            the length of the required String, must be zero or positive
     * @return the leftmost characters, <code>null</code> if null String input
     */
    public static String left(String str, int len) {
	if (str == null) {
	    return null;
	}
	if (len < 0) {
	    return EMPTY;
	}
	if (str.length() <= len) {
	    return str;
	}
	return str.substring(0, len);
    }

    /**
     * <p>
     * Gets the rightmost <code>len</code> characters of a String.
     * </p>
     *
     * <p>
     * If <code>len</code> characters are not available, or the String is
     * <code>null</code>, the String will be returned without an an exception.
     * An exception is thrown if len is negative.
     * </p>
     *
     * <pre>
     * StringUtils.right(null, *)    = null
     * StringUtils.right(*, -ve)     = ""
     * StringUtils.right("", *)      = ""
     * StringUtils.right("abc", 0)   = ""
     * StringUtils.right("abc", 2)   = "bc"
     * StringUtils.right("abc", 4)   = "abc"
     * </pre>
     *
     * @param str
     *            the String to get the rightmost characters from, may be null
     * @param len
     *            the length of the required String, must be zero or positive
     * @return the rightmost characters, <code>null</code> if null String input
     */
    public static String right(String str, int len) {
	if (str == null) {
	    return null;
	}
	if (len < 0) {
	    return EMPTY;
	}
	if (str.length() <= len) {
	    return str;
	}
	return str.substring(str.length() - len);
    }

    /**
     * <p>
     * Gets <code>len</code> characters from the middle of a String.
     * </p>
     *
     * <p>
     * If <code>len</code> characters are not available, the remainder of the
     * String will be returned without an exception. If the String is
     * <code>null</code>, <code>null</code> will be returned. An exception is
     * thrown if len is negative.
     * </p>
     *
     * <pre>
     * StringUtils.mid(null, *, *)    = null
     * StringUtils.mid(*, *, -ve)     = ""
     * StringUtils.mid("", 0, *)      = ""
     * StringUtils.mid("abc", 0, 2)   = "ab"
     * StringUtils.mid("abc", 0, 4)   = "abc"
     * StringUtils.mid("abc", 2, 4)   = "c"
     * StringUtils.mid("abc", 4, 2)   = ""
     * StringUtils.mid("abc", -2, 2)  = "ab"
     * </pre>
     *
     * @param str
     *            the String to get the characters from, may be null
     * @param pos
     *            the position to start from, negative treated as zero
     * @param len
     *            the length of the required String, must be zero or positive
     * @return the middle characters, <code>null</code> if null String input
     */
    public static String mid(String str, int pos, int len) {
	if (str == null) {
	    return null;
	}
	if (len < 0 || pos > str.length()) {
	    return EMPTY;
	}
	if (pos < 0) {
	    pos = 0;
	}
	if (str.length() <= (pos + len)) {
	    return str.substring(pos);
	}
	return str.substring(pos, pos + len);
    }

    // SubStringAfter/SubStringBefore
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the substring before the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A <code>null</code> string input will return <code>null</code>. An empty
     * ("") string input will return the empty string. A <code>null</code>
     * separator will return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBefore(null, *)      = null
     * StringUtils.substringBefore("", *)        = ""
     * StringUtils.substringBefore("abc", "a")   = ""
     * StringUtils.substringBefore("abcba", "b") = "a"
     * StringUtils.substringBefore("abc", "c")   = "ab"
     * StringUtils.substringBefore("abc", "d")   = "abc"
     * StringUtils.substringBefore("abc", "")    = ""
     * StringUtils.substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str
     *            the String to get a substring from, may be null
     * @param separator
     *            the String to search for, may be null
     * @return the substring before the first occurrence of the separator,
     *         <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBefore(String str, String separator) {
	if (isEmpty(str) || separator == null) {
	    return str;
	}
	if (separator.length() == 0) {
	    return EMPTY;
	}
	int pos = str.indexOf(separator);
	if (pos == -1) {
	    return str;
	}
	return str.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A <code>null</code> string input will return <code>null</code>. An empty
     * ("") string input will return the empty string. A <code>null</code>
     * separator will return the empty string if the input string is not
     * <code>null</code>.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str
     *            the String to get a substring from, may be null
     * @param separator
     *            the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     *         <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator) {
	if (isEmpty(str)) {
	    return str;
	}
	if (separator == null) {
	    return EMPTY;
	}
	int pos = str.indexOf(separator);
	if (pos == -1) {
	    return EMPTY;
	}
	return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the substring before the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A <code>null</code> string input will return <code>null</code>. An empty
     * ("") string input will return the empty string. An empty or
     * <code>null</code> separator will return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBeforeLast(null, *)      = null
     * StringUtils.substringBeforeLast("", *)        = ""
     * StringUtils.substringBeforeLast("abcba", "b") = "abc"
     * StringUtils.substringBeforeLast("abc", "c")   = "ab"
     * StringUtils.substringBeforeLast("a", "a")     = ""
     * StringUtils.substringBeforeLast("a", "z")     = "a"
     * StringUtils.substringBeforeLast("a", null)    = "a"
     * StringUtils.substringBeforeLast("a", "")      = "a"
     * </pre>
     *
     * @param str
     *            the String to get a substring from, may be null
     * @param separator
     *            the String to search for, may be null
     * @return the substring before the last occurrence of the separator,
     *         <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringBeforeLast(String str, String separator) {
	if (isEmpty(str) || isEmpty(separator)) {
	    return str;
	}
	int pos = str.lastIndexOf(separator);
	if (pos == -1) {
	    return str;
	}
	return str.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A <code>null</code> string input will return <code>null</code>. An empty
     * ("") string input will return the empty string. An empty or
     * <code>null</code> separator will return the empty string if the input
     * string is not <code>null</code>.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     *
     * @param str
     *            the String to get a substring from, may be null
     * @param separator
     *            the String to search for, may be null
     * @return the substring after the last occurrence of the separator,
     *         <code>null</code> if null String input
     * @since 2.0
     */
    public static String substringAfterLast(String str, String separator) {
	if (isEmpty(str)) {
	    return str;
	}
	if (isEmpty(separator)) {
	    return EMPTY;
	}
	int pos = str.lastIndexOf(separator);
	if (pos == -1 || pos == (str.length() - separator.length())) {
	    return EMPTY;
	}
	return str.substring(pos + separator.length());
    }

    // Substring between
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the String that is nested in between two instances of the same
     * String.
     * </p>
     *
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A
     * <code>null</code> tag returns <code>null</code>.
     * </p>
     *
     * <pre>
     * StringUtils.substringBetween(null, *)            = null
     * StringUtils.substringBetween("", "")             = ""
     * StringUtils.substringBetween("", "tag")          = null
     * StringUtils.substringBetween("tagabctag", null)  = null
     * StringUtils.substringBetween("tagabctag", "")    = ""
     * StringUtils.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str
     *            the String containing the substring, may be null
     * @param tag
     *            the String before and after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String tag) {
	return substringBetween(str, tag, tag);
    }

    /**
     * <p>
     * Gets the String that is nested in between two Strings. Only the first
     * match is returned.
     * </p>
     *
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A
     * <code>null</code> open/close returns <code>null</code> (no match). An
     * empty ("") open and close returns an empty string.
     * </p>
     *
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str
     *            the String containing the substring, may be null
     * @param open
     *            the String before the substring, may be null
     * @param close
     *            the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String open, String close) {
	if (str == null || open == null || close == null) {
	    return null;
	}
	int start = str.indexOf(open);
	if (start != -1) {
	    int end = str.indexOf(close, start + open.length());
	    if (end != -1) {
		return str.substring(start + open.length(), end);
	    }
	}
	return null;
    }


    /**
     * <p>
     * Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <pre>
     * StringUtils.deleteWhitespace(null)         = null
     * StringUtils.deleteWhitespace("")           = ""
     * StringUtils.deleteWhitespace("abc")        = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str
     *            the String to delete whitespace from, may be null
     * @return the String without whitespaces, <code>null</code> if null String
     *         input
     */
    public static String deleteWhitespace(String str) {
	if (isEmpty(str)) {
	    return str;
	}
	int sz = str.length();
	char[] chs = new char[sz];
	int count = 0;
	for (int i = 0; i < sz; i++) {
	    if (!Character.isWhitespace(str.charAt(i))) {
		chs[count++] = str.charAt(i);
	    }
	}
	if (count == sz) {
	    return str;
	}
	return new String(chs, 0, count);
    }


    /**
     * <p>
     * Gets the minimum of three <code>int</code> values.
     * </p>
     *
     * @param a
     *            value 1
     * @param b
     *            value 2
     * @param c
     *            value 3
     * @return the smallest of the values
     */
    /*
     * private static int min(int a, int b, int c) { // Method copied from
     * NumberUtils to avoid dependency on subpackage if (b < a) { a = b; } if (c
     * < a) { a = c; } return a; }
     */

    // startsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String starts with a specified prefix.
     * </p>
     *
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.
     * </p>
     *
     * <pre>
     * StringUtils.startsWith(null, null)      = true
     * StringUtils.startsWith(null, "abc")     = false
     * StringUtils.startsWith("abcdef", null)  = false
     * StringUtils.startsWith("abcdef", "abc") = true
     * StringUtils.startsWith("ABCDEF", "abc") = false
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str
     *            the String to check, may be null
     * @param prefix
     *            the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case
     *         sensitive, or both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWith(String str, String prefix) {
	return startsWith(str, prefix, false);
    }

    /**
     * <p>
     * Case insensitive check if a String starts with a specified prefix.
     * </p>
     *
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case
     * insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abc")     = false
     * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
     * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str
     *            the String to check, may be null
     * @param prefix
     *            the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case
     *         insensitive, or both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
	return startsWith(str, prefix, true);
    }

    /**
     * <p>
     * Check if a String starts with a specified prefix (optionally case
     * insensitive).
     * </p>
     *
     * @see java.lang.String#startsWith(String)
     * @param str
     *            the String to check, may be null
     * @param prefix
     *            the prefix to find, may be null
     * @param ignoreCase
     *            inidicates whether the compare should ignore case (case
     *            insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     *         <code>null</code>
     */
    private static boolean startsWith(String str, String prefix,
									  boolean ignoreCase) {
	if (str == null || prefix == null) {
	    return (str == null && prefix == null);
	}
	if (prefix.length() > str.length()) {
	    return false;
	}
	return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    // endsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String ends with a specified suffix.
     * </p>
     *
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.
     * </p>
     *
     * <pre>
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "def")     = false
     * StringUtils.endsWith("abcdef", null)  = false
     * StringUtils.endsWith("abcdef", "def") = true
     * StringUtils.endsWith("ABCDEF", "def") = false
     * StringUtils.endsWith("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str
     *            the String to check, may be null
     * @param suffix
     *            the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case
     *         sensitive, or both <code>null</code>
     * @since 2.4
     */
    public static boolean endsWith(String str, String suffix) {
	return endsWith(str, suffix, false);
    }

    /**
     * <p>
     * Case insensitive check if a String ends with a specified suffix.
     * </p>
     *
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case
     * insensitive.
     * </p>
     *
     * <pre>
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "def")     = false
     * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
     * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
     * </pre>
     *
     * @see java.lang.String#endsWith(String)
     * @param str
     *            the String to check, may be null
     * @param suffix
     *            the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case
     *         insensitive, or both <code>null</code>
     * @since 2.4
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
	return endsWith(str, suffix, true);
    }

    /**
     * <p>
     * Check if a String ends with a specified suffix (optionally case
     * insensitive).
     * </p>
     *
     * @see java.lang.String#endsWith(String)
     * @param str
     *            the String to check, may be null
     * @param suffix
     *            the suffix to find, may be null
     * @param ignoreCase
     *            inidicates whether the compare should ignore case (case
     *            insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     *         <code>null</code>
     */
    private static boolean endsWith(String str, String suffix,
									boolean ignoreCase) {
	if (str == null || suffix == null) {
	    return (str == null && suffix == null);
	}
	if (suffix.length() > str.length()) {
	    return false;
	}
	int strOffset = str.length() - suffix.length();
	return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }
    
    
	/**
	 * 匹配正则
	 * @param str
	 * 			需要匹配的字符串
	 * @param regex
	 * 			正则
	 * @return 匹配成功返回true 失败为false
	 */
	public static Boolean isMatchingPattern(String str, String regex){
		if(StringUtil.isEmpty(str))return false;
		if(StringUtil.isEmpty(regex))return false;
		if(Pattern.matches(regex, str))return true;
		return false;
	}
	
	/**
     * 是否为中文
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p= Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }
    
	/**
     * 获取字符串长度，英文1，中文2
     * @param str
     * @return
     */
    public static int getStringLength(String str){
    	int length = 0;
    	char[] arr = str.toCharArray();
    	for(int i = 0; i < arr.length; i++){
    		if(isChineseChar(arr[i]+"")){
    			length += 2;
    		}else{
    			length += 1;
    		}
    	}
    	return length;
    }
    
    /**
     * Check whether a String is a numeric.
     *@param str the String to be checked.
     *@return boolean if it is numeric then reurn true.
     */
    public static boolean isNumeric(String str)
    {
        int strLen;
        int i;
        
        if (str == null)
            return false;
        
        strLen = str.length();
        
        if (strLen < 1)
            return true;
        
        for (i = 0; i < strLen; i++)
        {
            if (!Character.isDigit(str.charAt(i))
                    && !Character.isWhitespace(str.charAt(i)))
                return false;
        }
        
        return true;
    }
    
    /**
     * TextColorGrey
     * 用于产品参数颜色
     * @param label：参数名，黑色
     * @param content：参数内容，灰色
     **/
    public static Spanned tcGray(String label, String content){
		return Html.fromHtml("<font color='#666666'>" + label + "</font><font color='#888888'>" + content + "</font>");
    }

    /**
     * TextColorGrey
     * 用于产品参数颜色
     * @param label：参数名，灰色
     * @param content：参数内容，灰色
     **/
    public static Spanned toGray(String label, String content){
		return Html.fromHtml("<font color='#666666'>" + label + "</font><font color='#888888'>" + content + "</font>");
    }

    /**
     * TextColorGrey
     * 用于产品参数颜色
     * @param label：参数名，灰色
     * @param content：参数内容，黑色
     **/
    public static Spanned tcGrayB(String label, String content){
		return Html.fromHtml("<font color='#999999'>" + label + "</font>"
				+ "<font color='#666666'>" + content + "</font>");
    }

    /**
     * TextColorBlack
     * 用于产品参数颜色
     * @param label：参数名，黑色
     * @param content：参数内容，黑色
     **/
    public static Spanned tcBlack(String label, String content){
		return Html.fromHtml("<font color='#666666'>" + label + "</font>"
				+ "<font color='#666666'>" + content + "</font>");
    }

    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，黑色
     * @param content：参数内容，红色
     **/
    public static Spanned tcRed(String label, String content){
		return Html.fromHtml("<font color='#000000'>" + label + "</font>"
				+ "<font color='#FD4746'>" + content + "</font>");
    }

    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，灰色
     * @param content：参数内容，红色
     **/
    public static Spanned GrayRed(String label, String content){
		return Html.fromHtml("<font color='#999999'>" + label + "</font>"
				+ "<font color='#FD4746'>" + content + "</font>");
    }


    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，红色
     * @param content：参数内容，黑色
     **/
    public static Spanned toRed(String label, String content){
		return Html.fromHtml("<font color='#ff0000'>" + label + "</font>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<font color='#666666'>" + content + "</font>");
    }

    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，蓝色
     * @param content：参数内容，黑色
     **/
    public static Spanned toBlue(String label, String content){
		return Html.fromHtml("<font color='#029CE6'>" + label + "</font>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<font color='#999999'>" + content + "</font>");
    }
    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，红色
     * @param content：参数内容，黑色
     **/
    public static Spanned pointRed(String label, String content){
		return Html.fromHtml("<font color='#ff0000'>" + label + "</font><font color='#666666'>" + content + "</font>");
    }

    /**
     * TextColorRed
     * 用于产品参数颜色
     * @param label：参数名，蓝色
     * @param content：参数内容，黑色
     **/
    public static Spanned pointBlue(String label, String content){
		return Html.fromHtml("<font color='#029CE6'>" + label + "</font><font color='#999999'>" + content + "</font>");
    }

	/**
	 * 隐藏电话号码，显示前三+后四
	 * @param num
	 * @return
	 */
	public static String hidePhoneNum(String num){
		return num.substring(0,3) + "***" + num.substring(num.length() - 4);
	}
}
