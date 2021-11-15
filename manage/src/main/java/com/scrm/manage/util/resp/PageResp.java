package com.scrm.manage.util.resp;

/**
 * 带有分页信息的Resp
 */
public class PageResp extends Resp {
    // 数据总条目数
    private int totalCount;
    // 当前页码
    private int currentPage;
    // 数据总页数
    private int totalPage;

    public static PageResp success() {
        return new PageResp(CodeEum.SUCCESS);
    }

    public static PageResp error() {
        return new PageResp(CodeEum.ERROR);
    }

    public PageResp setPage(Integer pageCount, Integer currentPage, int totalCount) {
        this.totalCount = totalCount;
        if (pageCount == null || currentPage == null) {
            return this;
        }
        this.currentPage = currentPage;
        this.totalPage = totalCount / pageCount;
        if (totalCount % pageCount != 0) {
            this.totalPage = this.totalPage + 1;
        }
        return this;
    }

    @Override
    public PageResp setData(Object data) {
        super.setData(data);
        return this;
    }

    @Override
    public PageResp setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }

    private PageResp(CodeEum codeEum) {
        this.setCode(codeEum.getCode());
        this.setMsg(codeEum.getMsg());
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
