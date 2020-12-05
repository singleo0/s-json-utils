package singleo.sjson.exception;

public class SJsonNullException extends SJsonException {
    public SJsonNullException(String message){
        super(message);
    }
    public SJsonNullException(String keyPath,String key){
        super("当前jsonObject为null, keyPath为: "+keyPath+", 要获取的key: "+key);
    }
}
