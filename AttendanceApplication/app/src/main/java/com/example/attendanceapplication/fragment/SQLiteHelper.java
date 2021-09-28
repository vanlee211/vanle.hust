package com.example.attendanceapplication.fragment;

import android.content.ContentValues;
import android.content.Context;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelper {

    private SQLiteDatabase database;
    private SQLiteDatabaseHelper dbHelper;
    private Context context;
    private String dbName;

    //khởi tạo database
    public SQLiteHelper(Context context, String dbName) {
        dbHelper = new SQLiteDatabaseHelper(context, dbName + ".db", 1);
        database = dbHelper.getWritableDatabase();
        this.context = context;
        this.dbName = dbName;
    }

    public void close() {
        database.close();
    }

    //upgrade database
    public boolean upgradeDatabase(SQLiteDatabase newDb) {
        if (newDb.getVersion() > database.getVersion()) {
            dbHelper.onUpgrade(newDb, database.getVersion(), newDb.getVersion());
            return true;
        }
        return false;
    }

    //lấy version database hiện tại
    public int getDatabaseVersion() {
        return database.getVersion();
    }

    public long getDatabasePageSizeInBytes() {
        return database.getPageSize();
    }

    public String getDatabasePath() {
        return database.getPath();
    }

    //tạo bảng database mới
    public void createTable(String tableName, ArrayList<String> columnNames,
                            HashMap<String, String> columnDataTypeMap) {
        database.execSQL(generateCreateTableCommandString(tableName,
                columnNames, columnDataTypeMap));
    }

    //lấy toàn bộ tên bảng
    public ArrayList<String> retrieveAllTableNames() {
        ArrayList<String> tableNamesList = new ArrayList<String>();
        Cursor cursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                tableNamesList.add(cursor.getString(cursor
                        .getColumnIndex("name")));
                cursor.moveToNext();
            }

        return tableNamesList;
    }

    public boolean isTablePresent(String tableName) {
        ArrayList<String> tableNamesList = retrieveAllTableNames();
        return (tableNamesList.contains(tableName));
    }

    //thêm cột vào bảng
    public void addColumn(String tableName, String columnName, String columnType) {

        database.execSQL("ALTER TABLE " + tableName + " ADD " + columnName
                + " " + columnType + ";");

    }

    //thay đổi kiểu dữ liệu  của một cột có sẵn
    public void changeColumnDataType(String tableName, String columnName,
                                     String newColumnType) {

        database.execSQL("ALTER TABLE " + tableName + " ALTER COLUMN "
                + columnName + " " + newColumnType + ";");

    }

    //xóa cột
    public void deleteColumn(String tableName, String columnName) {

        database.execSQL("ALTER TABLE " + tableName + " DROP " + columnName
                + ";");

    }

    //lấy tên của toàn bộ các cột
    public String[] retrieveAllColumnNames(String tableName) {

        Cursor cursor = database.query(tableName, null, null, null, null, null,
                null);
        cursor.moveToFirst();
        String columnNames[] = cursor.getColumnNames();

        return columnNames;
    }

    public boolean isColumnPresent(String tableName, String columnName) {

        String columnsToRetrieve[] = { columnName };
        Cursor cursor = database.query(tableName, columnsToRetrieve, null,
                null, null, null, null);
        return (!cursor.equals(null));
    }

    //thêm giá trị vào bảng
    public void insertSingleEntry(String tableName, ContentValues value) {

        database.insert(tableName, null, value);

    }
    public void insertMultipleEntries(String tableName,
                                      ArrayList<ContentValues> valueList) {

        for (ContentValues value : valueList)
            database.insert(tableName, null, value);

    }

    //update giá trị
    public void updateSingleEntry(String tableName, ContentValues value,
                                  String whereClause) {

        database.update(tableName, value, whereClause, null);

    }
    public void updateMultipleEntries(String tableName,
                                      ArrayList<ContentValues> valueList,
                                      ArrayList<String> whereClauseList) {

        for (int i = 0; i < valueList.size(); i++)
            database.update(tableName, valueList.get(i),
                    whereClauseList.get(i), null);

    }

    //xóa giá trị
    public void deleteSingleEntry(String tableName, String whereClause) {

        database.delete(tableName, whereClause, null);

    }
    public void deleteMultipleEntries(String tableName,
                                      ArrayList<String> whereClauseList) {

        for (String whereClause : whereClauseList)
            database.delete(tableName, whereClause, null);

    }
    public void deleteAllEntries(String tableName) {

        database.delete(tableName, null, null);

    }

    //lấy giá trị từ bảng
    public ContentValues retrieveSingleEntry(String tableName,
                                             String whereClause, String[] columnsToRetrieve)
            throws NullPointerException, SQLiteException {

        Cursor cursor = database.query(tableName, columnsToRetrieve,
                whereClause, null, null, null, null);
        ContentValues value = new ContentValues();
        if (!cursor.equals(null) && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++)
                value.put(cursor.getColumnName(i), cursor.getString(i));

        } else {
            value = null;
        }

        return value;
    }

    public ArrayList<ContentValues> retrieveMultipleEntries(String tableName,
                                                            ArrayList<String> whereClauseList, String[] columnsToRetrieve) {

        ArrayList<ContentValues> valueList = new ArrayList<ContentValues>();
        for (String whereClause : whereClauseList) {
            Cursor cursor = database.query(tableName, columnsToRetrieve,
                    whereClause, null, null, null, null);
            ContentValues value = new ContentValues();
            if (!cursor.equals(null) && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < columnsToRetrieve.length; i++)
                    value.put(columnsToRetrieve[i], cursor.getString(cursor
                            .getColumnIndex(columnsToRetrieve[i])));
                valueList.add(value);
            } else {
                value = null;
            }

        }
        return valueList;
    }

    public ArrayList<ContentValues> retrieveAllEntries(String tableName,
                                                      String[] columnsToRetrieve) {

        ArrayList<ContentValues> valueList = new ArrayList<ContentValues>();
        Cursor cursor = database.query(tableName, columnsToRetrieve, null,
                null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            {
                ContentValues value = new ContentValues();
                if (!cursor.equals(null) && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < columnsToRetrieve.length; i++)
                        value.put(columnsToRetrieve[i], cursor.getString(cursor
                                .getColumnIndex(columnsToRetrieve[i])));
                    valueList.add(value);
                } else {
                    value = null;
                }
            }

        }
        return valueList;
    }

    public void executeSqlCommand(String command) {
        database.execSQL(command);
    }

    public Cursor executeSqlCommandWithCursorReturn(String command) {
        return database.rawQuery(command, null);
    }

    //tạo ra chuỗi lệnh SQL để tạo bảng mới
    private String generateCreateTableCommandString(String tableName,
                                                    ArrayList<String> columnNames,
                                                    HashMap<String, String> columnDataTypeMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        for (int i = 0; i < columnNames.size(); i++) {
            sb.append(columnNames.get(i) + " "
                    + columnDataTypeMap.get(columnNames.get(i)));
            if (i < (columnNames.size() - 1))
                sb.append(", ");
        }
        sb.append("); ");
        return sb.toString();

    }

    //lưu bảng thành file excel
    public void saveSingleTableAsExcelFile(String tableName, String filePath,
                                           String fileName) throws RowsExceededException, WriteException,
            IOException {

        ExcelWriteHandler.saveSingleTableData(fileName, filePath, tableName,
                this);
    }

    public void saveCompleteDatabaseAsSingleExcelFile(String filePath,
                                                      String fileName) throws RowsExceededException, WriteException,
            IOException {
        Log.d("Log", "A " + getDatabasePageSizeInBytes());

        ExcelWriteHandler.saveAllTablesDataSingleFile(fileName, filePath, this);
    }

    public void saveCompleteDatabaseAsMultipleExcelFiles(String tableName,
                                                         String filePath, String fileName) throws RowsExceededException,
            WriteException, IOException {
        ExcelWriteHandler.saveAllTablesDataSeparateFiles(filePath, this);
    }

    //thêm bảng từ file excel
    public void addSingleTableFromExcelFile(String sheetName, String filePath,
                                            String fileName) throws RowsExceededException, WriteException,
            IOException, BiffException {
        ExcelReadHandler.addSingleTableFromExcelSheet(fileName, filePath,
                sheetName, this);
    }

    public void addMultipleTablesFromExcelFile(ArrayList<String> sheetNames,
                                               String filePath, String fileName) throws RowsExceededException,
            WriteException, IOException, BiffException {
        ExcelReadHandler.addMultipleTablesFromExcelSheet(fileName, filePath,
                sheetNames, this);
    }

    public void addAllTableFromExcelFile(String sheetName, String filePath,
                                         String fileName) throws RowsExceededException, WriteException,
            IOException, BiffException {
        ExcelReadHandler.addAllTableFromExcelSheet(fileName, filePath, this);
    }
}
