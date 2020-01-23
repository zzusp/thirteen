package org.hibernate.id.uuid;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.thirteen.authorization.common.utils.IDGenerator;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 自定义UUID生成策略
 * @date Created in 16:23 2020/1/23
 * @modified by
 */
public class CustomUuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return IDGenerator.id();
    }

}
