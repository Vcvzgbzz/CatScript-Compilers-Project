package edu.montana.csci.csci468.util;

import edu.montana.csci.csci468.parser.ParseElement;

public class HTMLParseTreeRenderer  {

    public static String render(ParseElement root) {
        StringBuilder buffer = new StringBuilder("<ul>");
        renderElement(buffer, root);
        buffer.append("</ul>");
        return buffer.toString();
    }

    private static void renderElement(StringBuilder buffer, ParseElement root) {
        buffer.append("<li>");
        buffer.append(root.toString());
        buffer.append("<ul>");
        for (ParseElement child : root.getChildren()) {
            renderElement(buffer, child);
        }
        buffer.append("</ul>");
        buffer.append("<l/i>");
    }

}
