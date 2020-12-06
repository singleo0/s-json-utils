package singleo.sjson.entity;

import singleo.sjson.exception.*;
import singleo.sjson.utils.SJsonHelper;

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

    public SJsonExpandDiff generateDiffByKey(String keyPath){
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

    public SJsonExpandDiff generateDiffByKey_NoSortArray(String keyPath){
        if(keyPath.endsWith(".***")){
            keyPath = keyPath.replace(".***", "");
        }
        SJsonExpand sJsonValue1 = null;
        SJsonExpand sJsonValue2=null;
        try {
            sJsonValue1 = SJsonHelper.getValueByKeyPath(json1, keyPath);
            sJsonValue2 = SJsonHelper.getValueByKeyPath(json2, keyPath);
            if(!SJsonHelper.simpleNoSortArrayCompareWith(sJsonValue1, sJsonValue2)){
                isEqual=false;
                diff+=keyPath+".***的值不相等,value1: "+sJsonValue1.toString()+";value2:"+sJsonValue2+"\r\n";
            }
        } catch (SJsonException e) {
            isEqual=false;
            diff+=keyPath+".*** "+e.toString()+"\r\n";
        }
        return this;
    }
}
