import java.io.*;
import java.util.*;

class IDException extends Exception{
	public String error() {
		return "Inputed student ID is either an invalid format or is already registered in the system. \n Returning to submenu . . .";
	}
}
    
public class ProjectDriver {
	private static String mainMenu() {
		// Local Variables
		String option;
		Scanner scanner = new Scanner(System.in);
		
		// Sets main menu for user
		System.out.println("\n Choose one of the following option . . . \n");
		System.out.println(" 1 - Student Management \n");
		System.out.println(" 2 - Course Management \n");
		System.out.println(" 0 - Exit Program \n \n");
		
		System.out.println("Enter your selection: ");
		option = scanner.nextLine();
		
		// returns user's choice of the menu
		return option;
	}
	
	private static String sMenu() {
		// Local Variables
		String opt;
		Scanner scanner = new Scanner(System.in);
		
		// Sets student menu for user
		System.out.println("\n Choose one of the following options . . . \n");
		System.out.println(" A - Search Add a student \n");
		System.out.println(" B - Delete a student \n");
		System.out.println(" C - Print Fee Invoice \n");
		System.out.println(" D - Print List of Students \n");
		System.out.println(" X - Exit to Main Menu \n \n");
		
		System.out.println("Enter your selection: ");
		opt = scanner.nextLine();
		
		// returns user's choice of the menu		
		return opt;
	}
	
	
	@SuppressWarnings({ "finally", "resource" })
	private static void snAddStudent(College c) {
		// Local Variables
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Enter Student's ID: ");
		String tmpId = scanner.nextLine();
		int flag = 0;
			
		// Checks to make sure entered id is valid	
		try {
			if((c.idCheck(tmpId)) == false) {
				// id found to be improper format
				throw new IDException();
			}
			else if(c.searchById(tmpId) == true){
				// id already exists within list of students
				throw new IDException();
			}
		}
		catch(IDException error) {
			// error above is thrown so code exits
			System.out.println("ERROR: " + error.error());
			flag++;
		}
		finally {
			if(flag == 0) {
				// More local variables
				String info = new String();
				System.out.println("Student Type (PhD, MS, or Undergrad):");
				String tmpType = scanner.nextLine();
				
				// Checks to find type of student to add and adds info accordingly
				switch(tmpType) {
				case "PhD":
					System.out.println("Enter Remaining Information: ");
					info = scanner.nextLine();
					String [] infoArr = info.split("\\|");;
					Student student = new PhdStudent(tmpType, infoArr[0], tmpId, infoArr[1], infoArr[2], infoArr[3].split(","));
					c.enrollStudent(student);
					System.out.println("[ " + infoArr[0] + " ] added!");
					break;
				case "MS":
					System.out.println("Enter Remaining Information: ");
					info = scanner.nextLine();
					infoArr = info.split("\\|");
					student = new MsStudent(tmpType, infoArr[0], tmpId, infoArr[1].split(","));
					c.enrollStudent(student);
					System.out.println("[ " + infoArr[0] + " ] added!");
					break;
				case "Undergrad":
					System.out.println("Enter Remaining Information: ");
					info = scanner.nextLine();
					infoArr = info.split("\\|");
					student = new UndergraduateStudent(tmpType, infoArr[0], tmpId, infoArr[1], infoArr[2], infoArr[3].split(","));
					c.enrollStudent(student);
					System.out.println("[ " + infoArr[0] + " ] added!");
					break;
				default:
					System.out.println("Invalid Option Entered: Returning to SubMenu");
					break;
			}
			return;
		}
	}
}
	
	private static void delStudent(College c) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Enter Student's ID: ");
		String tmpId = scanner.nextLine();
		if((c.searchById(tmpId)) == false){
			// ID does not exist within the system, so print an error
			System.out.println("Sorry, " + tmpId + " is not an enrolled student.");
			return;
		}
		else
		{
			// No error finding student so delete
			c.delete(tmpId);
		}
		return;
	}
	
	private static void printInvoiceFunct(College c, String [][] lecFile) {
		// Local Variables
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Enter Student's ID: ");
		String tmpId = scanner.nextLine();
		
		if((c.searchById(tmpId)) == false){
			// ID does not exist within the system, so print an error
			System.out.println("Sorry, " + tmpId + " is not an enrolled student.");
			return;
		}
		else
		{
			// No error finding student so print their invoice
			c.invoice(tmpId, lecFile);
		}
		return;
	}
	
	private static String cMenu() {
		// Local Variables
		String opt;
		Scanner scanner = new Scanner(System.in);
		
		// Sets class submenu for user
		System.out.println("\n Choose one of the following options . . . \n");
		System.out.println(" A - Search for a Class / Lab Using Course Number \n");
		System.out.println(" B - Delete a Class \n");
		System.out.println(" C - Add a Lab to a Class \n");
		System.out.println(" X - Exit to Main Menu \n \n");
		
		System.out.println("Enter your selection: ");
		opt = scanner.nextLine();
		
		// returns user's choice of the menu
		return opt;
	}
	
	private static void clSearch(String [][] lecFile) {
		// Local variables
		Scanner scanner = new Scanner(System.in);
		String lNum;
		
		System.out.println("Enter the Lecture/Lab Number: ");
		lNum = scanner.nextLine();
		for(int i = 0; i < lecFile.length; i++)
		{
			// Search for the class in the list
			if(lecFile[i][0] != null) {
				if(lecFile[i][0].equals(lNum) == true) {
					if(lecFile[i].length == 2) {
						// Course found is a lab, so print proper info
						System.out.println("Lab [" + lecFile[i][0] + "]");
						System.out.println("Lab Room [" + lecFile[i][1] + "]");
						return;
					}
					else {
						// Course found is a lecture, so print proper info
						System.out.println("Lecture [" + lecFile[i][0] + "," + lecFile[i][1] + "," + lecFile[i][2] + "]");
						if(lecFile[i].length == 8) {
							System.out.println("Lecture Room [" + lecFile[i][5] + "]");
						}
						else {
							System.out.println("Lecture is Online");
						}
						return;
					}
				}
			}	
		}
		System.out.println("Course Not Found. Returning to Submenu");
		return;
	}
	
	@SuppressWarnings("null")
	private static String [][] clDel(College c, String [][] lecFile) {
		// Local Variables
		Scanner scanner = new Scanner(System.in);
		String lNum;
		String [][] tmp = new String [10000][8];
		int j;
		
		System.out.println("Enter the Lecture/Lab Number: ");
		lNum = scanner.nextLine();
		
		// If a student is enrolled
		if(c.classCheck(lNum) == 1) {
			System.out.println("Course Could Not Be Deleted: At least one student is enrolled in this class. Returning to Submenu");
		}
		
		// Searches for class to delete 
		for(int i = 0; i < lecFile.length; i++) {
			if(lecFile[i][0] != null) {
				if(lecFile[i][0].compareTo(lNum) == 0) {
					// Class found so delete
					if(lecFile[i].length == 2) {
						// Output result for lab so user knows what is going on
						System.out.println("Lab [" + lecFile[i][0] + "] Has Been Deleted");
					}
					else {
						// Output result for lecture so user knows what is going on
						System.out.println("[" + lecFile[i][0] + ", " + lecFile[i][1] + ", " + lecFile[i][2] + "] Has Been Deleted");
					}
					
					// Copies lecFile into tmp to erase course
					for(j = 0; j < i; j++) {
						// Add until at course to be deleted
						tmp[j] = lecFile[j];
					}
					i++;	
					
					if(lecFile[j].length > 2 && lecFile[j + 1].length == 2 &&  (i + 1) < lecFile.length) {
						// If deleting a lecture, skip its associated labs so they are deleted too
						j++;
						while(lecFile[j + 1].length == 2 && (j + 1) < lecFile.length) {
							j++;
						}
					}
					while(i < lecFile.length) {
						// Add the rest of the classes from the list
						tmp[j] = lecFile[i];
						i++;
						j++;
					}
					
					// Return new list of classes
					return tmp;
				}
			}
		}
		// Course not found for deletion
		System.out.println("Course Not Found for Deletion. Returning to Submenu");
		return lecFile;
	}
	
	private static String [][] addLab(String [][] lecFile) {
		// Local Variables
		String option;
		Scanner scanner = new Scanner(System.in);
		String [] infoArr;
		String info;
		String lNum;
		String tmpLine;
		String [][] tmp = new String [10000][8];
		int j;
		
		// Allows user to choose which to add
		System.out.println("Enter Lab or Lecture: ");
		option = scanner.nextLine();
		switch(option) {
		case "Lab":
			System.out.println("Enter the Lecture Number to add a lab to: ");
			lNum = scanner.nextLine();
			// Checks to ensure course number is the correct format
			if(lNum.length() == 5) {
				// Searches for course number in the list
				for(int i = 0; i < lecFile.length; i++)
				{
					if(lecFile[i][0].compareTo(lNum) == 0) {
						// Course number found
						if(lecFile[i][2] != null){
							System.out.println(lNum + " is a valid lecture. Please enter lab number & location: ");
							tmpLine = scanner.nextLine();
							infoArr = tmpLine.split(",");
							
							// Adds lecFile to tmp up until the lecture underwhich the lab is being added
							for(j = 0; j < i; j++) {
								tmp[j] = lecFile[j];
							}
							
							// Add new lab in
							tmp[j] = infoArr;
							j++;
							
							while(j < lecFile.length && lecFile[j] != null) {
								// Add the rest of the courses in 
								tmp[j] = lecFile[j];
								j++;
							}
							// return the edited list
							return tmp;
						}
						
					}
				}
				System.out.println("Course number entered is not a valid Lecture. Returning to Submenu");
				return lecFile;
			}
			else {
				System.out.println("Invalid course number entered. Returning to Submenu");
				return lecFile;
			}
	case "Lecture":
		int pos = -1;
		System.out.println("Enter the Lecture Number to add to the course list: ");
		lNum = scanner.nextLine();			
		if(lNum.length() == 5) {
			// Course is inputed in the correct format
			System.out.println(lNum + " is valid. Please enter the rest of the course information: ");
			tmpLine = scanner.nextLine();
			infoArr = tmpLine.split(",");
			for(int i = 0; i < lecFile.length; i++) {
				if(pos == -1 && lecFile[i][0] == null)
				{
					pos = i;
				}
			}
			// add new lecture at the end of the list
			lecFile[pos][0] = lNum;
			lecFile[pos][1] = infoArr[0];
			lecFile[pos][2] = infoArr[1];
			lecFile[pos][3] = infoArr[2];
			lecFile[pos][4] = infoArr[3];
			lecFile[pos][5] = infoArr[4];
			if(infoArr.length > 6) {
				// course is not online & has the full 8 sections
				lecFile[pos][6] = infoArr[5];
				lecFile[pos][7] = infoArr[6];
			}
			System.out.println("Lecture added successfully!");
			return lecFile;
		}
		else {
			System.out.println("Invalid course number entered. Returning to Submenu");
			return lecFile;
		}
	default:
		System.out.println("Invalid Option Entered: Returning to SubMenu");
	}
	return lecFile;
}
	
	public static void main(String[] args) throws FileNotFoundException{
		College valenceCollege = new College();
		
		// File Stuff
		Scanner f = new Scanner(new File("lect.txt"));
		String line = "";
		String [][] lecFile = new String [10000][8];
		int pos = 0;
		
		while(f.hasNextLine()) {
			line = f.nextLine();
			lecFile[pos] = line.split(",");
			pos++;
		}
		
		// Variables
		Scanner scanner = new Scanner(System.in);
		String option = new String();
		String subOp = new String();
		
		// Compares selection to see if user wants to leave or continue
		while(option.compareTo("0") != 0) {
			// Switch statements for the menu
			option = mainMenu();
			switch(option) {
			case "1":
				subOp = ".";
				while(subOp.compareTo("X") != 0) {
					// Switch statements for the menu
					subOp = sMenu();
					switch(subOp) {
					// Goes through options and sends it to the proper section above
					case "A":
						// Search and add student
						snAddStudent(valenceCollege);
						break;
					case "B":
						// Del student
						delStudent(valenceCollege);
						break;
					case "C":
						// print fee invoice
						printInvoiceFunct(valenceCollege, lecFile);
						break;
					case "D":
						// print lists of students
						valenceCollege.sLists();
						break;
					case "X":
						System.out.println("Exiting To Main Menu . . .");
						break;
					default:
						System.out.println("Invalid Selection");
						break;
					}
				}
				break;
			case "2":
				subOp = ".";
				while(subOp.compareTo("X") != 0) {
					// Switch statements for the menu
					subOp = cMenu();
					switch(subOp) {
					// Goes through options and sends it to the proper section above
					case "A":
						// Search for class/lab via course #
						clSearch(lecFile);
						break;
					case "B":
						// Del class & associated labs via course #
						lecFile = clDel(valenceCollege, lecFile);
						break;
					case "C":
						// Add lab to class
						lecFile = addLab(lecFile);
						break;
					case "X":
						System.out.println("Exiting To Main Menu . . .");
						break;
					default:
						System.out.println("Invalid Selection: Please enter a selection option exactly as it is presented");
						break;
					}
				}
				break;
			case "0":
				System.out.println("Goodbye!");
				System.out.println("Exiting System . . .");
				break;
			default:
				System.out.println("Invalid Selection: Please enter a selection option exactly as it is presented");
				break;
			}
		}
		scanner.close();
		return;
	}
	
}

abstract class Student {
	// . . .
	String sName;
	String sId;
	String sType;
	
	public Student ( String name , String id, String type) {
		this.sName = name;
		this.sId = id;
		this.sType = type;
	}
	
	public void setName(String name) {
		this.sName = name;
	}
	public String getName() {
		return this.sName;
	}
	public void setId(String id) {
		this.sId = id;
	}
	public String getId() {
		return this.sId;
	}
	public void setType(String sType) {
		this.sType = sType;
	}
	public String getType() {
		return this.sType;
	}
	
	abstract public void printInvoice(String [][] lecFile);
	
	
}

//---------------------------
class UndergraduateStudent extends Student{
	// Local Variables
	double gpa;
	boolean res;
	String [] undergradCrnsTaken;
	
	public void setGpa (String gpa) {
		this.gpa = Double.parseDouble(gpa);
	}
	public double getGpa() {
		return this.gpa;
	}
	public void setRes(String res) {
		this.res = Boolean.parseBoolean(res);
	}
	public boolean getRes() {
		return this.res;
	}
	public void setCrns(String [] undergradCrnsTaken) {
		this.undergradCrnsTaken = undergradCrnsTaken;
	}
	public String [] getCrns() {
		return this.undergradCrnsTaken;
	}
	
	public UndergraduateStudent(String type, String name , String id , String gpa, String resident, String [] undergradCrnsTaken ) {
		super (name , id, type);
		this.gpa = Double.parseDouble(gpa);
		this.res = Boolean.parseBoolean(resident);
		this.undergradCrnsTaken = undergradCrnsTaken;
	}
	
	private Double priceCalc(String [][] lecFile) {
		Double res = 0.0;
		Double tmp;
		
		// Goes through each class
		for(int i = 0; i < this.undergradCrnsTaken.length; i++)
		{
			// Finds class in list
			for(int j = 0; j < lecFile.length; j++)
			{
				if(this.undergradCrnsTaken[i]!= null && lecFile[j][0] != null) {
					if(this.undergradCrnsTaken[i].compareTo(lecFile[j][0]) == 0) {
						if(lecFile[j][2] != null) {
							if(lecFile[j][7] == null) {
								// Add credits for online class
								tmp = Double.parseDouble(lecFile[j][5]);
								res = res + tmp;
							}
							else if(lecFile[j][7] != null) {
								// Add credits for other type of class
								tmp = Double.parseDouble(lecFile[j][7]);
								res = res + tmp;
							}
						}
					}
				}
			}
		}
		
		// Does arithmetic to get correct amount then returns it
		res = res * 120.25;
		res = res + 35;
		return res;
	}
	
	public void printInvoice(String [][] lecFile) {
		// Variables
		Double price = priceCalc(lecFile); // 120.25 per credit hour
		int tmpCH;
		
		// Print
		System.out.println("VALENCE COLLEGE");
		System.out.println("ORLANDO FL 10101");
		System.out.println("---------------------------------------------");
		System.out.println("");
		System.out.println("Fee Invoice Prepared for Student:");
		System.out.println(this.sId + "-" + this.sName);
		System.out.println("");
		System.out.println("1 Credit Hour = $120.25");
		System.out.println("");
		System.out.println("CRN		CR_PREFIX	CR_HOURS");
		// Iterates through the classes
		for(int i = 0; i < this.undergradCrnsTaken.length; i++)
		{
			for(int j = 0; j < lecFile.length; j++)
			{
				if(this.undergradCrnsTaken[i]!= null && lecFile[j][0] != null) {
					if(this.undergradCrnsTaken[i].compareTo(lecFile[j][0]) == 0) {
						// Finds class in fill & prints out proper info
						if(lecFile[j] != null) {
							if(lecFile[j][2] != null) {
								if(lecFile[j].length == 6) {
									// Print for online class
									tmpCH = (Integer.parseInt(lecFile[j][5]));
									System.out.println(lecFile[j][0] + "		" + lecFile[j][1] + "		" + lecFile[j][5] + "			$ " + (tmpCH * 300) + ".00");
								}
								else if(lecFile[j].length == 8) {
									// Print for other class
									tmpCH = (Integer.parseInt(lecFile[j][7]));
									System.out.println(lecFile[j][0] + "		" + lecFile[j][1] + "		" + lecFile[j][7] + "			$ " + (tmpCH * 300) + ".00");
								}
							}
						}
					}
				}
			}
		}
		// More format print
		System.out.println("");
		System.out.println("			Health & ID Fees $35.00");
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("");
		
		// Calculations for discount
		if(this.gpa >= 3.5 && price >= 500) {
			// Discount applied
			System.out.println("					$ 	" + price);
			System.out.println("					$ -" + (price - (price * 0.25)));
			System.out.println("					----------");
			System.out.println("		Total Payments	$ " + (price * 0.25));
		}
		else {
			// No discount
			System.out.println("		Total Payments	$ " + price);
		}
	}
}

	//---------------------------
abstract class GraduateStudent extends Student {
	
	public GraduateStudent (String type, String name , String id ) {
	//crn is the crn that the grad student is a teaching assistant for
	super ( name , id, type);
	}
}
	
//---------------------------
class PhdStudent extends GraduateStudent {
	// Local Variables
	String advisor;
	String researchSubject;
	String [] labs;
	public PhdStudent (String type, String name, String id , String advisor, String researchSubject , String []infoArr) {
		//crn is the course number that the Phd student is a teaching assistant for
		super (type, name , id);
		this.advisor = advisor;
		this.researchSubject = researchSubject;
		this.labs = infoArr;
	}
	
	private int labCount() {
		return this.labs.length;
	}
	
	public String getAdvisor() {
		return this.advisor;
	}
	public String getSub() {
		return this.researchSubject;
	}
	
	public String [] getLabs() {
		return this.labs;
	}
	
	@Override
	public void printInvoice(String [][] lecFile) {
		System.out.println("VALENCE COLLEGE");
		System.out.println("ORLANDO FL 10101");
		System.out.println("---------------------------------------------");
		System.out.println("");
		System.out.println("Fee Invoice Prepared for Student:");
		System.out.println(this.sId + "-" + this.sName);
		System.out.println("");
		System.out.println("");
		System.out.println("Research");
		System.out.println(this.researchSubject + "			$ -700.00");
		System.out.println("");
		System.out.println("			Health & ID Fees $35.00");
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("");
		
		// Discount application
		int labCount = labCount();
		if(labCount >= 3) {
			// Total discount applied
			System.out.println("		Total Payments	$ 35.00" );
		}
		else if (labCount >= 2) {
			// Partial discount applied
			System.out.println("					$ 	735.00");
			System.out.println("					$ -" + (735 - (735 * 0.5)));
			System.out.println("					----------");
			System.out.println("		Total Payments	$ " + (735 * 0.5));
		}
		else {
			// No discount applied
			System.out.println("		Total Payments	$ 735.00");
		}
	}
}

//---------------------------
class MsStudent extends GraduateStudent {
	// Local Variables
	String [] gradCrnsTaken;
	
	public MsStudent (String type, String name, String id , String [] gradCrnsTaken) {
		super (type, name , id);
		this.gradCrnsTaken = gradCrnsTaken;
	}
	
	public String [] getCrns() {
		return this.gradCrnsTaken;
	}
	
	private int priceCalc(String [][] lecFile) {
		int res = 0;
		int tmp;
	
		// Iterates through the classes and find out the credit hours
		for(int i = 0; i < this.gradCrnsTaken.length; i++)
		{
			for(int j = 0; j < lecFile.length; j++)
			{
				if(this.gradCrnsTaken[i]!= null && lecFile[j][0] != null) {
					if(this.gradCrnsTaken[i].compareTo(lecFile[j][0]) == 0) {
						if(lecFile[j][2] != null) {
							if(lecFile[j][7] == null) {
								// Add credits for online class
								tmp = Integer.parseInt(lecFile[j][5]);
								res = res + tmp;
							}
							else if(lecFile[j][7] != null) {
								// Add credits for other type of class
								tmp = Integer.parseInt(lecFile[j][7]);
								res = res + tmp;
							}
						}
					}
				}
			}
		}
		
		// Arithmetic to get the actual price
		res = res * 300;
		res = res + 35;
		return res;
	}
	
	@Override
	public void printInvoice(String [][] lecFile) {
		int tmpCH;
		System.out.println("VALENCE COLLEGE");
		System.out.println("ORLANDO FL 10101");
		System.out.println("---------------------------------------------");
		System.out.println("");
		System.out.println("Fee Invoice Prepared for Student:");
		System.out.println(this.sId + "-" + this.sName);
		System.out.println("1 Credit Hour = $300.00");
		System.out.println("");
		System.out.println("CRN		CR_PREFIX	CR_HOURS");
		
		// Iterates through classes student is taking
		for(int i = 0; i < (this.gradCrnsTaken.length); i++)
		{
			for(int j = 0; j < lecFile.length; j++)
			{
				// Goes through list of classes to find proper info 
				if(this.gradCrnsTaken[i]!= null && lecFile[j][0] != null) {
					if(this.gradCrnsTaken[i].compareTo(lecFile[j][0]) == 0) {
						// Finds class in fill & prints out proper info
						if(lecFile[j] != null) {
							if(lecFile[j][2] != null) {
								if(lecFile[j].length == 6) {
									// Print for online class
									tmpCH = (Integer.parseInt(lecFile[j][5]));
									System.out.println(lecFile[j][0] + "		" + lecFile[j][1] + "		" + lecFile[j][5] + "			$ " + (tmpCH * 300) + ".00");
								}
								else if(lecFile[j].length == 8) {
									// Print for other class
									tmpCH = (Integer.parseInt(lecFile[j][7]));
									System.out.println(lecFile[j][0] + "		" + lecFile[j][1] + "		" + lecFile[j][7] + "			$ " + (tmpCH * 300) + ".00");
								}
							}
						}
					}
				}
			}
		}
		System.out.println("");
		System.out.println("			Health & ID Fees $35.00");
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("");
		System.out.println("		Total Payments	$ " + priceCalc(lecFile) + ".00");
	}
}

class College{
	private ArrayList<Student> list;
	
	public College() {
		list = new ArrayList<>();
	}
	
	public void enrollStudent (Student s){
		// adds a student to the list
		list.add(s);
	}
	
	public boolean searchById(String studentId) {
		// returns true if studentID is found to be the id of a student in the list
		for(Student s : list)
		{
			if((s.getId().compareTo(studentId)) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public void invoice(String studentId, String [][] lecFile) {
		for(Student s : list)
		{
			// Finds correct student
			if((s.getId().compareTo(studentId)) == 0)
			{
				// Prints given student's invoice
				s.printInvoice(lecFile);
				return;
			}
		}
	}
	
	public boolean idCheck(String tmpId) {
		char [] idChars = tmpId.toCharArray();
		int pos = 0;
		System.out.println(idChars);
		if(idChars.length != 6) {
			// Incorrect length no need to check anything else
			return false;
		}
		
		// Checks each char to ensure proper info is there
		for(char element : idChars) {
			if(pos < 2) {
				if(Character.isLetter(element) == true) {
					pos++;
				}
				else {
					// Pos should be a letter so something is off
					return false;
				}
			}
			else {
				if(Character.isDigit(element) == true) {
					pos++;
				}
				else {
					// Pos should be a number so something is off
					return false;
				}
			}
		}
		
		// All checks above are passed so id is okay 
		return true;
		
	}
	
	public int classCheck(String c) {
		// Checks all students to ensure none of them are taking a given class 
		for(Student s : list)
		{
			if(s.getType().compareTo("Undergrad") == 0) {
				String [] classes = ((UndergraduateStudent) s).getCrns();
				for(int i = 0; i < classes.length; i++) {
					if(classes[i].compareTo(c) == 0);
					{
						return 1;
					}
				}
			}
			else if(s.getType().compareTo("MS") == 0) {
				String [] classes = ((MsStudent) s).getCrns();
				for(int i = 0; i < classes.length; i++) {
					if(classes[i].compareTo(c) == 0)
					{
						return 1;
					}
				}
			}
			else if(s.getType().compareTo("PhD") == 0) {
				String [] lab = ((PhdStudent) s).getLabs();
				for(int i = 0; i < lab.length; i++) {
					if(lab[i].compareTo(c) == 0)
					{
						return 1;
					}
				}
			}
		}
		// No students taking the given class
		return 0;
	}

	public void sLists() {
		
		System.out.println("PhD Students");
		System.out.println("------------------");
		for(Student s : list)
		{
			if(s.getType().compareTo("PhD") == 0) {
				// if student in list of students is PhD print it now 
				System.out.println("	- " + s.getName());
			}
		}
		
		System.out.println("MS Students");
		System.out.println("------------------");
		for(Student s : list)
		{
			if(s.getType().compareTo("MS") == 0) {
				// if student in list of students is MS print it now
				System.out.println("	- " + s.getName());		
			}
		}
		
		System.out.println("Undergrad Students");
		System.out.println("------------------");
		for(Student s : list)
		{
			if(s.getType().compareTo("Undergrad") == 0) {
				// if student in list of students is Undergrad print it now
				System.out.println("	- " + s.getName());		
			}
		}
		return;
	}

	public void delete(String tmpId) {
		// Finds student to remove
		for(Student s : list)
		{
			if(s.getId().compareTo(tmpId) == 0) {
				// Remove found student from the list 
				list.remove(s);	
				return;
			}
		}
	}
}
