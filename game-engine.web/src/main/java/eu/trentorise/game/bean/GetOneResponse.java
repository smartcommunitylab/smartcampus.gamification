package eu.trentorise.game.bean;

public class GetOneResponse {
	private Object data;

	public GetOneResponse(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
