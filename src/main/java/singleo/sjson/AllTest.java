//package singleo.sjson;
//
//import singleo.sjson.entity.SJsonExpand;
//import singleo.sjson.entity.SJsonExpandDiff;
//import singleo.sjson.entity.SJsonType;
//import singleo.sjson.exception.SJsonException;
//import singleo.sjson.exception.SJsonTypeException;
//import singleo.sjson.utils.FileUtils;
//import singleo.sjson.utils.SJsonHelper;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AllTest {
//
//    public static void main(String args){
//        AllTest allTest = new AllTest();
//        allTest.testSJsonHelper();
//    }
//
//    public static Object getJsonObjectFromJsonFile(String jsonFilePath) throws IOException, SJsonTypeException {
//        String jsonString = FileUtils.getStringFromFile(jsonFilePath);
//        Object object = JSON.parse(jsonString);
//        if(getObjectType(object)!= SJsonType.JSONObject && getObjectType(object)!= SJsonType.JSONArray){
//            throw new SJsonTypeException("该json文件中不是jsonObject");
//        }
//        return object;
//    }
//
//    public void testSJsonHelper(){
//        Object json;
//        Object json2;
//        Map<String,Object> omap = new HashMap<String, Object>();
//        omap.put("o-key1","o-value1");
//        omap.put("o-key2", "o-value2");
//        Map<String, Object> imap = new HashMap<String, Object>();
//        imap.put("o-key1","o-value3");
//        imap.put("i-key1", "i-value-1");
//        imap.put("i-key2", "i-value-2");
//
//        List<String> list = new ArrayList<String>();
//        list.add("list1");
//        list.add("list2");
//        imap.put("i-list",list);
//        omap.put("o-list",list);
//
//        List<Object> llit = new ArrayList<Object>();
//        llit.add(imap);
////        llit.add(list);
//        omap.put("o-llist",llit);
//        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//        listMap.add(imap);
//
//        omap.put("i-key", imap);
//
//        omap.put("o-listmap",listMap);
//
//        omap.put("o-key3",1);
//        omap.put("o-key4",true);
//        omap.put("o-key5", null);
////        String jsonStr = JSON.toJSONString(omap, SerializerFeature.WriteMapNullValue);
//
//        json = JSON.toJSON(omap);
//        List<String> list1 = new ArrayList<String>();
//        list1.add("list2");
//        list1.add("list3");
//        imap.put("o-list", list1);
//        json2=JSON.toJSON(imap);
//        SJsonHelper.getObjectType(json);
//        SJsonType sJsonType =null;
//        sJsonType = SJsonHelper.getObjectType(((Map<String, Object>)json).get("i-key"));
//        sJsonType = SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-list"));
//        sJsonType = SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key1"));
//        sJsonType = SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key3"));
//        sJsonType = SJsonHelper.getObjectType(((Map<String, Object>)json).get("o-key4"));
//
//        try {
//            SJsonExpand sJsonExpand = getValueByKeyPath(omap, "o-key1");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-key3");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-key4");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-key5");
//            sJsonExpand.printSJsonExpand();
////            sJsonExpand = getValueByKeyPath(omap, "o-key9");
////            sJsonExpand.toString();
//            sJsonExpand = getValueByKeyPath(omap, "i-key.i-key1");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "i-key.i-list");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-list");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-llist.i-key1");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(omap, "o-llit.i-list");
//            sJsonExpand.printSJsonExpand();
//            sJsonExpand = getValueByKeyPath(null, "o-llit.i-list");
//            sJsonExpand.printSJsonExpand();
//        }
//        catch (SJsonException e){
//            System.out.println(e.toString());
//        }
//        Object jsonFile=null;
//        try {
//            jsonFile=getJsonObjectFromJsonFile("D:\\singleo\\Software\\ideaIU\\git-repo\\s-json-utils\\TestCases\\user0001.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SJsonTypeException e) {
//            e.printStackTrace();
//        }
//        List<String> path=new ArrayList<String>();
//        path.add("o-key1");
//        path.add("o-list.***");
//        SJsonExpandDiff diff =  generateDiff(json,json2,path);
//
//
//        List<String> keyPathList = getKeyPathList(json);
//        System.out.print("123");
//    }
//
//    public void testSJsonExpand(){
//        String s="{\n" +
//                "  \"paramz\": {\n" +
//                "    \"feeds\": [\n" +
//                "      {\n" +
//                "        \"id\": null,\n" +
//                "        \"data\": {\n" +
//                "          \"changed\": \"2015-09-22 16:01:41\"\n" +
//                "        }\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"PageIndex\": 1,\n" +
//                "  }\n" +
//                "}";
//        String ss = "{\n" +
//                "  \"paramz\": {\n" +
//                "    \"feeds\": [\n" +
//                "      {\n" +
//                "        \"id\": 299076,\n" +
//                "        \"data\": {\n" +
//                "          \"changed\": \"2015-09-22 16:01:41\"\n" +
//                "        }\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"PageIndex\": 1,\n" +
//                "  }\n" +
//                "}";
//        SJsonExpand json1 = new SJsonExpand("j", JSON.parse(s));
//        SJsonExpand json11 = null;
//        try {
//            json11 = SJsonHelper.getValueByKeyPath((Map<String, Object>) json1.getValue(), "paramz.feeds.data.changed");
//            json11 = SJsonHelper.getValueByKeyPath((Map<String, Object>) json1.getValue(), "paramz.feeds.data.id");
//        } catch (SJsonNullException e) {
//            e.printStackTrace();
//        } catch (SJsonFormatException e) {
//            e.printStackTrace();
//        } catch (SJsonTypeException e) {
//            e.printStackTrace();
//        } catch (SJsonArrayBlankException e) {
//            e.printStackTrace();
//        }
//
//        SJsonExpand json2 = new SJsonExpand("j", JSON.parse(ss));
//        boolean isE= SJsonHelper.compareTwoSJsonExpandIsEqual(json1,json2);
//
//        System.out.print("123");
//    }
//}
