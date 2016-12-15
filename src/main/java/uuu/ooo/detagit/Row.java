package uuu.ooo.detagit;

import java.util.ArrayList;
import java.util.List;

public class Row {
	private List<Cell> cells = new ArrayList<>();

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	
	public void addCell(Cell cell) {
		cells.add(cell);
	}
}
