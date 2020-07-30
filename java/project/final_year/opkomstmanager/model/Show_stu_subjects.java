package project.final_year.opkomstmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Show_stu_subjects {
    String subject_name, teacher_name, class_held, class_attended, attendance, assignments_given, funiqueId;
    ArrayList<Map<String, String>> assignmentList;

    public Show_stu_subjects(String subject_name, String teacher_name, String class_held, String class_attended, String attendance, String assignments_given, ArrayList<Map<String, String>> assignmentList, String funiqueId) {
        this.subject_name = subject_name;
        this.teacher_name = teacher_name;
        this.class_held = class_held;
        this.class_attended = class_attended;
        this.attendance = attendance;
        this.assignments_given = assignments_given;
        this.assignmentList = assignmentList;
        this.funiqueId = funiqueId;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getClass_held() {
        return class_held;
    }

    public void setClass_held(String class_held) {
        this.class_held = class_held;
    }

    public String getClass_attended() {
        return class_attended;
    }

    public void setClass_attended(String class_attended) {
        this.class_attended = class_attended;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAssignments_given() {
        return assignments_given;
    }

    public void setAssignments_given(String assignments_given) {
        this.assignments_given = assignments_given;
    }

    public ArrayList<Map<String, String>> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(ArrayList<Map<String, String>> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public String getFuniqueId() {
        return funiqueId;
    }

    public void setFuniqueId(String funiqueId) {
        this.funiqueId = funiqueId;
    }
}
