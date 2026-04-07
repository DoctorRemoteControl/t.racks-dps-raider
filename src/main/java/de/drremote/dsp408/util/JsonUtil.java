package de.drremote.dsp408.util;

import java.util.List;

public final class JsonUtil {
    private JsonUtil() {
    }

    public static String jsonString(String json, String key) {
        int valueStart = findValueStart(json, key);
        if (valueStart < 0) {
            return null;
        }

        if (valueStart >= json.length() || json.charAt(valueStart) != '"') {
            return null;
        }

        return parseJsonString(json, valueStart).value();
    }

    public static Boolean jsonBoolean(String json, String key) {
        int valueStart = findValueStart(json, key);
        if (valueStart < 0) {
            return null;
        }

        if (json.startsWith("true", valueStart)) {
            return true;
        }
        if (json.startsWith("false", valueStart)) {
            return false;
        }
        return null;
    }

    public static Integer jsonInt(String json, String key) {
        Long value = jsonLong(json, key);
        if (value == null) {
            return null;
        }
        return Math.toIntExact(value);
    }

    public static Long jsonLong(String json, String key) {
        int valueStart = findValueStart(json, key);
        if (valueStart < 0) {
            return null;
        }

        String raw = readRawValue(json, valueStart);
        if (raw == null || raw.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void appendField(StringBuilder sb, int level, String key, String value, boolean comma) {
        sb.append(indent(level)).append(quote(key)).append(": ").append(value == null ? "null" : quote(value));
        if (comma) {
            sb.append(",");
        }
        sb.append("\n");
    }

    public static void appendField(StringBuilder sb, int level, String key, int value, boolean comma) {
        sb.append(indent(level)).append(quote(key)).append(": ").append(value);
        if (comma) {
            sb.append(",");
        }
        sb.append("\n");
    }

    public static void appendField(StringBuilder sb, int level, String key, long value, boolean comma) {
        sb.append(indent(level)).append(quote(key)).append(": ").append(value);
        if (comma) {
            sb.append(",");
        }
        sb.append("\n");
    }

    public static void appendField(StringBuilder sb, int level, String key, boolean value, boolean comma) {
        sb.append(indent(level)).append(quote(key)).append(": ").append(value);
        if (comma) {
            sb.append(",");
        }
        sb.append("\n");
    }

    public static String stringArray(List<String> values) {
        if (values == null) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(quote(values.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String quote(String s) {
        if (s == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04X", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String indent(int level) {
        return "  ".repeat(Math.max(0, level));
    }

    private static int findValueStart(String json, String key) {
        if (json == null || key == null || key.isBlank()) {
            return -1;
        }

        int pos = 0;
        while (pos < json.length()) {
            pos = nextQuote(json, pos);
            if (pos < 0) {
                return -1;
            }

            ParsedString parsedKey = parseJsonString(json, pos);
            if (parsedKey == null) {
                return -1;
            }

            int afterKey = skipWhitespace(json, parsedKey.endIndex());
            if (afterKey >= json.length() || json.charAt(afterKey) != ':') {
                pos = parsedKey.endIndex();
                continue;
            }

            if (key.equals(parsedKey.value())) {
                return skipWhitespace(json, afterKey + 1);
            }

            pos = parsedKey.endIndex();
        }

        return -1;
    }

    private static int nextQuote(String json, int start) {
        boolean escape = false;

        for (int i = Math.max(0, start); i < json.length(); i++) {
            char c = json.charAt(i);

            if (escape) {
                escape = false;
                continue;
            }

            if (c == '\\') {
                escape = true;
                continue;
            }

            if (c == '"') {
                return i;
            }
        }

        return -1;
    }

    private static int skipWhitespace(String json, int pos) {
        int i = pos;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        return i;
    }

    private static String readRawValue(String json, int start) {
        if (start < 0 || start >= json.length()) {
            return null;
        }

        int depthCurly = 0;
        int depthSquare = 0;
        boolean inQuotes = false;
        boolean escape = false;

        StringBuilder sb = new StringBuilder();

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);

            if (inQuotes) {
                sb.append(c);

                if (escape) {
                    escape = false;
                    continue;
                }

                if (c == '\\') {
                    escape = true;
                    continue;
                }

                if (c == '"') {
                    inQuotes = false;
                }
                continue;
            }

            if (c == '"') {
                inQuotes = true;
                sb.append(c);
                continue;
            }

            if (c == '{') {
                depthCurly++;
                sb.append(c);
                continue;
            }

            if (c == '}') {
                if (depthCurly == 0 && depthSquare == 0) {
                    break;
                }
                depthCurly--;
                sb.append(c);
                continue;
            }

            if (c == '[') {
                depthSquare++;
                sb.append(c);
                continue;
            }

            if (c == ']') {
                if (depthSquare == 0 && depthCurly == 0) {
                    break;
                }
                depthSquare--;
                sb.append(c);
                continue;
            }

            if (c == ',' && depthCurly == 0 && depthSquare == 0) {
                break;
            }

            sb.append(c);
        }

        return sb.toString().trim();
    }

    private static ParsedString parseJsonString(String json, int quotePos) {
        if (quotePos < 0 || quotePos >= json.length() || json.charAt(quotePos) != '"') {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean escape = false;

        for (int i = quotePos + 1; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escape) {
                switch (c) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (i + 4 >= json.length()) {
                            return null;
                        }
                        String hex = json.substring(i + 1, i + 5);
                        try {
                            sb.append((char) Integer.parseInt(hex, 16));
                        } catch (NumberFormatException e) {
                            return null;
                        }
                        i += 4;
                    }
                    default -> sb.append(c);
                }
                escape = false;
                continue;
            }

            if (c == '\\') {
                escape = true;
                continue;
            }

            if (c == '"') {
                return new ParsedString(sb.toString(), i + 1);
            }

            sb.append(c);
        }

        return null;
    }

    private record ParsedString(String value, int endIndex) {
    }
}