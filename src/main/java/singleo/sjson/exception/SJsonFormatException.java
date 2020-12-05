package singleo.sjson.exception;

public class SJsonFormatException extends SJsonException{
    public SJsonFormatException(String keyPath, String key, Object jsonObject){
        super("keyPath为: "+keyPath+", 当前jsonObject不包含名为 "+key +" 的key"+", jsonObject: "+jsonObject.toString());
    }
    public SJsonFormatException(String message){
        super(message);
    }
}
