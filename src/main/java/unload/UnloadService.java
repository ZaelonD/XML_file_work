package unload;

import model.Departments;
import read.DataReader;
import write.JaxbWriter;

public class UnloadService {
    private final DataReader reader;

    private final JaxbWriter jaxbWriter;

    public UnloadService(String fileName) {
        this.jaxbWriter = new JaxbWriter(fileName);
        this.reader = new DataReader();
    }

    public void unload() {
        Departments departments = reader.readDataFromDB();
        jaxbWriter.convertToXML(departments);
    }
}
