package com.velz.service.core._base;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.StandardBasicTypes;

public class SqlFunctionContributor implements FunctionContributor {

    public void contributeFunctions(FunctionContributions fc) {
        fc.getFunctionRegistry().registerPattern("similarity", "similarity(?1, ?2)", getBasicType(fc, StandardBasicTypes.BOOLEAN));
        fc.getFunctionRegistry().registerPattern("is_similar", "(?1 % ?2)", getBasicType(fc, StandardBasicTypes.BOOLEAN));
        fc.getFunctionRegistry().registerPattern("fts_en", "(?1 @@ to_tsquery('english', ?2))", getBasicType(fc, StandardBasicTypes.BOOLEAN));
        fc.getFunctionRegistry().registerPattern("fts_web_en", "(?1 @@ websearch_to_tsquery('english', ?2))", getBasicType(fc, StandardBasicTypes.BOOLEAN));
    }

    private BasicType getBasicType(FunctionContributions fc, BasicTypeReference<?> btr) {
        return fc.getTypeConfiguration().getBasicTypeRegistry().resolve(btr);
    }
}
