import javax.lang.model.element.ModuleElement;

public class StudentRecord {

	private Student student;

	private Module module;

	private double[] marks;

	private double finalScore;

	private Boolean isAboveAverage;

	// Contructor
	public StudentRecord(Student student, Module module, double[] marks) {
		this.student = student;
		this.module = module;
		this.marks = marks;

		// Update the users score
		updateScore();
	}

	// Methods

	/**
	 * @return The students current marks.
	 */
	public double[] getMarks() {
		return marks;
	}

	/**
	 * @return The students current marks.
	 */
	public Module getModule() {
		return this.module;
	}

	/**
	 * Sets a students marks
	 */
	public void setMarks(double[] marks) {
		this.marks = marks;
	}

	/**
	 * @return The students final score.
	 */
	public double getFinalScore() {
		return this.finalScore;
	}

	/**
	 * @return The students current marks.
	 */
	public String getInformation() {
		return "| " + this.module.getYear() + " | " + this.module.getTerm() + " | "
				+ this.module.getDescriptor().getCode() + " | " + finalScore + " |";
	}

	/**
	 * Updates a students score
	 */
	public void updateScore() {
		// Set the score to zero to ensure we aren't adding too people's scores
		finalScore = 0;
		// Get the modules weights
		double[] weightsLocal = module.getDescriptor().getWeights();
		for (int i = 0; i < weightsLocal.length; i++) {
			finalScore += (weightsLocal[i] * marks[i]);
		}
		checkAverage();
	}

	/**
	 * Update if the student is above average.
	 */
	public void checkAverage() {
		if (module.getAverageGrade() < this.finalScore) {
			this.isAboveAverage = true;
		} else {
			this.isAboveAverage = false;
		}
	}

	/**
	 * @return If the student is above average.
	 */
	public boolean getIsAboveAverage() {
		return this.isAboveAverage;
	}

}
