package migration;

import java.util.HashMap;

public class AttachmentsAttributes {
    String correspondenceID;
    String docTitle;
    String classification;
    String path;
    HashMap<String,Object> prop = new HashMap<String,Object>();

    public AttachmentsAttributes() {
    }

    public AttachmentsAttributes(String docTitle,String correspondenceID, String classification, String path, HashMap<String, Object> prop) {
        this.docTitle=docTitle;
        this.correspondenceID = correspondenceID;
        this.classification = classification;
        this.path = path;
        this.prop = prop;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getCorrespondenceID() {
        return correspondenceID;
    }

    public void setCorrespondenceID(String correspondenceID) {
        this.correspondenceID = correspondenceID;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, Object> getProp() {
        return prop;
    }

    public void setProp(HashMap<String, Object> prop) {
        this.prop = prop;
    }

    @Override
    public String toString() {
        return "AttachmentsAttributes{" +
                "docTitle='" + docTitle + '\'' +
                "CorrespondenceID='" + correspondenceID + '\'' +
                ", classification='" + classification + '\'' +
                ", path='" + path + '\'' +
                ", prop=" + prop +
                '}';
    }
}
