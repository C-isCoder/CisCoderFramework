package com.baichang.android.utils.pinyin;

import android.text.TextUtils;
import java.util.ArrayList;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin {
    private static final HanyuPinyinOutputFormat sFormat = new HanyuPinyinOutputFormat();

    public static String getSignPinyin(String hanZi) {
        if (TextUtils.isEmpty(hanZi)) {
            throw new NullPointerException("Han Zi not null.");
        }
        sFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        sFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        StringBuilder result = new StringBuilder();
        char[] hans = hanZi.toCharArray();
        for (char c : hans) {
            String[] pinyin = null;
            try {
                pinyin = PinyinHelper.toHanyuPinyinStringArray(c, sFormat);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
            if (pinyin == null) {
                result.append(c);
            } else {
                // 默认取第一个拼音，不考虑声调和多音字
                result.append(pinyin[0]);
            }
        }
        return result.toString();
    }

    public static String getSignPinyin(char hanZi) {
        if (hanZi == 0) {
            throw new NullPointerException("Han Zi not null.");
        }
        sFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        sFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        StringBuilder result = new StringBuilder();
        String[] pinyin = null;
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(hanZi, sFormat);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        if (pinyin == null) {
            result.append(hanZi);
        } else {
            // 默认取第一个拼音，不考虑声调和多音字
            result.append(pinyin[0]);
        }
        return result.toString();
    }

    public static ArrayList<String[]> getMultiplePinyin(String hanZi) {
        if (TextUtils.isEmpty(hanZi)) {
            throw new NullPointerException("Han Zi not null.");
        }
        sFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        sFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        final char[] hans = hanZi.toCharArray();
        final ArrayList<String[]> result = new ArrayList<>();
        for (char c : hans) {
            String[] pinyin = null;
            try {
                pinyin = PinyinHelper.toHanyuPinyinStringArray(c, sFormat);
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
            if (pinyin != null) {
                // 默认取第一个拼音，不考虑声调和多音字
                result.add(pinyin);
            }
        }
        return result;
    }

    public static String getFirstIndexPinyin(String hanZi) {
        if (TextUtils.isEmpty(hanZi)) {
            throw new NullPointerException("Han Zi not null.");
        }
        sFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        sFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        char[] hans = hanZi.toCharArray();
        String[] pinyin = null;
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(hans[0], sFormat);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        if (pinyin == null) {
            return "";
        } else {
            // 默认取第一个拼音，不考虑声调和多音字
            return pinyin[0].substring(0, 1);
        }
    }
}
