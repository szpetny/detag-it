package uuu.ooo.detagit;

import java.util.HashMap;
import java.util.Map;

public class Cell {
	private Map<CellAttributes, String> props = new HashMap<>();

	public Map<CellAttributes, String> getProps() {
		return props;
	}

	public void setProps(Map<CellAttributes, String> props) {
		this.props = props;
	}
	
	public void addProp(CellAttributes key, String value) {
		if (value != null && !value.trim().isEmpty() 
				&& !Boolean.FALSE.toString().equals(value)) {
			props.put(key, value);
		}
	}
}
