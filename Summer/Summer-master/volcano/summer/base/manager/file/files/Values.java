package volcano.summer.base.manager.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.FileManager.CustomFile;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Values extends CustomFile {

	public Values() {
		super("Values", true, true);
	}

	@Override
	public void saveFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
			for (Value value : Summer.valueManager.values) {
				if (value == null) {
					continue;
				}
				if (value instanceof ModeValue) {
					ModeValue v = (ModeValue) value;
					writer.write(String.valueOf(v.getName()) + ":" + v.getStringValue());
					writer.newLine();
				} else {
					writer.write(String.valueOf(value.getName()) + ":" + value.getValue());
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] arguments = line.split(":");
				if (arguments.length == 2) {
					for (Value value : Summer.valueManager.values) {
						if (value == null) {
							continue;
						}
						if (value instanceof ModeValue) {
							if (!arguments[0].equalsIgnoreCase(value.getName())) {
								continue;
							}
							try {
								((ModeValue) value).setStringValue(arguments[1]);
							} catch (Exception e3) {
								value.setValue(value.getDefaultValue());
							}
						} else {
							if (!arguments[0].equalsIgnoreCase(value.getName())) {
								continue;
							}
							try {
								if (value.getValue() instanceof Boolean) {
									value.setValue(Boolean.parseBoolean(arguments[1]));
								} else if (value.getValue() instanceof Byte) {
									value.setValue(Byte.parseByte(arguments[1]));
								} else if (value.getValue() instanceof Double) {
									value.setValue(Double.parseDouble(arguments[1]));
								} else if (value.getValue() instanceof Float) {
									value.setValue(Float.parseFloat(arguments[1]));
								} else if (value.getValue() instanceof Integer) {
									value.setValue(Integer.parseInt(arguments[1]));
								} else if (value.getValue() instanceof Long) {
									value.setValue(Long.parseLong(arguments[1]));
								} else {
									if (!(value.getValue() instanceof String) && !(value instanceof ModeValue)) {
										continue;
									}
									value.setValue(arguments[1]);
								}
							} catch (Exception e3) {
								value.setValue(value.getDefaultValue());
							}
						}
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}