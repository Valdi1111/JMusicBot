package org.valdi.jmusicbot.delivery.rendering.html.structure;

import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;
import org.valdi.jmusicbot.utils.Format;

/**
 * Html utility for creating navigation link html.
 *
 * @author Rsl1122
 */
public class NavLink {
    private final Icon icon;
    private final String tabName;
    private final boolean collapsed;

    public NavLink(Icon icon, String tabName) {
        this(icon, tabName, false);
    }

    private NavLink(Icon icon, String tabName, boolean collapsed) {
        this.icon = icon;
        this.tabName = tabName;
        this.collapsed = collapsed;
    }

    public static NavLink main(Icon icon, String tabName) {
        return new NavLink(icon, tabName, false);
    }

    public static NavLink collapsed(Icon icon, String tabName) {
        return new NavLink(icon, tabName, true);
    }

    public String toHtml() {
        String tabID = new Format(tabName).justLetters().lowerCase().toString();
        if (collapsed) {
            return "<a class=\"collapse-item nav-button\" href=\"#tab-" + tabID + "\">" +
                    icon.toHtml() + ' ' +
                    tabName + "</a>";
        }
        return "<li class=\"nav-item nav-button\">" +
                "<a class=\"nav-link\" href=\"#tab-" + tabID + "\">" +
                icon.toHtml() +
                "<span>" + tabName + "</span></a>" +
                "</li>";
    }

}
