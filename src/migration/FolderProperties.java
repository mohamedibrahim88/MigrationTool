package migration;

import com.filenet.api.collection.StringList;

import java.util.ArrayList;
import java.util.List;

public class FolderProperties {

    String code ;
    String arName;
    String enName ;
    String level;
    String type;
    String progressDuration;
    String interDuration;
    String durationUnit;
    String finalDeter;
    ArrayList<String> owners;
    String parentID;
    String folderID;


    public FolderProperties(String code, String arName, String enName, String level, String type, String progressDuration, String interDuration, String durationUnit, String finalDeter, ArrayList<String> owners, String parentID, String folderID) {
        this.code = code;
        this.arName = arName;
        this.enName = enName;
        this.level = level;
        this.type = type;
        this.progressDuration = progressDuration;
        this.interDuration = interDuration;
        this.durationUnit = durationUnit;
        this.finalDeter = finalDeter;
        this.owners = owners;
        this.parentID = parentID;
        this.folderID = folderID;
    }


    public FolderProperties() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProgressDuration() {
        return progressDuration;
    }

    public void setProgressDuration(String progressDuration) {
        this.progressDuration = progressDuration;
    }

    public String getInterDuration() {
        return interDuration;
    }

    public void setInterDuration(String interDuration) {
        this.interDuration = interDuration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getFinalDeter() {
        return finalDeter;
    }

    public void setFinalDeter(String finalDeter) {
        this.finalDeter = finalDeter;
    }

    public ArrayList<String> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<String> owners) {
        this.owners = owners;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    @Override
    public String toString() {
        return "FolderProperties{" +
                "code='" + code + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", progressDuration='" + progressDuration + '\'' +
                ", interDuration='" + interDuration + '\'' +
                ", durationUnit='" + durationUnit + '\'' +
                ", finalDeter='" + finalDeter + '\'' +
                ", owners=" + owners +
                ", parentID='" + parentID + '\'' +
                ", folderID='" + folderID + '\'' +
                '}';
    }
}
