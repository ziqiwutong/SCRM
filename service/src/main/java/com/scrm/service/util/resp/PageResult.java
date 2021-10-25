package com.scrm.service.util.resp;

/**
 * @author fzk
 * @date 2021-10-20 15:53
 */
public class PageResult extends Result {
    // 数据总条目数
    private int total;
    // 当前页码
    private int pageNum;
    // 数据总页数
    private int totalPage;

    public PageResult() {
        super();
    }

    public PageResult(int code, String msg, Object data, int total, int pageNum) {
        super(code, msg, data);
        this.total = total;
        this.pageNum = pageNum;
    }

    public static PageResult init(CodeEum codeEum, Object data, int total, int pageNum) {
        return init(codeEum.getCode(), codeEum.getMsg(), data, total, pageNum);
    }

    public static PageResult init(int code, String msg, Object data, int total, int pageNum) {
        return new PageResult(code, msg, data, total, pageNum);
    }

    public static PageResult success(Object data, int total, int pageNum) {
        return init(CodeEum.SUCCESS, data, total, pageNum);
    }

    public static PageResult error() {
        return init(CodeEum.ERROR, null, -1, -1);
    }

    public static PageResult error(CodeEum codeEum) {
        return init(codeEum, null, -1, -1);
    }

    /**
     * getter和setter方法
     */
    public int getTotal() {
        return total;
    }

    public PageResult setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public PageResult setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageResult setPage(Integer pageCount, Integer pageNum, int total) {
        this.total = total;
        if (pageCount == null || pageNum == null) {
            return this;
        }
        this.pageNum = pageNum;
        this.totalPage = total / pageCount;
        if (total % pageCount != 0) {
            this.totalPage = this.totalPage + 1;
        }
        return this;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", pageNum=" + pageNum +
                '}';
    }
}
