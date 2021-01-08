package singleo.sjson.entity;

import singleo.sjson.exception.*;
import singleo.sjson.utils.SJsonHelper;

import java.util.ArrayList;
import java.util.List;

public class SJsonExpandDiff {
    public boolean isEqual;
    public Object json1;
    public Object json2;
    public List<String> keyPathList;
    public String diff;

    public SJsonExpandDiff(Object json1, Object json2, List<String> keyPathList){
        this.isEqual=true;
        this.diff="";
        this.json1=json1;
        this.json2=json2;
        this.keyPathList = keyPathList;
    }

    public SJsonExpandDiff generateDiff(){
        for(int i = 0; i< keyPathList.size(); i++){
            String keyPath=keyPathList.get(i);
            if(!keyPath.endsWith(".***")){
                generateDiffByKey(keyPath);
            }
            else {
                generateDiffByKey_NoSortArray(keyPath);
            }
        }
        return this;
    }

    private SJsonExpandDiff generateDiffByKey(String keyPath){
        SJsonExpand sJsonValue1 = null;
        SJsonExpand sJsonValue2=null;
        try {
            sJsonValue1 = SJsonHelper.getValueByKeyPath(json1, keyPath);
            sJsonValue2 = SJsonHelper.getValueByKeyPath(json2, keyPath);
            if(!SJsonHelper.compareTwoSJsonExpandIsEqual(sJsonValue1, sJsonValue2)){
                isEqual=false;
                diff+=keyPath+"的值不相等,value1: "+sJsonValue1.toString()+";value2:"+sJsonValue2+"\r\n";
            }
        } catch (SJsonException e) {
            isEqual=false;
            diff+=e.toString()+"\r\n";
        }
        return this;
    }

    private SJsonExpandDiff generateDiffByKey_NoSortArray(String keyPath){
        if(keyPath.endsWith(".***")){
            keyPath = keyPath.replace(".***", "");
        }
        SJsonExpand sJsonValue1 = null;
        SJsonExpand sJsonValue2=null;
        try {
            sJsonValue1 = SJsonHelper.getValueByKeyPath(json1, keyPath);
            sJsonValue2 = SJsonHelper.getValueByKeyPath(json2, keyPath);
            if(!simpleNoSortArrayCompareWith(sJsonValue1, sJsonValue2)){
                isEqual=false;
                diff+=keyPath+".***的值不相等,value1: "+sJsonValue1.toString()+";value2:"+sJsonValue2+"\r\n";
            }
        } catch (SJsonException e) {
            isEqual=false;
            diff+="当前keyPath: "+keyPath+".***, "+e.toString()+"\r\n";
        }
        return this;
    }

    //不按顺序,两个列表比较
    private static boolean simpleNoSortArrayCompareWith(SJsonExpand sJsonExpand, SJsonExpand anotherSJsonExpand) throws SJsonTypeException {
        if(sJsonExpand.getValueType() != anotherSJsonExpand.getValueType()){
            return false;
        }
        if(sJsonExpand.getValueType()!= SJsonType.JSONArray){
            throw new SJsonTypeException("期望的json类型为array,但为: "+sJsonExpand.getValueType());
        }
        List jsonArray1 = (List) sJsonExpand.getValue();
        List jsonArray2 = (List) anotherSJsonExpand.getValue();

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

}
