/*
 * Name: Mahwish Ahmad
 * Project Name: Toronto Metropolitan University GPA Calculator 
 * Program Name: TorontoMetropolitanUniversityGPACalculator.java
 * Date: July 9th 2024
 */

import java.util.*;
import java.text.*;
import java.io.*;

public class TorontoMetropolitanUniversityGPACalculator {

    /**
     * The main bulk of code where information is gathered about the student's 
        course work and is used to calculate their overall grade
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{
        
        System.out.println("********************");
        System.out.println("* GRADE CALCULATOR *");
        System.out.println("********************");
        
        Scanner scanner = new Scanner(System.in);
        String name; // The student's name
        File file = new File("CourseInfo.txt");
        DecimalFormat decimal = new DecimalFormat("0.00");
        GradeInfo stats = new GradeInfo(); // instance of the GradeInfo class
       
        while (true) // Ensure no invalid name
        {
           try // Use a try-catch statement to catch the custom exception
           {
                System.out.print("Enter your name: ");
                name = scanner.nextLine();
        
                validateName(name, "Error: Invalid name."); break;
            }
            catch (InvalidNameException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        // Create 3 different instances of the Task class
        ArrayList<Task> assignments = new ArrayList<>();
        ArrayList<Task> quizzes = new ArrayList<>();
        ArrayList<Task> exams = new ArrayList<>();
        
        courseInfo(scanner, assignments, quizzes, exams, file); 
        
        int choice; // Create a variable for the student's choice from menu
        
        do
        {
            System.out.println("\nPlease choose from the following:");
            System.out.println("""
                               1. Calculate overall GPA for course
                               2. Calculate grade needed to pass
                               3. View Course Information
                               4. Quit""");
            
            choice = scanner.nextInt();
            
            switch (choice) 
            {
                // Calculate the student's GPA based on their tasks
                case 1 -> calculateGPA(assignments, quizzes, exams, decimal, 
                        file, stats); 
                /*
                    Calculate the grade they need to pass the course based on  
                    the remaining weight
                */
                case 2 -> calculateGradeNeeded(stats, decimal); 
                // Student can see their tasks and grade
                case 3 -> viewReport(file); 
                case 4 -> System.out.println("\nThank you for using this "
                        + "calculator!");
                default -> System.out.println("\nPlease enter an appropriate "
                        + "number.");
            }
        }
        while (choice != 4); // Repeat the loop unless student enters 4
        
        scanner.close(); // Close the scanner!
    }
 
/**
 * A method to check if the student provided an actual name and not an empty 
   string
 * @param name the name the user provides
 * @param e the message
 */
public static void validateName(String name, String e)
{
    if (name.equals(""))
    {
        throw new InvalidNameException(e);
    }
}

/**
 * A method to check if the student provided a number; not a character or string
 * @param num the number the user provided
 * @param e the message
 * @return the number 
 */
public static int validateNumber(String num, String e)
{
    try 
    {
        int number = Integer.parseInt(num);
        
        if (number <= 0) // The number must be positive(non-zero)
        {
            throw new InvalidNumberException(e);
        }
        return number; 
    }
    catch(NumberFormatException u)
    {
        throw new InvalidNumberException(e);
    }
}

/**
 * A method to check if the student provided a valid weight or grade; not a 
   character or string
 * @param num the weight or the grade entered by the student
 * @param e the custom message
 * @return the number
 */
public static double validateWeightOrGrade(String num, String e)
{
    try
    {
        double number = Double.parseDouble(num);
        if (number < 0 || number > 100)
        {
            throw new InvalidWeightOrGradeException(e);
        }
        return number;
    }
    catch (NumberFormatException u)
    {
        throw new InvalidWeightOrGradeException(e);
    }
}

/**
 * A custom exception class that extends RuntimeException
 */
static class InvalidNameException extends RuntimeException
{
    public InvalidNameException() {}
    
    public InvalidNameException(String message)
    {
        super(message);
    }
}

/**
 * A custom exception class that extends RuntimeException
 */
static class InvalidNumberException extends RuntimeException
{
    public InvalidNumberException() {}
    
    public InvalidNumberException(String message)
    {
        super(message);
    }
}

/**
 * A custom exception class that extends RuntimeException
 */
static class InvalidWeightOrGradeException extends RuntimeException
{
    public InvalidWeightOrGradeException() {}
    
    public InvalidWeightOrGradeException(String message)
    {
        super(message);
    }
}

/**
 * This method, based on the number the student enters, returns the appropriate 
   task
 * @param answer the number to be checked
 * @return the type of task
 */
public static String checkTask(int answer)
{
    return switch (answer) 
    {
        case 1 -> "assignment";
        case 2 -> "quiz";
        default -> "exam";
    };
}

/**
 * This method seeks out all the necessary information from the student and 
   writes it to a file
 * @param scanner the scanner that is used to get the required answers from the 
   student
 * @param assignments the array list of Task objects specifically for 
   assignments
 * @param quizzes the array list of Task objects specifically for quizzes
 * @param exams the array list of Task objects specifically for exams
 * @param file the "CourseInfo.txt" file
 */
public static void courseInfo(Scanner scanner, ArrayList<Task> assignments, 
        ArrayList<Task> quizzes, ArrayList<Task> exams, File file) 
{
    String course; // The name of the course
    
    while (true)
    {
        try
        {
            System.out.print("\nEnter the name of the course you are "
                + "calculating your grade for: ");
            course = scanner.nextLine();
            
            validateName(course, "Error: Invalid course name."); break;
        }
        
        catch (InvalidNameException e)
        {
            System.out.println("Error: " + e.getMessage());
        }   
    }
    
        String num = "-1";
        
        System.out.println("\nNow, you must enter the number of assignments, "
                + "quizzes or exams you had in your course.");
        
        while (true)
        {
            try
            {
                System.out.print("\nEnter the number of here: ");
                num = scanner.nextLine();
                
                validateNumber(num, "Error: Invalid number"); break;
            }
            catch(InvalidNumberException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        int length = Integer.parseInt(num); // Parse the string to an integer
        int answer; // The user's answer based on the menu given to them
        
        for (int i = 0; i < length; i++)
        {
            String title; // The title of the task ex. "Climate Report"
            String weight; // The weight of the task which should be a number
            String grade; // The grade on the task which should also be a number
            
            while (true)
            {
                System.out.print("\nIs your task an 1. assignment, 2. quiz, or "
                        + "3. exam? \n\nEnter the corresponding number: ");
                answer = scanner.nextInt();
                scanner.nextLine(); // Move to the next line
                
                if (answer >= 1 && answer <= 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Please enter a valid number");
                }
            }
            
            String taskType; // The type of the task
            // Use a method to check what type of task it is
            taskType = checkTask(answer); 
            
            System.out.print("\nEnter the title of the " + taskType + ": ");
            title = scanner.nextLine();
          
            while (true)
            {
                try
                {
                    System.out.print("\nEnter the weighting of the " + taskType
                        + " with only the number (no percentage sign): ");
                    weight = scanner.nextLine();
                    
                    validateWeightOrGrade(weight, "Error: Invalid weight."
                    ); break;
                }
                catch (InvalidWeightOrGradeException e)
                {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            
            while (true)
            {
                try
                {
                    System.out.print("\nEnter the grade you received on the " 
                            + taskType + ": "); 
                    grade = scanner.nextLine();
                
                    validateWeightOrGrade(grade, "Error: Invalid grade."); 
                    break;
                }
                catch (InvalidWeightOrGradeException e)
                {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            
            Task task = new Task(title, Double.parseDouble(weight), 
                    Double.parseDouble(grade)); // Create a new task
            
            switch (answer) // Add the task in the appropriate list
            {
                case 1 -> assignments.add(task);
                    
                case 2 -> quizzes.add(task);
            
                case 3 -> exams.add(task);
            }
        }
        
        try (PrintWriter pw = new PrintWriter(file))
        {
            // Check if any list is empty, if not, write the task to the file
            if (!assignments.isEmpty())
            {
                pw.println("Assignments!");
                for (Task task: assignments)
                {
                    pw.println(task.toString());
                }
            }
            if (!quizzes.isEmpty())
            {
                pw.println("Quizzes!");
                for (Task task: quizzes)
                {
                    pw.println(task.toString());
                }
            }
            if (!exams.isEmpty())
            {
                pw.println("Exams!");
                for (Task task: exams)
                {
                    pw.println(task.toString());
                }
            }
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("Error: File Not Found!"); 
        }   
}

/**
 * This method performs a calculation of the student's GPA, both a percentage 
   and a grade point to 2 decimal places
 * @param assignments the array list of Task objects specifically for 
   assignments
 * @param quizzes the array list of Task objects specifically for quizzes
 * @param exams the array list of Task objects specifically for exams
 * @param decimal the decimal formatter
 * @param file the "CourseInfo.txt" file
 * @param stats the object that holds information about the student's current 
   grade
 */
public static void calculateGPA(ArrayList<Task> assignments, ArrayList<Task> 
        quizzes, ArrayList<Task> exams, DecimalFormat decimal, File file, 
        GradeInfo stats)
{
    double totalWeight = 0; 
    double weightedSum = 0;
    
    // Create an array list for combining all the tasks
    ArrayList<Task> allTasks = new ArrayList<>(); 
    allTasks.addAll(assignments);
    allTasks.addAll(quizzes);
    allTasks.addAll(exams);
    
    for (Task task : allTasks) // Iterate through the tasks
    {
        double weight = task.getWeight() / 100.0; 
        double grade = task.getGrade();
            
        weightedSum += weight * grade;
        totalWeight += weight;
    }
    
    stats.totalWeight = totalWeight; // Actually set the totalWeight
    
    if (stats.totalWeight == 0) 
    {
        System.out.println("\nTotal weight is zero, cannot calculate GPA.");
    } 
    else 
    {
        stats.weightedAverage = weightedSum / stats.totalWeight;
        // Round the average as TMU uses whole-number grades for their scale
        int roundedAverage = (int) Math.round(stats.weightedAverage);
        double gradePoint = findGradePoint(roundedAverage);
        
        // Try the writing using the file writer so new info is appended
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true)))
        {
            pw.println("Total weighted sum: " + 
                    decimal.format(weightedSum));
            pw.println("Total weight: " + stats.totalWeight);
            pw.println(); // Skip a line for neatness
            pw.println("Your GPA for the course is: %" + 
                decimal.format(stats.weightedAverage));
            pw.println("In gradepoints, your GPA is " + gradePoint);
        }
        catch(IOException e)
        {
            System.out.println("Error: File Not Found!");
        }
    }
}

public static double findGradePoint(double average)
{
    double gradePoint;
    
    // Use the TMU grade-point scale to set the variable 
    
    if (average >= 90 && average <= 100)
    {
        gradePoint = 4.33;
    }
    else if (average >= 85 && average <= 89)
    {
        gradePoint = 4.00;
    }
    else if (average >= 80 && average <= 84)
    {
        gradePoint = 3.67;
    }
    else if (average >= 77 && average <= 79)
    {
        gradePoint = 3.33;
    }
    else if (average >= 73 && average <= 76)
    {
        gradePoint = 3.00;
    }
    else if (average >= 70 && average <= 72)
    {
        gradePoint = 2.67;
    }
    else if (average >= 67 && average <= 69)
    {
        gradePoint = 2.33;
    }
    else if (average >= 63 && average <= 66)
    {
        gradePoint = 2.00;
    }
    else if (average >= 60 && average <= 62)
    {
        gradePoint = 1.67;
    }
    else if (average >= 57 && average <= 59)
    {
        gradePoint = 1.33;
    }
    else if (average >= 53 && average <= 56)
    {
        gradePoint = 1.00;
    }
    else if (average >= 50 && average <= 52)
    {
        gradePoint = 0.67;
    }
    else
    {
        gradePoint = 0;
    }
    return gradePoint;
}

/**
 * This method uses the student's remaining weight to see a minimum of what 
   they need to pass the course (in TMU, a pass is 50%)
 * @param stats the object containing the total weight and the weighted average
 * @param decimal the decimal formatter
 */
public static void calculateGradeNeeded(GradeInfo stats, DecimalFormat decimal)
{
    double remainingWeight = 1 - stats.totalWeight;
    double passingGrade = 50;
    
    double currentContribution = stats.weightedAverage * stats.totalWeight;
    double gradeNeededToPass = (passingGrade - currentContribution) / remainingWeight;
    
    // Make the student doesn't have a full course load
    if (stats.totalWeight == 1.00) 
    {
        System.out.println("You already have a full load of 100%!");
    }
    /* 
        The calculation can produce a negative number or a 0 which means the 
        student has more than enough to pass the course
    */
    else if (gradeNeededToPass <= 0) 
    {
        System.out.println("\nYou already have enough to pass the course!");
    }
    else
    {
        System.out.println("\nYou will need a minimum of %" + 
                decimal.format(gradeNeededToPass ) + " to pass the "
                + "course!");
    }
}

/**
 * This method displays a report for the student by reading from the 
   "Courses.txt" file
 * @param file the "Courses.txt" file
 */
public static void viewReport(File file)
{
    String output = ""; // Create an empty string to append to afterward
    try (Scanner fileScanner = new Scanner(file))
    {
        while (fileScanner.hasNextLine())
        {
            output += fileScanner.nextLine() + "\n"; // Neatly print out lines
        }
    }
    catch (FileNotFoundException e)
    {
        System.out.println("Error: File Not Found!");
    }
    
    System.out.println(output.trim()); // Trim the last newline character
}}
