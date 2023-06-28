package com.yxr.base.util

import java.util.regex.Pattern

class StringUtil {
    companion object{
        @JvmStatic
        fun isZiMu(c: Char): Boolean {
            return c.code in 0x61..0x7a || c.code in 0x41..0x5a
        }

        /**
         * 是否包含中文
         */
        @JvmStatic
        fun hasChinese(text: String): Boolean {
            val p = Pattern.compile("[\u4e00-\u9fa5]")
            val m = p.matcher(text)
            return m.find()
        }

        @JvmStatic
        fun isChinese(ch: Char): Boolean {
            return !(ch.code < 0x4E00 || ch.code > 0x9FA5)
        }

        @JvmStatic
        fun isAllChinese(str: String?): Boolean {
            if (!str.isNullOrBlank()) {
                for (element in str) {
                    if (element.code < 0x4E00 || element.code > 0x9FA5) {
                        return false
                    }
                }
                return true
            }
            return false
        }

        @JvmStatic
        fun isPunctuation(ch: Char): Boolean {
            if (isCjkPunc(ch)) {
                return true
            }
            if (isEnPunc(ch)) {
                return true
            }
            if (ch.code in 0x2018..0x201F) {
                return true
            }
            if (ch.code == 0xFF01 || ch.code == 0xFF02) {
                return true
            }
            if (ch.code == 0xFF07 || ch.code == 0xFF0C) {
                return true
            }
            if (ch.code == 0xFF1A || ch.code == 0xFF1B) {
                return true
            }
            if (ch.code == 0xFF1F || ch.code == 0xFF61) {
                return true
            }
            if (ch.code == 0xFF0E) {
                return true
            }
            return ch.code == 0xFF65
        }

        @JvmStatic
        fun isEnPunc(ch: Char): Boolean {
            if (ch.code in 0x21..0x22) {
                return true
            }
            if (ch.code == 0x27 || ch.code == 0x2C) {
                return true
            }
            if (ch.code == 0x2E || ch.code == 0x3A) {
                return true
            }
            return ch.code == 0x3B || ch.code == 0x3F
        }

        @JvmStatic
        fun isCjkPunc(ch: Char): Boolean {
            if (ch.code in 0x3001..0x3003) {
                return true
            }
            return ch.code in 0x301D..0x301F
        }

        @JvmStatic
        fun isNum(ch: Char): Boolean {
            return ch.code in 0x30..0x39
        }

        @JvmStatic
        fun isEmail(email: String?): Boolean {
            if (email == null || email.isEmpty() || email.length > 256) {
                return false
            }
            val pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
            return pattern.matcher(email).matches()
        }
    }
}