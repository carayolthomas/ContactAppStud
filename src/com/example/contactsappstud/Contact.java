package com.example.contactsappstud;

/**
 * This class describe a contact
 * @author Thomas
 *
 */

public class Contact {
	
	/** Lastname */
	private String nom;
	
	/** Firstname */
	private String prenom;
	
	/** Photo url */
	private String picture;
	
	/** Email */
	private String email;

	public Contact() {
	}
	
	/** Constructor using fields */
	public Contact(String contactLastName, String contactFirstName,
			String contactPhotoUrl, String email) {
		super();
		this.nom = contactLastName;
		this.prenom = contactFirstName;
		this.picture = contactPhotoUrl;
		this.email = email;
	}

	/**
	 * Getters & Setters
	 */
	
	public String getContactLastName() {
		return nom;
	}

	public void setContactLastName(String contactLastName) {
		this.nom = contactLastName;
	}

	public String getContactFirstName() {
		return prenom;
	}

	public void setContactFirstName(String contactFirstName) {
		this.prenom = contactFirstName;
	}

	public String getContactPhotoUrl() {
		return picture;
	}

	public void setContactPhotoUrl(String contactPhotoUrl) {
		this.picture = contactPhotoUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return prenom + " " + nom.toUpperCase();
	}
	
	

}
