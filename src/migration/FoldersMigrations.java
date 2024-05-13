package migration;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import org.apache.poi.ss.usermodel.*;
import org.omg.PortableServer.POA;

import javax.activation.MimetypesFileTypeMap;
import javax.security.auth.Subject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class FoldersMigrations {

    private static Connection conn = null;

    public static Connection getCEConnection() {
        try {
            String ceURI = "http://192.168.1.40:9081/wsi/FNCEWS40MTOM";
            String userName = "wasadmin";
            String password = "wasadmin";

//			Aa123456789
//			los_migration_u
//			http://192.168.57.70:9080/wsi/FNCEWS40MTOM

            if (conn == null) {
                conn = Factory.Connection.getConnection(ceURI);
                Subject subject = UserContext.createSubject(conn, userName, password, null);
                UserContext uc = UserContext.get();
                uc.pushSubject(subject);
                System.out.println("CE Connection" + uc);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("CE Connection" + conn);
        return conn;
    }

    public static void CreateDocument(FolderProperties folderProp, String classID) {
        System.out.println("In Structure");
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
//        SearchScope search = new SearchScope(objectStore);
//        String mySQL = "SELECT [This], [ClassDescription], " +
//                "[CmIndexingFailureCode], [CmIsMarkedForDeletion], [CmRetentionDate], [ContainerType], [Creator]," +
//                " [DateCreated], [DateLastModified], [FolderName], [Id], [IndexationId], " +
//                "[InheritParentPermissions], [IsHiddenContainer], [LastModifier], [LockOwner]," +
//                " [LockTimeout], [LockToken], [Name], [Owner], [PathName] FROM [Folder] " +
//                "WHERE [Id] ="+ parentID+" OPTIONS(TIMELIMIT 180)";
//        SearchSQL sql= new SearchSQL(mySQL);
        // FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("100"), null, Boolean.valueOf(true));
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderProp.getParentID()), null);
        Folder myFolder = Factory.Folder.createInstance(objectStore, classID);
        Properties p = myFolder.getProperties();
        myFolder.set_Parent(parentFolder);
        myFolder.set_FolderName(folderProp.getArName() + "-" + folderProp.getCode());
        // myFolder.set_Owner(folderProp.getOwners());
        p.putValue("id", new Id(folderProp.getFolderID()));
        p.putValue("code", folderProp.getCode());
        p.putValue("enName", folderProp.getEnName());
        p.putValue("arName", folderProp.getArName());
        p.putValue("level", folderProp.getLevel());
        StringList owners = Factory.StringList.createList();
        boolean b = owners.addAll(folderProp.getOwners());
        p.putValue("Groups", owners);

        myFolder.save(RefreshMode.NO_REFRESH);
    }

    public static void CreateClassificationFolder(FolderProperties folderProp, String classID) {

        System.out.println("In classification");
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
//        SearchScope search = new SearchScope(objectStore);
//        String mySQL = "SELECT [This], [ClassDescription], " +
//                "[CmIndexingFailureCode], [CmIsMarkedForDeletion], [CmRetentionDate], [ContainerType], [Creator]," +
//                " [DateCreated], [DateLastModified], [FolderName], [Id], [IndexationId], " +
//                "[InheritParentPermissions], [IsHiddenContainer], [LastModifier], [LockOwner]," +
//                " [LockTimeout], [LockToken], [Name], [Owner], [PathName] FROM [Folder] " +
//                "WHERE [Id] ="+ parentID+" OPTIONS(TIMELIMIT 180)";
//        SearchSQL sql= new SearchSQL(mySQL);
        // FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("100"), null, Boolean.valueOf(true));
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderProp.getParentID()), null);
        Folder myFolder = Factory.Folder.createInstance(objectStore, classID);
        Properties p = myFolder.getProperties();
        myFolder.set_Parent(parentFolder);
        myFolder.set_FolderName(folderProp.getArName() + "-" + folderProp.getCode());
        // myFolder.set_Owner(folderProp.getOwners());
        p.putValue("id", new Id(folderProp.getFolderID()));
        p.putValue("code", folderProp.getCode());
        p.putValue("enName", folderProp.getEnName());
        p.putValue("arName", folderProp.getArName());
        p.putValue("level", folderProp.getLevel());
        p.putValue("progressDuration", folderProp.getProgressDuration());
        p.putValue("intermediateDuration", folderProp.getInterDuration());
        p.putValue("finalDetermination", folderProp.getFinalDeter());
        StringList owners = Factory.StringList.createList();
        boolean b = owners.addAll(folderProp.getOwners());
        p.putValue("Groups", owners);

        myFolder.save(RefreshMode.NO_REFRESH);
    }

    private static int countRowsWithContent(Sheet sheet) {
        int rowCount = 0;
        for (Row row : sheet) {
            boolean rowHasData = false;
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    rowHasData = true;
                    break;
                }
            }
            if (rowHasData) {
                rowCount++;
            }
        }
        return rowCount;
    }

    public static void MigrationFromExcel(String excelPath) {
        FolderProperties folderProp = new FolderProperties();
        try {
            String classID = "";
            // Create a workbook object from the Excel file
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = WorkbookFactory.create(fis);

            // Get the first sheet in the workbook
            Sheet sheet = workbook.getSheetAt(0);
            int rows = countRowsWithContent(sheet);
            String folderName;
            String code;
            String enName;
            String folderId;
            String parentId;
            ArrayList<String> owners = new ArrayList<>();
            String[] owners1;

            for (int i = 0; i < rows; i++) {

                Row row = sheet.getRow(i);
                System.out.println("value from excel" + row.getCell(7).toString().split("\\.")[0]);
                if (row.getCell(0).toString().equals("ClassificationID")) {
                    continue;
                }
                if (row.getCell(7).toString().split("\\.")[0].equals("0")) {

                    System.out.println("value from excel" + row.getCell(7).toString());
                    classID = "Structures";

                    folderProp.setFolderID(row.getCell(0).toString());
                    folderProp.setParentID(row.getCell(1).toString());
                    folderProp.setArName(row.getCell(2).toString());
                    folderProp.setEnName(row.getCell(3).toString());
                    folderProp.setCode(row.getCell(4).toString());
                    folderProp.setLevel(row.getCell(6).toString());
                    //   folderProp.setType(row.getCell(7).toString());
                    owners1 = row.getCell(12).toString().split(",");
                    owners.clear();
                    for (int j = 0; j < owners1.length; j++) {
                        owners.add(owners1[j]);
                    }
                    folderProp.setOwners(owners);


                    System.out.println(owners);
                    System.out.println(i + "--" + folderProp.toString());
                    CreateDocument(folderProp, classID);
                }

                if (row.getCell(7).toString().split("\\.")[0].equals("1")) {
                    classID = "classificationFolders";
                    folderProp.setFolderID(row.getCell(0).toString());
                    folderProp.setParentID(row.getCell(1).toString());
                    folderProp.setArName(row.getCell(2).toString());
                    folderProp.setEnName(row.getCell(3).toString());
                    folderProp.setCode(row.getCell(4).toString());
                    folderProp.setLevel(row.getCell(6).toString());
                    folderProp.setProgressDuration(row.getCell(8).toString());
                    folderProp.setInterDuration(row.getCell(9).toString());
                    folderProp.setFinalDeter(row.getCell(10).toString());

                    owners1 = row.getCell(12).toString().split(",");
                    owners.clear();
                    for (int j = 0; j < owners1.length; j++) {
                        owners.add(owners1[j]);
                    }
                    folderProp.setOwners(owners);

                    //66A4C63D-4466-4B5F-8545-ACCFCDDA8D99

                    System.out.println(owners);
                    System.out.println(i + "--" + folderProp.toString());

                    CreateClassificationFolder(folderProp, classID);
                }

            }
            // Close the workbook and file input stream
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FolderProperties> getLeafFoldersByOwnerID(String ownerID) {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);

        ArrayList<FolderProperties> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL = "SELECT [This], [arName], [ClassDescription], [CmIndexingFailureCode], " +
                "[CmIsMarkedForDeletion], [CmRetentionDate], [ContainerType], [Creator], [DateCreated], " +
                "[DateLastModified], [DurationUnit], [FinalDetermination], [FolderName], [Id], " +
                "[IndexationId], [InheritParentPermissions], [InprogressDuration], [IntermediateDuration], " +
                "[IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken], [Name], " +
                "[Owner], [Owners], [PathName], [code], [enName], [level], " +
                "[type] FROM [structures] WHERE '" + ownerID + "' in [Owners] OPTIONS(TIMELIMIT 180)";
        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.valueOf(true));
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            FolderProperties folderProperties = new FolderProperties();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("arName"));
            folderProperties.setEnName(folderProp.getStringValue("enName"));
            folderProperties.setCode(folderProp.getStringValue("code"));
            folderProperties.setLevel(folderProp.getStringValue("level"));
            folderProperties.setType(folderProp.getStringValue("type"));
            folderProperties.setProgressDuration(folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration(folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit(folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter(folderProp.getStringValue("FinalDetermination"));

            StringList owners = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) owners.toArray(new String[owners.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }


    public void createArchiveFile(LeafFolderProperties folderProp) {

        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderProp.getParentID()), null);
        Folder myFolder = Factory.Folder.createInstance(objectStore, "Leaf");
        Properties parentProp = parentFolder.getProperties();
        Properties p = myFolder.getProperties();



        myFolder.set_Parent(parentFolder);
        myFolder.set_FolderName(folderProp.getArName() + "-" + folderProp.getCode());
        // myFolder.set_Owner(folderProp.getOwners());
        p.putValue("id", new Id(folderProp.getFolderID()));
        p.putValue("code", folderProp.getCode());
        p.putValue("arName", folderProp.getArName());
        p.putValue("enName", folderProp.getEnName());
        p.putValue("type", folderProp.getType());
        p.putValue("level", folderProp.getLevel());
        p.putValue("isOpend", folderProp.getOpend());
        p.putValue("ownerID", folderProp.getOwnerID());
        p.putValue("InprogressDuration", parentProp.getStringValue("InprogressDuration"));
        p.putValue("IntermediateDuration", parentProp.getStringValue("IntermediateDuration"));
        p.putValue("DurationUnit", parentProp.getStringValue("DurationUnit"));
        p.putValue("FinalDetermination", parentProp.getStringValue("FinalDetermination"));
        p.putValue("Owners", parentProp.getStringListValue("Owners"));
//        StringList owners = Factory.StringList.createList();
//        boolean b = owners.addAll(folderProp.getOwners());
//        p.putValue("Owners", owners);


        myFolder.save(RefreshMode.NO_REFRESH);

    }

    public ArrayList<LeafFolderProperties> getFilesByStatus(String ownerID, Boolean isOpend) {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        ArrayList<LeafFolderProperties> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL = "SELECT [This], [ClassDescription], [CmIndexingFailureCode], [CmIsMarkedForDeletion]," +
                " [CmRetentionDate], [ContainerType], [Creator], [DateCreated], [DateLastModified]," +
                " [DurationUnit], [FinalDetermination], [FolderName], [Id], [IndexationId]," +
                " [InheritParentPermissions], [InprogressDuration], [IntermediateDuration]," +
                " [IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken]," +
                " [Name], [Owner], [Owners], [PathName], [arName], [code], [enName], [isOpend], [level]," +
                " [ownerID], [type]  FROM [leaf] WHERE [ownerID] ='" + ownerID + "'and [isOpend] =" + isOpend +
                " OPTIONS(TIMELIMIT 180)";
        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            LeafFolderProperties folderProperties = new LeafFolderProperties();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("ArName"));
            folderProperties.setEnName(folderProp.getStringValue("enName"));
            folderProperties.setCode(folderProp.getStringValue("code"));
            folderProperties.setLevel(folderProp.getStringValue("level"));
            folderProperties.setType(folderProp.getStringValue("type"));
            folderProperties.setProgressDuration(folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration(folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit(folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter(folderProp.getStringValue("FinalDetermination"));
            folderProperties.setOpend(folderProp.getBooleanValue("isOpend"));
            folderProperties.setOwnerID(folderProp.getStringValue("ownerID"));

            StringList owners = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) owners.toArray(new String[owners.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }

    public ArrayList<LeafFolderProperties> getFilesByOwnerID(String ownerID) {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        ArrayList<LeafFolderProperties> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL = "SELECT [This], [arName], [ClassDescription], [CmIndexingFailureCode], " +
                "[CmIsMarkedForDeletion], [CmRetentionDate], [ContainerType], [Creator], [DateCreated], " +
                "[DateLastModified], [DurationUnit], [FinalDetermination], [FolderName], [Id], " +
                "[IndexationId], [InheritParentPermissions], [InprogressDuration], [IntermediateDuration], " +
                "[IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken], [Name], " +
                "[Owner], [Owners], [PathName], [code], [enName], [ownerID], [level], [isOpend], " +
                "[type] FROM [leaf] WHERE [ownerID] ='" + ownerID + "' OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            LeafFolderProperties folderProperties = new LeafFolderProperties();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("ArName"));
            folderProperties.setEnName(folderProp.getStringValue("enName"));
            folderProperties.setCode(folderProp.getStringValue("code"));
            folderProperties.setLevel(folderProp.getStringValue("level"));
            folderProperties.setType(folderProp.getStringValue("type"));
            folderProperties.setOpend(folderProp.getBooleanValue("isOpend"));
            folderProperties.setOwnerID(folderProp.getStringValue("ownerID"));
            folderProperties.setProgressDuration(folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration(folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit(folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter(folderProp.getStringValue("FinalDetermination"));


            StringList owners = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) owners.toArray(new String[owners.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }

    public void updateFolderStatus(String folderID, Boolean isOpend) {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        Properties p = folder.getProperties();
        p.putValue("isOpend", isOpend);
        folder.save(RefreshMode.NO_REFRESH);
    }

    public void deleteFolderByID(String folderID) {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        folder.delete();
        folder.save(RefreshMode.NO_REFRESH);
    }

    public static String getMimeType(String fileUrl) {

        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(fileUrl);
        return mimeType;
    }

    public static void createCorrespondenceDoc(ArrayList<CorrespondenceAttribute> correspondenceAttribute) {
        //////////////////CREATE CORRESPONDENCE FOLDER
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
        for (int y = 0; y < correspondenceAttribute.size(); y++) {
            Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(correspondenceAttribute.get(y).getFolderID()), null);
            Folder myFolder = Factory.Folder.createInstance(objectStore, "CorrespondenceFolder");
            Properties parentProp = parentFolder.getProperties();
            Properties p1 = myFolder.getProperties();
            myFolder.set_Parent(parentFolder);
            myFolder.set_FolderName(correspondenceAttribute.get(y).getCorrespondenceID());
            // myFolder.set_Owner(folderProp.getOwners());
            // p.putValue("id",new Id(folderProp.getFolderID()));
            // p.putValue("code",folderProp.getCode());
            p1.putValue("Owner_ID", correspondenceAttribute.get(y).getUserID());
            p1.putValue("CorrespondenceID", correspondenceAttribute.get(y).getCorrespondenceID());

//        StringList owners = Factory.StringList.createList();
//        boolean b = owners.addAll(folderProp.getOwners());
//        p.putValue("Owners", owners);


            myFolder.save(RefreshMode.REFRESH);


            ///////////////CREATE DOCUMENT
//        Connection conn = getCEConnection();
//        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
//        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
            Document document = null;
            //Get Folder
            Folder folder = null;
            String folderName = myFolder.get_PathName();
            folder = Factory.Folder.fetchInstance(objectStore, folderName, null);

            File f = new File(correspondenceAttribute.get(y).getPath());

            //Get the File details
            String fileName = "";
            int fileSize = 0;

// Create Document
            System.out.println(correspondenceAttribute);
            String docClass = "Correspondence";
            try {
                FileInputStream file = new FileInputStream(f);
                Document doc = Factory.Document.createInstance(objectStore, docClass);
                if (f.exists()) {
                    ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
                    ContentElementList contentElementList = Factory.ContentElement.createList();

                    contentTransfer.setCaptureSource(file);
                    contentElementList.add(contentTransfer);
                    doc.set_ContentElements(contentElementList);
                    contentTransfer.set_RetrievalName(correspondenceAttribute.get(y).getCorrespondenceID());
                    //   doc.set_Creator(correspondenceAttribute.get(y).getUserID());
                    doc.set_MimeType(getMimeType(correspondenceAttribute.get(y).getPath()));

                }


//Check-in the doc
                doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
//Get and put the doc properties
                String documentName = "Mohammed1";
                Properties p = doc.getProperties();
                // p.putValue("DocumentTitle",correspondenceAttribute.get(y).getDocTitle());
                p.putValue("CorrespondenceID", correspondenceAttribute.get(y).getCorrespondenceID());
                p.putValue("Subject", correspondenceAttribute.get(y).getSubject());
                StringList senders = Factory.StringList.createList();
                boolean b = senders.addAll(correspondenceAttribute.get(y).getSenders());
                p.putValue("Senders", senders);

                StringList recievers = Factory.StringList.createList();
                boolean b1 = recievers.addAll(correspondenceAttribute.get(y).getRecievers());
                p.putValue("Recievers", recievers);

                StringList cc = Factory.StringList.createList();
                boolean b2 = cc.addAll(correspondenceAttribute.get(y).getCc());
                p.putValue("CC", cc);

                StringList bcc = Factory.StringList.createList();
                boolean b3 = bcc.addAll(correspondenceAttribute.get(y).getBcc());
                p.putValue("BCC", bcc);
                doc.save(RefreshMode.REFRESH);

//Stores above content to the folder
                ReferentialContainmentRelationship rc = folder.file(doc,
                        AutoUniqueName.AUTO_UNIQUE,
                        documentName,
                        DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                rc.save(RefreshMode.REFRESH);
            } catch (Exception e) {
                System.out.println("Error MSG FROM CROSS:" + e.getMessage());
            }
///////////////////////////////UPLOAD ATTACHMENTS////////////////////
            try {
                for (int i = 0; i < correspondenceAttribute.get(y).getAttachmentsAttributes().size(); i++) {
                    File attachmentPath = new File(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getPath());

                    FileInputStream file = new FileInputStream(attachmentPath);
                    Document doc = Factory.Document.createInstance(objectStore, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getClassification());
                    if (attachmentPath.exists()) {
                        ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
                        ContentElementList contentElementList = Factory.ContentElement.createList();

                        contentTransfer.setCaptureSource(file);
                        contentElementList.add(contentTransfer);
                        doc.set_ContentElements(contentElementList);
                        contentTransfer.set_RetrievalName(correspondenceAttribute.get(y).getCorrespondenceID());
                        doc.set_MimeType(getMimeType(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getPath()));
                        //doc.set_Creator(correspondenceAttribute.get(y).getUserID());
                    }


//Check-in the doc
                    doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
//Get and put the doc properties
                    String documentName2 = correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getDocTitle();
                    Properties p = doc.getProperties();
                    // p.putValue("DocumentTitle",correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getDocTitle());
                    for (String key : correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().keySet()) {
                        if (correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).getClass().isInstance(new Date())) {
                            System.out.println("IS Date :>" + correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).getClass().isInstance(new Date()));
                            p.putValue(key, (Date) correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key));
                        } else {
                            System.out.println("IS NO  Date :>");

                            p.putValue(key, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).toString());

                        }
                    }
//                for (int j=0; j<correspondenceAttribute.getAttachmentsAttributes().get(i).getProp().size();j++) {
//                    p.putValue(correspondenceAttribute.getAttachmentsAttributes().get(i).getProp()., correspondenceAttribute.getCorrespondenceID());
//                }

                    doc.save(RefreshMode.REFRESH);

//Stores above content to the folder
                    ReferentialContainmentRelationship rc = folder.file(doc,
                            AutoUniqueName.AUTO_UNIQUE,
                            documentName2,
                            DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                    rc.save(RefreshMode.REFRESH);
                }
            } catch (Exception e) {
                System.out.println("ERROR MSG FROM ATTACHMENTS" + e.getMessage());
            }
        }
    }

    public static void schedulerJob() {
        Connection conn = getCEConnection();
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);

        SearchScope search = new SearchScope(objectStore);
        LocalDateTime minDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime maxDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);

        System.out.println(maxDate);
        String mySQL = "SELECT [This],* FROM [UserArchivingFolder] " +
                "WHERE [progressEndDate] >= "+minDate+" AND [progressEndDate] < "+maxDate+" " +
                "AND [retentionStatus] = 'ACTIVE'" +
                "OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        System.out.println("folder set "  +folders);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Folder parentFolder = folder.get_Parent();
            Properties parentProp = parentFolder.getProperties();
            String finalDetermination= parentProp.getStringValue("finalDetermination");
            Properties p = folder.getProperties();
            p.putValue("retentionStatus", "INTERMEDIATE test now");
            Date testDate = new Date();
            Calendar testDate2 = Calendar.getInstance();
            testDate2.setTime(new Date());
            testDate2.add(Calendar.MINUTE,2);
            testDate = testDate2.getTime();
            System.out.println("Final determinaion "+ finalDetermination);
            if (finalDetermination.equals("DELETE")) {
                System.out.println("Delete Case");
                Date deleteDate = p.getDateTimeValue("intermediateEndDate");
                folder.set_CmRetentionDate(new Date());
            }
            if(finalDetermination == "PERMANENT")
            {
                System.out.println("Permanent Case");
                folder.set_CmRetentionDate(RetentionConstants.PERMANENT);
            }
            System.out.println(p.getStringValue("retentionStatus"));
            folder.save(RefreshMode.REFRESH);
        }

    }
}
