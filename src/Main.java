//Group Names: 
//Aridsondez Jerome 
//Abrar Aurora
//Mohamed Moussa
//Dylan Weber

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main { //main class where all the classes and methods will be called
    static studentLinkedList school= new studentLinkedList();
    public static void main(String[] args) {
        //Linked List holds all the students
        //not sure where to initialize this based on menu objects
        Menus menu = new MainMenu();

        menu.menuSelectCheck();
        
    }
}

class MyException extends Throwable {

    public static boolean validateID(String id) {
        // Your ID validation logic here
        // For example, using a regular expression:
        String ID_PATTERN = "[a-zA-Z]{2}\\d{4}";
        return id.matches(ID_PATTERN);
    }

}

// Abstract class for all menus to be based off
abstract class Menus<T> {

    // Prints out menu prompt and stores user's input selection
    abstract public T menuPromptAndSelect();

    // Takes user input performed by first class and performs action based on user input
    abstract public void menuSelectCheck();
}

class MainMenu extends Menus<Integer> {

    // Objects for class to access overridden methods
    StudentManagement studentManagementMenu = new StudentManagement();
    CourseManagement courseManagementMenu = new CourseManagement();

    // Takes user input stored in the Menu Class
    @Override
    public void menuSelectCheck() {
        int userSelection = -1;
        userSelection = menuPromptAndSelect();
        switch (userSelection) {

            // User selects "Exit"
            case 0->{ System.out.println("Take Care!");
                System.exit(0);break;}

            // User selects "Student Management"
            case 1->{studentManagementMenu.menuSelectCheck();
                break;
            }

            // User selects "Course Management"
            case 2->{courseManagementMenu.menuSelectCheck();break;}

            // Error, should never be reached
            default -> {System.out.println("fatal error! program will shutdown");System.exit(1);}
        }
    }

    @Override
    public Integer menuPromptAndSelect() {
        boolean toggle = true;
        int mainSelection = -1;
        Scanner mainScan = new Scanner(System.in);

        while (toggle) {
            try {
                System.out.println("-------------------------------------------\nMain Menu\n\n1 : Student Management\n2 : Course Management\n0 : Exit");
                mainSelection = mainScan.nextInt();

                if (2<mainSelection || mainSelection<0) {
                    throw new MyException();
                }
            } catch (MyException | Exception e) {
                System.out.println("invalid input!");
                mainScan.nextLine();
                
            } finally {
                if (mainSelection >= 0 && mainSelection <= 2) {
                    toggle = false;
                }
            }
        }
        return mainSelection;
    }
}

class StudentManagement extends Menus<Character> {

    @Override
    public Character menuPromptAndSelect() {
        boolean toggle = true;
        char studentSelection = ' ';

        Scanner studentScan = new Scanner(System.in);

        try {
            System.out.println("Student management Menu:\nChoose one of:\nA - Search add a student\nB - Delete a student\nC - Print Fee Invoice\nD - Print List of Students\nX - Back to Main menu");
            studentSelection = studentScan.next().charAt(0);
            studentSelection = Character.toUpperCase(studentSelection);

            switch (studentSelection) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'X':
                    break; // Valid input
                default:
                    throw new MyException(); // Invalid input
            }
        }
        catch (MyException | Exception e) {
            System.out.println("invalid input!");
        }
        finally {
            //this isn't necessary
            //if(studentSelection == 'a'  studentSelection != 'b' && studentSelection != 'c' && studentSelection != 'd' && studentSelection != 'e') ) { // will change
            //    toggle = false;
            // ...
        }
        return studentSelection;
    }

    // Need to fix formatting for this class
    // ...
    @Override
    public void menuSelectCheck() {

        char sMenuSelection;
        sMenuSelection = menuPromptAndSelect();
        Scanner addStudent= new Scanner(System.in);

        switch (sMenuSelection){
            case 'A'->{

                String name,id, type;
                double gpa;
                int classes;


            try {
            
                System.out.println("Enter student ID:");
                id = addStudent.nextLine();

                if (Main.school.searchStudent(id)) {
                    System.out.println("Student with ID " + id + " already exists.");
                    return;
                }

                if (!MyException.validateID(id)) {
                    throw new MyException();
                }

            } catch (MyException e) {
                System.out.println("Invalid ID format. Please enter in the correct format.");
                return;
            }
                System.out.println("Enter the student's name:");
                name=addStudent.nextLine();

                try{
                     System.out.println("What Is The Students GPA");
                    gpa=addStudent.nextDouble();
                    addStudent.nextLine();//discard '\n'

                    if  (gpa < 0 || gpa > 4.0){
                        throw new IllegalArgumentException("GPA must be between 0 and 4.0");
                    }
                }catch (InputMismatchException e){
                    System.out.println("Invalid input format. Please enter a valid GPA.");
                    return;
                }catch(IllegalArgumentException e){
                    System.out.println(e.getMessage());
                    return;
                }



                System.out.println("What Type of Student (PhD, MS or Undergrad):");
                type=addStudent.nextLine();

                if (!type.equals("PhD") || !type.equals("MS")|| !type.equals("Undergrad")){
                    System.out.println("Please Type Exactly:  PhD || MS || Undergrade");
                    return; 
                }


                if(type.equals("Undergrad")){
                    System.out.println("ADDING AN : Undergrad Student");
                    System.out.println("How Many CRNS Are They Taking");
                    classes=addStudent.nextInt();
                    addStudent.nextLine();//discard '\n'
                    int undergradCrnsTaken[]=new int[classes];
                    for (int i=0; i<classes;i++){
                        System.out.println("What Class Number "+(i+1)+" Code");
                        undergradCrnsTaken[i]=addStudent.nextInt();
                        addStudent.nextLine();//discard '\n'
                    }

                    System.out.println("Are They A Resident");
                    //not sure what this is?

                    //creating student
                    Student newStudent= new UndergraduateStudent(name,id,undergradCrnsTaken,gpa,false);
                    Main.school.addNewStudent(newStudent);
                }

                //different inputs for different types of students
                if (type.equals("PhD")){
                    System.out.println("ADDING AN : PHD Student");
                    String subject, advisorName;
                    int labAmount, labNumbers;
                    System.out.println("What Is The name of advisor");
                    advisorName=addStudent.nextLine();
                    System.out.println("What Is Their Reseach Subject");
                    subject=addStudent.nextLine();

                    System.out.println("What is the amount of labs");
                    labAmount=addStudent.nextInt();
                    addStudent.nextLine();//discard '\n'
                    int labsSupervised[] = new int [labAmount];
                    for (int i = 0; i<labAmount;i++){
                        System.out.println("The" +(i+1)+"Lab Number is :");
                        labsSupervised[i]=addStudent.nextInt();
                        addStudent.nextLine();//discard '\n'
                    }

                    Student newPhdStudent= new PHDStudent(name, id, labsSupervised,advisorName,subject);
                    Main.school.addNewStudent(newPhdStudent);
                }

                if(type.equals("MS")){
                    System.out.println("ADDING AN : Master Student");

                    System.out.println("How Many CRNS Are They Taking");

                    try{

                        classes=addStudent.nextInt();
                        addStudent.nextLine();//discard '\n'

                    }catch(InputMismatchException e){
                        System.out.println("Invalid input. It Has To Be A Number");
                        return;
                    }
                    
                    int gradCrnsTaken[]=new int[classes];
                    for (int i=0; i<classes;i++){
                        System.out.println("What Class Number "+(i+1)+" Code");
                        try{
                            gradCrnsTaken[i]=addStudent.nextInt();
                            addStudent.nextLine();//discard '\n'
                        } catch(InputMismatchException e){
                            System.out.println("Invalid input. It Has To Be A Number");
                        }
                    }


                    Student newMasterStudent= new MSStudent(name, id, gradCrnsTaken);
                    Main.school.addNewStudent(newMasterStudent);
                }

                System.out.println(name + " ADDED");
                break;
            }//Add studeent
            case 'B'->{
                String id;
                System.out.println("What Is The Student's ID:");
                id=addStudent.nextLine();

                Main.school.searchStudent(id);
                break;
            }//Search Student
            case 'C'->{
                //get input w/ scanner
                String id;
                
            try {
            
                System.out.println("Enter student ID:");
                id = addStudent.nextLine();

                if (Main.school.searchStudent(id)) {
                    System.out.println("Student with ID " + id + " already exists.");
                    return;
                }

                if (!MyException.validateID(id)) {
                    throw new MyException();
                }

            } catch (MyException e) {
                System.out.println("Invalid ID format. Please enter in the correct format.");
                return;
            }

                Main.school.deleteStudent(id);
                break;
            }//Delete a student
            case 'D'->{

                String id;
                
            try {
            
                System.out.println("Enter student ID:");
                id = addStudent.nextLine();

                if (Main.school.searchStudent(id)) {
                    System.out.println("Student with ID " + id + " already exists.");
                    return;
                }

                if (!MyException.validateID(id)) {
                    throw new MyException();
                }

            } catch (MyException e) {
                System.out.println("Invalid ID format. Please enter in the correct format.");
                return;
            }

                Main.school.printFeeInvoice(id);
                break;
            }//Print Fee invoice
            case 'E'->{Main.school.printListStudents();break;}//Print all students

            default -> {}
        }
    }
}

class CourseManagement extends Menus<Character> {
    FileInteractions fileIntObject = new FileInteractions();
    public Character menuPromptAndSelect() {
        System.out.println("Course Management Menu:\n\nChoose one of:\n\nA - search for a class or lab using class/lab number\nB - delete a class\nC - Add a lab to a class\nX – Back to main menu");
        return 'b' ;
    }

    @Override
    public void menuSelectCheck() {
        char cMenuSelection;
        cMenuSelection = Character.toLowerCase(menuPromptAndSelect());
        switch (cMenuSelection){
            case 'a' -> {
                System.out.println("Enter the Lab/course number you want to look up:");
                Scanner crnScan = new Scanner(System.in);
                int selectionNum = crnScan.nextInt();
                boolean negativeFlag = true;
                for (Lecture course: fileIntObject.getAllCourseList()){
                    if (Integer.parseInt(course.getCrn()) == selectionNum){
                        System.out.print("[" + course.getCrn() + "-" + course.getPrefix() + "-" + course.getLectureName());
                        if(course.getLectureMode() == LectureMode.ONLINE){
                            System.out.println("-Online" + "]");
                            negativeFlag = false;
                        }
                        else if(course.getLectureMode() == LectureMode.F2F || course.getLectureMode() == LectureMode.MIXED){
                            System.out.println("-" + course.getLectureType() + "-" + course.getClassroom() + "]");
                            negativeFlag = false;
                        }
                    }
                    else{
                        if (course.getLectureMode() != LectureMode.ONLINE){
                            if(course.isHasLabs()){
                                for(Lab lab: course.getLabs()) {
                                    if (Integer.parseInt(lab.getCrn()) == selectionNum){
                                        System.out.println("Lab for:" + "[" + course.getCrn() + "-" + course.getPrefix() + "-" + course.getLectureName() +"-" + course.getLectureType() + "-" + course.getClassroom()+ "]");
                                        System.out.println("Lab info:" + lab.getCrn() + "--" + lab.getClassroom());
                                        negativeFlag = false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (negativeFlag){
                    System.out.println("Course/Lab was not found :(");
                }
            }

            case 'b' -> {System.out.println("Enter the Lab/course number you want deleted:");
                    Scanner crnScan = new Scanner(System.in);
                    int selectionNum = crnScan.nextInt();
                    ArrayList<Lecture> dummyCourseList = new ArrayList<>(fileIntObject.getAllCourseList());
                    boolean negativeflag = true;
                    for(Lecture course: dummyCourseList){
                        if(Integer.parseInt(course.getCrn()) == selectionNum && !course.isHasLabs()){
                            System.out.println("[" + course.getCrn() + "," + course.getPrefix() + "," + course.getLectureName() + "] deleted!");
                            fileIntObject.getAllCourseList().remove(course);
                            negativeflag = false;
                        }
                    }
                    if (negativeflag){
                        System.out.println("Class not found!");
                    }
            }
            case 'c' -> {Scanner crnScan = new Scanner(System.in);
                System.out.println("Enter the course you'd like to add a lab to:");
                int selectionNum = crnScan.nextInt();
                for(Lecture course: fileIntObject.getAllCourseList()){
                    if(Integer.parseInt(course.getCrn()) == selectionNum && !course.isHasLabs()) {
                            System.out.println("enter the information for the lab in this format -> crn,classroom :");
                            String unparsedInput = crnScan.nextLine();
                            String[] labInfo = unparsedInput.split(",");
                            Lab newLab = new Lab(labInfo[0],labInfo[1]);
                            course.getLabs().add(newLab);
                        }
                    }
            }
            case 'x' -> {}
        }
    }
}

abstract class Student {

    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Student(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toUpperCase() + "-" + name.toUpperCase();
    }

    abstract public void printInvoice();
}

class UndergraduateStudent extends Student{

    private int[] undergradCrnsTaken;
    private double gpa;
    private boolean resident;

    public int[] getUndergradCrnsTaken() {
        return undergradCrnsTaken;
    }

    public void setUndergradCrnsTaken(int[] undergradCrnsTaken) {
        this.undergradCrnsTaken = undergradCrnsTaken;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public boolean isResident() {
        return resident;
    }

    public void setResident(boolean resident) {
        this.resident = resident;
    }

    public UndergraduateStudent(String name, String id, int[] undergradCrnsTaken, double gpa, boolean resident) {
        super (name, id);
        this.undergradCrnsTaken = undergradCrnsTaken.clone();
        this.gpa = gpa;
        this.resident= resident;
    }

    @Override
    public void printInvoice() {
        double price = 0;
        double basePrice = 120.25;
        double health = 35.00;

        if (!isResident()){
            basePrice *= 2;
        }

        System.out.println("VALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("-----------------------------");

        System.out.println(getId() + "-" + getName());
        System.out.println("\n1 Credit Hour = $" + basePrice);
        System.out.println("CRN\tCR_PREFIX\tCR_HOURS");

        // Loop through classes
        for (int i = 0; i < this.undergradCrnsTaken.length; i++) {
            System.out.printf("\n%d\t%s\t\t%d", undergradCrnsTaken[i], undergradCrnsTaken[i], undergradCrnsTaken[i]);
        }
        System.out.println("\n\t\tHealth & id Fees  $ " + health);
        System.out.println("\n---------------------------------");
        double discount = 0;
        if (getGpa() >= 3.5 && price > 500) { // For some reason, it says that this is always false?
            discount = price * 0.25;
            // print total minus discounted prices
        } else {
            System.out.println("\t\tTOTAL PAYMENTS\t\t$ " + price);
        }
    }
}

abstract class GraduateStudent extends Student {

    public GraduateStudent (String name, String id) {
        super(name, id);
    }
}

class PHDStudent extends GraduateStudent {

    private int[] labsSupervised;
    private String advisor;
    private String researchSubject;

    public int[] getlabsSupervised() {
        return labsSupervised;
    }

    public void setLabsSupervised(int[] labsSupervised) {
        this.labsSupervised = labsSupervised;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getResearchSubject() {
        return researchSubject;
    }

    public void setResearchSubject(String researchSubject) {
        this.researchSubject = researchSubject;
    }

    public PHDStudent(String name, String id, int[] labsSupervised, String advisor, String researchSubject) {
        super(name, id);
        this.labsSupervised = labsSupervised.clone();
        this.advisor = advisor;
        this.researchSubject = researchSubject;
    }

    // Function to get total cost for a PHD student
    // ...

    @Override
    public void printInvoice() {
        double total = 0;
        double basePrice= 120.00;
        double health =  35.00;
        System.out.println("\nVALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("-----------------------------");
        System.out.println(getId() + "-" + getName());
        System.out.println("\n1 Credit Hour = \t$" + basePrice);
        
        System.out.println("RESEARCH");
        //if they surpervise 3 labs or more 

        System.out.println("\n\t\tHealth & id Fees  $ " + health);
        System.out.println("\n---------------------------------");
        System.out.println("\t\tTotal Payments\t$ "+ total);
        
    }
}
class  MSStudent extends GraduateStudent {

    private int[] gradCrnsTaken;

    public int[] getGradCrnsTaken() {
        return gradCrnsTaken;
    }

    public void setGradCrnsTaken(int[] gradCrnsTaken) {
        this.gradCrnsTaken = gradCrnsTaken;
    }

    public MSStudent(String name, String id, int[] gradCrnsTaken) {
        super(name, id);
        this.gradCrnsTaken = gradCrnsTaken.clone();

    }

    public void printInvoice() {
        double price = 0;
        double basePrice = 300.00;
        double health = 35.00;

        System.out.println("\nVALENCE COLLEGE\nORLANDO FL 10101");
        System.out.println("-----------------------------");
        System.out.println(getId() + "-" + getName());
        System.out.println("\n1 Credit Hour = $" + basePrice);
        System.out.println("CRN\tCR_PREFIX\tCR_HOURS");

        for (int i = 0; i < this.gradCrnsTaken.length; i++) {
            System.out.println(); //...
        }
        System.out.println("\n\t\tHealth & id Fees  $ " + health);
        System.out.println("\n---------------------------------");
        System.out.println("\t\tTOTAL PAYMENTS\t\t$ " + price);
    }
}

class studentNode {

    private Student e;
    private studentNode next;

    public studentNode(Student e) {
        this.e = e;
        this.next = null;
    }

    public Student getStudent() {
        return e;
    }

    public studentNode getNext() {
        return next;
    }

    public void setNext(studentNode next) {
        this.next = next;
    }
}

class studentLinkedList {
    studentNode school;

    public studentLinkedList() {
        school = null;
    }

    //Adds a new student to the linked list
    public void addNewStudent(Student e) {
        studentNode newNode = new studentNode(e);

        // Checks if the school is null if the newNode is the first student
        if (school == null) {
            school = newNode;
            return;
        }

        studentNode current = school;

        while (current != null) {
            if (current.getNext() == null) {
                current.setNext(newNode);
                return;
            }
            current = current.getNext();
        }
    }

    // Search the student by ID
    public boolean searchStudent(String id) {

        studentNode current = school;
        while (current != null) {
            if (current.getStudent().getId().equals(id)) {
                System.out.println(current.getStudent());
                return true;
            }
            current = current.getNext();
        }
        System.out.println("Student Not Found");
        return false;
    }

    // Deletes the student by id
    public void deleteStudent(String id) {

        // If there are no students in the school, return
        if (school == null) {
            return;
        }

        // If the first student is being deleted, return
        if (school.getStudent().getId().equals(id)) {
            school = school.getNext();
            return;
        }

        studentNode prev = school;
        studentNode current = school.getNext();

        while (current != null) {
            if (current.getStudent().getId().equals(id)) {
                prev.setNext(current.getNext());
                System.out.println(current.getStudent().getName() + " Has Been Successfully Deleted");
                return;
            }
            prev = current;
            current = current.getNext();
        }
        System.out.println("Student Not Found");
        return;
    }

    // Prints all students in the linked list
    // Implementation for grouping based on class
    public void printListStudents() {
        if (school == null) {
            return;
        }

        studentNode current = school;
        while (current != null) {
            System.out.println("- " + current.getStudent().getName());
            current = current.getNext();
        }
        return;
    }

    // Print fee invoice based on Id
    public void printFeeInvoice(String id) {
        if (school == null) {
            return;
        }

        studentNode current = school;
        while (current != null) {
            if (current.getStudent().getId().equals(id)) {
                current.getStudent().printInvoice();
                return;
            }
            current = current.getNext();
        }
        return;
    }
}



enum LectureType {
    GRADUATE, UNDERGRADUATE;
}

enum LectureMode {
    F2F, MIXED, ONLINE;
}

class Lab {
    private String crn;
    private String classroom;

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return crn + "," + classroom;
    }

    public Lab(String crn, String classroom) {
        this.crn = crn;
        this.classroom = classroom;
    }
}

class Lecture {

    // Make setters and getters
    private String crn;
    private String prefix;
    private String lectureName;

    private LectureType lectureType; //Grad or UnderGrad
    private LectureMode lectureMode; //F2F, Mixed or Online
    private String classroom;
    private boolean hasLabs;
    private int creditHours;

    ArrayList<Lab> labs;

    public String labReturn(){
       if(hasLabs){
           return "Yes";
       }
       if(!hasLabs){
           return "No";
       }
       else {
           return "";
       }
    }
    // _________________

    // Helper method-used in constructors to set up the common fields
    private void LectureCommonInfoSetUp(String crn, String prefix, String lectureName, LectureType lectureType, LectureMode lectureMode) {
        this.crn = crn;
        this.prefix = prefix;
        this.lectureName = lectureName;
        this.lectureType = lectureType;
        this.lectureMode = lectureMode;
    }

    // Non-online with Labs
    public Lecture(String crn,
                   String prefix,
                   String lectureName,
                   LectureType lectureType,
                   LectureMode lectureMode,
                   String classroom,
                   boolean hasLabs,
                   int creditHours,
                   ArrayList<Lab> labs) {

        LectureCommonInfoSetUp(crn, prefix, lectureName, lectureType, lectureMode);
        this.classroom = classroom;
        this.hasLabs = hasLabs;
        this.creditHours = creditHours;
        this.labs = labs;
    }

    // Constructor for non-online without labs
    public Lecture(String crn, String prefix, String lectureName, LectureType lectureType, LectureMode lectureMode, String classroom,
                   boolean hasLabs, int creditHours) {

        LectureCommonInfoSetUp(crn, prefix, lectureName, lectureType, lectureMode);
        this.classroom = classroom;
        this.hasLabs = hasLabs;
        this.creditHours = creditHours;
    }

    // Constructor for online lectures
    public Lecture(String crn, String prefix, String lectureName, LectureType lectureType, LectureMode lectureMode, int creditHours) {
        LectureCommonInfoSetUp(crn, prefix, lectureName, lectureType, lectureMode);
        this.classroom = classroom;
        this.hasLabs = hasLabs;
        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        String lectureAndLabs = crn + "," + prefix + "," + lectureName + "," + lectureType + ","
                + lectureMode + "," + hasLabs + "," + creditHours + "\n";

        // Printing corresponding labs
        if (labs != null) {

            //lectureAndLabs+="\n";
            for (Lab lab : labs)
                lectureAndLabs += lab + "\n";
        }
        return lectureAndLabs;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public LectureType getLectureType() {
        return lectureType;
    }

    public void setLectureType(LectureType lectureType) {
        this.lectureType = lectureType;
    }

    public LectureMode getLectureMode() {
        return lectureMode;
    }

    public void setLectureMode(LectureMode lectureMode) {
        this.lectureMode = lectureMode;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public boolean isHasLabs() {
        return hasLabs;
    }

    public void setHasLabs(boolean hasLabs) {
        this.hasLabs = hasLabs;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public ArrayList<Lab> getLabs() {
        return labs;
    }

    public void setLabs(ArrayList<Lab> labs) {
        this.labs = labs;
    }
}

class FileInteractions {

    private ArrayList<Lecture> courseLabList = new ArrayList<>(); // array list of F2F/Mixed courses with labs
    private ArrayList<Lecture> courseNoLabList = new ArrayList<>(); // array list of F2F/Mixed courses without labs
    private ArrayList<Lecture> courseOnlineList = new ArrayList<>(); // array list of online courses
    private ArrayList<Lab> LabList = new ArrayList<>(); //List of labs
    private static ArrayList<Lecture> allCourseList = new ArrayList<>();



    public ArrayList<Lecture> getAllCourseList() {
        return allCourseList;
    }

    public void setAllCourseList(ArrayList<Lecture> allCourseList) {
        this.allCourseList = allCourseList;
    }

    public ArrayList<Lab> getLabList() {
        return LabList;
    }

    public void setLabList(ArrayList<Lab> labList) {
        LabList = labList;
    }

    File lecfile = new File("lec.txt");

    public ArrayList<Lecture> getCourseLabList() {
        return courseLabList;
    }

    public void setCourseLabList(ArrayList<Lecture> courseLabList) {
        this.courseLabList = courseLabList;
    }

    public ArrayList<Lecture> getCourseNoLabList() {
        return courseNoLabList;
    }

    public void setCourseNoLabList(ArrayList<Lecture> courseNoLabList) {
        this.courseNoLabList = courseNoLabList;
    }

    public ArrayList<Lecture> getCourseOnlineList() {
        return courseOnlineList;
    }

    public void setCourseOnlineList(ArrayList<Lecture> courseOnlineList) {
        this.courseOnlineList = courseOnlineList;
    }

    public void initialize() {
        try {
            boolean shouldReadLine = true;
            Scanner fileRead = new Scanner(lecfile);
            String line;
            String[] readLine = {};
            while (fileRead.hasNextLine()) {
                //if the lab section has read the line, we do not want to read it again
                if (shouldReadLine) {
                    line = fileRead.nextLine();
                    readLine = line.split(",");
                } else {
                    shouldReadLine = true; //next iteration read line
                }
                if (readLine.length == 8) {
                    String crn = readLine[0];
                    String prefix = readLine[1];
                    String title = readLine[2];
                    LectureType lecType = LectureType.valueOf(readLine[3].toUpperCase());
                    LectureMode lecMode = LectureMode.valueOf(readLine[4].toUpperCase());
                    String classroom = readLine[5];
                    String hasLabPre = readLine[6];
                    boolean hasLab = false;
                    switch (hasLabPre.toLowerCase()) {
                        case "yes" -> {
                            hasLab = true;
                        }
                        case "no" -> {
                            hasLab = false;
                        }
                    }
                    int creditHours = Integer.parseInt(readLine[7]);
                    boolean fileToggle = true;
                    if (hasLab) {
                        ArrayList<Lab> labList = new ArrayList<>();
                        shouldReadLine = false;
                        String nextLine = fileRead.nextLine();
                        readLine = nextLine.split(",");
                        while (readLine.length == 2) {
                            String crnLab = readLine[0];
                            String clasroomLab = readLine[1];
                            Lab newLab = new Lab(crnLab, clasroomLab);
                            labList.add(newLab);
                            LabList.add(newLab);
                            nextLine = fileRead.nextLine();
                            readLine = nextLine.split(",");
                        }
                        Lecture newCourse = new Lecture(crn, prefix, title, lecType, lecMode, classroom, hasLab, creditHours, labList);
                        courseLabList.add(newCourse);
                        allCourseList.add(newCourse);
                    }
                    else{
                        Lecture newCourse = new Lecture(crn, prefix, title, lecType, lecMode, classroom, hasLab, creditHours);
                        courseNoLabList.add(newCourse);
                        allCourseList.add(newCourse);
                    }

                }
                else if(readLine.length == 6){
                    String crn = readLine[0];
                    String prefix = readLine[1];
                    String title = readLine[2];
                    LectureType lecType = LectureType.valueOf(readLine[3].toUpperCase());
                    LectureMode lecMode = LectureMode.valueOf(readLine[4].toUpperCase());
                    int creditHours = Integer.parseInt(readLine[5]);
                    Lecture newCourse = new Lecture(crn,prefix,title,lecType,lecMode,creditHours);
                    courseOnlineList.add(newCourse);
                    allCourseList.add(newCourse);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");

        }
    }
}

class FileWriteIO {
    FileInteractions fileIntDummy = new FileInteractions();
    public void fileWriter(){
        try {
            FileWriter fileOverwrite = new FileWriter("lec.txt", false);
            for(Lecture course: fileIntDummy.getAllCourseList()){
                if(course.isHasLabs()){
                    fileOverwrite.write(course.getCrn() + "," + course.getPrefix() + "," + course.getLectureName() + "," + course.getLectureType() + "," + course.getLectureMode() + "," + );
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading/writing to file!!!");
        }
    }
}
