package app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.TimeCard;
import com.scg.util.ListFactory;

/**
 * Assignment 04 application.
 */
public final class InitLists {

	private static FileOutputStream fileOut = null;
	private static ObjectOutputStream objectOut = null;
	final static org.slf4j.Logger log = LoggerFactory.getLogger(InitLists.class);

	/**
	 * Prevent instantiation.
	 */
	private InitLists() {
	}

	/**
	 * Serializes the Client Account List
	 * 
	 * @param listToSerialize
	 *            - Client account list to be serialized.
	 */
	private static void serializeClientAccountList(List<ClientAccount> listToSerialize) {

		try {

			fileOut = new FileOutputStream("ClientList.ser");
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(listToSerialize);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) {
					log.error("Serialization of timecard list failed. ", e);
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Serializes the Timecard List
	 * 
	 * @param listToSerialize
	 *            - Timecard list to be serialized.
	 */
	private static void serializeTimeCardList(List<TimeCard> listToSerialize) {

		try {

			fileOut = new FileOutputStream("TimeCardList.ser");
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(listToSerialize);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) {
					log.error("Serialization of timecard list failed. ", e);

				}
			}

		}

	}

	/**
	 * Create invoices for the clients from the timecards.
	 *
	 * @param accounts
	 *            the accounts to create the invoices for
	 * @param timeCards
	 *            the time cards to create the invoices from
	 *
	 * @return the created invoices
	 */

	/**
	 * The application method.
	 *
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(final String[] args) {

		/**
		 * This class' logger.
		 */

		// Create lists to be populated by factory
		final List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		final List<Consultant> consultants = new ArrayList<Consultant>();
		final List<TimeCard> timeCards = new ArrayList<TimeCard>();
		ListFactory.populateLists(accounts, consultants, timeCards);

		// Serialize accounts
		serializeClientAccountList(accounts);
		System.out.println("Account List Serialized");
		// Serialize timeCards
		serializeTimeCardList(timeCards);
		System.out.println("Timecard List Serialized");
	}
}
