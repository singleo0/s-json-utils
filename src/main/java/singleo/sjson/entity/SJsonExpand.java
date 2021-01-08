package singleo.sjson.entity;

import singleo.sjson.utils.SJsonHelper;

public class SJsonExpand {
    private String key;
    private Object value;
    private SJsonType valueType;

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

    public SJsonType getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "SJsonExpand{" +
                "key=" + key  +
                ", value=" + value +
                ", valueType=" + valueType +
                '}';
    }

}

