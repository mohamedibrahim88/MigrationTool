package migration;

import java.util.ArrayList;

public class LeafFolderProperties extends FolderProperties{

    public Boolean isOpend;
    public String ownerID;

    public LeafFolderProperties() {
    }

    public LeafFolderProperties(String code, String arName, String enName, String level, String type, String progressDuration, String interDuration, String durationUnit, String finalDeter, ArrayList<String> owners, String parentID, String folderID, Boolean isOpend, String ownerID) {
        super(code, arName, enName, level, type, progressDuration, interDuration, durationUnit, finalDeter, owners, parentID, folderID);
        this.isOpend = isOpend;
        this.ownerID = ownerID;
    }

    public LeafFolderProperties(Boolean isOpend, String ownerID) {
        this.isOpend = isOpend;
        this.ownerID = ownerID;
    }

    public Boolean getOpend() {
        return isOpend;
    }

    public void setOpend(Boolean opend) {
        isOpend = opend;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public String toString() {
        return "LeafFolderProperties{" +
                "isOpend=" + isOpend +
                ", ownerID='" + ownerID + '\'' +
                ", code='" + code + '\'' +
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
