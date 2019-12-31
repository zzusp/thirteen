package org.thirteen.authorization.service.base;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口，仅适用包含code字段的实体类
 * @date Created in 11:30 2019/12/31
 * @modified by
 */
public interface CodeService {

    /**
     * 检查编码是否已存在
     *
     * @param code 编码
     * @return 编码是否已存在
     */
    boolean checkCode(String code);

}
