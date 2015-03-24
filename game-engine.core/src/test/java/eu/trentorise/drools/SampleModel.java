package eu.trentorise.drools;

public class SampleModel {
	public String hello;
	private boolean valid;

	public SampleModel(String hello) {
		this.hello = hello;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getHello() {
		return hello;
	}

	public void setHello(String hello) {
		this.hello = hello;
	}

}
