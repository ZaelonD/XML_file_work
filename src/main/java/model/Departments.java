package model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Departments {

    private List<Department> departments;

    public Departments() {
        this.departments = new ArrayList<>();
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @XmlElement(name = "department")
    public List<Department> getDepartments() {
        return departments;
    }
}
