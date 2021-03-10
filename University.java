import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.lang.model.util.ElementScanner14;
import javax.print.event.PrintEvent;

public class University {

	private ModuleDescriptor[] moduleDescriptors;

	private Student[] students;

	private Module[] modules;

	private int[] currentIDs;

	private String[] currentCodes;

	private double epsilon = 0.01;

	/**
	 * @return The number of students registered in the system.
	 */
	public int getTotalNumberStudents() {
		return students.length;
	}

	/**
	 * @return The student with the highest GPA.
	 */
	public Student getBestStudent() {

		Student bestStudent = null;
		if (students.length > 0) {
			// Set out first value as the first student
			bestStudent = students[0];
			double currentHighestGPA = students[0].getGPA();
			for (Student s : this.students) {
				if (((s.getGPA() - currentHighestGPA) > 0)) {
					currentHighestGPA = s.getGPA();
					bestStudent = s;
				}
			}
		}
		return bestStudent;
	}

	/**
	 * @return The module with the highest average score.
	 */
	public Module getBestModule() {
		Module bestModule = null;
		if (students.length > 0) {
			// Set out first value as the first student
			bestModule = modules[0];
			double currentHighestAverage = modules[0].getAverageGrade();
			for (Module m : this.modules) {

				if (((m.getAverageGrade() - currentHighestAverage) > 0)) {
					currentHighestAverage = m.getAverageGrade();
					bestModule = m;
				}
			}
		}
		return bestModule;
	}

	/**
	 * @return Gives a student with a given id.
	 */
	public Student findStudent(int id) {
		for (Student s : this.students) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	/**
	 * @return Gives a module with a given code.
	 */
	public ModuleDescriptor findModuleDescriptor(String code) {
		for (ModuleDescriptor mDescriptor : this.moduleDescriptors) {
			if (mDescriptor != null && mDescriptor.getCode().equals(code)) {
				return mDescriptor;
			}
		}
		return null;
	}

	/**
	 * @return A list of students imported from a csv
	 */
	public boolean isIdUnique(int id, int index) {
		for (int i : currentIDs) {
			if (i == id) {
				return false;
			}
		}
		this.currentIDs[index] = id;
		return true;
	}

	/**
	 * @return A list of students imported from a csv
	 */
	public boolean isCodeUnique(String id, int index) {
		for (String i : currentCodes) {
			if (i != null && i.equals(id)) {
				return false;
			}
		}
		this.currentCodes[index] = id;
		return true;
	}

	/**
	 * Adds a module if one doesn't exist
	 * 
	 * @return Returns the index of the module
	 */
	public int addModule(Module testModule) {
		int index = 0;
		for (Module m : this.modules) {

			if (m != null && m.compareModules().equals(testModule.compareModules())) {

				return index;
			}

			if (m == null) {
				this.modules[index] = testModule;

				return index;
			}
			index += 1;
		}
		return -1;
	}

	/**
	 * Adds a list of modules imported from a csv
	 */
	public void importModuleDescriptors() {
		// Creating a empty string and intialise a buffered reader
		String line;
		BufferedReader br = null;

		ModuleDescriptor[] moduleDescriptors = {};

		boolean isEmptyModule = false;

		int failedImports = 0;

		// Running in a try catch to make sure we have no errors
		try {
			// Create the reader and run the lines
			br = new BufferedReader(new FileReader("module_descriptors.csv"));

			// Get the total number of users as one less than the line length
			int totalModules = 0;
			totalModules = (int) (br.lines().count()) - 1;

			// As we use the line count we need two seperate BufferedReaders
			br.close();

			// Intialise both the local moduleDescriptor variable and the gloabl code check
			// for
			// modules.

			moduleDescriptors = new ModuleDescriptor[totalModules];
			this.currentCodes = new String[totalModules];

			// Reopen the reader to read the lines
			br = new BufferedReader(new FileReader("module_descriptors.csv"));

			// Loop lines to add information and moudles
			int currentLine = 0;
			while ((line = br.readLine()) != null) {

				// Add to line count
				currentLine += 1;

				// Read data of any line that isn't the table start
				if (currentLine > 1) {

					int index = currentLine - 2;

					// Split the input into a array and sort it
					String[] currentValues = line.split(",");

					// Trim the string to remove whitespace
					String code = currentValues[1].trim();

					// Check the id is unique and add if not (note: if the id is zero this will also
					// fail) also checks the name is not an empty string
					if (isCodeUnique(code, index) && !currentValues[0].equals("")) {

						// Split the strings twice and then parse to double and sort into the array
						String[] weightStrip = line.split("\\[|]");
						String[] localWeightsString = weightStrip[1].split(",");
						double[] localWeights = new double[localWeightsString.length];
						double weightsTotal = 0;
						boolean isZeroWeight = false;
						for (int i = 0; i < localWeightsString.length; i++) {
							double value = Double.parseDouble(localWeightsString[i]);
							localWeights[i] = value;
							weightsTotal += value;
							if (value == 0) {
								isZeroWeight = true;
							}
						}

						if ((Math.abs(weightsTotal - 1) < epsilon) && !isZeroWeight) {

							// Add a local module descriptor
							ModuleDescriptor localModule = new ModuleDescriptor(currentValues[0], code, localWeights);
							moduleDescriptors[index] = localModule;
						} else {
							System.out.println("Module weights must be non zero and add up too 1, module: "
									+ currentValues[0] + " is not added");

							// Take one off the line so the index's match up
							currentLine -= 1;

							// Alert the import of another import fail
							isEmptyModule = true;
							failedImports += 1;
						}

					} else {
						System.out.println(
								"Code of a module must be unique and not empty it's name must also be not a empty, module: "
										+ currentValues[0] + " wasn't added");

						// Take one off the line so the index's match up
						currentLine -= 1;

						// Alert the import of another import fail
						isEmptyModule = true;
						failedImports += 1;
					}
				}
			}
		} catch (IOException e) {
			// In case there is an error
			e.printStackTrace();
		} finally {

			// Close the Buffered Reader
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Check for a failed module import
		if (isEmptyModule) {
			// If so we cut down the modules global array and only import actual modules
			int newLength = moduleDescriptors.length - failedImports;
			this.moduleDescriptors = new ModuleDescriptor[newLength];
			for (int i = 0; i < moduleDescriptors.length; i++) {
				if (moduleDescriptors[i] != null) {
					this.moduleDescriptors[i] = moduleDescriptors[i];
				}
			}
		} else {
			// There are no errors so we can directly pass modules in
			this.moduleDescriptors = moduleDescriptors;
		}
	}

	/**
	 * Adds a list of students imported from a csv
	 */
	public void importStudents() {
		// Creating a empty string and intialise a buffered reader
		String line;
		BufferedReader br = null;

		Student[] students = {};

		boolean isEmptyStudent = false;

		int failedImports = 0;

		// Running in a try catch to make sure we have no errors
		try {
			// Create the reader and run the lines
			br = new BufferedReader(new FileReader("students.csv"));

			// Get the total number of users as one less than the line length
			int totalUsers = 0;
			totalUsers = (int) (br.lines().count()) - 1;

			// As we use the line count we need two seperate BufferedReaders
			br.close();

			// Intialise both the local students variable and the gloabl ID check for
			// students.

			students = new Student[totalUsers];
			this.currentIDs = new int[totalUsers];

			// Reopen the reader to read the lines
			br = new BufferedReader(new FileReader("students.csv"));

			// Loop lines to add information and students
			int currentLine = 0;
			while ((line = br.readLine()) != null) {

				// Add to line count
				currentLine += 1;

				// Read data of any line that isn't the table start
				if (currentLine > 1) {

					int index = currentLine - 2;

					// Split the input into a array and sort it
					String[] currentValues = line.split(",");
					int id = Integer.parseInt(currentValues[0]);

					// Check the id is unique and add if not (note: if the id is zero this will also
					// fail)
					if (isIdUnique(id, index) && !currentValues[1].equals("")) {
						char gender = ' ';
						try {
							if (!currentValues[2].equals("")) {
								gender = currentValues[2].charAt(0);
							}
						} catch (Exception e) {
						}
						Student localStudent = new Student(id, currentValues[1], gender);
						students[index] = localStudent;

					} else {
						System.out.println(
								"ID of a student must be unique and not zero also it's name must not be empty, student: "
										+ currentValues[1] + " wasn't added");

						// Take one off the line so the index's match up
						currentLine -= 1;

						// Alert the import of another import fail
						isEmptyStudent = true;
						failedImports += 1;
					}
				}
			}
		} catch (IOException e) {
			// In case there is an error
			e.printStackTrace();
		} finally {

			// Close the Buffered Reader
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Check for a failed student import
		if (isEmptyStudent) {
			// If so we cut down the students global array and only import actual students
			int newLength = students.length - failedImports;
			this.students = new Student[newLength];
			for (int i = 0; i < students.length; i++) {
				if (students[i] != null) {
					this.students[i] = students[i];
				}
			}
		} else {
			// There are no errors so we can directly pass students in
			this.students = students;
		}
	}

	/**
	 * Adds students module data from a csv
	 */
	public void importModuleData() {
		// Creating a empty string and intialise a buffered reader
		String line;
		BufferedReader br = null;

		// Running in a try catch to make sure we have no errors
		try {

			// TODO: Asses this patch of code

			// Create the reader and run the lines
			br = new BufferedReader(new FileReader("module.csv"));

			// Get the total number of users as one less than the line length
			int totalData = 0;
			totalData = (int) (br.lines().count()) - 1;

			// Create largest array possible and we will trim it later
			this.modules = new Module[totalData];

			// As we use the line count we need two seperate BufferedReaders
			br.close();

			// Reopen the reader to read the lines
			br = new BufferedReader(new FileReader("module.csv"));

			// Loop lines to add information and students
			int currentLine = 0;
			while ((line = br.readLine()) != null) {

				// Add to line count
				currentLine += 1;

				// Read data of any line that isn't the table start
				if (currentLine > 1) {

					boolean failedStudentImport = false;

					boolean failedModuleImport = false;

					int index = currentLine - 2;

					int indexModule = 0;

					// Split the input into a array and sort it
					String[] currentValues = line.split(",");

					// Find the intended student
					ModuleDescriptor localModuleDescriptor = findModuleDescriptor(currentValues[1].trim());

					// Make sure the module exsits
					if (localModuleDescriptor == null) {
						// TODO: add a way of making sure this data point is missed
						System.out.println("Failed to find module in line: " + currentLine);

						failedModuleImport = true;
					} else {

						// Get the module
						Module localModule = new Module(localModuleDescriptor,
								Integer.parseInt(currentValues[2].trim()), Byte.parseByte(currentValues[3].trim()));
						indexModule = addModule(localModule);
					}

					// Find the intended student
					Student localStudent = findStudent(Integer.parseInt(currentValues[0]));

					// Make sure the student exsits
					if (localStudent == null) {
						System.out.println("Failed to find student in line: " + currentLine);

						failedStudentImport = true;

					} else if (!failedModuleImport) {

						// Split the strings twice and then parse to double and sort into the array
						String[] marksStrip = line.split("\\[|]");
						String[] localMarksString = marksStrip[1].split(",");
						double[] marks = new double[localMarksString.length];
						for (int i = 0; i < localMarksString.length; i++) {
							marks[i] = Double.parseDouble(localMarksString[i]);

						}

						StudentRecord localRecord = new StudentRecord(localStudent, modules[indexModule], marks);

						localStudent.addRecord(localRecord);

						modules[indexModule].updateRecords(localRecord);

					}

				}
			}
		} catch (IOException e) {
			// In case there is an error
			e.printStackTrace();
		} finally {

			// Close the Buffered Reader
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		int modulesTotal = 0;
		// Trim the modules
		for (Module m : this.modules) {
			if (m != null) {
				modulesTotal += 1;
			}
		}
		Module[] localModules = new Module[modulesTotal];
		int index = 0;

		for (Module m : localModules) {
			localModules[index] = this.modules[index];
			index += 1;
		}

		this.modules = localModules;
	}

	public void loadData() {
		importModuleDescriptors();
		importStudents();
		importModuleData();
	}

	public static void main(String[] args) {
		University university = new University();
		university.loadData();
	}
}
