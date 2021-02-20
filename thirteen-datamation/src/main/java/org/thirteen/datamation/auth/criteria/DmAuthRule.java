package org.thirteen.datamation.auth.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 规则参数对象
 * @date Created in 9:52 2021/2/13
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmAuthRule {

    /**
     * 当前用户账号
     */
    private List<String> currentAccount;
    /**
     * 当前日期
     */
    private List<String> currentDate;
    /**
     * 当前日期时间
     */
    private List<String> currentDateTime;

    public DmAuthRule() {
        this.currentAccount = new ArrayList<>();
        this.currentDate = new ArrayList<>();
        this.currentDateTime = new ArrayList<>();
    }

    public static DmAuthRule of() {
        return new DmAuthRule();
    }

    public DmAuthRule addCurrentAccount(String account) {
        if (this.currentAccount == null) {
            this.currentAccount = new ArrayList<>();
        }
        this.currentAccount.add(account);
        return this;
    }

    public DmAuthRule addCurrentDate(String currentDate) {
        if (this.currentDate == null) {
            this.currentDate = new ArrayList<>();
        }
        this.currentDate.add(currentDate);
        return this;
    }

    public DmAuthRule addCurrentDateTime(String currentDateTime) {
        if (this.currentDateTime == null) {
            this.currentDateTime = new ArrayList<>();
        }
        this.currentDateTime.add(currentDateTime);
        return this;
    }

    public List<String> getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(List<String> currentAccount) {
        this.currentAccount = currentAccount;
    }

    public List<String> getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(List<String> currentDate) {
        this.currentDate = currentDate;
    }

    public List<String> getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(List<String> currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
