package bean;

/**
 * Obycajny bean az na to ze obsahuje atribut message a metodu validate, message
 * sa vo validate nastavi na prislusnu spravu a cez get informuje uzivatela o
 * vysledku...
 * 
 * @author Martin Cuka
 *
 */
public class User {
	private String email = "";
	private String password = "";
	private String message = "";

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Metoda skontroluje vstup od uzivatela, pokial sa nepodarilo prihlasit
	 * vypise adekvatnu spravu
	 * 
	 * @return true - zadal adekvatne heslo a email inak false...
	 */
	public boolean validate() {

		if (!isEmailAndPasswordEntered()) {
			return false;
		}

		if (!isEmailValid()) {
			return false;
		}

		if (!isPasswordValid()) {
			return false;
		}

		return true;
	}

	private boolean isEmailValid() {
		if (!email.matches("\\w+@\\w+\\.\\w+")) {
			message = "Invalid email address";
			return false;
		}

		return true;
	}

	private boolean isPasswordValid() {
		if (password.length() < 8) {
			message = "Password must be at least 8 characters.";
			return false;
		} else if (password.matches("\\w*\\s+\\w*")) {
			message = "Password cannot contain space.";
			return false;
		}

		return true;
	}

	private boolean isEmailAndPasswordEntered() {
		if (email == null) {
			message = "Invalid email address";
			return false;
		}

		if (password == null) {
			message = "Invalid password";
			return false;
		}

		return true;
	}
}
