package singleo.sjson.exception;

public class SJsonArrayBlankException extends SJsonException{
    public SJsonArrayBlankException(String keyPath, String key, Object jsonObject){
        super("当前jsonArray为空,要获取的keyPath: "+keyPath+" 当前key: "+ key+", jsonObject: "+jsonObject.toString());
    }
    public SJsonArrayBlankException(String message){
        super(message);
    }
}
