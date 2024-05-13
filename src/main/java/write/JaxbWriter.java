package write;

import errors.ConversionToXmlException;
import errors.IncorrectFileNameException;
import model.Departments;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

import static log.MyLogger.LOGGER;

public class JaxbWriter {

    private final String fileName;

    public JaxbWriter(String fileName) {
        if (isXMLFile(fileName))
            this.fileName = fileName;
        else {
            LOGGER.error("Invalid file format. " + fileName + " is not XML file");
            throw new IncorrectFileNameException("Invalid file format. " + fileName + " is not XML file");
        }
    }

    public void convertToXML(Departments departments) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Departments.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File file = new File(fileName);
            marshaller.marshal(departments, file);
            LOGGER.info("Upload completed successfully to file " + fileName);
        } catch (JAXBException e) {
            LOGGER.error("Error converting to XML {}", e.getMessage());
            throw new ConversionToXmlException("Error converting to XML", e);
        }
    }

    private boolean isXMLFile(String fileName) {
        return fileName.contains(".xml");
    }
}