package com.example.attendanceapplication.fragment.addlist;


import android.graphics.Picture;
import android.util.ArrayMap;

import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.Image;

public class StudentValue {
    private int MSSV;
    private String StudentName;
    private String DateOfBirth;
    private int ClassId;
    private String ClassName;
    private String ID_NFC;
    private String ID_FPS;
    private PictureData StudentPhoto;
//    XSSFWorkbook wb = new XSSFWorkbook();
//    Sheet sheet = wb.createSheet("My Sample Excel");
//    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);


    //private final ArrayMap<String, Object> mMap;

    public StudentValue(int MSSV, String studentName, String dateOfBirth, int classId, String className, String ID_NFC, String ID_FPS, PictureData studentPhoto) {
        this.MSSV = MSSV;
        StudentName = studentName;
        DateOfBirth = dateOfBirth;
        ClassId = classId;
        ClassName = className;
        this.ID_NFC = ID_NFC;
        this.ID_FPS = ID_FPS;
        StudentPhoto = studentPhoto;
        //this.mMap = new ArrayMap<>();
    }

    public int getMSSV()
    {return MSSV;}

    public void setMSSV(int MSSV)
    {this.MSSV = MSSV;}

    public String getStudentName()
    {return StudentName;}

    public void setStudentName(String StudentName)
    {this.StudentName = StudentName;}

    public String getDateOfBirth()
    {return DateOfBirth;}

    public void setDateOfBirth(String DateOfBirth)
    {this.DateOfBirth = DateOfBirth;}

    public int getClassId()
    {return ClassId;}

    public void setClassId(int ClassId)
    {this.ClassId = ClassId;}

    public String getClassName()
    {return ClassName;}

    public void setClassName(String ClassName)
    {this.ClassName = ClassName;}

    public String getID_NFC() {
        return ID_NFC;
    }

    public String getID_FPS() {
        return ID_FPS;
    }

    public PictureData getStudentPhoto() {
        return StudentPhoto;
    }

    public void setID_NFC(String ID_NFC) {
        this.ID_NFC = ID_NFC;
    }

    public void setID_FPS(String ID_FPS) {
        this.ID_FPS = ID_FPS;
    }

    public void setStudentPhoto(PictureData studentPhoto) {
        StudentPhoto = studentPhoto;
    }

//    public int size() {
//        return mMap.size();
//    }
//
//    public String getAsString(String key) {
//        Object value = mMap.get(key);
//        return value != null ? value.toString() : null;
//    }
}
