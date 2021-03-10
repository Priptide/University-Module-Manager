public class ModuleDescriptor {

	private String code;

	private String name;

	private double[] continuousAssignmentWeights;

	// Constructors
	public ModuleDescriptor(String name, String code, double[] continuousAssignmentWeights) {
		this.name = name;
		this.code = code;
		this.continuousAssignmentWeights = continuousAssignmentWeights;
	}

	// Methods

	/**
	 * @return The modules weigths.
	 */
	public double[] getWeights() {
		return continuousAssignmentWeights;
	}

	/**
	 * @return The modules code.
	 */
	public String getCode() {
		return code;
	}

}
