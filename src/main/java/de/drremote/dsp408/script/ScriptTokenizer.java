package de.drremote.dsp408.script;

import java.util.ArrayList;
import java.util.List;

final class ScriptTokenizer {
    private ScriptTokenizer() {
    }

    static List<String> splitStatements(String script) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inQuotes = false;
        boolean escaping = false;
        boolean inComment = false;
        int parenDepth = 0;
        int bracketDepth = 0;

        for (int i = 0; i < script.length(); i++) {
            char c = script.charAt(i);

            if (inComment) {
                if (c == '\n' || c == '\r') {
                    inComment = false;
                    if (parenDepth == 0 && bracketDepth == 0) {
                        flushStatement(out, current);
                    } else {
                        current.append(' ');
                    }
                }
                continue;
            }

            if (escaping) {
                current.append(c);
                escaping = false;
                continue;
            }

            if (inQuotes && c == '\\') {
                escaping = true;
                current.append(c);
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
                continue;
            }

            if (!inQuotes && c == '#') {
                inComment = true;
                continue;
            }
            if (!inQuotes && c == '/' && i + 1 < script.length() && script.charAt(i + 1) == '/') {
                inComment = true;
                i++;
                continue;
            }

            if (!inQuotes) {
                if (c == '(') {
                    parenDepth++;
                    current.append(c);
                    continue;
                }
                if (c == ')') {
                    parenDepth--;
                    current.append(c);
                    continue;
                }
                if (c == '[') {
                    bracketDepth++;
                    current.append(c);
                    continue;
                }
                if (c == ']') {
                    bracketDepth--;
                    current.append(c);
                    continue;
                }

                if (c == '{' || c == '}') {
                    if (parenDepth == 0 && bracketDepth == 0) {
                        flushStatement(out, current);
                        out.add(String.valueOf(c));
                    } else {
                        current.append(c);
                    }
                    continue;
                }

                if (c == ';' || c == '\n' || c == '\r') {
                    if (parenDepth == 0 && bracketDepth == 0) {
                        flushStatement(out, current);
                    } else {
                        current.append(' ');
                    }
                    continue;
                }
            }

            current.append(c);
        }

        flushStatement(out, current);
        return out;
    }

    static String stripComments(String line) {
        boolean inQuotes = false;
        boolean escaping = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (escaping) {
                sb.append(c);
                escaping = false;
                continue;
            }

            if (inQuotes && c == '\\') {
                escaping = true;
                sb.append(c);
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                sb.append(c);
                continue;
            }

            if (!inQuotes && c == '#') {
                break;
            }
            if (!inQuotes && c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                break;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean escaping = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (escaping) {
                current.append(c);
                escaping = false;
                continue;
            }

            if (inQuotes && c == '\\') {
                escaping = true;
                current.append(c);
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
                continue;
            }

            if (!inQuotes) {
                if (Character.isWhitespace(c)) {
                    flushToken(out, current);
                    continue;
                }

                if (c == '(' || c == ')' || c == ',' || c == '{' || c == '}') {
                    flushToken(out, current);
                    out.add(String.valueOf(c));
                    continue;
                }
            }

            current.append(c);
        }

        if (inQuotes) {
            throw new IllegalArgumentException("Invalid line: unclosed quotes.");
        }

        flushToken(out, current);
        return out;
    }

    static int findEqualsOperator(String raw) {
        boolean inQuotes = false;
        boolean escaping = false;
        int parenDepth = 0;
        int bracketDepth = 0;

        for (int i = 0; i < raw.length() - 1; i++) {
            char c = raw.charAt(i);

            if (escaping) {
                escaping = false;
                continue;
            }

            if (inQuotes && c == '\\') {
                escaping = true;
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }

            if (inQuotes) {
                continue;
            }

            if (c == '(') {
                parenDepth++;
                continue;
            }
            if (c == ')') {
                parenDepth--;
                continue;
            }
            if (c == '[') {
                bracketDepth++;
                continue;
            }
            if (c == ']') {
                bracketDepth--;
                continue;
            }

            if (parenDepth == 0 && bracketDepth == 0 && c == '=' && raw.charAt(i + 1) == '=') {
                return i;
            }
        }

        return -1;
    }

    private static void flushToken(List<String> out, StringBuilder current) {
        if (current.length() == 0) {
            return;
        }
        out.add(current.toString());
        current.setLength(0);
    }

    private static void flushStatement(List<String> out, StringBuilder current) {
        String text = current.toString().trim();
        if (!text.isEmpty()) {
            out.add(text);
        }
        current.setLength(0);
    }
}
