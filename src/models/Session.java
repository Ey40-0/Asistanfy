package models;

public class Session {
    private static Session instancia;
    
    private Employee employee;
    private int selectedEmployeeId;
    private Test selectedTest;
    private Student selectedStud;

    /**
     * Constructor vacío.
     */
    public Session() {
        
    }

    /**
     * Constructor con ID y rol.
     * @param employee usuario.
     */
    public Session(Employee employee) {
        this.employee = employee;
    }
    
    /**
     * Inicia una nueva sesión.
     * @param employee usuario.
     */
    public static void iniciarSesion(Employee employee) {
        instancia = new Session(employee);
    }

    /**
     * Obtiene la instancia singleton de la sesión.
     * @return La sesión actual.
     */
    public static Session getInstance() {
        return instancia;
    }
    
    /**
     * Cierra la sesión actual, limpiando todas las variables.
     */
    public static void cerrarSesion() {
        if (instancia != null) {
            instancia.selectedEmployeeId = 0;
            instancia.selectedTest = null;
            instancia.selectedStud = null;
        }
        instancia = null;
    }  
    
    public Test getSelectedTest() {
        return selectedTest;
    }

    public void setSelectedTest(Test selectedTest) {
        this.selectedTest = selectedTest;
    }

    public int getSelectedEmployeeId() {
        return selectedEmployeeId;
    }

    public void setSelectedEmployeeId(int selectedEmployeeId) {
        this.selectedEmployeeId = selectedEmployeeId;
    }

    public Student getSelectedStud() {
        return selectedStud;
    }

    public void setSelectedStud(Student selectedStud) {
        this.selectedStud = selectedStud;
    }

    public Employee getEmployee() {
        return employee;
    }
    
}