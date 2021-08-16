package org.thirteen.datamation.mongodb.model.po;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Aaron.Sun
 * @description mongodb中数据的信息对象
 * @date Created in 15:17 2021/7/27
 * @modified by
 */
public class DmDataPO extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -2867344644541542789L;

    @Override
    public String toString() {
        return "DmDataPO{" + super.toString() + "}";
    }

}
