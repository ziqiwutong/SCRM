package com.scrm.marketing.entity.wrapper;

import com.scrm.marketing.entity.ArticleShareRecord;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-13 23:01
 */
public class ArticleShareRecordWrapper {
    private int readTimes;
    private int readPeople;
    private List<ArticleShareRecord> articleShareRecords;

    public ArticleShareRecordWrapper(List<ArticleShareRecord> articleShareRecords, int readTimes, int readPeople) {
        this.articleShareRecords = articleShareRecords;
        this.readTimes = readTimes;
        this.readPeople = readPeople;
    }

    public List<ArticleShareRecord> getArticleShareRecords() {
        return articleShareRecords;
    }

    public void setArticleShareRecords(List<ArticleShareRecord> articleShareRecords) {
        this.articleShareRecords = articleShareRecords;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public int getReadPeople() {
        return readPeople;
    }

    public void setReadPeople(int readPeople) {
        this.readPeople = readPeople;
    }

    @Override
    public String toString() {
        return "ArticleShareRecordWrapper{" +
                "articleShareRecords=" + articleShareRecords +
                ", readTimes=" + readTimes +
                ", readPeople=" + readPeople +
                '}';
    }
}
