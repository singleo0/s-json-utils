package singleo.sjson.utils.parser;

import singleo.sjson.entity.SJsonType;

import java.util.List;
import java.util.Map;

//alibaba.fastjson
public class DefaultSJsonParser implements ISJsonParser {

    public SJsonType getObjectType(Object object){
        SJsonType sJsonType = SJsonType.JSONObject;

        try {
            Map jsonObject = (Map) object;
            return sJsonType;
        } catch (Exception e){
            sJsonType = SJsonType.JSONArray;
        }

        try {
            List jsonArray = (List)object;
            return sJsonType;
        }
        catch (Exception e){
            sJsonType = SJsonType.Integer;
        }

        try {
            Integer integer = (Integer) object;
            return sJsonType;
        }
        catch (Exception e){
            sJsonType = SJsonType.Boolean;
        }
        try {
            Boolean b = (Boolean) object;
            return sJsonType;
        }
        catch (Exception e){
            sJsonType = SJsonType.String;
        }
        try {
            String str = (String) object;
            return sJsonType;
        }
        catch (Exception e){
            sJsonType = SJsonType.Undefine;
        }
        return sJsonType;
    }
}
