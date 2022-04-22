package com.wdbyte.leetcode;

/**
 * https://leetcode-cn.com/problems/goat-latin/
 * 824. 山羊拉丁文
 *
 * @author niulang
 * @date 2022/04/21
 */
public class LeetCode824_ToGoatLatin {

    public static void main(String[] args) {
        //String goatLatin = new LeetCode824().toGoatLatin("I speak Goat Latin");
        String goatLatin = new LeetCode824_ToGoatLatin().toGoatLatin("The quick brown fox jumped over the lazy dog");
        //String goatLatin = new LeetCode824().toGoatLatin("goat");
        System.out.println(goatLatin);
    }

    /**
     * 如果单词以元音开头（'a', 'e', 'i', 'o', 'u'），在单词后添加"ma"。
     * 例如，单词 "apple" 变为 "applema" 。
     * 如果单词以辅音字母开头（即，非元音字母），移除第一个字符并将它放到末尾，之后再添加"ma"。
     * 例如，单词 "goat" 变为 "oatgma" 。
     * 根据单词在句子中的索引，在单词最后添加与索引相同数量的字母'a'，索引从 1 开始。
     * 例如，在第一个单词后添加 "a" ，在第二个单词后添加 "aa" ，以此类推。
     *
     * @param sentence
     * @return
     */
    public String toGoatLatin(String sentence) {
        String[] array = sentence.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            String word = array[i];
            if (builder.length() != 0) {
                builder.append(" ");
            }
            // 元音开头
            char[] chars = word.toCharArray();
            if (chars[0] == 'A' || chars[0] == 'E' || chars[0] == 'I' || chars[0] == 'O' || chars[0] == 'U' ||
                chars[0] == 'a' || chars[0] == 'e' || chars[0] == 'i' || chars[0] == 'o' || chars[0] == 'u' ) {
                builder.append(word).append("ma");
            } else {
                // 辅音开头
                for (int j = 0; j < chars.length; j++) {
                    if (j == 0) {continue;}
                    builder.append(chars[j]);
                }
                builder.append(chars[0]).append("ma");
            }
            for (int j = 0; j <= i; j++) {
                builder.append("a");
            }
        }
        return builder.toString();
    }

    public String toGoatLatin2(String sentence) {
        String[] array = sentence.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            String word = array[i];
            // 元音开头
            char charAt0 = word.charAt(0);
            if (charAt0 == 'A' || charAt0 == 'E' || charAt0 == 'I' || charAt0 == 'O' || charAt0 == 'U' ||
                charAt0 == 'a' || charAt0 == 'e' || charAt0 == 'i' || charAt0 == 'o' || charAt0 == 'u') {
                builder.append(word).append("ma");
            } else {
                // 辅音开头
                builder.append(word.substring(1)).append(charAt0).append("ma");
            }
            for (int j = 0; j <= i; j++) {
                builder.append("a");
            }
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
