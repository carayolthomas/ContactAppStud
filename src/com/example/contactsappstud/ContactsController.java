package com.example.contactsappstud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import android.os.Environment;
import android.util.Log;

/**
 * This class in made in order to retrieve JSON files for all the contacts
 * 
 * @author Thomas
 * 
 */
public class ContactsController {

	private static final String CONTACT_URL_1 = "http://www.appstud.me/test_recrutement/contacts.json";
	private static final String CONTACT_URL_2 = "http://touchtz.com/data/contacts.json";

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private ArrayList<Contact> contactList = null;
	private Contacts contacts = null;
	private File jsonOutputFile;
	private File jsonFile;

	public ContactsController() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonFactory = new JsonFactory();
	}

	public void init() {
		downloadJsonFile();
		try {
			jp = jsonFactory.createJsonParser(jsonFile);
			contacts = objectMapper.readValue(jp, Contacts.class);
			contactList = contacts.get("contacts");
			Log.e("CHECK", contactList.size() + " ---------------------------------------------------");
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void downloadJsonFile() {
		try {
			createFileAndDirectory();
			URL url1 = new URL(CONTACT_URL_1);
			URL url2 = new URL(CONTACT_URL_2);
			HttpURLConnection urlConnection;
			urlConnection = (HttpURLConnection) url1.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			FileOutputStream fileOutput = new FileOutputStream(jsonFile);
			InputStream inputStream = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			/*TODO : Test retrieve with url2 */
		}
	}

	private void createFileAndDirectory() throws FileNotFoundException {
		final String extStorageDirectory = Environment
				.getExternalStorageDirectory().toString();
		final String meteoDirectory_path = extStorageDirectory
				+ "/contactsappstud";
		jsonOutputFile = new File(meteoDirectory_path, "/");
		if (jsonOutputFile.exists() == false)
			jsonOutputFile.mkdirs();
		jsonFile = new File(jsonOutputFile, "contactsappstud.json");
	}

	public ArrayList<Contact> findAll() {
		return contactList;
	}

	public Contact findById(int id) {
		return contactList.get(id);
	}
}
