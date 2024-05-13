package sync;

import errors.DuplicateDepartmentException;
import errors.SynchronizationException;
import errors.TransactionException;
import model.Department;
import model.DepartmentKey;
import model.Departments;
import read.DataReader;
import sql_queries.Queries;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static log.MyLogger.LOGGER;

public class SynchronizationService {

    private final String fileName;
    private final DataReader reader;
    private final Map<DepartmentKey, Department> dataFromXMLMap, dataFromDBMap;

    public SynchronizationService(String fileName) {
        this.dataFromDBMap = new HashMap<>();
        this.dataFromXMLMap = new HashMap<>();
        this.fileName = fileName;
        this.reader = new DataReader();
    }

    public void sync() {
        try {
            Departments dataFromXML = reader.readDataFromXML(fileName),
                    dataFromDB = reader.readDataFromDB();
            if (!checkDuplicates(dataFromXML)) {
                throw new DuplicateDepartmentException("XML file contains more than one record with one natural key");
            } else {
                fillMap(dataFromXML.getDepartments(), dataFromXMLMap); // Заполнение для dataFromXMLMap
                fillMap(dataFromDB.getDepartments(), dataFromDBMap);   // Заполнение для dataFromDBMap
                // Выполнение транзакции
                performTransaction(getListForAdding(), getListForUpdating(), getListForDeleting());
            }
        } catch (DuplicateDepartmentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void fillMap(List<Department> departmentsFromXML, Map<DepartmentKey, Department> dataFromXMLMap) {
        for (Department department : departmentsFromXML) {
            dataFromXMLMap.put(new DepartmentKey(department.getDepCode(), department.getDepJob()), department);
        }
    }

    private void performTransaction(List<Department> listForAdding,
                                    List<Department> listForUpdating,
                                    List<Department> listForDeleting) {
        try (Connection connection = ConnectionManager.open()) {
            connection.setAutoCommit(false); // Отключаем авто-коммит для ручного управления
            try {
                if (listForAdding.isEmpty() && listForUpdating.isEmpty() && listForDeleting.isEmpty()) {
                    LOGGER.info("The database is already synchronized");
                } else {
                    // Добавление отделов
                    if (!listForAdding.isEmpty()) {
                        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(Queries.INSERT)) {
                            addEntries(listForAdding, insertPreparedStatement);
                            LOGGER.info("{} departments successfully added", listForAdding.size());
                        }
                    }

                    // Обновление отделов
                    if (!listForUpdating.isEmpty()) {
                        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(Queries.UPDATE)) {
                            updateEntries(listForUpdating, updatePreparedStatement);
                            LOGGER.info("{} departments successfully updated", listForUpdating.size());
                        }
                    }

                    // Удаление отделов
                    if (!listForDeleting.isEmpty()) {
                        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(Queries.DELETE)) {
                            deleteEntries(listForDeleting, deletePreparedStatement);
                            LOGGER.info("{} departments successfully deleted", listForDeleting.size());
                        }
                    }
                }
                connection.commit(); // Коммит транзакции
                LOGGER.info("Transaction completed successfully");
            } catch (SQLException e) {
                connection.rollback(); // Откат транзакции в случае ошибки
                LOGGER.error("Error during transaction {}", e.getMessage());
                throw new TransactionException("Error during transaction", e);
            }
        } catch (SQLException e) {
            LOGGER.error("Error synchronizing data {}", e.getMessage());
            throw new SynchronizationException("Error synchronizing data", e);
        }
    }

    private List<Department> getListForUpdating() {
        List<Department> listForUpdating = new ArrayList<>();
        for (Map.Entry<DepartmentKey, Department> entry : dataFromXMLMap.entrySet()) {
            if (dataFromDBMap.containsKey(entry.getKey()) &&
                    !dataFromDBMap.get(entry.getKey()).getDescription().equals(entry.getValue().getDescription()))
                listForUpdating.add(entry.getValue());
        }
        return listForUpdating;
    }

    private List<Department> getListForAdding() {
        List<Department> listForAdding = new ArrayList<>();
        for (Map.Entry<DepartmentKey, Department> entry : dataFromXMLMap.entrySet())
            if (!dataFromDBMap.containsKey(entry.getKey()))
                listForAdding.add(entry.getValue());
        return listForAdding;
    }

    private List<Department> getListForDeleting() {
        List<Department> listForDeleting = new ArrayList<>();
        for (Map.Entry<DepartmentKey, Department> entry : dataFromDBMap.entrySet())
            if (!dataFromXMLMap.containsKey(entry.getKey()))
                listForDeleting.add(entry.getValue());
        return listForDeleting;
    }

    // Добавление новых отделов
    private void addEntries(List<Department> departmentListForAdding,
                            PreparedStatement preparedStatement) throws SQLException {
        for (Department department : departmentListForAdding) {
            preparedStatement.setString(1, department.getDepCode());
            preparedStatement.setString(2, department.getDepJob());
            preparedStatement.setString(3, department.getDescription());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    // Обновление отделов
    private void updateEntries(List<Department> departmentListForUpdating,
                               PreparedStatement preparedStatement) throws SQLException {
        for (Department department : departmentListForUpdating) {
            preparedStatement.setString(1, department.getDescription());
            preparedStatement.setString(2, department.getDepCode());
            preparedStatement.setString(3, department.getDepJob());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    // Удаление отделов
    private void deleteEntries(List<Department> departmentListForDeleting,
                               PreparedStatement preparedStatement) throws SQLException {
        for (Department department : departmentListForDeleting) {
            preparedStatement.setString(1, department.getDepCode());
            preparedStatement.setString(2, department.getDepJob());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    private boolean checkDuplicates(Departments dataFromXML) {
        Set<DepartmentKey> uniqueDepartmentKeys = new HashSet<>();
        for (Department department : dataFromXML.getDepartments()) {
            // Создаем уникальный ключ для каждого отдела
            DepartmentKey departmentKey = new DepartmentKey(department.getDepCode(), department.getDepJob());
            if (!uniqueDepartmentKeys.add(departmentKey)) {
                return false; // Если ключ не добавился, значит есть дубликат
            }
        }
        return true; // Если размеры совпадают, значит, нет дубликатов
    }
}