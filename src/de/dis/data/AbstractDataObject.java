package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Abstract class to handle data objects.
 */
public abstract class AbstractDataObject {

    abstract List<String> getDBFields();

    abstract String getTableName();

    abstract String getIdName();

    abstract int getIdValue();

    abstract void setIdValue(int newId);

    /**
     * Method to set the attributes of the data object as parameter values for the given sql statement.
     * @param stmt statement to be filled with the attributes
     * @throws SQLException
     */
    abstract void setValues(PreparedStatement stmt) throws SQLException ;


    /**
     * Method to load the values of the sql result into the data object.
     * @param rs SQL result
     * @throws SQLException
     */
    abstract void loadValues(ResultSet rs) throws SQLException ;


    /**
     * Loads the data for the given id. If there is a result for the id, the given object is filled with the found data.
     * Otherwise, null is returned.
     * @param <D> The type of the data object to be handled
     * @param id id of the data object to be loaded
     * @param dataObject object to be filled with data if there is a result
     * @return  data / null if there is no result for the id
     */
    protected static <D extends AbstractDataObject> D loadInternal (int id, D dataObject){
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectFields = String.join(",", dataObject.getDBFields());
            PreparedStatement stmt = con.prepareStatement("SELECT "+ selectFields + " FROM "+dataObject.getTableName() + " WHERE "+ dataObject.getIdName() + " = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            D result = null;
            if (rs.next()) {
                dataObject.loadValues(rs);
                result = dataObject;
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected static <D extends AbstractDataObject> List<D> loadAllInternal(Supplier<D> objectSupplier) {
        try {
            D dummyObject = objectSupplier.get();
            Connection con = DbConnectionManager.getInstance().getConnection();
            String selectSQL = "SELECT * FROM "+dummyObject.getTableName() ;
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            var objects = new ArrayList<D>();
            while (rs.next()) {
                D object = objectSupplier.get();
                object.loadValues(rs);
                objects.add(object);
            }
            rs.close();
            pstmt.close();
            return objects;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected void insert() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO "+getTableName() + " (" + String.join(",", getDBFields()) + ") VALUES ("
                    + getDBFields().stream().map(s -> "?").collect(Collectors.joining(", ")) + ") RETURNING " + getIdName());
            setValues(stmt);
            stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void insertOrUpdate() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            PreparedStatement stmt;
            if (getIdValue() == -1) {
                stmt = con.prepareStatement("INSERT INTO "+getTableName()+" (" + String.join(",", getDBFields()) + ") VALUES ("
                        + getDBFields().stream().map(s -> "?").collect(Collectors.joining(", ")) + ") RETURNING " + getIdName());
                setValues(stmt);

                var rs = stmt.executeQuery();
                if (rs.next()) {
                    setIdValue(rs.getInt(1));
                }
            } else {
                java.lang.String fieldsToUpdate = getDBFields().stream().map(s -> s + " = ? ").collect(Collectors.joining(", "));
                stmt = con.prepareStatement("UPDATE "+getTableName() + " SET " + fieldsToUpdate + " WHERE "+getIdName() + " = ?");
                setValues(stmt);
                stmt.setInt(getDBFields().size() + 1, getIdValue());
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected  void delete() {
        try {
            Connection con = DbConnectionManager.getInstance().getConnection();
            String sql = "DELETE FROM "+getTableName()+ " WHERE "+getIdName() + " = ?";
            var stm = con.prepareStatement(sql);
            stm.setInt(1, getIdValue());
            stm.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




}