package eu.trentorise.game.bean;

public class GetListResponse {
	private int total;
	private Object data;

	public GetListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GetListResponse(int total, Object data) {
		super();
		this.total = total;
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
