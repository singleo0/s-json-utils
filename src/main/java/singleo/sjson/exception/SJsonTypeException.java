package singleo.sjson.exception;

import singleo.sjson.entity.SJsonType;

public class SJsonTypeException extends SJsonException {
    public SJsonTypeException(SJsonType sJsonType, String keyPath, String key, Object jsonObject){
        super("期望jsonObject为JSONObject或JSONArray,但为: "+ sJsonType.toString()+" 当前keyPath: "+keyPath+" 当前key: "+key+", jsonObject: "+jsonObject.toString() );
    }
    public SJsonTypeException(String message){
        super(message);
    }
}
