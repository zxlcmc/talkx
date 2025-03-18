package org.bigmouth.gpt.scheduler.gpts;

import org.apache.commons.lang3.StringUtils;

/**
 * 枚举定义了不同的分组类型。
 */
public enum GroupEnum {
    /**
     *
     */
    FEATURED("Featured", "特色"),
    TRENDING("Trending", "趋势"),
    BY_CHATGPT("By ChatGPT", "ChatGPT"),
    DALLE("DALL·E", "DALL·E"),
    WRITING("Writing", "文案"),
    PRODUCTIVITY("Productivity", "效率"),
    RESEARCH_ANALYSIS("Research & Analysis", "研究分析"),
    PROGRAMMING("Programming", "编程"),
    EDUCATION("Education", "教育"),
    LIFESTYLE("Lifestyle", "生活"),
    OTHER("Other", "其他");

    private final String title;
    private final String tagName;

    GroupEnum(String title, String tagName) {
        this.title = title;
        this.tagName = tagName;
    }

    public String getTitle() {
        return title;
    }

    public String getTagName() {
        return tagName;
    }

    public static GroupEnum of(String title) {
        for (GroupEnum groupEnum : values()) {
            if (StringUtils.equals(title, groupEnum.getTitle())) {
                return groupEnum;
            }
        }
        return OTHER;
    }
}