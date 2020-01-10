package org.thirteen.authorization.model.params;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.params.base.BaseParam;

/**
 * @author Aaron.Sun
 * @description 查询入参对象-用户
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "查询参数-用户")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserParam extends BaseParam {
}
