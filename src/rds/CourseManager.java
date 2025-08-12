package rds;

import java.util.*;

public class CourseManager {
    private List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
    }

    public void initializeCourses() {
        courses.add(new Course("MIS107", "Introduction to MIS", "1", "RA", "1:00-2:30", "JSM", 3));
        courses.add(new Course("MIS107", "Introduction to MIS", "2", "ST", "2:30-4:00", "KLM", 3));
        courses.add(new Course("MIS207", "Advanced MIS", "1", "MW", "2:30-4:00", "ABC", 3));
        courses.add(new Course("ACT201", "Accounting Principles", "1", "RA", "1:00-2:30", "XYZ", 3));
        courses.add(new Course("FIN254", "Financial Management", "1", "ST", "1:00-2:30", "PQR", 3));
        courses.add(new Course("CSE101", "Introduction to Programming", "1", "RA", "1:00-2:30", "DEF", 3));
        courses.add(new Course("CSE101", "Introduction to Programming", "2", "RA", "2:30-4:00", "GHI", 3));
        courses.add(new Course("CSE101", "Introduction to Programming", "3", "ST", "1:00-2:30", "JKL", 3));
        courses.add(new Course("MAT101", "Calculus I", "1", "MW", "1:00-2:30", "MNO", 3));
        courses.add(new Course("ENG102", "English Composition", "1", "RA", "2:30-4:00", "STU", 3));
        courses.add(new Course("PHY101", "Physics I", "1", "ST", "2:30-4:00", "VWX", 3));
    }

    public List<Course> getCourses() {
        return courses;
    }
}