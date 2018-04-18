package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.domain.ClientAccount;
import com.scg.domain.Invoice;
import com.scg.domain.TimeCard;
import com.scg.util.DateRange;
import com.scg.util.ListFactory;
import com.scg.util.TimeCardListUtil;

/**
 * Assignment 04 application.
 */
public final class Assignment05 {
	/** The invoice month. */
	private static final Month INVOICE_MONTH = Month.MARCH;

	/** The test year. */
	private static final int INVOICE_YEAR = 2006;

	/** This class' logger. */
	private static final Logger log = LoggerFactory.getLogger("Assignment04");

	/**
	 * Prevent instantiation.
	 */
	private Assignment05() {
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
	private static List<Invoice> createInvoices(final List<ClientAccount> accounts, final List<TimeCard> timeCards) {
		final List<Invoice> invoices = new ArrayList<Invoice>();

		final List<TimeCard> timeCardList = TimeCardListUtil.getTimeCardsForDateRange(timeCards,
				new DateRange(INVOICE_MONTH, INVOICE_YEAR));
		for (final ClientAccount account : accounts) {
			final Invoice invoice = new Invoice(account, INVOICE_MONTH, INVOICE_YEAR);
			invoices.add(invoice);
			for (final TimeCard currentTimeCard : timeCardList) {
				invoice.extractLineItems(currentTimeCard);
			}
		}

		return invoices;
	}

	/**
	 * Print the invoice to a PrintStream.
	 *
	 * @param invoices
	 *            the invoices to print
	 * @param out
	 *            The output stream; can be System.out or a text file.
	 */
	private static void printInvoices(final List<Invoice> invoices, final PrintStream out) {
		for (final Invoice invoice : invoices) {
			out.println(invoice.toReportString());
		}
	}

	/**
	 * The application method.
	 *
	 * @param args
	 *            Command line arguments.
	 */
	@SuppressWarnings("unchecked")
	public static void main(final String[] args) {

		// Create lists to be populated by deserialization process.
		List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		List<TimeCard> timeCards = new ArrayList<TimeCard>();

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ClientList.ser"));
			accounts = (ArrayList<ClientAccount>) in.readObject();
			in.close();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("TimeCardList.ser"));
			timeCards = (ArrayList<TimeCard>) in.readObject();
			in.close();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// De-Serialize

		// Print them
		ListFactory.printTimeCards(timeCards, System.out);

		// Create the Invoices
		final List<Invoice> invoices = createInvoices(accounts, timeCards);
		// Print them
		System.out.println();
		System.out.println("==================================================================================");
		System.out.println("=============================== I N V O I C E S ==================================");
		System.out.println("==================================================================================");
		System.out.println();
		printInvoices(invoices, System.out);
		// Now print it to a file
		PrintStream writer;
		try {
			writer = new PrintStream(new FileOutputStream("invoice.txt"));
			printInvoices(invoices, writer);
		} catch (final IOException ex) {
			log.error("Unable to print invoice.", ex);
		}
	}
}
