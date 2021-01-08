package singleo.sjson.utils;

import singleo.sjson.entity.SJsonType;
import singleo.sjson.entity.SJsonExpand;
import singleo.sjson.entity.SJsonExpandDiff;
import singleo.sjson.exception.*;
import singleo.sjson.utils.parser.DefaultSJsonParser;
import singleo.sjson.utils.parser.ISJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SJsonHelper {

    private static ISJsonParser sJsonParser =new DefaultSJsonParser();

    public static List<String> getAllKeyPath(Object jsonObject){
        List<String> keyPathList = new ArrayList<String>();
        getKeyPathList(jsonObject, keyPathList, "");
        for(int i=0; i<keyPathList.size(); i++){
            keyPathList.set(i,keyPathList.get(i).substring(1));
        }
        return keyPathList;
    }

    private static void getKeyPathList(Object jsonObject,List<String> keyPathList, String parentKeyPath){
        SJsonType sJsonType = getObjectType(jsonObject);
        if(sJsonType!=SJsonType.JSONObject && sJsonType!=SJsonType.JSONArray){
            return;
        }
        if(sJsonType==SJsonType.JSONArray){
            List currentList = (List)jsonObject;
            if(currentList.size()==0){
                return;
            }
            getKeyPathList(currentList.get(0), keyPathList, parentKeyPath);
        }
        if(sJsonType==SJsonType.JSONObject){
            Map currentObject = (Map) jsonObject;
            if(currentObject==null){
                return;
            }
            List<String> tempKeyList = new ArrayList<String>();
            tempKeyList.addAll(currentObject.keySet());
            for(int i=0; i<tempKeyList.size(); i++){
                String currentKeyPath = parentKeyPath+"."+tempKeyList.get(i);
                keyPathList.add(currentKeyPath);
                getKeyPathList(currentObject.get(tempKeyList.get(i)), keyPathList,currentKeyPath );
            }
        }
    }

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
        if(sJsonExpand.getValueType()== SJsonType.String){
            return ((String)sJsonExpand.getValue()).equals((String) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType()== SJsonType.Integer){
            return ((Integer)sJsonExpand.getValue()).equals((Integer) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType()== SJsonType.Boolean){
            return ((Boolean)sJsonExpand.getValue()).equals((Boolean) anotherSJsonExpand.getValue());
        }
        if(sJsonExpand.getValueType() == SJsonType.JSONArray){
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
        else if(sJsonExpand.getValueType() == SJsonType.JSONObject){
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
                SJsonExpand json2 =new SJsonExpand(mapKeyList1.get(i), map2.get(mapKeyList1.get(i)));
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

        SJsonType sJsonType = getObjectType(jsonObject);
        String key = keyPath.get(currentDepth);

        String kkeyPath = "";
        for(int i=0; i<keyPath.size()-1; i++){
            kkeyPath+=keyPath.get(i)+".";
        }
        kkeyPath+=keyPath.get(keyPath.size()-1);

        if(sJsonType != SJsonType.JSONObject && sJsonType != SJsonType.JSONArray){

            throw new SJsonTypeException(sJsonType,kkeyPath,key,jsonObject);
        }

        if(currentDepth == keyPath.size()-1 ){
            if(sJsonType == SJsonType.JSONObject){
                return getValueFromSimpleJsonObjectByKey(jsonObject, key, kkeyPath);
            }
            else if(sJsonType != SJsonType.JSONArray){
                throw new SJsonTypeException(sJsonType,kkeyPath,key,jsonObject);
            }
        }

        if(sJsonType == SJsonType.JSONArray){
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

        if(sJsonType == SJsonType.JSONObject){
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

    private static SJsonExpand getValueFromSimpleJsonObjectByKey(Object jsonObject, String key,String keyPath) throws SJsonFormatException, SJsonNullException {
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


    public static SJsonType getObjectType(Object object){
        return sJsonParser.getObjectType(object);
    }

    public static void setIJsonParser(ISJsonParser sJsonParser) {
        SJsonHelper.sJsonParser = sJsonParser;
    }
}
