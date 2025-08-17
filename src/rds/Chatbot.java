package rds;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chatbot {

    private static final Map<String, Double> letterGradeToPoints = new HashMap<>(); // New map for letter grade to grade points
    private static final Map<String, String> coursePrerequisites = new HashMap<>();
    private final List<CourseGrade> courseGrades = new ArrayList<>(); // Store courses for GPA calculation

    
    private static class CourseGrade {
        String courseCode;
        double credits;
        String letterGrade;
        double gradePoint;

        CourseGrade(String courseCode, double credits, String letterGrade, double gradePoint) {
            this.courseCode = courseCode;
            this.credits = credits;
            this.letterGrade = letterGrade;
            this.gradePoint = gradePoint;
        }
    }

    static {
       

        // Initialize letter grade to grade points mapping
        letterGradeToPoints.put("A", 4.0);
        letterGradeToPoints.put("A-", 3.7);
        letterGradeToPoints.put("B+", 3.3);
        letterGradeToPoints.put("B", 3.0);
        letterGradeToPoints.put("B-", 2.7);
        letterGradeToPoints.put("C+", 2.3);
        letterGradeToPoints.put("C", 2.0);
        letterGradeToPoints.put("C-", 1.7);
        letterGradeToPoints.put("D+", 1.3);
        letterGradeToPoints.put("D", 1.0);
        letterGradeToPoints.put("F", 0.0);
        letterGradeToPoints.put("I", 0.0); // Incomplete
        letterGradeToPoints.put("W", 0.0); // Withdrawal

        
        coursePrerequisites.put("ECO101", "NONE");
        coursePrerequisites.put("ECO104", "ECO101");
        coursePrerequisites.put("MIS107", "NONE");
        coursePrerequisites.put("BUS251", "ENG105");
        coursePrerequisites.put("BUS172", "NONE");
        coursePrerequisites.put("BUS173", "BUS172");
        coursePrerequisites.put("BUS135", "BUS112");
        coursePrerequisites.put("ACT201", "NONE");
        coursePrerequisites.put("ACT202", "ACT201");
        coursePrerequisites.put("FIN254", "ACT201 & BUS172");
        coursePrerequisites.put("LAW200", "NONE");
        coursePrerequisites.put("INB372", "MKT202");
        coursePrerequisites.put("MKT202", "NONE");
        coursePrerequisites.put("MIS207", "MIS107");
        coursePrerequisites.put("MGT212", "NONE");
        coursePrerequisites.put("MGT351", "MGT212");
        coursePrerequisites.put("MGT314", "BUS172 & MKT202");
        coursePrerequisites.put("MGT368", "MGT212");
        coursePrerequisites.put("MGT489", "COMPLETE 100 CREDITS");
        coursePrerequisites.put("ENG103", "ENG102");
        coursePrerequisites.put("ENG105", "ENG103");
        coursePrerequisites.put("BEN205", "NONE");
        coursePrerequisites.put("PHI401", "NONE");
        coursePrerequisites.put("HIS103", "NONE");
        coursePrerequisites.put("HIS101", "NONE");
        coursePrerequisites.put("HIS102", "NONE");
        coursePrerequisites.put("HIS202", "NONE");
        coursePrerequisites.put("HIS205", "NONE");
        coursePrerequisites.put("POL101", "NONE");
        coursePrerequisites.put("POL202", "NONE");
        coursePrerequisites.put("POL104", "NONE");
        coursePrerequisites.put("PAD201", "NONE");
        coursePrerequisites.put("SOC101", "NONE");
        coursePrerequisites.put("GEO205", "NONE");
        coursePrerequisites.put("ANT101", "NONE");
        coursePrerequisites.put("BIO103", "NONE");
        coursePrerequisites.put("BIO103L", "NONE");
        coursePrerequisites.put("ENV107", "NONE");
        coursePrerequisites.put("ENV107L", "NONE");
        coursePrerequisites.put("PBH101", "NONE");
        coursePrerequisites.put("PBH101L", "NONE");
        coursePrerequisites.put("PSY101", "NONE");
        coursePrerequisites.put("PSY101L", "NONE");
        coursePrerequisites.put("PHY107", "NONE");
        coursePrerequisites.put("PHY107L", "NONE");
        coursePrerequisites.put("CHE101", "NONE");
        coursePrerequisites.put("CHE101L", "NONE");
        coursePrerequisites.put("MKT337", "MKT202");
        coursePrerequisites.put("MKT344", "MKT202");
        coursePrerequisites.put("MKT460", "MKT202");
        coursePrerequisites.put("MKT470", "BUS173/MKT202");
        coursePrerequisites.put("MKT412", "MKT202");
        coursePrerequisites.put("MKT465", "MKT202");
        coursePrerequisites.put("MKT382", "MKT202");
        coursePrerequisites.put("MKT417", "NONE");
        coursePrerequisites.put("MKT330", "NONE");
        coursePrerequisites.put("MKT450", "MKT202");
        coursePrerequisites.put("MKT355", "MKT202");
        coursePrerequisites.put("MKT445", "MKT202");
        coursePrerequisites.put("MKT475", "MKT202/MKT470");
        coursePrerequisites.put("INB400", "FIN444-BEFORE 143 BATCH");
        coursePrerequisites.put("INB490", "NONE");
        coursePrerequisites.put("INB480", "INB372");
        coursePrerequisites.put("INB410", "MKT202/INB372");
        coursePrerequisites.put("INB350", "MKT202/INB372");
        coursePrerequisites.put("INB355", "MKT202/INB372");
        coursePrerequisites.put("INB415", "MKT202/INB372");
        coursePrerequisites.put("INB450", "MKT202/INB372");
        coursePrerequisites.put("INB495", "INB372");
        coursePrerequisites.put("ACT310", "FIN254");
        coursePrerequisites.put("ACT320", "ACT310");
        coursePrerequisites.put("ACT360", "ACT202 & FIN254");
        coursePrerequisites.put("ACT370", "ACT201 & ACT202");
        coursePrerequisites.put("ACT380", "ACT201 & ACT202");
        coursePrerequisites.put("ACT460", "ACT320");
        coursePrerequisites.put("ACT430", "ACT202 & FIN254");
        coursePrerequisites.put("ACT410", "ACT201 & ACT202");
        coursePrerequisites.put("FIN433", "FIN254");
        coursePrerequisites.put("FIN435", "FIN433");
        coursePrerequisites.put("FIN440", "FIN254");
        coursePrerequisites.put("FIN444", "INB372 & FIN254");
        coursePrerequisites.put("FIN410", "FIN254 & ACT202");
        coursePrerequisites.put("FIN455", "FIN440");
        coursePrerequisites.put("FIN464", "FIN254");
        coursePrerequisites.put("FIN470", "FIN435 & FIN440");
        coursePrerequisites.put("FIN480", "FIN435 & FIN440");
        coursePrerequisites.put("MIS210", "MIS207");
        coursePrerequisites.put("MIS310", "MIS207");
        coursePrerequisites.put("MIS320", "MIS207");
        coursePrerequisites.put("MIS470", "MIS310");
        coursePrerequisites.put("MIS410", "MIS207 & BUS173");
        coursePrerequisites.put("MIS450", "MIS207 & MIS310");
        coursePrerequisites.put("MGT490", "MGT314");
        coursePrerequisites.put("MIS499", "NONE");
        coursePrerequisites.put("MGT321", "MGT212");
        coursePrerequisites.put("MGT330", "MGT321");
        coursePrerequisites.put("MGT370", "MGT321");
        coursePrerequisites.put("MGT410", "MGT321");
        coursePrerequisites.put("MGT350", "MGT321");
        coursePrerequisites.put("HRM340", "MGT351");
        coursePrerequisites.put("HRM360", "MGT351");
        coursePrerequisites.put("HRM380", "MGT351");
        coursePrerequisites.put("HRM450", "MGT351");
        coursePrerequisites.put("HRM370", "MGT351");
        coursePrerequisites.put("HRM410", "MGT351");
        coursePrerequisites.put("HRM499", "MGT351");
        coursePrerequisites.put("HRM470", "MGT351");
        coursePrerequisites.put("SCM310", "MKT202 & MGT212");
        coursePrerequisites.put("SCM320", "NONE");
        coursePrerequisites.put("SCM450", "MGT314");
        coursePrerequisites.put("SCM360", "MKT202 & MGT212");
        coursePrerequisites.put("SCM460", "NONE");
        coursePrerequisites.put("SCM390", "NONE");
        coursePrerequisites.put("MGT470", "NONE");
        coursePrerequisites.put("ECO201", "ECO101 & BUS135");
        coursePrerequisites.put("ECO204", "ECO101 & ECO104");
        coursePrerequisites.put("ECO328", "ECO101 & ECO104");
        coursePrerequisites.put("ECO348", "ECO101 & ECO104");
        coursePrerequisites.put("ECO372", "BUS173");
        coursePrerequisites.put("ECO472", "ECO372");
        
    }

    public String getPrerequisites(String courseCode) {
        courseCode = courseCode.trim().toUpperCase();
        String prereq = coursePrerequisites.getOrDefault(courseCode, "Prerequisite information not available.");
        return "Course: " + courseCode + "\nPrerequisites: " + prereq;
    }

    // Add a course with credits and letter grade
    public String addCourse(String courseCode, String credits, String letterGrade) {
        try {
            double creditValue = Double.parseDouble(credits.trim());
            if (creditValue <= 0) {
                return "Invalid credits. Please enter a positive number.";
            }
            letterGrade = letterGrade.trim().toUpperCase();
            if (!letterGradeToPoints.containsKey(letterGrade)) {
                return "Invalid letter grade. Valid grades are: A, A-, B+, B, B-, C+, C, C-, D+, D, F, I, W.";
            }
         
            courseCode = courseCode.trim().toUpperCase();
           
            double gradePoint = letterGradeToPoints.get(letterGrade);
            courseGrades.add(new CourseGrade(courseCode, creditValue, letterGrade, gradePoint));
            return "Added course: " + courseCode + " (" + credits + " credits, Grade: " + letterGrade + ")";
        } catch (NumberFormatException e) {
            return "Invalid credits format. Please enter a numeric value for credits.";
        }
    }

    // Calculate GPA based on added courses
    public String calculateGPA() {
        if (courseGrades.isEmpty()) {
            return "No courses added. Please add courses using 'addcourse <course code> <credits> <grade>'.";
        }
        double totalGradePoints = 0.0;
        double totalCredits = 0.0;
        StringBuilder summary = new StringBuilder("GPA Calculation:\n");
        summary.append("----------------------------------------\n");
        summary.append(String.format("%-15s %-10s %-10s %-10s\n", "Course", "Credits", "Grade", "Grade Points"));
        summary.append("----------------------------------------\n");

        for (CourseGrade course : courseGrades) {
            double courseGradePoints = course.credits * course.gradePoint;
            totalGradePoints += courseGradePoints;
            totalCredits += course.credits;
            summary.append(String.format("%-15s %-10.2f %-10s %-10.2f\n",
                    course.courseCode, course.credits, course.letterGrade, courseGradePoints));
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
        summary.append("----------------------------------------\n");
        summary.append(String.format("Total Credits: %.2f\n", totalCredits));
        summary.append(String.format("GPA: %.2f\n", gpa));
        return summary.toString();
    }

    // Clear all added courses
    public void clearCourses() {
        courseGrades.clear();
    }

    // List all added courses
    public String listCourses() {
        if (courseGrades.isEmpty()) {
            return "No courses added.";
        }
        StringBuilder summary = new StringBuilder("Added Courses:\n");
        summary.append("----------------------------------------\n");
        summary.append(String.format("%-15s %-10s %-10s\n", "Course", "Credits", "Grade"));
        summary.append("----------------------------------------\n");
        for (CourseGrade course : courseGrades) {
            summary.append(String.format("%-15s %-10.2f %-10s\n",
                    course.courseCode, course.credits, course.letterGrade));
        }
        return summary.toString();
    }
}


