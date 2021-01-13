package singleo.sjson.utils.parser;

import singleo.sjson.entity.SJsonType;

import java.util.List;
import java.util.Map;

//alibaba.fastjson
public class DefaultSJsonParser implements ISJsonParser {

    public SJsonType getObjectType(Object object){
        if(object instanceof String){
            return SJsonType.String;
        }
        if(object instanceof Integer){
            return SJsonType.Integer;
        }
        if(object instanceof Boolean){
            return SJsonType.Boolean;
        }
        if(object instanceof Map){
            return SJsonType.JSONObject;
        }
        if(object instanceof List){
            return SJsonType.JSONArray;
        }
        return SJsonType.Undefine;
    }
}
