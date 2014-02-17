package com.example.contactsappstud;

/**
 * This class describe a contact
 * @author Thomas
 *
 */

public class Contact implements Comparable<Contact>{
	
	/** nom */
	private String nom;
	
	/** prenom */
	private String prenom;
	
	/** Photo url */
	private String picture;
	
	/** Email */
	private String email;

	/** Default constructor */
	public Contact() {
	}
	
	/**
	 * Constructor using fields
	 * @param contactLastName
	 * @param contactFirstName
	 * @param contactPhotoUrl
	 * @param email
	 */
	public Contact(String contactLastName, String contactFirstName,
			String contactPhotoUrl, String email) {
		super();
		this.nom = contactLastName;
		this.prenom = contactFirstName;
		this.picture = contactPhotoUrl;
		this.email = email;
	}


	/**
	 * getContactLastName
	 * @return ContactLastName
	 */
	public String getContactLastName() {
		return nom;
	}

	/**
	 * setContactLastName
	 * @param contactLastName
	 */
	public void setContactLastName(String contactLastName) {
		this.nom = contactLastName;
	}

	/**
	 * getContactFirstName
	 * @return ContactFirstName
	 */
	public String getContactFirstName() {
		return prenom;
	}

	/**
	 * setContactFirstName
	 * @param contactFirstName
	 */
	public void setContactFirstName(String contactFirstName) {
		this.prenom = contactFirstName;
	}

	/**
	 * getContactPhotoUrl
	 * @return ContactPhotoUrl
	 */
	public String getContactPhotoUrl() {
		return picture;
	}

	/**
	 * setContactPhotoUrl
	 * @param contactPhotoUrl
	 */
	public void setContactPhotoUrl(String contactPhotoUrl) {
		this.picture = contactPhotoUrl;
	}

	/**
	 * getEmail
	 * @return Email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * setEmail
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return prenom + " " + nom.toUpperCase();
	}

	@Override
	public int compareTo(Contact another) {
		/** In order to sort all the names in the list */
		return this.nom.compareTo(another.nom);
	}
}
