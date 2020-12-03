package singleo.sjson.entity;

public class SJsonCompareResult {
    private Boolean isEqual;
    private String desc;

    public SJsonCompareResult(Boolean isEqual, String desc){
        this.isEqual = isEqual;
        this.desc = desc;
    }

    public void addDesc(String moreDesc){
        desc+=moreDesc;
    }

    public Boolean getEqual() {
        return isEqual;
    }

    public String getDesc() {
        return desc;
    }
}
