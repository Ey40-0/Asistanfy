package models;

import org.mindrot.jbcrypt.BCrypt;

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
    
    
    
    /**
     * Hashea la contraseña.
     * @param plainPassword
     * @return 
     */
    public static String hashPassword(String plainPassword) {
        int cost = 12; // puedes usar 10-14, depende del rendimiento que busques
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(cost));
    }

    /**
     * Verifica contraseña (login).
     * @param plainPassword
     * @param hashedPassword
     * @return 
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.length() < 20) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al verificar contraseña: hash inválido: " + e.getMessage());
            return false;
        }
    }
    
}