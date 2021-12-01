package com.scrm.marketing.entity.wrapper;

import com.scrm.marketing.entity.WxReadRecord;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-13 23:01
 */
@SuppressWarnings("unused")
public class WxReadRecordWrapper {
    private int readTimes;
    private int readPeople;
    private List<WxReadRecord> wxReadRecords;

    public WxReadRecordWrapper(int readTimes, int readPeople, List<WxReadRecord> wxReadRecords) {
        this.readTimes = readTimes;
        this.readPeople = readPeople;
        this.wxReadRecords = wxReadRecords;
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

    public List<WxReadRecord> getWxReadRecords() {
        return wxReadRecords;
    }

    public void setWxReadRecords(List<WxReadRecord> wxReadRecords) {
        this.wxReadRecords = wxReadRecords;
    }
}
