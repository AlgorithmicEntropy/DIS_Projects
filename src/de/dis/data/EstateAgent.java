package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Makler-Bean
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE estate_agents (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class EstateAgent {

	// TODO Sollte man ggf. auch noch auf AbstractDataObject umstellen.

	private int id = -1;
	private String name;
	private String address;
	private String login;
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static List<EstateAgent> getAll() {
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();
			String selectSQL = "SELECT id FROM estate_agents";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();
			var agents = new ArrayList<EstateAgent>();
			while (rs.next()) {
				var id = rs.getInt(1);
				agents.add(EstateAgent.load(id));
			}
			rs.close();
			pstmt.close();
			return agents;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static EstateAgent load(int id) {
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();
			String selectSQL = "SELECT * FROM estate_agents WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				EstateAgent agent = new EstateAgent();
				agent.setId(id);
				agent.setName(rs.getString("name"));
				agent.setAddress(rs.getString("address"));
				agent.setLogin(rs.getString("login"));
				agent.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
				return agent;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static EstateAgent fromLogin(String login) {
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();
			String selectSQL = "SELECT id FROM estate_agents WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				rs.close();
				pstmt.close();

				return EstateAgent.load(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

		/**
		 * Speichert den Makler in der Datenbank. Ist noch keine ID vergeben
		 * worden, wird die generierte Id von der DB geholt und dem Model übergeben.
		 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit später generierte IDs zurückgeliefert werden!
				String insertSQL = "INSERT INTO estate_agents(name, address, login, password) VALUES (?, ?, ?, ?) RETURNING id";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate_agents SET name = ?, address = ?, login = ?, password = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.setInt(5, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			Connection con = DbConnectionManager.getInstance().getConnection();
			String sql = "DELETE FROM estate_agents WHERE id = ?";
			var stm = con.prepareStatement(sql);
			stm.setInt(1, this.id);
			stm.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "EstateAgent{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
