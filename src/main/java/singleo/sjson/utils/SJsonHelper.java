package singleo.sjson.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import singleo.sjson.entity.JsonType;
import singleo.sjson.entity.SJsonExpand;
import singleo.sjson.exception.SJsonArrayBlankException;
import singleo.sjson.exception.SJsonFormatException;
import singleo.sjson.exception.SJsonNullException;
import singleo.sjson.exception.SJsonTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJsonHelper {

    Object json;

    public SJsonExpand getValueByKeyPath(Map<String, Object> jsonObject, String keyPath) throws SJsonNullException, SJsonFormatException, SJsonTypeException, SJsonArrayBlankException {
        if(jsonObject==null){
            throw new SJsonNullException("当前jsonObject为null,要获取的key: "+keyPath);
        }
        String[] keyArray = keyPath.split("\\.");
        List<String> keyList = new ArrayList<String>();
        for(int i=0; i<keyArray.length;i++){
            keyList.add(keyArray[i]);
        }

        return getValueByKeyPath(new JSONObject(jsonObject), keyList,0);

    }

    /**
     * [[{"key":"qwe"}],["123"],456,{"key":"asd"}]
     * 暂规定jsonarray中的类型一致,都为对象，都为数组，都为字符串
     * @param jsonObject
     * @param keyPath
     * @param currentDepth
     * @return
     * @throws SJsonTypeException
     * @throws SJsonFormatException
     */
    private SJsonExpand getValueByKeyPath(Object jsonObject, List<String> keyPath, int currentDepth) throws SJsonTypeException, SJsonFormatException, SJsonArrayBlankException, SJsonNullException {


        JsonType jsonType = getObjectType(jsonObject);
        String key = keyPath.get(currentDepth);

        if(jsonType != JsonType.JSONObject && jsonType != JsonType.JSONArray){
            throw new SJsonTypeException("当前为keyPath最后一个key: "+key+ ",期望jsonObject为JSONObject或JSONArray,但为: "+jsonType.toString());
        }

        if(currentDepth == keyPath.size()-1 ){
            if(jsonType==JsonType.JSONObject){
                return getValueFromSimpleJsonObjectByKey(jsonObject, key);
            }
            else if(jsonType!=JsonType.JSONArray){
                throw new SJsonTypeException("当前为keyPath最后一个key: "+key+ ",期望object为JSONObject或JSONArray,但为: "+jsonType.toString());
            }
        }

        if(jsonType == JsonType.JSONArray){
            ArrayList jsonArray = (ArrayList) jsonObject;
            List<Object> objectList = new ArrayList<Object>();
            Object subObject=null;
            if(jsonArray.size()!=0){
                for(int i=0; i<jsonArray.size(); i++){
                    subObject = getValueByKeyPath(jsonArray.get(i),keyPath, currentDepth);
                    objectList.add(subObject);
                }
                //get(0)默认array中对象类型一致
                return new SJsonExpand(key, objectList);
            }
            else {
                throw new SJsonArrayBlankException("当前jsonArray为空,要获取的key: "+key);
            }
        }

        if(jsonType==JsonType.JSONObject){
            Object value=null;


            if(((Map)jsonObject).containsKey(key)){
                value = ((Map)jsonObject).get(key);
            }
            else {
                throw new SJsonFormatException("当前jsonObject不包含名为 "+key +" 的key");
            }
            return getValueByKeyPath(value,keyPath,currentDepth+1);
        }
        return null;
    }

    public SJsonExpand getValueFromSimpleJsonObjectByKey(Object jsonObject, String key) throws SJsonFormatException, SJsonNullException {
        SJsonExpand sJsonExpand = null;
        Object value =null;
        if(jsonObject==null){
            throw new SJsonNullException("当前jsonObject为null,要获取的key: "+key);
        }
        if(((Map)jsonObject).containsKey(key)){
            value = ((Map)jsonObject).get(key);
        }
        else {
            throw new SJsonFormatException("当前jsonObject不包含名为 "+key +" 的key");
        }
        sJsonExpand = new SJsonExpand(key,value);
        return sJsonExpand;
    }

    public static String getJsonStringWithNull(Map<String, Object> map){
        return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    public void test(){
        Map<String,Object> omap = new HashMap<String, Object>();
        omap.put("o-key1","o-value1");
        omap.put("o-key2", "o-value2");
        Map<String, Object> imap = new HashMap<String, Object>();
        imap.put("i-key1", "i-value-1");
        imap.put("i-key2", "i-value-2");

        List<String> list = new ArrayList<String>();
        list.add("list1");
        list.add("list2");
        imap.put("i-list",list);
        omap.put("o-list",list);

        List<Object> llit = new ArrayList<Object>();
        llit.add(imap);
//        llit.add(list);
        omap.put("o-llist",llit);
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        listMap.add(imap);

        omap.put("i-key", imap);

        omap.put("o-listmap",listMap);

        omap.put("o-key3",1);
        omap.put("o-key4",true);
        omap.put("o-key5", null);
        String jsonStr = JSON.toJSONString(omap, SerializerFeature.WriteMapNullValue);

        json = JSON.parse(jsonStr);

        SJsonHelper.getObjectType(json);
        JsonType jsonType=null;
        jsonType= SJsonHelper.getObjectType(((Map<String, Object>)json).get("i-key"));
        jsonType= SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-list"));
        jsonType= SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key1"));
        jsonType= SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key3"));
        jsonType= SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key4"));

        try {
            SJsonExpand sJsonExpand = getValueByKeyPath(omap, "o-key1");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "o-key3");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "o-key4");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "o-key5");
            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-key9");
//            sJsonExpand.toString();
            sJsonExpand = getValueByKeyPath(omap, "i-key.i-key1");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "i-key.i-list");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "o-list");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(omap, "o-llist.i-key1");
            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-llit.i-list");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(null, "o-llit.i-list");
//            sJsonExpand.printSJsonExpand();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        System.out.print("123");
    }

    public static void main(String[] args){
        SJsonHelper sJsonHelper = new SJsonHelper();
        sJsonHelper.test();

    }


























    public static JsonType getObjectType(Object object){
        JsonType jsonType = JsonType.JSONObject;

        try {
            Map jsonObject = (Map) object;
            return jsonType;
        } catch (Exception e){

        }

        try {
            JSONObject jsonObject = (JSONObject) object;
            return jsonType;
        } catch (Exception e){
            jsonType = JsonType.JSONArray;
        }

        try {
            ArrayList jsonArray = (ArrayList)object;
            return jsonType;
        }
        catch (Exception e){

        }

        try {
            JSONArray jsonArray = (JSONArray)object;
            return jsonType;
        }
        catch (Exception e){
            jsonType = JsonType.Integer;
        }

        try {
            Integer integer = (Integer) object;
            return jsonType;
        }
        catch (Exception e){
            jsonType = JsonType.Boolean;
        }
        try {
            Boolean b = (Boolean) object;
            return jsonType;
        }
        catch (Exception e){
            jsonType = JsonType.String;
        }
        try {
            String str = (String) object;
            return jsonType;
        }
        catch (Exception e){
            jsonType = JsonType.Undefine;
        }
        return jsonType;
    }
}
