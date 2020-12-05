package singleo.sjson.entity;

import com.alibaba.fastjson.JSON;
import singleo.sjson.exception.SJsonArrayBlankException;
import singleo.sjson.exception.SJsonFormatException;
import singleo.sjson.exception.SJsonNullException;
import singleo.sjson.exception.SJsonTypeException;
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
        SJsonExpand json11 = null;
        try {
            json11 = SJsonHelper.getValueByKeyPath((Map<String, Object>) json1.getValue(), "paramz.feeds.data.changed");
            json11 = SJsonHelper.getValueByKeyPath((Map<String, Object>) json1.getValue(), "paramz.feeds.data.id");
        } catch (SJsonNullException e) {
            e.printStackTrace();
        } catch (SJsonFormatException e) {
            e.printStackTrace();
        } catch (SJsonTypeException e) {
            e.printStackTrace();
        } catch (SJsonArrayBlankException e) {
            e.printStackTrace();
        }

        SJsonExpand json2 = new SJsonExpand("j", JSON.parse(ss));
        boolean isE= SJsonHelper.compareTwoSJsonExpandIsEqual(json1,json2);

        System.out.print("123");
    }
}

