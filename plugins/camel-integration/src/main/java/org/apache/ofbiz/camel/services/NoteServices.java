package org.apache.ofbiz.camel.services;


import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import java.util.Map;
import java.util.List;

public class NoteServices {
    private static String module = NoteServices.class.getName();

    public static Map<String, Object> getNotes(DispatchContext ctx, Map<String, Object> context) {
        Delegator delegator = ctx.getDelegator();
        List<GenericValue> notes = null;
        try {
            notes = delegator.findAll("NoteData", true);
        } catch (GenericEntityException cee) {
            return ServiceUtil.returnError(cee.getMessage());
        }
        Map<String, Object> resp = ServiceUtil.returnSuccess();
        resp.put("notes", notes);
        return resp;
    }
}

