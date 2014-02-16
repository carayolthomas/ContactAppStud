package utils;

public class ItemContact {

	/** The text of a row */
	private String mItemContactText;

	/** The icon of a row */
	private int mItemContactIconFile;

	/**
	 * Constructor used
	 * 
	 * @param pItemContactText
	 * @param pItemContactIconFile
	 */
	public ItemContact(String pItemContactText, int pItemContactIconFile) {
		super();
		this.mItemContactText = pItemContactText;
		this.mItemContactIconFile = pItemContactIconFile;
	}

	/**
	 * Return the text of the row
	 * 
	 * @return string
	 */
	public String getItemRideText() {
		return mItemContactText;
	}

	/**
	 * Set the text of a row
	 * 
	 * @param pItemContactText
	 */
	public void setItemContactText(String pItemContactText) {
		this.mItemContactText = pItemContactText;
	}

	/**
	 * Return the icon file of the row
	 * 
	 * @return int
	 */
	public int getItemContactIconFile() {
		return mItemContactIconFile;
	}

	/**
	 * Set the icon file of a row
	 * 
	 * @param pItemContactIconFile
	 */
	public void setItemContactIconFile(int pItemContactIconFile) {
		this.mItemContactIconFile = pItemContactIconFile;
	}

}