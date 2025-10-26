package com.susu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * 哈希生成工具类（用于生成账单唯一哈希值）
 */
public class HashUtils {

    /**
     * 基于输入字符串生成 MD5 哈希值（32 位小写）
     * @param input 要生成哈希的原始字符串（需确保相同内容生成相同字符串）
     * @return MD5 哈希值（null 或空字符串输入返回 null）
     */
    public static String generateMd5Hash(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            // 1. 获取 MD5 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 2. 将输入字符串转为字节数组，更新摘要
            md.update(input.getBytes());
            // 3. 计算哈希值（字节数组）
            byte[] digest = md.digest();
            // 4. 将字节数组转为 32 位小写字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b)); // %02x 确保每个字节占 2 位，不足补 0
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // MD5 是 Java 内置算法，不会抛此异常，此处仅为兼容语法
            e.printStackTrace();
            return null;
        }
    }
}