package com.healspan.claimservice.mstupload.claim.util;

import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Component
public class CommonUtil {

    public static Predicate<Object> isObjectNotNull = obj->obj!=null;
    public static BiPredicate<Object,Boolean> isNewEntityOrCaseSubmitted = (id,isSubmitted)->isSubmitted || id==null;
}
