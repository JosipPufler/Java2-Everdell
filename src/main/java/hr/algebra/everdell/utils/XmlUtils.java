package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.*;
import org.w3c.dom.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {
    private XmlUtils() {}

    private static final String DTD = "dtd/gameActions.dtd";
    private static final String DOCTYPE = "DOCTYPE";
    private static final String GAME_ACTIONS = "GameActions";
    public static final String XML_FILE_NAME = "xml/gameActions.xml";
    public static final String JAXB_XML_FILE_NAME = "xml/gameActions2.xml";

    public static void saveGameAction(GameAction gameAction) {

        if (!Files.exists(Path.of(JAXB_XML_FILE_NAME))) {
            File file = new File(JAXB_XML_FILE_NAME);
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            List<GameAction> gameMoves = retrieveGameMoves(new File(JAXB_XML_FILE_NAME));
            gameMoves.add(gameAction);
            for (GameAction gm : gameMoves) {
                appendGameMoveElement(gm);
            }
        }
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        DocumentType docType = dom.createDocumentType(DOCTYPE, null, DTD);
        return dom.createDocument(null, element, docType);
    }

    private static void appendGameMoveElement(GameAction gameAction) {
        Marshaller marshallerObj = null;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext contextObj = JAXBContext.newInstance(GameAction.class);
            marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(gameAction, sw);
            marshallerObj.marshal(gameAction, new File(JAXB_XML_FILE_NAME));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);

        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String filename) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
        transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
    }

    private static List<GameAction> retrieveGameMoves(File document) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameAction.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GameAction que = (GameAction) jaxbUnmarshaller.unmarshal(document);
            return new ArrayList<>(List.of(que));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*private static String jaxbObjectToXML(Customer customer) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            StringWriter sw = new StringWriter();
            m.marshal(customer, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return xmlString;
    }*/
}
