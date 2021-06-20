package ru.itis.homework;

import ru.itis.homework.utils.RowMapper;
import ru.itis.homework.utils.SimpleJDBSTemplate;
import ru.itis.homework.utils.TableInfo;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityManager {

    protected DataSource dataSource;
    protected SimpleJDBSTemplate simpleJDBSTemplate;

    //language=SQL
    protected static final String SQL_GET_COLUMNS_INFO_FOR_TABLE = "SELECT TABLE_NAME, COLUMN_NAME, data_type FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name=?";

    public EntityManager() {
        this.dataSource = null;
    }

    public EntityManager(DataSource dataSource) {
        this.dataSource = dataSource;
        this.simpleJDBSTemplate = new SimpleJDBSTemplate(dataSource);
    }

    protected RowMapper<TableInfo> tableInfoRowMapper = row -> TableInfo.builder()
            .tableName(row.getString("table_name"))
            .columnName(row.getString("column_name"))
            .dataType(row.getString("data_type"))
            .build();

    public <T> void createTable(String tableName, Class<T> entityClass) {
        //Сгенерировать CREATE TABLE на основе класса
        if (tableName.contains(" ")) {
            throw new IllegalArgumentException();
        }
        StringBuilder sql_create_table = new StringBuilder("CREATE TABLE ");
        sql_create_table.append("\"").append(tableName).append("\"");
        sql_create_table.append(" ( ");
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            int modifiersCode = fields[i].getModifiers();
            if ((!Modifier.isStatic(modifiersCode)) && (!Modifier.isFinal(modifiersCode)) && (!Modifier.isPrivate(modifiersCode))) {
                sql_create_table.append("\"").append(this.generateSQLNameByFieldName(fields[i].getName())).append("\"");
                sql_create_table.append(" ");
                sql_create_table.append(this.getSQLClassForJavaClass(fields[i].getType().getSimpleName()));
                if (fields[i].getName().equals("id")) {
                    sql_create_table.append(" primary key");
                }
                if (i != fields.length - 1) {
                    sql_create_table.append(", ");
                }
            }
        }
        sql_create_table.append(" );");
        System.out.println("New table will be created with this sql : " + sql_create_table.toString());
        simpleJDBSTemplate.query(sql_create_table.toString(), null);
    }

    public void save(String tableName, Object entity) {
        if (tableName.contains(" ")) {
            throw new IllegalArgumentException();
        }
        StringBuilder sql_insert = new StringBuilder("INSERT INTO ");
        sql_insert.append("\"").append(tableName).append("\"");
        sql_insert.append(" ( ");
        Class<?> classOfEntity = entity.getClass();
        List<Field> fields = Arrays.asList(classOfEntity.getDeclaredFields());
        List<TableInfo> tableInfos = getSQLFieldsForTable(tableName);
        StringBuilder sql_parameters = new StringBuilder("(");
        for (TableInfo tableInfo : tableInfos) {
            boolean isCorrespond = false;
            for (Field field : fields) {
                isCorrespond = this.checkConformity(tableInfo, field);
                if (isCorrespond) {
                    try {
                        Object result = this.getFieldValue(entity, field.getName(), field.getType());
                        sql_insert.append(tableInfo.getColumnName());
                        sql_insert.append(", ");
                        if (field.getType().getSimpleName().equals("String")) {
                            sql_parameters.append("\'").append(result).append("\', ");
                        } else {
                            sql_parameters.append(result).append(", ");
                        }
                        //fields.remove(field);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        //ignore - cannon (t) get access to this field
                    } catch (IllegalStateException ex) {
                        //ignore - no method to get this field
                    }
                    break;
                }
            }
        }
        if (sql_parameters.length() > 1) {
            sql_parameters.replace(sql_parameters.length() - 2, sql_parameters.length(), ");");
        } else {
            sql_parameters = new StringBuilder("();");
        }
        if (sql_insert.charAt(sql_insert.length() - 2) != '(') {
            sql_insert.replace(sql_insert.length() - 2, sql_insert.length(), ") VALUES ");
        } else {
            sql_insert.append(") VALUES");
        }
        sql_insert.append(sql_parameters.toString());
        System.out.println("Insert into table new value with sql : " + sql_insert.toString());
        simpleJDBSTemplate.query(sql_insert.toString(), null);
        //Сканируем его поля и их значения
        //Генерируем insert into
    }

    public <T, ID> T findById(String tableName, Class<T> resultType, Class<ID> idType, ID idValue) throws SQLException {
        //сгенерировать select
        StringBuilder sql_select = new StringBuilder("SELECT * FROM ");
        sql_select.append("\"").append(tableName).append("\" ");
        sql_select.append("WHERE ");
        List<TableInfo> tableInfos = getSQLFieldsForTable(tableName);
        String id_type = this.getSQLClassForJavaClass(idType.getSimpleName());
        TableInfo idTableInfo = null;
        for (TableInfo tableInfo : tableInfos) {
            if ((Arrays.asList(tableInfo.getColumnName().split("_")).contains("id")) && (tableInfo.getDataType().equals(id_type))) {
                idTableInfo = tableInfo;
                break;
            }
        }
        sql_select.append("\"").append(idTableInfo.getColumnName()).append("\"=");
        if (idType.getSimpleName().equals("String")) {
            sql_select.append("\"").append(idValue).append("\";");
        } else {
            sql_select.append(idValue).append(";");
        }
        System.out.println("Select from table with sql : " + sql_select.toString());
        try{
            return this.getObject(sql_select.toString(), tableInfos, resultType);
        } catch (IllegalAccessException|InstantiationException ex){
            throw new IllegalArgumentException(ex);
        }
    }

    private String getSQLClassForJavaClass(String className) {
        String result = null;
        switch (className) {
            case "byte":
            case "short":
            case "int":
            case "Byte":
            case "Short":
            case "Integer":
                result = "integer";
                break;
            case "long":
            case "Long":
                result = "bigint";
                break;
            case "boolean":
            case "Boolean":
                result = "boolean";
                break;
            case "float":
            case "Float":
                result = "double precision";
                break;
            case "double":
            case "Double":
                result = "real";
                break;
            case "String":
                result = "character varying";
                break;
            default:
                throw new IllegalArgumentException(className);

        }
        return result;
    }

    private String generateSQLNameByFieldName(String fieldName) {
        StringBuilder result = new StringBuilder(fieldName);
        int value;
        for (int i = 0; i < fieldName.length(); i++) {
            value = (int) result.charAt(i);
            if ((value >= 65) && (value <= 90)) {
                result.replace(i, i + 1, "_" + (char) (value + 32));
                i++;
            }
        }
        return result.toString();
    }

    private List<TableInfo> getSQLFieldsForTable(String tableName) {
        return simpleJDBSTemplate.query(SQL_GET_COLUMNS_INFO_FOR_TABLE, tableInfoRowMapper, tableName);
    }

    private boolean checkConformity(TableInfo tableInfo, Field field) {
        List<String> columnName = Arrays.asList(tableInfo.getColumnName().split("_"));
        List<String> fieldName = Arrays.asList(this.generateSQLNameByFieldName(field.getName()).split("_"));
//        System.out.println(columnName);
//        System.out.println(fieldName);
//        System.out.println("-------------");
        String fieldType = this.getSQLClassForJavaClass(field.getType().getSimpleName());
        boolean result = false;
        if (((columnName.containsAll(fieldName)) || (fieldName.containsAll(columnName))) && (tableInfo.getDataType().equals(fieldType))) {
            result = true;
        }
        return result;
    }

    private Object getFieldValue(Object entity, String fieldName, Class<?> fieldType) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = entity.getClass().getDeclaredMethods();
        String methodName = "get" + fieldName;
        methodName = methodName.toLowerCase();
        Object result = null;
        boolean isValue = false;
//        System.out.println(methodName);
//        System.out.println(fieldType);
//        System.out.println("-----------------");
        for (Method method : methods) {
//            System.out.println(method.getName());
//            System.out.println(method.getReturnType());
            if ((method.getName().toLowerCase().equals(methodName)) && (method.getReturnType().equals(fieldType))) {
//                System.out.println("Нашёл!");
                result = method.invoke(entity);
                isValue = true;
            }
        }
        if (isValue) {
            return result;
        } else {
            throw new IllegalStateException();
        }
    }

    private <T> T getObject(String sql, List<TableInfo> tableInfos, Class<T> resultClass) throws SQLException, IllegalAccessException, InstantiationException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Field> fields = Arrays.asList(resultClass.getDeclaredFields());
        T result = null;
        connection = this.dataSource.getConnection();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        if (resultSet != null) {
            resultSet.next();
            result = resultClass.newInstance();
            for (int i = 0; i < tableInfos.size(); i++) {
                for (int j = 0; j < fields.size(); j++) {
                    if (this.checkConformity(tableInfos.get(i), fields.get(j))) {
                        fields.get(j).setAccessible(true);
                        if (fields.get(j).getType().getSimpleName().toLowerCase().equals("byte")){
                            int num = resultSet.getInt(tableInfos.get(i).getColumnName());
                            fields.get(j).set(result, (byte) num);
                        } else if (fields.get(j).getType().getSimpleName().toLowerCase().equals("short")){
                            int num = resultSet.getInt(tableInfos.get(i).getColumnName());
                            fields.get(j).set(result, (short) num);
                        } else if (fields.get(j).getType().getSimpleName().toLowerCase().equals("float")){
                            double num = resultSet.getInt(tableInfos.get(i).getColumnName());
                            fields.get(j).set(result, (float) num);
                        } else if (this.isPrimitive(fields.get(j).getType().getSimpleName())){
                            if (resultSet.getObject(tableInfos.get(i).getColumnName()) != null){
                                fields.get(j).set(result, resultSet.getObject(tableInfos.get(i).getColumnName()));
                            }
                        } else {
                            fields.get(j).set(result, resultSet.getObject(tableInfos.get(i).getColumnName()));
                        }
                        //fields.remove(j);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private boolean isPrimitive(String type){
        return ((type.equals("byte"))||(type.equals("short"))||(type.equals("int"))||type.equals("long")
                ||(type.equals("boolean"))||(type.equals("float"))||(type.equals("double")));
    }
}
