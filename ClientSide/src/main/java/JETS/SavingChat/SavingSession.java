package JETS.SavingChat;

import JETS.ui.helpers.FriendsManager;
import JETS.ui.helpers.ModelsFactory;
import Models.CurrentUser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingSession {

    //<chatId,DOM>
    private static Map<Long, Document> documents=new HashMap<>();
    public void saveChat(long chatId, List<MessageType> messageTypeList,List<String> participants,File savedPath){
        Document document=null;
        if(documents.containsKey(chatId)){
           document=documents.get(chatId);
        }else {
//            File file = new File("ClientSide/target/classes/XML/" + friendPhoneNumber + ".xml");
//            //check file system contains xml or not to append
//            if (file.exists()) {
//                document = createDoc(friendPhoneNumber + ".xml");
//            } else {
                document = createDoc();
       //     }
           documents.put(chatId, document);
       }
        byte[] userPhoto=ModelsFactory.getInstance().getCurrentUser().getUserPhoto();
        try(FileOutputStream os = new FileOutputStream(savedPath.getParentFile() +"\\"+ ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()+".png")){
                os.write(userPhoto);

        }catch (IOException e){
            e.printStackTrace();
        }
        for(String phoneNumber:participants.subList(1,participants.size())){
            if(ModelsFactory.getInstance().getCurrentUser().getFriends().containsKey(phoneNumber)){
                byte[] friendPhoto=ModelsFactory.getInstance().getCurrentUser().getUserPhoto();
                try(FileOutputStream os = new FileOutputStream(savedPath.getParentFile()+"\\" + phoneNumber+".png")){
                    os.write(friendPhoto);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(ModelsFactory.getInstance().getCurrentUser().getParticipantsInGroup().containsKey(phoneNumber)){
                byte[] participantPhoto=ModelsFactory.getInstance().getCurrentUser().getUserPhoto();
                try(FileOutputStream os = new FileOutputStream(savedPath.getParentFile()+"\\" + phoneNumber+".png")){
                    os.write(participantPhoto);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        populateDocNewSession(document,document.getDocumentElement(), messageTypeList,savedPath);
        TransformerFactory factory = TransformerFactory.newInstance();
        DOMSource xmlSource = new DOMSource(document);
        //we can only create new updated version of xml when the client close the app in stop()
        //createXMLMessages(factory, xmlSource,savedPath);
       generateHtml(factory, xmlSource, savedPath);

    }

    private static void populateDocNewSession(Document document,Element root,List<MessageType> messageTypeList,File savedPath){
        //store LocalDate.now() as attribute in <messages> to indicate new Session in html
        if(document!=null) {
            Element sessionMessages = document.createElement("SessionMessages");
            Element date=document.createElement("SessionDate");
            date.setTextContent(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a")));
            sessionMessages.appendChild(date);
            for (MessageType msgType : messageTypeList) {
                Element msg = createMessageElement(document, msgType,savedPath);
                sessionMessages.appendChild(msg);
            }

            root.appendChild(sessionMessages);
        }
    }

    private static Element createMessageElement(Document document,MessageType messageType,File savedPath){

        Element messageElement=document.createElement("Message");
       // messageElement.setAttribute("date", LocalDate.now().toString());
        messageElement.setAttribute("pos", messageType.getDirection());

        Element fromElement=document.createElement("From");
        Element imgElement=document.createElement("Image");
        imgElement.setTextContent(savedPath.getParent()+"\\"+messageType.getSenderPhone()+".png");
        if(messageType.getDirection().equals("right")) {
            fromElement.setTextContent(FriendsManager.getInstance().getFriendName(messageType.getSenderPhone()));

        }else{
            fromElement.setTextContent(ModelsFactory.getInstance().getCurrentUser().getDisplayName());
        }
        messageElement.appendChild(fromElement);
        messageElement.appendChild(imgElement);
//
//        FriendsManager.getInstance().getFriendPhoto(messageType.getSenderPhone()
//        fromElement.setTextContent());
//        messageElement.appendChild(fromElement);

        Element bodyElement=document.createElement("Body");
        bodyElement.setTextContent(messageType.getMsg());
        messageElement.appendChild(bodyElement);
        return messageElement;

    }
    private static void createXMLMessages(TransformerFactory factory,DOMSource xmlSource,File savedPath){
        File file = new File("ClientSide/target/classes/HTML");

        file.mkdir();
        StreamResult output=new StreamResult(new File(savedPath.getParentFile()+"hh"+".xml"));
        try {
            System.out.println("start");
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(xmlSource,output);

            System.out.println("end");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
//empty Dom
    public Document createDoc(){

        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        Document document=null;
        try {
            DocumentBuilder parser = factory.newDocumentBuilder();
            document=parser.newDocument();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        Element chatHistory = document.createElement("chatHistory");
        document.appendChild(chatHistory);
        return document;
    }
//if it is stored in xml
    public Document createDoc(String fname){

         DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
         Document document=null;
         try {
             DocumentBuilder parser = factory.newDocumentBuilder();
             document = parser.parse(new File("ClientSide/target/classes/XML/"+fname));
         }
         catch (Exception e){
             System.out.println(e.getMessage());
        }
         return document;
}

    private static void generateHtml(TransformerFactory factory,DOMSource xmlSource,File savedPath){
        StreamSource xslSource=new StreamSource(new File(SavingSession.class.getResource("/XML/trans.xslt").getPath()));

        try {
            //System.out.println(SavingSession.class.getResource("//").getPath()+"/HTML"+friendPhoneNumber+".html");
            File file = new File("ClientSide/target/classes/HTML");

            file.mkdir();
            //StreamResult output=new StreamResult(new FileOutputStream("ClientSide/target/classes/HTML/"+friendPhoneNumber+".html"));
            StreamResult output=new StreamResult(new FileOutputStream((savedPath)));

            Transformer transformer = factory.newTransformer(xslSource);
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(xmlSource,output);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
