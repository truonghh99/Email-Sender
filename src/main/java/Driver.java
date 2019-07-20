import javax.mail.MessagingException;
import java.util.List;
import java.util.Collections;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Scanner;


public class Driver {
    public static void main(String... args) throws MessagingException, IOException, GeneralSecurityException {
        String newline = System.getProperty("line.separator");
        String host = "smtp.gmail.com";
        String port = "587";
        Scanner scnr = new Scanner(System.in);

        System.out.println("Sheet ID: ");
        String sheetID = scnr.nextLine();
        System.out.println("Starting cell: ");
        String starting = scnr.nextLine();
        System.out.println("Ending cell: ");
        String ending = scnr.nextLine();
        System.out.println("Column containing sender's emails: ");
        char senderRow = scnr.nextLine().charAt(0);
        System.out.println("Column containing sender's passwords: ");
        char passRow = scnr.nextLine().charAt(0);
        System.out.println("Column containing receiver's emails: ");
        char receiverRow = scnr.nextLine().charAt(0);
        System.out.println("Subject: ");
        String subject = scnr.nextLine();
        System.out.println("Template: ");
        String template = "";
        while (scnr.hasNext()) {
            String line = scnr.nextLine();
            if (line.equals("end")) break;
            template += line;
        }

        List<List<Object>> values = SheetsCrawler.getInfo(sheetID, starting, ending);

        Boolean agreement = false;
        for (List row : values) {
            final String from = row.get((int) senderRow - starting.charAt(0)).toString();
            final String pass = row.get((int) passRow - starting.charAt(0)).toString();
            String to = row.get((int) receiverRow - starting.charAt(0)).toString();
            String bodyText = "";
            for (int i = 0; i < template.length(); ++i) {
                if (template.charAt(i) == '$') {
                    ++i;
                    int col = template.charAt(i) - starting.charAt(0);
                    bodyText += row.get(col);                    
                } else {
                    if (template.charAt(i) == '^') {
                        bodyText += newline;
                    } else {
                        bodyText += template.charAt(i);
                    }
                }
            }
            if (!agreement) {
                System.out.println("Please review your email:\n" + bodyText);
                System.out.println("Ready to send? (Y/N)");
                String answer = scnr.nextLine();
                if (answer.charAt(0) == 'Y') {
                    agreement = true;
                } else {
                    System.out.println("Sending process is cancelled. Please run this program again.");
                    return;
                }
            }
            EmailSender.sendEmail(port, host, to, from, pass, subject, bodyText);
            System.out.println("Email sent successfully to " + to);
        }
        return;
    }
}
