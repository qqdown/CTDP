package com.dataprocess;

public class SparseMatrixElement
{

	public int x;
	public int y;
	public double value;
	
	public SparseMatrixElement(int x, int y, double value)
	{
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return x + "," + y + "," + String.format("%.4f", value);
	}

	
}
