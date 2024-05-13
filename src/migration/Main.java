package migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main {
public static void main(String[] args) {
FoldersMigrations.schedulerJob();
//FoldersMigrations.MigrationFromExcel("C:\\Users\\Administrator\\Desktop\\Maktaby\\classification_folders.xlsx");
    //RetrieveUserGroups.GetUserGroups2("user1","fgs@123");
    //RetrieveUserGroups.getUserGroups("user1","fgs@123");
//    FoldersMigrations foldersMigrations = new FoldersMigrations();
//    foldersMigrations.getLeafFoldersByOwnerID("HR");
//    LeafFolderProperties leafFolderProperties = new LeafFolderProperties();
//    leafFolderProperties.setFolderID("1666DF43-9BE0-4137-8DB2-B666EB5DF38F");
//    leafFolderProperties.setParentID("231E650C-052D-4CDA-8181-562F722BAA8D");
//    leafFolderProperties.setCode("2009");
//    leafFolderProperties.setEnName("test");
//    leafFolderProperties.setArName("تجرية");
//    leafFolderProperties.setLevel("1");
//    leafFolderProperties.setType("Child");
//    leafFolderProperties.setOpend(true);
//    leafFolderProperties.setOwnerID("mohamed22");
//
//    leafFolderProperties.setOwners(new ArrayList<String>(){
//        {
//            add("HR");
//            add("FR");
//        }
//    });
//    leafFolderProperties.setDurationUnit("year");
//    leafFolderProperties.setProgressDuration("1");
//    leafFolderProperties.setInterDuration("1");
//    leafFolderProperties.setFinalDeter("Delete");
//
////    FoldersMigrations foldersMigrations = new FoldersMigrations();
//    foldersMigrations.createArchiveFile(leafFolderProperties);
//    foldersMigrations.updateFolderStatus("1666DF43-9BE0-4137-8DB2-B666EB5DF38F", true);
   // foldersMigrations.getFilesByOwnerID("mohamed22");
   // foldersMigrations.deleteFolderByID("1666DF43-9BE0-4137-8DB2-B666EB5DF38F");

//ArrayList<CorrespondenceAttribute>correspondenceAttributes = new ArrayList<>();
//
//CorrespondenceAttribute correspondenceAttribute = new CorrespondenceAttribute();
//
//AttachmentsAttributes attachmentsAttributes = new AttachmentsAttributes();
//correspondenceAttribute.setCorrespondenceID("TestID123");
//correspondenceAttribute.setDocTitle("Test1");
//correspondenceAttribute.setBcc(new ArrayList<String>(){
//    {
//        add("BCC1");
//        add("BCC2");
//    }
//});
//    correspondenceAttribute.setCc(new ArrayList<String>(){
//        {
//            add("CC1");
//            add("CC2");
//        }
//    });
//    correspondenceAttribute.setSenders(new ArrayList<String>(){
//        {
//            add("Sender1");
//            add("Sender2");
//        }
//    });
//
//    correspondenceAttribute.setRecievers(new ArrayList<String>(){
//        {
//            add("Receiver1");
//            add("Receiver2");
//        }
//    });
//
//    correspondenceAttribute.setSubject("TEST SUBJECT");
//
//    correspondenceAttribute.setUserID("TEST USERID");
//
//    correspondenceAttribute.setFolderID("25AFDC64-CAFD-4CDF-85D1-10694E0220D2");
//
//    correspondenceAttribute.setPath("C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Test1.txt");
//
//    attachmentsAttributes.setPath("C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Attachment1.txt");
//
//    attachmentsAttributes.setCorrespondenceID(attachmentsAttributes.getCorrespondenceID());
//
//    attachmentsAttributes.setClassification("Meeting");
//    attachmentsAttributes.setDocTitle("Attachement1");
//    attachmentsAttributes.setProp(new HashMap<String,Object>(){{
//      put("MeetingDate",new Date());
//      put("MeetingReason","اجتماع لتحديد وقت بدأ المشروع");
//    }});
//    correspondenceAttribute.setAttachmentsAttributes(new ArrayList<AttachmentsAttributes>()
//    {
//      {
//        add(attachmentsAttributes);
//      }
//
//    });
//
//    //////////////////////
//    CorrespondenceAttribute correspondenceAttribute2 = new CorrespondenceAttribute();
//
//    AttachmentsAttributes attachmentsAttributes2 = new AttachmentsAttributes();
//    correspondenceAttribute2.setCorrespondenceID("TestID1234");
//    correspondenceAttribute2.setDocTitle("Test2");
//    correspondenceAttribute2.setBcc(new ArrayList<String>(){
//        {
//            add("BCC1");
//            add("BCC2");
//        }
//    });
//    correspondenceAttribute2.setCc(new ArrayList<String>(){
//        {
//            add("CC1");
//            add("CC2");
//        }
//    });
//    correspondenceAttribute2.setSenders(new ArrayList<String>(){
//        {
//            add("Sender1");
//            add("Sender2");
//
//        }
//    });
//
//    correspondenceAttribute2.setRecievers(new ArrayList<String>(){
//        {
//            add("Receiver1");
//            add("Receiver2");
//        }
//    });
//
//    correspondenceAttribute2.setSubject("TEST SUBJECT");
//
//    correspondenceAttribute2.setUserID("TEST USERID");
//
//    correspondenceAttribute2.setFolderID("25AFDC64-CAFD-4CDF-85D1-10694E0220D2");
//
//    correspondenceAttribute2.setPath("C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Test2.txt");
//
//
//    attachmentsAttributes2.setPath("C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Attachment2.txt");
//    attachmentsAttributes2.setDocTitle("Attachment2");
//    attachmentsAttributes2.setCorrespondenceID(attachmentsAttributes.getCorrespondenceID());
//    attachmentsAttributes2.setClassification("Holiday");
//    attachmentsAttributes2.setProp(new HashMap<String,Object>(){{
//        put("CorrespondenceID",attachmentsAttributes2.getCorrespondenceID());
//        put("ReturnDate",new Date());
//        put("LeaveDate",new Date());
//        put("LeaveReason","الذهاب الي الطبيب");
//        put("DaysNumber","5");
//        //
//    }});
//    correspondenceAttribute2.setAttachmentsAttributes(new ArrayList<AttachmentsAttributes>()
//    {
//        {
//            add(attachmentsAttributes2);
//        }
//
//    });
//
//
//correspondenceAttributes.add(correspondenceAttribute);
//correspondenceAttributes.add(correspondenceAttribute2);
//FoldersMigrations.createCorrespondenceDoc(correspondenceAttributes);
}
}