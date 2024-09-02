import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentGradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();

        Student student = new Student(studentName);

        while (true) {
            System.out.println("1. Add course");
            System.out.println("2. Add grade");
            System.out.println("3. Print report");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (option) {
                case 1:
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    Course course = new Course(courseName);
                    student.addCourse(course);
                    break;
                case 2:
                    System.out.print("Enter course name: ");
                    String courseNameForGrade = scanner.nextLine();
                    Course courseForGrade = null;
                    for (Course c : student.getCourses()) {
                        if (c.getName().equals(courseNameForGrade)) {
                            courseForGrade = c;
                            break;
                        }
                    }
                    if (courseForGrade != null) {
                        System.out.print("Enter score: ");
                        double score = scanner.nextDouble();
                        System.out.print("Enter max score: ");
                        double maxScore = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline left-over
                        Grade grade = new Grade(score, maxScore);
                        courseForGrade.addGrade(grade);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;
                case 3:
                    student.printReport();
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}

class Grade {
    private double score;
    private double maxScore;

    public Grade(double score, double maxScore) {
        this.score = score;
        this.maxScore = maxScore;
    }

    public double getScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public double getPercentage() {
        return (score / maxScore) * 100;
    }
}

class Course {
    private String name;
    private List<Grade> grades;

    public Course(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public double calculateAverage() {
        if (grades.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getPercentage();
        }
        return sum / grades.size();
    }
}

class Student {
    private String name;
    private List<Course> courses;

    public Student(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public double calculateOverallAverage() {
        if (courses.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Course course : courses) {
            sum += course.calculateAverage();
        }
        return sum / courses.size();
    }

    public void printReport() {
        System.out.println("Student: " + name);
        for (Course course : courses) {
            System.out.println("Course: " + course.getName());
            System.out.println("Average: " + course.calculateAverage() + "%");
        }
        System.out.println("Overall Average: " + calculateOverallAverage() + "%");
    }
}


