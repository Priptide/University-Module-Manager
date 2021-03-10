public class Module {

	private int year;

	private byte term;

	private ModuleDescriptor module;

	private StudentRecord[] records = {};

	private double finalAverageGrade;

	// Constructor
	public Module(ModuleDescriptor module, int year, byte term) {
		this.module = module;
		this.year = year;
		this.term = term;
	}

	// Methods

	/**
	 * @return The modules descriptor.
	 */
	public ModuleDescriptor getDescriptor() {
		return module;
	}

	/**
	 * @return The modules average grade.
	 */
	public Double getAverageGrade() {
		return this.finalAverageGrade;
	}

	/**
	 * Set the modules year.
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Set the modules year.
	 */
	public void setTerm(byte term) {
		this.term = term;
	}

	/**
	 * Update the modules records.
	 */
	public void updateRecords(StudentRecord record) {

		StudentRecord[] localRecord = this.records.clone();

		this.records = new StudentRecord[this.records.length + 1];

		int index = 0;
		for (StudentRecord sr : this.records) {
			if (index < localRecord.length) {
				this.records[index] = localRecord[index];
			} else {
				this.records[index] = record;
			}
			index += 1;
		}

		updateAverage();

	}

	/**
	 * @return The modules unique identifier.
	 */
	public String compareModules() {
		return year + term + module.getCode();
	}

	/**
	 * @return The modules unique year identifier.
	 */
	public String compareModulesStudent() {
		return String.valueOf(year) + String.valueOf(term);
	}

	/**
	 * @return The modules average grade.
	 */
	public int getTerm() {
		return this.term;
	}

	/**
	 * @return The modules average grade.
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Update the modules average grade.
	 */
	public void updateAverage() {
		int total = 0;
		double sumTotal = 0;
		for (StudentRecord sr : this.records) {
			sumTotal += sr.getFinalScore();
			total += 1;
		}
		if (total > 0) {
			this.finalAverageGrade = (sumTotal / total);
		} else {
			this.finalAverageGrade = 0;
		}
	}

}
