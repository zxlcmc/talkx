package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author allen
 * @date 2023/6/13
 * @since 1.0.0
 */
public class PageVo<T> {

    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;
    private long pages = 1;

    public PageVo() {
    }

    public PageVo(IPage<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.pages = page.getPages();
    }

    public static  <T, S> PageVo<T> of(IPage<S> result, Function<S, T> mapper) {
        if (Objects.isNull(result)) {
            return new PageVo<>();
        }
        IPage<T> mid = new Page<>();
        mid.setCurrent(result.getCurrent());
        mid.setSize(result.getSize());
        mid.setPages(result.getPages());
        mid.setTotal(result.getTotal());
        List<S> records = result.getRecords();
        List<T> pages = records.stream().map(mapper).collect(Collectors.toList());
        mid.setRecords(pages);
        return new PageVo<>(mid);
    }

    public List<T> getRecords() {
        return records;
    }

    public long getTotal() {
        return total;
    }

    public long getSize() {
        return size;
    }

    public long getCurrent() {
        return current;
    }

    public long getPages() {
        return pages;
    }
}
