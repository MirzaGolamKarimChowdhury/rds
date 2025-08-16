package rds;

import java.util.*;

public class Payment {
    private Student student;
    private List<Course> advisedCourses;

    public Payment(Student student, List<Course> advisedCourses) {
        this.student = student;
        this.advisedCourses = advisedCourses;
    }

    public String getAccountStatus() {
        int tuitionTotal = advisedCourses.stream().mapToInt(course -> course.getCredit() * 6500).sum();
        int studentActivityFee = 3000;
        int computerLabFee = 2500;
        int libraryFee = 1500;
        int scienceLabFee = 2500;
        int studioLabFee = 0;
        int finalTotal = tuitionTotal + studentActivityFee + computerLabFee + libraryFee + scienceLabFee + studioLabFee;

        StringBuilder status = new StringBuilder();
        status.append("=== Account Status ===\n");
        status.append("Student ID: ").append(student.getId()).append("\n");
        status.append("Student Name: ").append(student.getName()).append("\n");
        status.append("Outstanding Balance: ").append(finalTotal).append("\n");
        return status.toString();
    }

    public String getPaymentHistory() {
        return "Online Payment History feature is under development (Mock).\n";
    }
}
