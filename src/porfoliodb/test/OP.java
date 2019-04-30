package porfoliodb.test;

/**
 * Class util for test cases
 * 
 * @author cam
 *
 */
public class OP {
	
	public String opType;
	public int qt;
	public String timestampString;
	
	public OP(String opType, int qt, String timestampString) {
		this.opType = opType;
		this.qt = qt;
		this.timestampString = timestampString;
	}
}
