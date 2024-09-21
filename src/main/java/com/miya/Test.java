package com.miya;

public class Test {

    /**
     * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号
     * 子串
     * 的长度。
     *
     *
     *
     * 示例 1：
     *
     * 输入：s = "(()"
     * 输出：2
     * 解释：最长有效括号子串是 "()"
     * 示例 2：
     *
     * 输入：s = ")()())"
     * 输出：4
     * 解释：最长有效括号子串是 "()()"
     * 示例 3：
     *
     * 输入：s = ""
     * 输出：0
     */
    public int process(String str){
        if (str == null || str.length() <= 1) {
            return 0;
        }

        char[] charArray = str.toCharArray();

        for (int length = charArray.length; length > 0; length--) {
            for (int i = 0; i <= charArray.length - length; i++) {
                if (check(charArray, i, i + length - 1)) {
                    return length;
                }
            }
        }

        return 0;
    }


    public boolean check(char[] chars, int l, int r) {

        int leftCount = 0;
        int rightCount = 0;

        for (int i = l; i <= r; i++) {
            if (chars[i] == '(') {
                leftCount++;
            }

            if (chars[i] == ')') {
                rightCount++;

                if (leftCount > 0) {
                    leftCount--;
                    rightCount--;
                } else {
                    return false;
                }
            }
        }

        return leftCount == 0 && rightCount == 0;
    }








    public String mask (String originStr, int startIndex, int endIndex, char targetChar) {
        // 校验字符串是否有效
        if (originStr == null || originStr.length() == 0) {
            return originStr;
        }

        if (endIndex > originStr.length()) {
            endIndex = originStr.length();
        }

        if (startIndex < 1) {
            startIndex = 1;
        }


        int start = startIndex - 1;
        int end = endIndex - 1;

        // 校验开始和结束的下标，是否有效
        if (start < 0 || start >= originStr.length() || end < 0 || end >= originStr.length() || start > end) {
            return originStr;
        }

        char[] originChars = originStr.toCharArray();

        for (int i = 0; i < originChars.length; i++) {
            if (i >= start && i <= end) {
                originChars[i] = targetChar;
            }
        }

        return new String(originChars);
    }


    /**
     * 招银网络 算法
     *
     * 定义一个 change 方法，入参为一个 int 数字，返回为 该入参中，二进制包含多少个 1；
     *      如 入参 7 （111） 返回 3
     *      入参 3 （11） 返回 2
     *      入参 2 （10） 返回 1
     *
     *
     * 给定一个数组，求数组中的所有元素，需要经历多少次 change 方法后，才能都变成 1
     * @param numbers
     * @return
     */
    public int countOne (int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }

        int count = 0;

        for (int i = 0; i < numbers.length; i++) {
            int num = numbers[i];

            while (num != 1) {
                num = change(num);
                count++;
            }
        }

        return count;
    }


    public int change(int a){
        String binaryString = Integer.toBinaryString(a);
        char[] charArray = binaryString.toCharArray();
        int sum = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '1') {
                sum++;
            }
        }

        return sum;
    }
}
