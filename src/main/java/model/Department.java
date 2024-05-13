package model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Department {
    @XmlTransient
    private int id;
    private String depCode;
    private String depJob;
    private String description;

    @XmlTransient
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "depcode")
    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    @XmlElement(name = "depjob")
    public String getDepJob() {
        return depJob;
    }

    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}