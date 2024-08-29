/*
 * A class for containing information about the student's task (its title, weight, and grade)
 */

public class Task {
    
    private String title;
    private double weight;
    private double grade;
    
    public Task(String title, double weight, double grade)
    {
        this.title = title;
        this.weight = weight;
        this.grade = grade;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public double getGrade()
    {
        return grade;
    }
    
    public String toString()
    {
        return "Title: " + title + "\n" + "Weight: " + weight + "\n" + 
                "Grade: " + grade + "\n";
    }
}
