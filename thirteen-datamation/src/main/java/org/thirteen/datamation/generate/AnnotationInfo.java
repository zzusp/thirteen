package org.thirteen.datamation.generate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 生成一个注解所需要的所有信息
 * @date Created in 17:43 2020/8/5
 * @modified By
 */
public class AnnotationInfo implements Serializable {

    /** 注释类的类描述符。即注释路径 */
    private String desc;
    /** true注释在运行时可见 */
    private Boolean visible;
    /** 参数集合 */
    private List<Param> params;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    /** 注释类的参数 */
    class Param {
        /** name */
        private String name;
        /** value */
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
