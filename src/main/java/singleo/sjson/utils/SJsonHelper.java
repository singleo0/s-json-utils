package singleo.sjson.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import singleo.sjson.entity.JsonType;
import singleo.sjson.entity.SJsonExpand;
import singleo.sjson.entity.SJsonExpandDiff;
import singleo.sjson.exception.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJsonHelper {



    public static Boolean compareTwoSJsonExpandIsEqual(SJsonExpand sJsonExpand, SJsonExpand anotherSJsonExpand){
        if(!sJsonExpand.getKey().equals(anotherSJsonExpand.getKey())){
            return false;
        }
        if(sJsonExpand.getValue()==null && anotherSJsonExpand.getValue()==null){
            return true;
        }
        if((sJsonExpand.getValue()!=null && anotherSJsonExpand.getValue()==null)||(sJsonExpand.getValue()==null && anotherSJsonExpand.getValue()!=null)){
            return false;
        }
        if(sJsonExpand.getValueType() != anotherSJsonExpand.getValueType()){
            return false;
        }
        if(sJsonExpand.getValueType()==JsonType.String){
            return ((String)sJsonExpand.getValue()).equals((String) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType()==JsonType.Integer){
            return ((Integer)sJsonExpand.getValue()).equals((Integer) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType()==JsonType.Boolean){
            return ((Boolean)sJsonExpand.getValue()).equals((Boolean) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType() == JsonType.JSONArray){
            if(((List)sJsonExpand.getValue()).size() != ((List)anotherSJsonExpand.getValue()).size()){
                return false;
            }
            for(int i=0; i<((List)sJsonExpand.getValue()).size();i++){
                SJsonExpand json1 =new SJsonExpand(sJsonExpand.getKey(), ((List)sJsonExpand.getValue()).get(i));
                SJsonExpand json2 =new SJsonExpand(anotherSJsonExpand.getKey(), ((List) anotherSJsonExpand.getValue()).get(i));
                if(!compareTwoSJsonExpandIsEqual(json1,json2)){
                    return false;
                }
            }
            return true;
        }
        else if(sJsonExpand.getValueType() == JsonType.JSONObject){
            Map<String,Object> map1 = (Map<String,Object>) sJsonExpand.getValue();
            Map<String,Object> map2 = (Map<String,Object>) anotherSJsonExpand.getValue();

            List<String> mapKeyList1 = new ArrayList<String>() ;
            for(int i=0; i<map1.keySet().size();i++){
                mapKeyList1.add(map1.keySet().toArray()[i].toString());
            }
            List<String> mapKeyList2 = new ArrayList<String>();
            for(int i=0; i<map2.keySet().size();i++){
                mapKeyList2.add(map2.keySet().toArray()[i].toString());
            }
            mapKeyList1.removeAll(mapKeyList2);
            if(mapKeyList1.size()!=0){
                return false;
            }
            mapKeyList1.clear();
            for(int i=0; i<map1.keySet().size();i++){
                mapKeyList1.add(map1.keySet().toArray()[i].toString());
            }

            for(int i=0; i<mapKeyList1.size();i++){
                SJsonExpand json1 =new SJsonExpand(mapKeyList1.get(i), map1.get(mapKeyList1.get(i)));
                SJsonExpand json2 =new SJsonExpand(mapKeyList2.get(i), map2.get(mapKeyList2.get(i)));
                if(!compareTwoSJsonExpandIsEqual(json1,json2)){
                    return false;
                }
            }
            return true;
        }
        else {
            return true;
        }
    }

    //不按顺序,两个列表比较
    public static boolean simpleNoSortArrayCompareWith(SJsonExpand sJsonExpand, SJsonExpand anotherSJsonExpand) throws SJsonTypeException {
        if(sJsonExpand.getValueType() != anotherSJsonExpand.getValueType()){
            return false;
        }
        if(sJsonExpand.getValueType()!=JsonType.JSONArray){
            throw new SJsonTypeException("期望的json类型为array,但为: "+sJsonExpand.getValueType());
        }
        ArrayList jsonArray1 = (ArrayList) sJsonExpand.getValue();
        ArrayList jsonArray2 = (ArrayList) anotherSJsonExpand.getValue();

        if(jsonArray1.size()!=jsonArray2.size()){
            return false;
        }
        ArrayList list = new ArrayList();
        list.addAll(jsonArray1);
        list.removeAll(jsonArray2);
        if(list.size()!=0){
            return false;
        }

        return true;
    }

    public static SJsonExpandDiff generateDiff(Object json1, Object json2, List<String> keyPathList){
        return new SJsonExpandDiff(json1, json2,keyPathList).generateDiff();
    }

    public static SJsonExpand getValueByKeyPath(Object jsonObject, String keyPath) throws SJsonNullException, SJsonFormatException, SJsonTypeException, SJsonArrayBlankException {
        if(jsonObject==null){
            throw new SJsonNullException("当前jsonObject为null,要获取的keyPath: "+keyPath);
        }
        String[] keyArray = keyPath.split("\\.");
        List<String> keyList = new ArrayList<String>();
        for(int i=0; i<keyArray.length;i++){
            keyList.add(keyArray[i]);
        }

        return getValueByKeyPath(jsonObject, keyList,0);

    }

    /**
     * [[{"key":"qwe"}],["123"],456,{"key":"asd"}]
     * 暂规定jsonarray中的类型一致,1.都为对象，2.都为数组，3.都为字符串
     * @param jsonObject
     * @param keyPath
     * @param currentDepth
     * @return
     * @throws SJsonTypeException
     * @throws SJsonFormatException
     */
    private static SJsonExpand getValueByKeyPath(Object jsonObject, List<String> keyPath, int currentDepth) throws SJsonTypeException, SJsonFormatException, SJsonArrayBlankException, SJsonNullException {

        JsonType jsonType = getObjectType(jsonObject);
        String key = keyPath.get(currentDepth);

        String kkeyPath = "";
        for(int i=0; i<keyPath.size()-1; i++){
            kkeyPath+=keyPath.get(i)+".";
        }
        kkeyPath+=keyPath.get(keyPath.size()-1);

        if(jsonType != JsonType.JSONObject && jsonType != JsonType.JSONArray){

            throw new SJsonTypeException(jsonType,kkeyPath,key,jsonObject);
        }

        if(currentDepth == keyPath.size()-1 ){
            if(jsonType==JsonType.JSONObject){
                return getValueFromSimpleJsonObjectByKey(jsonObject, key, kkeyPath);
            }
            else if(jsonType!=JsonType.JSONArray){
                throw new SJsonTypeException(jsonType,kkeyPath,key,jsonObject);
            }
        }

        if(jsonType == JsonType.JSONArray){
            List jsonArray = (List) jsonObject;
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
                throw new SJsonArrayBlankException(kkeyPath,key,jsonObject);
            }
        }

        if(jsonType==JsonType.JSONObject){
            Object value=null;

            if(((Map)jsonObject).containsKey(key)){
                value = ((Map)jsonObject).get(key);
            }
            else {
                throw new SJsonFormatException(kkeyPath,key,jsonObject);
            }
            return getValueByKeyPath(value,keyPath,currentDepth+1);
        }
        return null;
    }

    public static SJsonExpand getValueFromSimpleJsonObjectByKey(Object jsonObject, String key,String keyPath) throws SJsonFormatException, SJsonNullException {
        SJsonExpand sJsonExpand = null;
        Object value =null;
        if(jsonObject==null){
            throw new SJsonNullException(keyPath,key);
        }
        if(((Map)jsonObject).containsKey(key)){
            value = ((Map)jsonObject).get(key);
        }
        else {
            throw new SJsonFormatException(keyPath,key,jsonObject);
        }
        sJsonExpand = new SJsonExpand(key,value);
        return sJsonExpand;
    }

    public static String getJsonStringWithNull(Object jsonObject){
        return JSON.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
    }

    public static Object getJsonObjectFromJsonFile(String jsonFilePath) throws IOException, SJsonTypeException {
        String jsonString = FileUtils.getStringFromFile(jsonFilePath);
        Object object = JSON.parse(jsonString);
        if(getObjectType(object)!=JsonType.JSONObject){
            throw new SJsonTypeException("该json文件中不是jsonObject");
        }
        return object;
    }

    public void test(){
        Object json;
        Object json2;
        Map<String,Object> omap = new HashMap<String, Object>();
        omap.put("o-key1","o-value1");
        omap.put("o-key2", "o-value2");
        Map<String, Object> imap = new HashMap<String, Object>();
        imap.put("o-key1","o-value3");
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
//        String jsonStr = JSON.toJSONString(omap, SerializerFeature.WriteMapNullValue);

        json = JSON.toJSON(omap);

        json2=JSON.toJSON(imap);
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
            sJsonExpand = getValueByKeyPath(omap, "o-llit.i-list");
            sJsonExpand.printSJsonExpand();
            sJsonExpand = getValueByKeyPath(null, "o-llit.i-list");
            sJsonExpand.printSJsonExpand();
        }
        catch (SJsonException e){
            System.out.println(e.toString());
        }
        Object jsonFile=null;
        try {
            jsonFile=getJsonObjectFromJsonFile("D:\\singleo\\Software\\ideaIU\\git-repo\\s-json-utils\\TestCases\\user0001.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SJsonTypeException e) {
            e.printStackTrace();
        }
        List<String> path=new ArrayList<String>();
        path.add("o-key1");
        SJsonExpandDiff diff =  generateDiff(json,json2,path);

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
            List jsonArray = (List)object;
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
