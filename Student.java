import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {

	private int id;

	private String name;

	private char gender;

	private double gpa;

	// Setting it as empty intially to instantiate the array with no elements
	private StudentRecord[] records = {};

	// Constructors

	/**
	 * Creating a new student (3 Arguments)
	 */
	public Student(int id, String name, char gender) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		gpa = 0;
		// No need to set the records as this is a new student
	}

	/**
	 * Adding a full setup student (5 Arguments)
	 */
	public Student(int id, String name, char gender, double gpa, StudentRecord[] records) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.gpa = gpa;
		this.records = records;
	}

	// Methods

	/**
	 * @return The students GPA.
	 */
	public double getGPA() {
		return this.gpa;
	}

	/**
	 * @return The students current transcript.
	 */
	public String printTranscript() {
		String output = "";
		output += "University of Knowledge - Official Transcript \n \n";
		output += "ID: " + id + "\n";
		output += "Name: " + name + "\n";
		output += "GPA: " + gpa + "\n\n";

		// Map the records to the key of their unique id
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int index = 0;
		for (StudentRecord record : this.records) {
			int identifier = Integer.parseInt(record.getModule().compareModulesStudent());
			map.put(identifier, index);
			index += 1;
		}

		// Sort these keys to find the lowest value of id
		List<Integer> modulesById = new ArrayList<>(map.keySet());

		Collections.sort(modulesById);
		int previousId = 0;
		for (int i : modulesById) {
			output += records[map.get(i)].getInformation() + "\n";
			if (i != previousId) {
				output += "\n";
			}
			previousId = i;
		}

		return output;
	}

	/**
	 * @return The students id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Add student records.
	 */
	public void addRecord(StudentRecord newRecord) {
		StudentRecord[] localRecord = this.records.clone();

		this.records = new StudentRecord[this.records.length + 1];

		int index = 0;
		for (StudentRecord sr : this.records) {
			if (index < localRecord.length) {
				this.records[index] = localRecord[index];
			} else {
				this.records[index] = newRecord;
			}
			index += 1;
		}

		updateGPA();
	}

	/**
	 * Update the modules average grade.
	 */
	public void updateGPA() {
		int total = 0;
		double sumTotal = 0;
		for (StudentRecord sr : this.records) {
			sumTotal += sr.getFinalScore();
			total += 1;
		}
		if (total > 0) {
			this.gpa = (sumTotal / total);
		} else {
			this.gpa = 0;
		}
	}

}
