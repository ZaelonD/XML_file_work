package model;

import java.util.Objects;

public class DepartmentKey {
    private final String depCode;
    private final String depJob;

    public DepartmentKey(String depCode, String depJob) {
        this.depCode = depCode;
        this.depJob = depJob;
    }

    public String getDepCode() {
        return depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentKey departmentKey = (DepartmentKey) o;
        return Objects.equals(depCode, departmentKey.depCode) &&
                Objects.equals(depJob, departmentKey.depJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depCode, depJob);
    }
}
