package org.valdi.jmusicbot.utils;

import java.util.Comparator;

/**
 * Compares Strings and sorts them by length (Longest fist).
 *
 * @author Rsl1122
 */
public class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return -Long.compare(o1.length(), o2.length());
    }
}
