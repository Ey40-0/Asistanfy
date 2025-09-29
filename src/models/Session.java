package models;

public class Session {
    private static Session instancia;
    private int id;
    private int id_rol;
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
     * @param id ID del usuario.
     * @param id_rol Rol del usuario.
     */
    public Session(int id, int id_rol) {
        this.id = id;
        this.id_rol = id_rol;
    }
    
    /**
     * Inicia una nueva sesión.
     * @param id ID del usuario.
     * @param id_rol Rol del usuario.
     */
    public static void iniciarSesion(int id, int id_rol) {
        instancia = new Session(id, id_rol);
    }

    /**
     * Obtiene la instancia singleton de la sesión.
     * @return La sesión actual.
     */
    public static Session getInstance() {
        return instancia;
    }

    public int getId() {
        return id;
    }

    public int getId_rol() {
        return id_rol;
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
    
    
}