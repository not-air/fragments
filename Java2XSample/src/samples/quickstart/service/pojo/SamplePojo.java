package samples.quickstart.service.pojo;

public class SamplePojo {
    String str;
    int[] values;
    Child child;

    public String getStr() {
	return str;
    }

    public void setStr(String str) {
	this.str = str;
    }

    public int[] getValues() {
	return values;
    }

    public void setValues(int[] values) {
	this.values = values;
    }

    public Child getChild() {
	return child;
    }

    public void setChild(Child child) {
	this.child = child;
    }

    class Child {
	String[] strs;
	double value;

	public String[] getStrs() {
	    return strs;
	}

	public void setStrs(String[] strs) {
	    this.strs = strs;
	}

	public double getValue() {
	    return value;
	}

	public void setValue(double value) {
	    this.value = value;
	}

    }
}
