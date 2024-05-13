package read;

import errors.DBReadingException;
import errors.XMLReadingException;
import model.Department;
import model.Departments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sql_queries.Queries;
import util.ConnectionManager;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static log.MyLogger.LOGGER;

public class DataReader {


    public Departments readDataFromXML(String fileName) {
        Departments departments = new Departments();
        File file = new File(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            doc = dbf.newDocumentBuilder().parse(file);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.error("Error reading data from XML {}", e.getMessage());
            throw new XMLReadingException("Error reading data from XML", e);
        }
        NodeList nList = doc.getElementsByTagName("department");
        List<Department> departmentsList = new ArrayList<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                Department department = new Department();
                department.setDepCode(elem.getElementsByTagName("depcode").item(0).getTextContent());
                department.setDepJob(elem.getElementsByTagName("depjob").item(0).getTextContent());
                department.setDescription(elem.getElementsByTagName("description").item(0).getTextContent());
                departmentsList.add(department);
            }
        }
        departments.setDepartments(departmentsList);
        return departments;
    }

    public Departments readDataFromDB() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(Queries.SELECT);
             ResultSet rs = stmt.executeQuery()) {
            Departments departments = new Departments();
            List<Department> departmentList = new ArrayList<>();
            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt(1));
                department.setDepCode(rs.getString(2));
                department.setDepJob(rs.getString(3));
                department.setDescription(rs.getString(4));
                departmentList.add(department);
            }
            departments.setDepartments(departmentList);
            return departments;
        } catch (SQLException e) {
            LOGGER.error("Error reading data from database {}", e.getMessage());
            throw new DBReadingException("Error reading data from database", e);
        }
    }
}