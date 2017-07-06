import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Clinica {

	private Connection conn = null;

	private void showMenu() {

		int option = -1;
		do {
			System.out.println("Bienvenido a CLINICA\n");
			System.out.println("Selecciona una opción:\n");
			System.out.println("\t1. Obtener la lista completa de doctores que hay en la base de datos.");
			System.out.println("\t2. Obtener la lista de pacientes disponibles.");
			System.out.println("\t3. Obtener la lista de enfermedades que hay en la base de datos.");
			System.out.println("\t4. Obtener la lista de fármacos disponibles.");
			System.out.println("\t5. Obtener los pacientes de un doctor dado.");
			System.out.println("\t6. Obtener el diagnóstico de un paciente determinado.");
			System.out.println("\t7. Obtener cuantas veces una enfermedad ha sido diagnosticada (y la distribución de pacientes que han sufrido dicha enfermedad y cuantas veces).");
			System.out.println("\t8. Obtener la lista de enfermedades que pueden ser tratadas con un fármaco concreto.");
			System.out.println("\t9. Añadir un nuevo paciente y asociarle una o varias enfermedades.");
			System.out.println("\t10. Salir.");
			try {
				option = readInt();
				switch (option) {
				case 1:
					obtenerDoctores();
					break;
				case 2:
					obtenerPacientes();
					break;
				case 3:
					obtenerEnfermedades();
					break;
				case 4:
					obtenerFarmacos();
					break;
				case 5:
					obtenerPacientesDeUnDoctor();
					break;
				case 6:
					obtenerDiagnosticoDeUnPaciente();
					break;
				case 7:
					obtenerDistribucionDiagnosticoEnfermedad();
					break;
				case 8:
					obtenerEnfermedadesTratadasConFarmaco();
					break;
				case 9:
					anadirPacienteYDiagnosticos();
					break;
				}
			} catch (Exception e) {
				System.err.println("Opción introducida no válida!");
			}
		}
		while (option != 10);
		exit();
	}

	private void exit(){

		try{
			if (conn != null){
				conn.close();
			}

			System.out.println("Desonectado de la base de datos");
		}

		catch (Exception e){
			System.err.println("Error al desconectar de la BD: " + e.getMessage());
		}

		System.out.println("Saliendo.. ¡hasta otra!");
		System.exit(0);
	}

	private void conectar() {

		try{
			String drv = "com.mysql.jdbc.Driver";
			Class.forName(drv);
			String serverAddress = "localhost:3306";
			String db = "clinica";
			String user = "clinica";
			String pass = "clicli_pwd";
			String url = "jdbc:mysql://" + serverAddress + "/" + db;		
			conn = DriverManager.getConnection(url, user, pass);
			System.out.println("Conectado a la base de datos!");
		} 

		catch (Exception e){
			System.err.println("Error al conectar a la BD: " + e.getMessage());
		}
	}

	private void anadirPacienteYDiagnosticos() {
		//implementar
	}

	private void obtenerEnfermedadesTratadasConFarmaco() {

		int option = -1;
		do {
			farmacoAux();
			System.out.println("\tPor favor, seleccione el ID del farmaco a consultar \n\tPulse 0 para salir del menú de opciones");

			try {
				Statement st = conn.createStatement();
				option = readInt();

				switch (option) {
				default:
					ResultSet rs = st.executeQuery("SELECT EN.nombre FROM enfermedad EN, trata TR, medicamento M"
							+ " WHERE EN.id = TR.id_enfermedad AND M.id = TR.id_medicamento AND M.id ="+option);

					System.out.println("\n\tEl medicamento elegido trata las siguientes enfermedades:\n");

					while (rs.next()) {
						String nombreEnf = rs.getString("EN.nombre");
						System.out.println("\t " + nombreEnf);
					}

					System.out.println("\n");
					st.close();
					break;
				}


			} catch (Exception e) {
				System.err.println("Opción introducida no válida!");
			}
		}
		while (option != 0);
	}

	private void farmacoAux(){

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  id, nombre FROM medicamento");
			System.out.println("\n\tMedicamentos: \n");

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				System.out.println("\tID: " + id + "\tFármaco: " + nombre);
			}

			System.out.println("\n");
			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerDistribucionDiagnosticoEnfermedad() {

		int option = -1;
		do {
			enfermedadesAux();
			System.out.println("\tPor favor, seleccione el ID de la enfermedad a consultar \n\tPulse 0 para salir del menú de opciones");

			try {
				Statement st = conn.createStatement();
				option = readInt();

				switch (option) {
				default:
					ResultSet rs = st.executeQuery("SELECT PA.nombre FROM enfermedad EN, paciente PA, diagnostica DI "
							+ "WHERE PA.id = DI.id_paciente AND EN.id = DI.id_enfermedad AND EN.id ="+option);

					System.out.println("\n\tDistribución de la enfermedad:\n");
					int i = 0;

					while (rs.next()) {
						i++;
						String nombrePaciente = rs.getString("PA.nombre");
						System.out.println("\t " + nombrePaciente);
					}

					System.out.println("\n\tLa enfermedad ha sido diagnosticada " + i + " veces");
					System.out.println("\n");
					st.close();
					break;
				}


			} catch (Exception e) {
				System.err.println("Opción introducida no válida!");
			}
		}
		while (option != 0);
	}

	private void enfermedadesAux(){

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  id, nombre FROM enfermedad");
			System.out.println("\n\tEnfermedades: \n");

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				System.out.println("\tID: " + id + "\tEnfermedad: " + nombre);
			}

			System.out.println("\n");
			st.close();
		}

		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerDiagnosticoDeUnPaciente() {

		int option = -1;
		do {
			obtenerPacienteApoyo();
			System.out.println("\n\t0. Salir del menú");

			try {
				option = readInt();

				switch (option) {
				default:
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery("SELECT EN.nombre, DI.fecha FROM paciente PA, enfermedad EN, diagnostica DI"
							+ " WHERE EN.id = DI.id_enfermedad AND PA.id = DI.id_paciente AND PA.id="+option);

					while (rs.next()) {
						String nombre = rs.getString("EN.nombre");
						String fecha = rs.getString("DI.fecha");
						System.out.println("\tDiagnostico: " + nombre + "\t\t\t" + "Fecha del diagnóstico: " + fecha);
					}

					System.out.println("\n");
					st.close();
					break;
				}


			} catch (Exception e) {
				System.err.println("Opción introducida no válida!");
			}
		}
		while (option != 0);
	}

	private void obtenerPacienteApoyo(){

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  id, nombre FROM paciente");
			System.out.println("\n\tPacientes:\n");

			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("nombre");
				System.out.println("\tID: " + id + "\tNombre: " + firstName);
			}
			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerPacientesDeUnDoctor() {

		int option = -1;
		do {
			obtenerDoctores();
			System.out.println("\tPor favor, seleccione el ID del doctor a consultar \n\tPulse 0 para salir del menú de opciones");

			try {
				Statement st = conn.createStatement();
				option = readInt();

				switch (option) {
				default:
					ResultSet rs = st.executeQuery("SELECT nombre FROM paciente WHERE id_doctor ="+option);

					while (rs.next()) {
						String nombre = rs.getString("nombre");
						System.out.println("\tPaciente: " + nombre);
					}

					System.out.println("\n");
					st.close();
					break;
				}


			} catch (Exception e) {
				System.err.println("Opción introducida no válida!");
			}
		}
		while (option != 0);
	}

	private void obtenerFarmacos() {

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  M.id, M.nombre, IA.nombre, EX.nombre FROM medicamento M, principio_activo IA, tiene_excipiente TE, excipiente EX"
					+ " WHERE IA.id = M.id_principio_activo AND TE.id_excipiente = EX.id AND TE.id_medicamento = M.id");

			System.out.println("\n\tMedicamentos: \n");

			while (rs.next()) {
				int id = rs.getInt("M.id");
				String nombreMedicamento = rs.getString("M.nombre");
				String ingActivo = rs.getString("IA.nombre");
				String nombreExcipiente = rs.getString("EX.nombre");
				System.out.println("\n\tID: " + id + "\n\tFármaco: " + nombreMedicamento + "\n\tIngrediente activo: " + ingActivo + "\n\tExcipiente: " + nombreExcipiente);
			}

			System.out.println("\n");
			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerEnfermedades() {

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  EN.id, EN.nombre, SIN.nombre FROM enfermedad EN , sintomas SIN WHERE EN.id = SIN.id");
			System.out.println("\n\tEnfermedades: \n");

			while (rs.next()) {
				int id = rs.getInt("EN.id");
				String nombre = rs.getString("EN.nombre");
				String sintomas = rs.getString("SIN.nombre");
				System.out.println("\tID: " + id + "\n\tEnfermedad: " + nombre + "\n\tSintomas: " + sintomas + "\n");
			}

			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerPacientes() {

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT  PA.id, PA.nombre, D.nombre FROM paciente PA, doctor D WHERE D.id = PA.id_doctor");

			System.out.println("\n\tPacientes:\n");

			while (rs.next()) {
				int id = rs.getInt("PA.id");
				String firstName = rs.getString("PA.nombre");
				String nomDoctor = rs.getString("D.nombre");
				System.out.println("\tID: " + id + "\n\tNombre: " + firstName + "\n\tNombre Doctor: " + nomDoctor + "\n");
			}

			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private void obtenerDoctores() {

		if(conn==null){
			conectar();
		}

		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM doctor");
			System.out.println("\n\tDoctores\n");

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				System.out.println("\tID: " + id + "\t   Nombre: " + nombre);
			}

			System.out.println("\n");
			st.close();
		}
		catch(Exception e){
			System.err.println("Error al seleccionar a la BD: " + e.getMessage());
		}
	}

	private int readInt()throws Exception {
		try {
			System.out.print("> ");
			return Integer.parseInt(new BufferedReader(new InputStreamReader(
					System.in)).readLine());
		} catch (Exception e) {
			throw new Exception("Not number");
		}
	}

	private String readString()throws Exception {
		try {
			System.out.print("> ");
			return new BufferedReader(new InputStreamReader(
					System.in)).readLine();
		} catch (Exception e) {
			throw new Exception("Error reading line");
		}
	}

	public static void main(String args[]) {
		new Clinica().showMenu();
	}
}
