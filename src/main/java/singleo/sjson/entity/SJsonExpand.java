package singleo.sjson.entity;

import com.alibaba.fastjson.JSON;
import singleo.sjson.utils.SJsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SJsonExpand {
    private String key;
    private Object value;
    private JsonType valueType;

    public SJsonExpand(String key, Object value){
        this.key = key;
        this.value = value;
        this.valueType = SJsonHelper.getObjectType(value);
    }

    public static Boolean simpleCompareWith(SJsonExpand sJsonExpand,SJsonExpand anotherSJsonExpand){
        if(sJsonExpand.getKey()!=anotherSJsonExpand.getKey()){
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
                if(!simpleCompareWith(json1,json2)){
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
                if(!simpleCompareWith(json1,json2)){
                    return false;
                }
            }
            return true;
        }
        else {
            return true;
        }
    }

    private boolean isTwoListEqual(List list1,List list2){
        list1.removeAll(list1);
        return list1.size()==0;
    }


    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public JsonType getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "SJsonExpand{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", valueType=" + valueType +
                '}';
    }

    public void printSJsonExpand(){
        System.out.println(toString());
    }

    public static void main(String[] args){
        String s="{\n" +
                "  \"paramz\": {\n" +
                "    \"feeds\": [\n" +
                "      {\n" +
                "        \"id\": null,\n" +
                "        \"data\": {\n" +
                "          \"changed\": \"2015-09-22 16:01:41\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"PageIndex\": 1,\n" +
                "  }\n" +
                "}";
        String ss = "{\n" +
                "  \"paramz\": {\n" +
                "    \"feeds\": [\n" +
                "      {\n" +
                "        \"id\": 299076,\n" +
                "        \"data\": {\n" +
                "          \"changed\": \"2015-09-22 16:01:41\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"PageIndex\": 1,\n" +
                "  }\n" +
                "}";
        SJsonExpand json1 = new SJsonExpand("j", JSON.parse(s));
        SJsonExpand json11 = new SJsonExpand("j", JSON.parseObject(s));
        SJsonExpand json2 = new SJsonExpand("j", JSON.parse(ss));
        boolean isE= SJsonExpand.simpleCompareWith(json1,json2);

        System.out.print("123");
    }
}

