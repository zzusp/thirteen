package org.thirteen.authorization.service.base;

import org.thirteen.authorization.model.vo.base.BaseRecordVO;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口（实体类包含编码，状态，创建，更新，备注，删除标记信息）
 * @date Created in 11:30 2019/12/31
 * @modified by
 */
public interface BaseRecordService<VO extends BaseRecordVO> extends BaseDeleteService<VO> {

    /**
     * 检查编码是否已存在
     *
     * @param code 编码
     * @return 编码是否已存在
     */
    boolean checkCode(String code);

    /**
     * 由编码获取一条数据
     *
     * @param code 编码
     * @return VO对象
     */
    VO findByCode(String code);

}
