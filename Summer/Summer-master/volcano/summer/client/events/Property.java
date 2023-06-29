package volcano.summer.client.events;

import java.util.function.Supplier;

public class Property<T> {
	
	 protected final String label;
	    protected T value;
	    protected final Supplier<Boolean> dependency;

	    public Property(String label, T value, Supplier<Boolean> dependency) {
	        this.label = label;
	        setValue(value);
	        this.dependency = dependency;
	    }

	    public Property(String label, T value) {
	        this(label, value, () -> true);
	    }

	    public boolean isAvailable() {
	        return dependency.get();
	    }

	    public String getLabel() {
	        return label;
	    }

	    public T getValue() {
	        return value;
	    }

	    public void setValue(T value) {
	        onValueChanged(this.value, value);
	        this.value = value;
	    }

	    public Class<?> getType() {
	        return value.getClass();
	    }

	    public void onValueChanged(T oldValue, T value) {
	    }

}
