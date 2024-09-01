import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToDoListApp {

    // Task class
    static class Task {
        private String description;
        private boolean isCompleted;

        public Task(String description) {
            this.description = description;
            this.isCompleted = false;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void markAsCompleted() {
            this.isCompleted = true;
        }

        @Override
        public String toString() {
            return (isCompleted ? "[X] " : "[ ] ") + description;
        }
    }

    // ToDoList class
    static class ToDoList {
        private List<Task> tasks;

        public ToDoList() {
            tasks = new ArrayList<>();
        }

        public void addTask(String description) {
            tasks.add(new Task(description));
        }

        public void markTaskAsCompleted(int index) {
            if (index >= 0 && index < tasks.size()) {
                tasks.get(index).markAsCompleted();
            } else {
                System.out.println("Invalid task number.");
            }
        }

        public void deleteTask(int index) {
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
            } else {
                System.out.println("Invalid task number.");
            }
        }

        public void listTasks() {
            if (tasks.isEmpty()) {
                System.out.println("No tasks to show.");
                return;
            }

            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    // Main application class
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ToDoList toDoList = new ToDoList();
        String command;

        while (true) {
            System.out.println("ToDo List Application");
            System.out.println("1. Add Task");
            System.out.println("2. Mark Task as Completed");
            System.out.println("3. Delete Task");
            System.out.println("4. List Tasks");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    toDoList.addTask(description);
                    System.out.println("Task added.");
                    break;
                case "2":
                    toDoList.listTasks();
                    System.out.print("Enter task number to mark as completed: ");
                    int markIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    toDoList.markTaskAsCompleted(markIndex);
                    System.out.println("Task marked as completed.");
                    break;
                case "3":
                    toDoList.listTasks();
                    System.out.print("Enter task number to delete: ");
                    int deleteIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    toDoList.deleteTask(deleteIndex);
                    System.out.println("Task deleted.");
                    break;
                case "4":
                    toDoList.listTasks();
                    break;
                case "5":
                    System.out.println("Exiting application.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
