import java.util.ArrayList;
import java.util.List;

abstract class OrganizationComponent {
    protected String name;

    public OrganizationComponent(String name) {
        this.name = name;
    }

    public abstract void showDetails();
    public abstract int getBudget();
    public abstract int getEmployeeCount();
    public abstract OrganizationComponent findByName(String name);
    public abstract List<String> getAllEmployees();
}

class Employee extends OrganizationComponent {
    private String position;
    private int salary;

    public Employee(String name, String position, int salary) {
        super(name);
        this.position = position;
        this.salary = salary;
    }

    @Override
    public void showDetails() {
        System.out.println("Сотрудник: " + name + ", Должность: " + position + ", Зарплата: " + salary);
    }

    @Override
    public int getBudget() {
        return salary;
    }

    @Override
    public int getEmployeeCount() {
        return 1;
    }

    public void setSalary(int newSalary) {
        this.salary = newSalary;
    }

    @Override
    public OrganizationComponent findByName(String name) {
        return this.name.equals(name) ? this : null;
    }

    @Override
    public List<String> getAllEmployees() {
        List<String> employees = new ArrayList<>();
        employees.add(name);
        return employees;
    }
}

class Contractor extends OrganizationComponent {
    private String position;
    private int fixedPayment;

    public Contractor(String name, String position, int fixedPayment) {
        super(name);
        this.position = position;
        this.fixedPayment = fixedPayment;
    }

    @Override
    public void showDetails() {
        System.out.println("Контрактор: " + name + ", Должность: " + position + ", Оплата: " + fixedPayment);
    }

    @Override
    public int getBudget() {
        return 0; // Оплата контракторов не включается в бюджет отдела
    }

    @Override
    public int getEmployeeCount() {
        return 1;
    }

    @Override
    public OrganizationComponent findByName(String name) {
        return this.name.equals(name) ? this : null;
    }

    @Override
    public List<String> getAllEmployees() {
        List<String> employees = new ArrayList<>();
        employees.add(name);
        return employees;
    }
}

class Department extends OrganizationComponent {
    private List<OrganizationComponent> subordinates = new ArrayList<>();

    public Department(String name) {
        super(name);
    }

    public void add(OrganizationComponent component) {
        subordinates.add(component);
    }

    public void remove(OrganizationComponent component) {
        subordinates.remove(component);
    }

    @Override
    public void showDetails() {
        System.out.println("Отдел: " + name);
        for (OrganizationComponent component : subordinates) {
            component.showDetails();
        }
    }

    @Override
    public int getBudget() {
        int totalBudget = 0;
        for (OrganizationComponent component : subordinates) {
            totalBudget += component.getBudget();
        }
        return totalBudget;
    }

    @Override
    public int getEmployeeCount() {
        int totalEmployees = 0;
        for (OrganizationComponent component : subordinates) {
            totalEmployees += component.getEmployeeCount();
        }
        return totalEmployees;
    }

    @Override
    public OrganizationComponent findByName(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        for (OrganizationComponent component : subordinates) {
            OrganizationComponent found = component.findByName(name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Override
    public List<String> getAllEmployees() {
        List<String> employees = new ArrayList<>();
        for (OrganizationComponent component : subordinates) {
            employees.addAll(component.getAllEmployees());
        }
        return employees;
    }
}

public class Main {
    public static void main(String[] args) {
        Employee employee1 = new Employee("Алия", "Разработчик", 1000);
        Employee employee2 = new Employee("Ержан", "Разработчик", 1200);
        Contractor contractor1 = new Contractor("Болат", "Тестировщик", 800);

        Employee employee3 = new Employee("Айгуль", "Рекрутер", 900);

        Department devDepartment = new Department("Отдел Разработки");
        devDepartment.add(employee1);
        devDepartment.add(employee2);
        devDepartment.add(contractor1);

        Department hrDepartment = new Department("Кадровый отдел");
        hrDepartment.add(employee3);

        Department company = new Department("Компания");
        company.add(devDepartment);
        company.add(hrDepartment);

        System.out.println("Структура компании:");
        company.showDetails();
        System.out.println("Общий бюджет компании: " + company.getBudget());
        System.out.println("Общее количество сотрудников компании: " + company.getEmployeeCount());

        System.out.println("\nИзменение зарплаты сотрудника Ержан...");
        employee2.setSalary(1300);
        System.out.println("Общий бюджет компании после изменения зарплаты: " + company.getBudget());

        String searchName = "Алия";
        OrganizationComponent found = company.findByName(searchName);
        if (found != null) {
            System.out.println("\nНайден сотрудник:");
            found.showDetails();
        } else {
            System.out.println("\nСотрудник " + searchName + " не найден.");
        }

        System.out.println("\nСписок всех сотрудников отдела разработки:");
        List<String> allEmployees = devDepartment.getAllEmployees();
        for (String empName : allEmployees) {
            System.out.println(empName);
        }
    }
}
