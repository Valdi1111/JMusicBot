package org.valdi.jmusicbot.delivery.rendering.html.structure;

import org.valdi.jmusicbot.utils.Format;

/**
 * Represents a structural HTML element that has Tabs on the top.
 *
 * @author Rsl1122
 */
public class TabsElement {
    private final Tab[] tabs;

    public TabsElement(Tab... tabs) {
        this.tabs = tabs;
    }

    public String toHtmlFull() {
        String[] navAndContent = toHtml();
        return navAndContent[0] + navAndContent[1];
    }

    public String[] toHtml() {
        StringBuilder nav = new StringBuilder();
        StringBuilder content = new StringBuilder();

        nav.append("<ul class=\"nav nav-tabs tab-nav-right\" role=\"tablist\">");
        content.append("<div class=\"tab-content\">");
        boolean first = true;
        for (Tab tab : tabs) {
            String id = tab.getId();
            String navText = tab.getNavText();
            String contentHtml = tab.getContentHtml();

            nav.append("<li role=\"presentation\" class=\"nav-item col-black\"")
                    .append("><a href=\"#").append(id).append("\" class=\"nav-link col-black").append(first ? " active" : "").append('"').append(" data-toggle=\"tab\">")
                    .append(navText).append("</a></li>");
            content.append("<div role=\"tabpanel\" class=\"tab-pane fade").append(first ? " in active show" : "")
                    .append("\" id=\"").append(id).append("\">")
                    .append(contentHtml).append("</div>");
            first = false;
        }
        content.append("</div>");
        nav.append("</ul>");

        return new String[]{nav.toString(), content.toString()};
    }

    public static class Tab {
        private final String navText;
        private final String contentHtml;

        public Tab(String navText, String contentHtml) {
            this.navText = navText;
            this.contentHtml = contentHtml;
        }

        public String getNavText() {
            return navText;
        }

        public String getContentHtml() {
            return contentHtml;
        }

        public String getId() {
            return "tab_" + new Format(navText).removeSymbols().removeWhitespace().lowerCase().toString();
        }
    }
}
