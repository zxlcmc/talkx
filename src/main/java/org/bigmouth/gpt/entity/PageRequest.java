package org.bigmouth.gpt.entity;

import org.bigmouth.gpt.utils.Constants;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
public class PageRequest {

    private int current = 1;

    private int size = 10;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return Math.min(size, Constants.MAX_PAGE_SIZE);
    }

    public void setSize(int size) {
        this.size = size;
    }
}
