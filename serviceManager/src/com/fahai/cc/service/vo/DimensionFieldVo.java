package com.fahai.cc.service.vo;

import com.fahai.cc.service.dimension.entity.Dimension;
import com.fahai.cc.service.domain.entity.Domain;
import com.fahai.cc.service.field.entity.Field;

import java.util.List;

public class DimensionFieldVo {

    private Dimension dimension;
    private Domain domain;
    private List<Field> fields;

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
